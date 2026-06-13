package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.RegistrationResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.dto.response.StudentResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.OtpSendingException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StudentMapper;
import com.dmantz.lms.mapper.StudentRegistrationOtpMapper;
import com.dmantz.lms.repository.StudentOtpRepository;
import com.dmantz.lms.repository.StudentRegistrationOtpRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.SmsService;
import com.dmantz.lms.service.StudentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger logger = LogManager.getLogger(StudentServiceImpl.class);

	private final StudentRepository studentRepository;
	private final StudentOtpRepository otpRepository;
	private final StudentMapper studentMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final StudentOtpRepository studentOtpRepository;
	private final JwtUtil jwtUtil;
	private final SmsService smsService;
	private final StudentRegistrationOtpRepository studentRegistrationOtpRepository;
	private final StudentRegistrationOtpMapper studentRegistrationOtpMapper;

	@Value("${strapi.url}")
	private String strapiUrl;

	@Value("${strapi.api.token}")
	private String strapiApiToken;

	private final RestTemplate restTemplate = new RestTemplate();

	public StudentServiceImpl(StudentRepository studentRepository, StudentOtpRepository otpRepository,
                              StudentMapper studentMapper, BCryptPasswordEncoder passwordEncoder, EmailService emailService,
                              StudentOtpRepository studentOtpRepository, JwtUtil jwtUtil, SmsService smsService, StudentRegistrationOtpRepository studentRegistrationOtpRepository, StudentRegistrationOtpMapper studentRegistrationOtpMapper) {
		this.studentRepository = studentRepository;
		this.otpRepository = otpRepository;
		this.studentMapper = studentMapper;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.studentOtpRepository = studentOtpRepository;
		this.jwtUtil = jwtUtil;
		this.smsService = smsService;
        this.studentRegistrationOtpRepository = studentRegistrationOtpRepository;
        this.studentRegistrationOtpMapper = studentRegistrationOtpMapper;
    }

	@Override
	@Transactional
	public RegistrationResponse register(StudentRegistrationRequest request) {

		logger.info("Registration started for email: {}", request.getEmailId());

		if (studentRepository.existsByEmailId(request.getEmailId())) {
			throw new DuplicateValuesException("Email already exists");
		}

		if (studentRepository.existsByMobileNum(request.getMobileNum())) {
			throw new DuplicateValuesException("Mobile number already exists");
		}

		StudentRegistrationOTP registration = studentRegistrationOtpMapper.toEntity(request);
		registration.setPassword(passwordEncoder.encode(request.getPassword()));
		registration.setCurrentStatus(request.getCurrentStatus());

		StudentRegistrationOTP savedRegistration =
				studentRegistrationOtpRepository.save(registration);

		logger.info("Registration data saved for email: {}",
				savedRegistration.getEmailId());

		StudentOtp otp = generateOtp(savedRegistration);

		try {
			// Email OTP
			emailService.sendOtpEmail(savedRegistration.getEmailId(),
					otp.getOtp(),
					OtpPurpose.REGISTRATION);

//			// SMS OTP
//			smsService.sendOtp(
//					savedRegistration.getMobileNum(),
//					otp.getOtp());

			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());

			otpRepository.save(otp);

			logger.info("OTP sent successfully to email {} and mobile {}",
					savedRegistration.getEmailId(),
					savedRegistration.getMobileNum());

		} catch (Exception ex) {

			otp.setStatus(OtpStatus.FAILED);
			otp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(otp);
			logger.error("Failed to send OTP", ex);

			throw new OtpSendingException("Failed to send OTP: " + ex.getMessage(), ex);
		}

		RegistrationResponse response = new RegistrationResponse();

		response.setEmailId(savedRegistration.getEmailId());
		response.setMobileNum(savedRegistration.getMobileNum());
		response.setStatus("SUCCESS");
		response.setMessage("OTP sent successfully");
		return response;
	}

	@Override
	@Transactional
	public StudentResponse verifyOtp(OtpVerifyRequest request) {

		logger.info("OTP verification started for email: {}", request.getEmailId());

		StudentOtp otpEntity = otpRepository
				.findTopByEmailIdOrderByCreatedDtDesc(request.getEmailId())
				.orElseThrow(() -> new ResourceNotFoundException("OTP not found"));

		if (!otpEntity.getOtp().equals(request.getOtp())) {

			otpEntity.setAttemptsNum(otpEntity.getAttemptsNum() + 1);
			otpRepository.save(otpEntity);
			throw new RuntimeException("Invalid OTP");
		}

		StudentRegistrationOTP registration =
				studentRegistrationOtpRepository.findByEmailId(request.getEmailId())
						.orElseThrow(() -> new ResourceNotFoundException("Registration data not found"));

		Student student = new Student();
		student.setStudentId(generateStudentId());
		student.setLoginId(registration.getEmailId());

		student.setFirstNm(registration.getFirstNm());
		student.setLastNm(registration.getLastNm());
		student.setEmailId(registration.getEmailId());
		student.setMobileNum(registration.getMobileNum());
		student.setPassword(registration.getPassword());

		student.setStatus(registration.getCurrentStatus());
		student.setEnabled("Y");

		// Default values
		student.setGender("NOT_SET");
		student.setDob(LocalDate.of(2000, 1, 1));
		student.setAddr1("NOT_SET");
		student.setCity("NOT_SET");
		student.setState("NOT_SET");
		student.setCountry("NOT_SET");

		Student savedStudent = studentRepository.save(student);
		otpEntity.setStatus(OtpStatus.VERIFIED);
		otpEntity.setUpdatedDt(LocalDateTime.now());

		otpRepository.save(otpEntity);
		studentRegistrationOtpRepository.delete(registration);

		logger.info("Student registration completed successfully. StudentId={}",
				savedStudent.getStudentId());

		return studentMapper.toResponse(savedStudent);
	}

	private String generateStudentId() {
		String lastId = studentRepository.findMaxStudentId();
		if (lastId == null || lastId.isBlank()) {
			return "S000001";
		}
		int number = Integer.parseInt(lastId.substring(1));
		return String.format("S%06d", number + 1);
	}

	private StudentOtp generateOtp(StudentRegistrationOTP registration) {

		logger.info("Generating OTP for email: {}", registration.getEmailId());

		StudentOtp otp = new StudentOtp();
		otp.setEmailId(registration.getEmailId());
		otp.setMobileNum(registration.getMobileNum());
		otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));
		otp.setStatus(OtpStatus.NEW);
		otp.setAttemptsNum(0);
		otp.setCreatedDt(LocalDateTime.now());
		StudentOtp savedOtp = otpRepository.save(otp);
		logger.info("OTP generated successfully for email: {}", registration.getEmailId());
		return savedOtp;
	}

	private StudentOtp generateOtp(String emailId, String mobileNum, OtpPurpose purpose) {

		logger.info("Generating OTP for email: {}", emailId);

		StudentOtp otp = new StudentOtp();
		otp.setEmailId(emailId);
		otp.setMobileNum(mobileNum);
		otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));
		otp.setStatus(OtpStatus.NEW);
		otp.setAttemptsNum(0);
		otp.setCreatedDt(LocalDateTime.now());

		StudentOtp savedOtp = otpRepository.save(otp);

		logger.info("OTP generated successfully for email: {}", emailId);

		return savedOtp;
	}

	@Override
	@Transactional
	public StudentLoginResponse login(StudentLoginRequest request) {

		logger.info("Login attempt for username: {}", request.getUsername());

		String username = request.getUsername();
		Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(
				username, username, username);

		if (student == null) {
			throw new RuntimeException("Invalid credentials");
		}

		if (!"Y".equalsIgnoreCase(student.getEnabled())) {
			throw new RuntimeException("Account is disabled");
		}

		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		StudentOtp otp = generateOtp(student.getEmailId(), student.getMobileNum(),
				OtpPurpose.LOGIN);
		try {
			emailService.sendOtpEmail(student.getEmailId(), otp.getOtp(), OtpPurpose.LOGIN);
			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(otp);
			logger.info("Login OTP sent successfully");

		} catch (Exception e) {
			otp.setStatus(OtpStatus.FAILED);
			otpRepository.save(otp);
			throw new RuntimeException("Failed to send OTP");
		}

		StudentLoginResponse response = studentMapper.toLoginResponse(student);
		response.setMessage("OTP sent successfully");
		return response;
	}

	@Override
	@Transactional
	public StudentLoginResponse verifyLoginOtp(OtpVerifyRequest request) {

		logger.info("Verifying login OTP for email: {}", request.getEmailId());

		Student student = studentRepository.findByEmailId(request.getEmailId())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		if (student == null) {
			throw new RuntimeException("Student not found");
		}

		StudentOtp otp = otpRepository
				.findTopByEmailIdOrderByCreatedDtDesc(request.getEmailId())
				.orElseThrow(() -> new RuntimeException("OTP not found"));

		if (otp == null) {
			throw new RuntimeException("OTP not found");
		}

		if (OtpStatus.VERIFIED.equals(otp.getStatus())) {
			throw new RuntimeException("OTP already used");
		}

		if (!otp.getOtp().equals(request.getOtp())) {
			otp.setAttemptsNum(otp.getAttemptsNum() == null ? 1 : otp.getAttemptsNum() + 1);
			otpRepository.save(otp);
			throw new RuntimeException("Invalid OTP");
		}

		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());
		otpRepository.save(otp);

		String token = jwtUtil.generateToken(student.getEmailId(),
				"STUDENT",
				String.valueOf(student.getStudentId())
		);

		StudentLoginResponse response = studentMapper.toLoginResponse(student);
		response.setToken(token);
		response.setMessage("Login successful");
		return response;
	}


//	@Override
//	public StudentResponse register(StudentRegistrationRequest request) {
//
//		logger.info("Registration started for email: {}", request.getEmailId());
//
//		if (studentRepository.existsByEmailId(request.getEmailId()))
//			throw new DuplicateValuesException("Email already exists");
//
//		if (studentRepository.existsByMobileNum(request.getMobileNum()))
//			throw new DuplicateValuesException("Mobile number already exists");
//
//		Student student = studentMapper.toEntity(request);
//		student.setStudentId(generateStudentId());
//		student.setLoginId(request.getEmailId());
//		student.setPassword(passwordEncoder.encode(request.getPassword()));
//		student.setStatus(request.getCurrentStatus());
//		student.setEnabled("N");
//
//		student.setGender("NOT_SET");
//		student.setDob(LocalDate.of(2000, 1, 1));
//		student.setAddr1("NOT_SET");
//		student.setCity("NOT_SET");
//		student.setState("NOT_SET");
//		student.setCountry("NOT_SET");
//
//		Student saved = studentRepository.save(student);
//		logger.info("Student saved: {}", saved.getStudentId());
//
//		StudentOtp otp = generateOtp(saved);
//
//		OtpChannel channel = request.getOtpChannel();
//
//		if (channel == null)
//			throw new InvalidOtpChannelException("OTP channel must be specified: EMAIL or MOBILE");
//
//		try {
//			switch (channel) {
//
//			case EMAIL:
//				if (saved.getEmailId() == null || saved.getEmailId().isBlank())
//					throw new InvalidOtpChannelException("Email not provided. Cannot send OTP via EMAIL.");
//				emailService.sendOtpEmail(saved.getEmailId(), otp.getOtp(), OtpPurpose.REGISTRATION);
//				logger.info("Registration OTP sent via EMAIL to: {}", saved.getEmailId());
//				break;
//
//			case MOBILE:
//				if (saved.getMobileNum() == null || saved.getMobileNum().isBlank())
//					throw new InvalidOtpChannelException("Mobile not provided. Cannot send OTP via MOBILE.");
//				smsService.sendOtp(saved.getMobileNum(), OtpPurpose.REGISTRATION);
//				logger.info("Registration OTP sent via MOBILE to: {}", saved.getMobileNum());
//				break;
//			}
//
//			otp.setStatus(OtpStatus.SENT);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//
//		} catch (InvalidOtpChannelException | SmsSendingException ex) {
//			otp.setStatus(OtpStatus.FAILED);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//			logger.error("OTP send failed via {}: {}", channel, ex.getMessage(), ex);
//			throw ex;
//
//		} catch (Exception ex) {
//			otp.setStatus(OtpStatus.FAILED);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//			logger.error("Unexpected error sending OTP via {}: {}", channel, ex.getMessage(), ex);
//			throw new OtpSendingException("Failed to send OTP via " + channel + ": " + ex.getMessage(), ex);
//		}
//
//		return studentMapper.toResponse(saved);
//	}

//	@Override
//	public void verifyRegistrationOtp(OtpVerifyRequest request) {
//
//	}

//	public void verifyRegistrationOtp(OtpVerifyRequest request) {
//
//		Student student = studentRepository.findByStudentId(request.getStudentId())
//				.orElseThrow(() -> new RuntimeException("Student not found"));
//
//		if (request.getChannel() == OtpChannel.EMAIL) {
//
//			// ✅ EMAIL OTP (DB-based, NOT Twilio)
//			StudentOtp otp = otpRepository.findTopByStudentOrderByCreatedDtDesc(student)
//					.orElseThrow(() -> new RuntimeException("OTP not found"));
//
//			if (otp.getCreatedDt().plusMinutes(5).isBefore(LocalDateTime.now())) {
//				otp.setStatus(OtpStatus.EXPIRED);
//				otpRepository.save(otp);
//				throw new RuntimeException("OTP expired");
//			}
//
//			if (!otp.getOtp().equals(request.getOtp())) {
//				throw new RuntimeException("Invalid OTP");
//			}
//
//			otp.setStatus(OtpStatus.VERIFIED);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//
//		} else if (request.getChannel() == OtpChannel.MOBILE) {
//
//			// ✅ MOBILE OTP (Twilio Verify)
//			boolean verified = smsService.verifyOtp(student.getMobileNum(), request.getOtp(), OtpPurpose.REGISTRATION);
//
//			if (!verified) {
//				throw new RuntimeException("Invalid OTP");
//			}
//		}
//
//
//		student.setEnabled("Y");
//		studentRepository.save(student);
//	}


	private String uploadToStrapi(MultipartFile file) {
		try {
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
			file.transferTo(tempFile);
			try {
				MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
				body.add("files", new FileSystemResource(tempFile));

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + strapiApiToken);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);

				ResponseEntity<String> response = restTemplate.exchange(strapiUrl + "/api/upload", HttpMethod.POST,
						new HttpEntity<>(body, headers), String.class);

				JsonNode root = new ObjectMapper().readTree(response.getBody());
				JsonNode fileNode = root.get(0);

				if (fileNode == null) {
					throw new RuntimeException("Invalid Strapi upload response: " + response.getBody());
				}

				String fileUrl = strapiUrl + fileNode.get("url").asText();
				logger.info("Profile image uploaded to Strapi: {}", fileUrl);
				return fileUrl;

			} finally {
				tempFile.delete();
			}
		} catch (Exception e) {
			logger.error("Failed to upload profile image to Strapi", e);
			throw new RuntimeException("Failed to upload profile image to Strapi", e);
		}
	}

	private void deleteFromStrapiByUrl(String fileUrl) {
		try {
			String urlPath = fileUrl.replace(strapiUrl, "");
			String searchUrl = strapiUrl + "/api/upload/files?filters[url][$eq]=" + urlPath;

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + strapiApiToken);

			ResponseEntity<String> searchResponse = restTemplate.exchange(searchUrl, HttpMethod.GET,
					new HttpEntity<>(headers), String.class);

			JsonNode root = new ObjectMapper().readTree(searchResponse.getBody());

			if (root == null || !root.isArray() || root.size() == 0) {
				logger.warn("Profile image not found in Strapi by URL: {}", urlPath);
				return;
			}

			Long strapiFileId = root.get(0).get("id").asLong();
			String deleteUrl = strapiUrl + "/api/upload/files/" + strapiFileId;

			restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

			logger.info("Profile image deleted from Strapi: {}", urlPath);

		} catch (Exception e) {
			logger.error("Failed to delete profile image from Strapi. URL: {}", fileUrl, e);

		}
	}

	@Override
	public StudentResponse updateStudentProfile(String studentId, StudentUpdateRequest request) {

		logger.info("Updating student profile for studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found for studentId: {}", studentId);
			return new RuntimeException("Student not found");
		});

		// Read profileImg directly from request
		MultipartFile profileImg = request.getProfileImg();
		if (profileImg != null && !profileImg.isEmpty()) {
			String oldImgUrl = student.getProfileImg();
			if (oldImgUrl != null && !oldImgUrl.isBlank()) {
				deleteFromStrapiByUrl(oldImgUrl);
			}
			String newImgUrl = uploadToStrapi(profileImg);
			student.setProfileImg(newImgUrl); // set directly on entity, not via request
		}

		studentMapper.updateStudentFromDto(request, student);
		studentRepository.save(student);

		logger.info("Student profile updated successfully for studentId: {}", studentId);

		return studentMapper.toResponse(student);
	}

	@Override
	public List<StudentResponse> getAllStudents() {

		logger.info("Fetching all students");

		List<StudentResponse> students = studentRepository.findAll().stream().map(studentMapper::toResponse)
				.collect(Collectors.toList());

		logger.info("Total students fetched: {}", students.size());

		return students;
	}


//	@Override
//	public StudentLoginResponse login(StudentLoginRequest request) {
//
//		logger.info("Login attempt for username: {}", request.getUsername());
//
//		String username = request.getUsername();
//
//		Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(username, username, username);
//
//		if (student == null) {
//			logger.warn("Invalid login credentials for username: {}", username);
//			throw new RuntimeException("Invalid login credentials");
//		}
//
//		if (!"Y".equals(student.getEnabled())) {
//			logger.warn("Account disabled for studentId: {}", student.getStudentId());
//			throw new RuntimeException("Account disabled");
//		}
//
//		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
//			logger.warn("Wrong password attempt for studentId: {}", student.getStudentId());
//			throw new RuntimeException("Invalid login credentials");
//		}
//
//		StudentOtp otp = generateOtp(student);
//
//		try {
//			emailService.sendOtpEmail(student.getEmailId(), otp.getOtp(), OtpPurpose.LOGIN);
//
//			otp.setStatus(OtpStatus.SENT);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//
//			logger.info("Login OTP sent successfully to email: {}", student.getEmailId());
//
//		} catch (Exception e) {
//
//			logger.error("Failed to send login OTP to email: {}", student.getEmailId(), e);
//
//			otp.setStatus(OtpStatus.FAILED);
//			otpRepository.save(otp);
//		}
//
//		StudentLoginResponse response = studentMapper.toLoginResponse(student);
//		response.setMessage("OTP sent to your registered email");
//
//		return response;
//	}

//	@Override
//	public StudentLoginResponse verifyLoginOtp(OtpVerifyRequest request) {
//
//		logger.info("OTP verification started for studentId: {}", request.getStudentId());
//
//		Student student = studentRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> {
//			logger.warn("Student not found with studentId: {}", request.getStudentId());
//			return new RuntimeException("Student not found");
//		});
//
//		// GET LATEST OTP
//		StudentOtp otp = otpRepository.findTopByStudentOrderByCreatedDtDesc(student).orElseThrow(() -> {
//			logger.warn("OTP not found for studentId: {}", student.getStudentId());
//			return new RuntimeException("OTP not found");
//		});
//
//		// INVALID OTP
//		if (!otp.getOtp().equals(request.getOtp())) {
//			logger.warn("Invalid OTP for studentId: {}", student.getStudentId());
//
//			otp.setAttemptsNum(otp.getAttemptsNum() + 1);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//			throw new RuntimeException("Invalid OTP");
//		}
//
//		// OTP EXPIRED
//		if (otp.getCreatedDt().plusMinutes(5).isBefore(LocalDateTime.now())) {
//			logger.warn("OTP expired for studentId: {}", student.getStudentId());
//			throw new RuntimeException("OTP expired");
//		}
//		// OTP VERIFIED
//		otp.setStatus(OtpStatus.VERIFIED);
//		otp.setUpdatedDt(LocalDateTime.now());
//		otpRepository.save(otp);
//		logger.info("OTP verified successfully for studentId: {}", student.getStudentId());
//
//		// GENERATE TOKEN
//		String token = jwtUtil.generateToken(student.getEmailId(), "STUDENT", student.getStudentId());
//
//		StudentLoginResponse response = new StudentLoginResponse();
//		response.setStudentId(student.getStudentId());
//		response.setEmail(student.getEmailId());
//		response.setRole("STUDENT");
//		response.setToken(token);
//		response.setMessage("Login successful");
//		return response;
//	}


	@Override
	public void forgotPassword(ForgotPasswordRequest request) {

//		logger.info("Forgot password request started for email: {}", request.getEmail());
//
//		Student student = studentRepository.findByEmailId(request.getEmail()).orElseThrow(() -> {
//			logger.error("Student not found for email: {}", request.getEmail());
//			return new RuntimeException("Student not found");
//		});
//
//		StudentOtp otp = generateOtp(student);
//
//		try {
//			emailService.sendOtpEmail(student.getLoginId(), otp.getOtp(), OtpPurpose.FORGOT_PASSWORD);
//
//			otp.setStatus(OtpStatus.SENT);
//			otp.setUpdatedDt(LocalDateTime.now());
//			otpRepository.save(otp);
//
//			logger.info("Forgot password OTP sent successfully to email: {}", student.getEmailId());
//
//		} catch (Exception e) {
//			otp.setStatus(OtpStatus.FAILED);
//			otpRepository.save(otp);
//
//			logger.error("Failed to send forgot password OTP to email: {}", student.getEmailId(), e);
//			throw new RuntimeException("Failed to send OTP");
//		}
	}

	@Override
	public void resetPassword(ResetPasswordRequest request) {

	}

//	@Override
//	@Transactional
//	public void resetPassword(ResetPasswordRequest request) {
//
//		logger.info("Reset password started for studentId: {}", request.getStudentId());
//
//		String studentId = request.getStudentId();
//
//		StudentOtp studentOtp = otpRepository.findLatestOtpByStudentId(studentId).stream().findFirst()
//				.orElseThrow(() -> {
//					logger.error("OTP not found for studentId: {}", studentId);
//					return new RuntimeException("OTP not found");
//				});
//
//		if (studentOtp.getStatus() == OtpStatus.VERIFIED) {
//			logger.warn("OTP already used for studentId: {}", studentId);
//			throw new RuntimeException("OTP already used");
//		}
//
//		if (studentOtp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(10))) {
//			studentOtp.setStatus(OtpStatus.EXPIRED);
//			otpRepository.save(studentOtp);
//
//			logger.warn("OTP expired for studentId: {}", studentId);
//			throw new RuntimeException("OTP expired");
//		}
//
//		if (!studentOtp.getOtp().equals(request.getOtp())) {
//			logger.warn("Invalid OTP entered for password reset, studentId: {}", studentId);
//			throw new RuntimeException("Invalid OTP");
//		}
//
//		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
//			logger.error("Student not found for studentId: {}", studentId);
//			return new RuntimeException("Student not found");
//		});
//
//		student.setPassword(passwordEncoder.encode(request.getNewPassword()));
//		studentRepository.save(student);
//
//		studentOtp.setStatus(OtpStatus.VERIFIED);
//		studentOtp.setUpdatedDt(LocalDateTime.now());
//		otpRepository.save(studentOtp);
//
//		emailService.sendOtpEmail(student.getEmailId(), null, OtpPurpose.PASSWORD_RESET_SUCCESS);
//
//		logger.info("Password reset successful for studentId: {}", studentId);
//	}

}
