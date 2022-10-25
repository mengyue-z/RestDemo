package com.example.restapidemo.exception;

public class StudentNotFoundException extends StudentException {
    public StudentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
