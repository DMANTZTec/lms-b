package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

	@NotBlank(message = "Email or Mobile number is required")
	private String EmailIdOrMobileNo;

	public String getEmailIdOrMobileNo() {
		return EmailIdOrMobileNo;
	}

	public void setEmailIdOrMobileNo(String emailIdOrMobileNo) {
		EmailIdOrMobileNo = emailIdOrMobileNo;
	}

}
