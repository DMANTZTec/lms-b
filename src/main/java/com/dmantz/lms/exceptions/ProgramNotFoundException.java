package com.dmantz.lms.exceptions;

public class ProgramNotFoundException extends RuntimeException {
    public ProgramNotFoundException(String message) {
        super(message);
    }
}
