package com.dmantz.lms.dto.request;

public class StudentOtpVerifyRequest {

	private String emailIdOrMobileNo;
	private String otp;

	public String getEmailIdOrMobileNo() {
		return emailIdOrMobileNo;
	}

	public void setEmailIdOrMobileNo(String emailIdOrMobileNo) {
		this.emailIdOrMobileNo = emailIdOrMobileNo;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
}
