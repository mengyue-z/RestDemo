package com.example.restapidemo.controller;

import com.example.restapidemo.exception.StudentException;
import com.example.restapidemo.exception.StudentNotFoundException;
import com.example.restapidemo.service.StudentService;
import com.example.restapidemo.vo.ErrorResponse;
import com.example.restapidemo.vo.ResponseMessage;
import com.example.restapidemo.vo.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/api")
@Api(value = "Student", description = "REST API for Students", tags = {"Student"})
public class StudentController {
    private static Logger logger = LoggerFactory.getLogger(StudentController.class);

    StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * retrives a single student
     *
     **/
    @RequestMapping(value = "/student/{sid}", method = RequestMethod.GET)
    public ResponseEntity<?> getStudent(@PathVariable("sid") long id) throws StudentException {
        Student student = studentService.findById(id);
        if (student == null) {
            throw new StudentNotFoundException("STUDENT_NOT_FOUND");
        }
        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }

    /** create a student **/
    @ApiOperation(value = "create a student")
    @RequestMapping(value = "/student", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<ResponseMessage> createUser(@Validated @RequestBody Student student, UriComponentsBuilder ucBuilder) {
        Student savedStudent = studentService.saveStudent(student);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/student/{id}").buildAndExpand(student.getId()).toUri());
        return new ResponseEntity<ResponseMessage>(new ResponseMessage("STUDENT_CREATED",savedStudent), headers, HttpStatus.CREATED);
    }

    /**
     * update a student
     *
     **/
    @ApiOperation(value = "update a student")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Student> updateUser(@PathVariable("id") long id, @RequestBody Student user){
        Student currentStudent = studentService.findById(id);

        if (currentStudent == null) {
            throw new StudentNotFoundException("STUDENT_NOT_FOUND");
        }

        currentStudent.setFirstName(user.getFirstName());
        currentStudent.setLastName(user.getLastName());
        currentStudent.setEmail(user.getEmail());

        studentService.updateStudent(currentStudent);
        return new ResponseEntity<Student>(currentStudent, HttpStatus.OK);
    }

    /**
     * delete a student
     *
     * @throws StudentException
     **/
    @ApiOperation(value = "delete a student")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseMessage> deleteStudent(@PathVariable("id") long id) {

        Student student = studentService.findById(id);
        if (student == null) {
            throw new StudentNotFoundException("STUDENT_NOT_FOUND");
        }
        studentService.deleteStudentById(id);
        return new ResponseEntity<ResponseMessage>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(ex.getMessage());
        logger.error("Controller Error",ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerStudentNotFound(Exception ex) {
        logger.error("Cannot find student");
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


}
