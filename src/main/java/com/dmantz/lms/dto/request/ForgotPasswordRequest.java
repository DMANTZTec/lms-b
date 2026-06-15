package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ForgotPasswordRequest {

	@NotBlank(message = "Email or Mobile number is required")
	private String getEmailIdOrMobileNo;

	@NotNull(message = "OTP channel is required (EMAIL or MOBILE)")
	private OtpChannel OtpChannel;

	

	public String getGetEmailIdOrMobileNo() {
		return getEmailIdOrMobileNo;
	}

	public void setGetEmailIdOrMobileNo(String getEmailIdOrMobileNo) {
		this.getEmailIdOrMobileNo = getEmailIdOrMobileNo;
	}

	public OtpChannel getOtpChannel() {
		return OtpChannel;
	}

	public void setOtpChannel(OtpChannel otpChannel) {
		OtpChannel = otpChannel;
	}

	

}
