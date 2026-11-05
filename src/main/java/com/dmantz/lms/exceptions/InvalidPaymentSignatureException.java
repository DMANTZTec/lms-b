package com.dmantz.lms.exceptions;

public class InvalidPaymentSignatureException extends RuntimeException {

    public InvalidPaymentSignatureException(String message) {
        super(message);
    }
}
