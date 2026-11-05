package com.dmantz.lms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dmantz.lms.entity.OtpPurpose;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otp, OtpPurpose purpose);

    void sendStaffPasswordSetupMail(String toEmail, String staffName, String token);

    void sendResetPasswordEmail(String toEmail, String staffName, String resetLink);

    // Admin enrolled the student with "pay later": ask the student to complete payment
    void sendPaymentRequestEmail(String toEmail, String studentName, String itemName,
                                 BigDecimal amount, LocalDateTime expiresAt);

}
