package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your LearningManagementSystem OTP");
            message.setText(
                    "Hello Student,\n\n" +
                            "Your OTP for logging into the LMS is: " + otp + "\n" +
                            "Please use this OTP within 5 minutes.\n\n" +
                            "Thank you,\nLMS Team"
            );
            mailSender.send(message);

        } catch (Exception ex) {
            // Log only – do NOT throw
            System.err.println("Failed to send OTP email to " + toEmail);
            ex.printStackTrace();
        }
    }
}
