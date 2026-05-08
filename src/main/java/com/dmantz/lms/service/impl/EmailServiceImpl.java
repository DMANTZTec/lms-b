package com.dmantz.lms.service.impl;

import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.service.EmailService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);
	
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp, OtpPurpose purpose) {
    	
    	 logger.info("Preparing to send OTP email to: {} for purpose: {}", toEmail, purpose);

         if (toEmail == null || toEmail.isBlank()) {
             logger.error("Recipient email is null or blank. Aborting email send.");
             throw new IllegalArgumentException("Recipient email must not be null or blank");
         }

         if (purpose == null) {
             logger.error("OTP purpose is null. Aborting email send.");
             throw new IllegalArgumentException("OTP purpose must not be null");
         }
    	
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
            	logger.error("Unsupported OTP purpose received: {}", purpose);
                throw new IllegalArgumentException("Invalid OTP purpose: " + purpose);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            logger.debug("Sending email - To: {}, Subject: {}", toEmail, subject);
            mailSender.send(message);
            logger.info("OTP email sent successfully to: {} for purpose: {}", toEmail, purpose);

        } catch (MailException ex) {
            logger.error("Mail delivery failure while sending OTP email to: {} for purpose: {}. Reason: {}",
                    toEmail, purpose, ex.getMessage(), ex);
            throw new RuntimeException("Failed to send OTP email to " + toEmail + ". Please try again later.", ex);

        } catch (Exception ex) {
            logger.error("Unexpected error while sending OTP email to: {} for purpose: {}",
                    toEmail, purpose, ex);
            throw new RuntimeException("An unexpected error occurred while sending email.", ex);
        }
    }
}