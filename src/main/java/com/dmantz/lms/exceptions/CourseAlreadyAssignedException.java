package com.dmantz.lms.exceptions;

public class CourseAlreadyAssignedException extends RuntimeException {
    public CourseAlreadyAssignedException(String message) {
        super(message);
    }
}
