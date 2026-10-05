package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

public class OtpVerifyRequest {

	private String emailIdOrMobileNo;
	private String otp;
	private OtpChannel channel;

	
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

	public OtpChannel getChannel() {
		return channel;
	}

	public void setChannel(OtpChannel channel) {
		this.channel = channel;
	}
}
