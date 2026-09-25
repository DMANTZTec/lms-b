package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpChannel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StudentRegistrationRequest {

	@NotBlank(message = "First name is required")
	private String firstNm;

	private String lastNm;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String emailId;

	@NotBlank(message = "Mobile number is required")
	@Size(min = 10, max = 10, message = "Mobile number must be exactly 10 digits")
	private String mobileNum;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 100, message = "Password must be at least 6 characters")
	private String password;

	private String currentStatus;

	@NotNull(message = "OTP channel is required (EMAIL or MOBILE)")
	private OtpChannel otpChannel;

	public String getFirstNm() {
		return firstNm;
	}

	public void setFirstNm(String firstNm) {
		this.firstNm = firstNm;
	}

	public String getLastNm() {
		return lastNm;
	}

	public void setLastNm(String lastNm) {
		this.lastNm = lastNm;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public OtpChannel getOtpChannel() {
		return otpChannel;
	}

	public void setOtpChannel(OtpChannel otpChannel) {
		this.otpChannel = otpChannel;
	}

}