package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.entity.OtpPurpose;
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
    public void sendOtpEmail(String toEmail, String otp, OtpPurpose purpose) {

        String subject;
        String body;

        switch (purpose) {

            case LOGIN:
                subject = "Your LMS Login OTP";
                body =
                        "Hello Student,\n\n" +
                                "Your OTP for logging into the LMS is: " + otp + "\n" +
                                "Please use this OTP within 5 minutes.\n\n" +
                                "Thank you,\nLMS Team";
                break;

            case FORGOT_PASSWORD:
                subject = "LMS Password Reset OTP";
                body =
                        "Hello Student,\n\n" +
                                "Your OTP to reset your LMS password is: " + otp + "\n" +
                                "This OTP is valid for 10 minutes.\n\n" +
                                "If you did not request this, please ignore this email.\n\n" +
                                "Thanks,\nLMS Team";
                break;

            case PASSWORD_RESET_SUCCESS:
                subject = "LMS Password Reset Successful";
                body =
                        "Hello Student,\n\n" +
                                "Your LMS password has been reset successfully.\n\n" +
                                "If this was not you, please contact support immediately.\n\n" +
                                "Regards,\nLMS Team";
                break;

            case STAFF_LOGIN:
                subject = "Your LMS Staff Login OTP";
                body =
                        "Hello Staff,\n\n" +
                                "Your OTP for logging into the LMS is: " + otp + "\n" +
                                "Please use this OTP within 5 minutes.\n\n" +
                                "Thank you,\nLMS Team";
                break;

            case STAFF_FORGOT_PASSWORD:
                subject = "LMS Password Reset OTP";
                body =
                        "Hello Staff,\n\n" +
                                "Your OTP to reset your LMS password is: " + otp + "\n" +
                                "This OTP is valid for 10 minutes.\n\n" +
                                "If you did not request this, please ignore this email.\n\n" +
                                "Thanks,\nLMS Team";
                break;

            case STAFF_PASSWORD_RESET_SUCCESS:
                subject = "LMS Password Reset Successful";
                body =
                        "Hello Staff,\n\n" +
                                "Your LMS password has been reset successfully.\n\n" +
                                "If this was not you, please contact support immediately.\n\n" +
                                "Regards,\nLMS Team";
                break;



            default:
                throw new IllegalArgumentException("Invalid OTP purpose");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

        } catch (Exception ex) {
            System.err.println("Failed to send OTP email to " + toEmail);
            ex.printStackTrace();
        }
    }
}
