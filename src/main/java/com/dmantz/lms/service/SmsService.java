package com.dmantz.lms.service;

import com.dmantz.lms.entity.OtpPurpose;

public interface SmsService {

	void sendOtpSms(String mobileNumber, String otp, OtpPurpose purpose);
}