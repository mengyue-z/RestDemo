package com.example.restapidemo.service;

import com.example.restapidemo.dao.StudentRepository;
import com.example.restapidemo.entity.StudentEntity;
import com.example.restapidemo.util.StudentEntityConverter;
import com.example.restapidemo.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAllStudents() {
        List<StudentEntity> students = studentRepository.findAll();
        return students.stream().map(e -> new Student(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail()))
                .collect(Collectors.toList());

    }
    @Override
    public Student findById(long id) {
        StudentEntity studentEntity = studentRepository.findById(id).orElse(null);
        return StudentEntityConverter.convertEntityToUser(studentEntity);
    }

    public Student saveStudent(Student student) {
        StudentEntity studentEntity = studentRepository.save(new StudentEntity(student.getId(),student.getFirstName(), student.getLastName(), student.getEmail()));
        return StudentEntityConverter.convertEntityToUser(studentEntity);
    }

    public Student updateStudent(Student student) {
        StudentEntity studentEntity = studentRepository.saveAndFlush(new StudentEntity(student.getId(),student.getFirstName(), student.getLastName(), student.getEmail()));
        return StudentEntityConverter.convertEntityToUser(studentEntity);
    }
    @Override
    public void deleteStudentById(long id) {
        studentRepository.deleteById(id);
    }

}
