package com.dmantz.lms.exceptions;

public class OtpSendingException extends RuntimeException {

    public OtpSendingException(String message) {
        super(message);
    }

    public OtpSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}