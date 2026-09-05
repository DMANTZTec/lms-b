package com.dmantz.lms.service.impl;

import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.exceptions.SmsSendingException;
import com.dmantz.lms.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

	private static final Logger logger = LogManager.getLogger(SmsServiceImpl.class);

	@Value("${twilio.account.sid}")
	private String accountSid;

	@Value("${twilio.auth.token}")
	private String authToken;

	@Value("${twilio.from.number}")
	private String twilioFromNumber;

	@PostConstruct
	public void init() {
		Twilio.init(accountSid, authToken);
		logger.info("Twilio SMS initialized successfully");
	}

	@Override
	public void sendOtpSms(String mobileNumber, String otp, OtpPurpose purpose) {

		if (mobileNumber == null || mobileNumber.isBlank()) {
			throw new SmsSendingException("Mobile number must not be null or blank");
		}

		if (purpose == null) {
			throw new SmsSendingException("OTP purpose must not be null");
		}

		String messageBody;

		switch (purpose) {

		case REGISTRATION:
			messageBody = "Welcome to LMS! Your account verification OTP is: " + otp
					+ ". Valid for 5 minutes. Do not share this with anyone.";
			break;

		case LOGIN:
			messageBody = "Your LMS login OTP is: " + otp + ". Valid for 5 minutes. Do not share this with anyone.";
			break;

		case FORGOT_PASSWORD:
			messageBody = "Your LMS password reset OTP is: " + otp
					+ ". Valid for 10 minutes. If you did not request this, ignore this message.";
			break;

		case STAFF_LOGIN:
			messageBody = "Your LMS Staff login OTP is: " + otp
					+ ". Valid for 5 minutes. Do not share this with anyone.";
			break;

		case STAFF_FORGOT_PASSWORD:
			messageBody = "Your LMS Staff password reset OTP is: " + otp
					+ ". Valid for 10 minutes. If you did not request this, ignore this message.";
			break;

		case PASSWORD_RESET_SUCCESS:
			messageBody = "Your LMS password has been reset successfully. "
					+ "If this wasn't you, please contact support immediately.";
			break;
			
		case PASSWORD_CHANGE_SUCCESS:
			messageBody = "Your LMS password has been changed successfully. If this wasn't you, please contact support immediately.";
		    break;

		default:
			throw new SmsSendingException("Unsupported OTP purpose for SMS: " + purpose);
		}

		try {
			String formattedNumber = formatToE164(mobileNumber);

			Message message = Message
					.creator(new PhoneNumber(formattedNumber), new PhoneNumber(twilioFromNumber), messageBody).create();

			logger.info("OTP SMS sent successfully to {} | SID: {} | Purpose: {}", formattedNumber, message.getSid(),
					purpose);

		} catch (Exception ex) {
			logger.error("Failed to send OTP SMS to {} for purpose {}: {}", mobileNumber, purpose, ex.getMessage(), ex);
			throw new SmsSendingException("Failed to send OTP SMS to " + mobileNumber + ": " + ex.getMessage(), ex);
		}
	}

	private String formatToE164(String mobile) {
		if (mobile.startsWith("+")) {
			return mobile;
		}
		if (mobile.length() == 10) {
			return "+91" + mobile; // India prefix — adjust if multi-country
		}
		return "+" + mobile;
	}
	
	
	@Override
	public void sendStaffPasswordSetupSms(String mobileNumber, String staffName, String token) {

	    logger.info("Preparing staff password setup SMS for: {}", mobileNumber);

	    if (mobileNumber == null || mobileNumber.isBlank()) {
	        throw new SmsSendingException("Mobile number must not be null or blank");
	    }

	    if (token == null || token.isBlank()) {
	        throw new SmsSendingException("Password setup token must not be null or blank");
	    }

	    if (staffName == null || staffName.isBlank()) {
	        staffName = "Staff";
	    }

	    String setupLink =
	            "http://localhost:5173/staff/set-password?token=" + token;

	    String messageBody =
	            "Hello " + staffName + ", "
	            + "Welcome to the LMS team. "
	            + "Your staff account has been created successfully. "
	            + "Set your password using this link: "
	            + setupLink
	            + " This link is valid for 24 hours. "
	            + "Regards, LMS Team";

	    try {

	        String formattedNumber = formatToE164(mobileNumber);

	        Message message = Message
	                .creator(
	                        new PhoneNumber(formattedNumber),
	                        new PhoneNumber(twilioFromNumber),
	                        messageBody
	                )
	                .create();

	        logger.info(
	                "Staff password setup SMS sent successfully to {} | SID: {}",
	                formattedNumber,
	                message.getSid()
	        );

	    } catch (Exception ex) {

	        logger.error(
	                "Failed to send staff password setup SMS to {}",
	                mobileNumber,
	                ex
	        );

	        throw new SmsSendingException(
	                "Failed to send staff password setup SMS: " + ex.getMessage(),
	                ex
	        );
	    }
	}
	
	@Override
	public void sendResetPasswordSms(
	        String mobileNumber,
	        String staffName,
	        String resetLink) {

	    logger.info("Preparing reset password SMS for: {}", mobileNumber);

	    if (mobileNumber == null || mobileNumber.isBlank()) {
	        throw new SmsSendingException(
	                "Mobile number must not be null or blank");
	    }

	    if (resetLink == null || resetLink.isBlank()) {
	        throw new SmsSendingException(
	                "Reset password link must not be null or blank");
	    }

	    if (staffName == null || staffName.isBlank()) {
	        staffName = "Staff";
	    }

	    String messageBody =
	            "Hello " + staffName + ", "
	            + "We received a request to reset your LMS password. "
	            + "Reset your password using this link: "
	            + resetLink
	            + " This link is valid for 30 minutes. "
	            + "If you did not request this password reset, please ignore this message. "
	            + "LMS Team";

	    try {

	        String formattedNumber = formatToE164(mobileNumber);

	        Message message = Message
	                .creator(
	                        new PhoneNumber(formattedNumber),
	                        new PhoneNumber(twilioFromNumber),
	                        messageBody
	                )
	                .create();

	        logger.info(
	                "Reset password SMS sent successfully to {} | SID: {}",
	                formattedNumber,
	                message.getSid()
	        );

	    } catch (Exception ex) {

	        logger.error(
	                "Failed to send reset password SMS to {}",
	                mobileNumber,
	                ex
	        );

	        throw new SmsSendingException(
	                "Failed to send reset password SMS: "
	                        + ex.getMessage(),
	                ex
	        );
	    }
	}
}