package com.dmantz.lms_b.service;

import com.dmantz.lms_b.entity.OtpPurpose;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otp, OtpPurpose purpose);
}
