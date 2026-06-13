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

	@Value("${twilio.verify.service.sid}")
	private String verifyServiceSid;

	@PostConstruct
	public void init() {
		Twilio.init(accountSid, authToken);
		logger.info("Twilio Verify initialized successfully");
	}

//	@Override
//	public void sendOtp(String mobileNumber, String otp) {
//
//		logger.info("Sending OTP {} to mobile {}", otp, mobileNumber);
//
//		try {
//
//			String formatted = formatToE164(mobileNumber);
//
//			Message.creator(
//					new PhoneNumber(formatted),
//					new PhoneNumber(twilioPhoneNumber),
//					"Your LMS OTP is: " + otp
//			).create();
//
//			logger.info("OTP sent successfully to {}", formatted);
//
//		} catch (Exception ex) {
//
//			logger.error("Failed to send OTP", ex);
//
//			throw new SmsSendingException(
//					"Failed to send OTP: " + ex.getMessage(),
//					ex);
//		}
//	}

//	@Override
//	public boolean verifyOtp(String mobileNumber, String otp, OtpPurpose purpose) {
//
//		if (mobileNumber == null || mobileNumber.isBlank()) {
//			throw new SmsSendingException("Mobile number must not be blank");
//		}
//
//		if (otp == null || otp.isBlank()) {
//			throw new SmsSendingException("OTP must not be blank");
//		}
//
//		try {
//
//			String formatted = formatToE164(mobileNumber);
//
//			VerificationCheck verificationCheck = VerificationCheck.creator(verifyServiceSid).setTo(formatted)
//					.setCode(otp).create();
//
//			boolean verified = "approved".equalsIgnoreCase(verificationCheck.getStatus());
//
//			logger.info("OTP verification result for {} : {}", formatted, verified);
//
//			return verified;
//
//		} catch (Exception ex) {
//
//			logger.error("OTP verification failed for {}", mobileNumber, ex);
//
//			throw new SmsSendingException("OTP verification failed: " + ex.getMessage(), ex);
//		}
//	}

//	private String formatToE164(String mobile) {
//
//		if (mobile.startsWith("+")) {
//			return mobile;
//		}
//
//		if (mobile.length() == 10) {
//			return "+91" + mobile;
//		}
//
//		return "+" + mobile;
//	}
}