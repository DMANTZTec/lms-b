package com.dmantz.lms.service;

import com.dmantz.lms.entity.OtpPurpose;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otp, OtpPurpose purpose);
}
