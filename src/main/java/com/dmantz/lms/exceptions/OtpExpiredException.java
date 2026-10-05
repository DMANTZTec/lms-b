package com.dmantz.lms.exceptions;

public class OtpExpiredException extends RuntimeException {
    public OtpExpiredException(String msg) { super(msg); }
}
