package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.StaffLoginRequest;
import com.dmantz.lms.dto.request.StudentLoginRequest;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.entity.OtpChannel;
import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.entity.OtpStatus;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.StaffOtp;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentOtp;
import com.dmantz.lms.exceptions.InvalidOtpChannelException;
import com.dmantz.lms.repository.StaffOtpRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StudentOtpRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.AuthService;
import com.dmantz.lms.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

	private final StudentRepository studentRepository;
	private final StaffRepository staffRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final StudentOtpRepository otpRepository;
	private final StaffOtpRepository staffOtpRepository;
	private final EmailService emailService;
	private final SmsServiceImpl smsService;

	public AuthServiceImpl(StudentRepository studentRepository, StaffRepository staffRepository,
			PasswordEncoder passwordEncoder, JwtUtil jwtUtil, StudentOtpRepository otpRepository,
			StaffOtpRepository staffOtpRepository, EmailService emailService, SmsServiceImpl smsService) {

		this.studentRepository = studentRepository;
		this.staffRepository = staffRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.otpRepository = otpRepository;
		this.staffOtpRepository = staffOtpRepository;
		this.emailService = emailService;
		this.smsService = smsService;
	}

//	@Override
//	public StudentLoginResponse studentLogin(StudentLoginRequest request) {
//
//		logger.info("Login attempt for username: {}", request.getUsername());
//
//		String username = request.getUsername();
//
//		Student student = studentRepository
//				.findByEmailIdOrMobileNumOrLoginId(username, username, username);
//
//		// INVALID USER
//		if (student == null) {
//			logger.warn("Invalid login credentials for username: {}", username);
//			throw new RuntimeException("Invalid Credentials");
//		}
//
//		// ACCOUNT DISABLED
//		if (!"Y".equals(student.getEnabled())) {
//			logger.warn("Account disabled for studentId: {}", student.getStudentId());
//			throw new RuntimeException("Account disabled");
//		}
//
//		// PASSWORD CHECK
//		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
//			logger.warn("Wrong password attempt for studentId: {}", student.getStudentId());
//			throw new RuntimeException("Invalid Credentials");
//		}
//
//		StudentOtp otp = generateOtp(student);
//
//		try {
//			emailService.sendOtpEmail(student.getEmailId(), otp.getOtp(), OtpPurpose.LOGIN);
//			otp.setStatus(OtpStatus.SENT);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//
//			logger.info("Login OTP sent successfully to email: {}", student.getEmailId());
//
//		} catch (Exception e) {
//			logger.error("Failed to send login OTP to email: {}", student.getEmailId(), e);
//			otp.setStatus(OtpStatus.FAILED);
//			otpRepository.save(otp);
//			throw new RuntimeException("Failed to send OTP");
//		}
//
//		StudentLoginResponse response = new StudentLoginResponse();
//		response.setStudentId(student.getStudentId());
//		response.setEmail(student.getEmailId());
//		response.setRole("STUDENT");
//		response.setToken(null);
//		response.setMessage("OTP sent successfully to registered email");
//		return response;
//	}

//	public StudentOtp generateOtp(Student student) {
//
//		logger.info("Generating OTP for studentId: {}", student.getStudentId());
//
//		StudentOtp otp = new StudentOtp();
//
//		otp.setStudent(student);
//
//		otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));
//
//		otp.setStatus(OtpStatus.NEW);
//
//		otp.setAttemptsNum(0);
//
//		otp.setCreatedDt(LocalDateTime.now());
//
//		StudentOtp savedOtp = otpRepository.save(otp);
//
//		logger.info("OTP generated successfully for studentId: {}", student.getStudentId());
//
//		return savedOtp;
//	}

	// STAFF OTP METHOD
	public StaffOtp generateStaffOtp(Staff staff) {

		logger.info("Generating OTP for staffId: {}", staff.getStaffId());

		StaffOtp otp = new StaffOtp();

		otp.setStaffId(staff.getStaffId());

		otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));

		otp.setStatus(OtpStatus.NEW);

		otp.setAttemptsNum(0);

		otp.setCreatedDt(LocalDateTime.now());

		StaffOtp savedOtp = staffOtpRepository.save(otp);

		logger.info("OTP generated successfully for staffId: {}", staff.getStaffId());

		return savedOtp;
	}

//	@Override
//	public StudentLoginResponse studentLogin(StudentLoginRequest studentLoginRequest) {
//		return null;
//	}

	@Override
	public StaffLoginResponse staffLogin(StaffLoginRequest request) {

		logger.info("Staff login attempt for username: {}", request.getUsername());

		String username = request.getUsername();

		Staff staff = staffRepository.findByLoginId(username).orElseThrow(() -> {

			logger.warn("Invalid credentials for username: {}", username);

			return new RuntimeException("Invalid Credentials");
		});

		// ACCOUNT DISABLED
		if (!"Y".equals(staff.getEnabled())) {

			logger.warn("Disabled account for staffId: {}", staff.getStaffId());

			throw new RuntimeException("Account disabled");
		}

		// PASSWORD CHECK
		if (!passwordEncoder.matches(request.getPassword(), staff.getPassword())) {

			logger.warn("Wrong password for staffId: {}", staff.getStaffId());

			throw new RuntimeException("Invalid Credentials");
		}

		// ROLE
		String role = staff.getRoles().stream().findFirst().map(r -> r.getRoleNm()).orElse("STAFF");

		// GENERATE OTP
		StaffOtp otp = generateStaffOtp(staff);

		try {

			// CHECK OTP CHANNEL
			OtpChannel channel = request.getOtpChannel();

			if (channel == null) {
				throw new InvalidOtpChannelException("OTP channel must be specified: EMAIL or MOBILE");
			}

			switch (channel) {

			case EMAIL:

				// SEND OTP EMAIL
				emailService.sendOtpEmail(staff.getEmailId(), otp.getOtp(), OtpPurpose.LOGIN);

				logger.info("Login OTP sent successfully to email: {}", staff.getEmailId());

				break;

			case MOBILE:

				// SEND OTP SMS USING TWILIO
				smsService.sendOtpSms(staff.getMobileNum(), otp.getOtp(), OtpPurpose.STAFF_LOGIN);

				logger.info("Login OTP sent successfully to mobile: {}", staff.getMobileNum());

				break;

			default:

				throw new InvalidOtpChannelException("Invalid OTP channel: " + channel);
			}

			otp.setStatus(OtpStatus.SENT);

			otp.setUpdatedDt(LocalDateTime.now());

			staffOtpRepository.save(otp);

		} catch (Exception e) {

			logger.error("Failed to send login OTP for staffId: {}", staff.getStaffId(), e);

			otp.setStatus(OtpStatus.FAILED);

			otp.setUpdatedDt(LocalDateTime.now());

			staffOtpRepository.save(otp);

			throw new RuntimeException("Failed to send OTP");
		}

		// RESPONSE
		StaffLoginResponse response = new StaffLoginResponse();

		response.setRole(role);
		response.setStaffId(staff.getStaffId());
		response.setEmail(staff.getEmailId());

		// TOKEN SHOULD BE NULL BEFORE OTP VERIFICATION
		response.setToken(null);

		response.setMessage("OTP sent successfully via " + request.getOtpChannel());

		logger.info("OTP sent successfully for staffId: {}", staff.getStaffId());

		return response;
	}
}