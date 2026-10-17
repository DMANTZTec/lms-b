package com.dmantz.lms.exceptions;

public class InvalidPasswordException extends RuntimeException {

	public InvalidPasswordException(String message) {
		super(message);
	}
}