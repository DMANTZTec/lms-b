package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

import jakarta.validation.constraints.NotBlank;

public class StaffLoginRequest {

	@NotBlank
	private String username;
	@NotBlank
	private String password;

	private OtpChannel otpChannel;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public OtpChannel getOtpChannel() {
		return otpChannel;
	}

	public void setOtpChannel(OtpChannel otpChannel) {
		this.otpChannel = otpChannel;
	}

}
