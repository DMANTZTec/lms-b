package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ForgotPasswordRequest {

	@NotBlank(message = "Email or Mobile number is required")
	private String EmailIdOrMobileNo;

	@NotNull(message = "OTP channel is required (EMAIL or MOBILE)")
	private OtpChannel OtpChannel;

	public String getEmailIdOrMobileNo() {
		return EmailIdOrMobileNo;
	}

	public void setEmailIdOrMobileNo(String emailIdOrMobileNo) {
		EmailIdOrMobileNo = emailIdOrMobileNo;
	}

	public OtpChannel getOtpChannel() {
		return OtpChannel;
	}

	public void setOtpChannel(OtpChannel otpChannel) {
		OtpChannel = otpChannel;
	}

}
