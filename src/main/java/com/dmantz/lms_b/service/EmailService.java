package com.dmantz.lms_b.service;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otp);
}
