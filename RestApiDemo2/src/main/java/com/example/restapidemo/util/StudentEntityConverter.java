package com.example.restapidemo.util;

import com.example.restapidemo.entity.StudentEntity;
import com.example.restapidemo.vo.Student;

public class StudentEntityConverter {
    public static Student convertEntityToUser(StudentEntity studentEntity){
        if (studentEntity != null) {
            Student student = new Student();
            student.setId(studentEntity.getId());
            student.setFirstName(studentEntity.getFirstName());
            student.setLastName(studentEntity.getLastName());
            student.setEmail(studentEntity.getEmail());
            return student;
        } else {
            return null;
        }
    }
}
