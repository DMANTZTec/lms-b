package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

public class OtpVerifyRequest {

	private String emailId;
	private String otp;
	private OtpChannel channel;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public OtpChannel getChannel() {
		return channel;
	}

	public void setChannel(OtpChannel channel) {
		this.channel = channel;
	}
}
