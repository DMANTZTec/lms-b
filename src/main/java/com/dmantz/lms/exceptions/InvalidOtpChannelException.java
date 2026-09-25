package com.dmantz.lms.exceptions;

public class InvalidOtpChannelException extends RuntimeException {

    public InvalidOtpChannelException(String message) {
        super(message);
    }

    public InvalidOtpChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}