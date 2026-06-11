package com.dmantz.lms.exceptions;

import com.dmantz.lms.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<String> handleNotFound(StudentNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(AccountNotVerifiedException.class)
	public ResponseEntity<String> handleAccountNotVerified(AccountNotVerifiedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleOther(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<ApiResponse> handleOtpExpired(OtpExpiredException ex) {
		return ResponseEntity.status(HttpStatus.GONE).body(new ApiResponse("OTP_410", ex.getMessage()));
	}

	@ExceptionHandler(OtpInvalidException.class)
	public ResponseEntity<ApiResponse> handleOtpInvalid(OtpInvalidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("OTP_400", ex.getMessage()));
	}

	@ExceptionHandler(OtpNotFoundException.class)
	public ResponseEntity<ApiResponse> handleOtpNotFound(OtpNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("OTP_404", ex.getMessage()));
	}

	@ExceptionHandler(DuplicateValuesException.class)
	public ResponseEntity<String> handleDuplicateValues(DuplicateValuesException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<String> handleUnauthorized(UnauthorizedAccessException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	@ExceptionHandler(AccountDisabledException.class)
	public ResponseEntity<String> handleAccountDisabled(AccountDisabledException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}

	@ExceptionHandler(EmailSendingException.class)
	public ResponseEntity<String> handleEmailSending(EmailSendingException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidPositionException.class)
	public ResponseEntity<String> handleInvalidPosition(InvalidPositionException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<String> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("File size exceeds the maximum allowed limit of 500 MB");
	}

	@ExceptionHandler(SmsSendingException.class)
	public ResponseEntity<String> handleSmsSending(SmsSendingException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidOtpChannelException.class)
	public ResponseEntity<String> handleInvalidOtpChannel(InvalidOtpChannelException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(OtpSendingException.class)
	public ResponseEntity<String> handleOtpSending(OtpSendingException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

}
