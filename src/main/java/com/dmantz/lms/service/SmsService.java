package com.dmantz.lms.service;

import com.dmantz.lms.entity.OtpPurpose;

public interface SmsService {

	void sendOtp(String mobileNumber, OtpPurpose purpose);

	boolean verifyOtp(String mobileNumber, String otp, OtpPurpose purpose);
}