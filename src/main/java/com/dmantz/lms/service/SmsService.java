package com.dmantz.lms.service;

import com.dmantz.lms.entity.OtpPurpose;

public interface SmsService {

	void sendOtpSms(String mobileNumber, String otp, OtpPurpose purpose);

	void sendStaffPasswordSetupSms(String mobileNumber, String staffName, String token);

	void sendResetPasswordSms(String mobileNumber, String staffName, String resetLink);
}