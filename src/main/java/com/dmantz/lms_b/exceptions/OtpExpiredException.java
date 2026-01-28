package com.dmantz.lms_b.exceptions;

public class OtpExpiredException extends RuntimeException {
    public OtpExpiredException(String msg) { super(msg); }
}
