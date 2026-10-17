package com.dmantz.lms.exceptions;

public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
