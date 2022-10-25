package com.example.restapidemo.service;

import com.example.restapidemo.entity.StudentEntity;
import com.example.restapidemo.vo.Student;

import java.util.List;

public interface StudentService {
    Student findById(long id);

    Student saveStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudentById(long id);

    List<Student> findAllStudents();


}
