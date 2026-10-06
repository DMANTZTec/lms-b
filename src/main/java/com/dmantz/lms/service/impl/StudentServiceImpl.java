package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.RegistrationResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.dto.response.StudentResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.InvalidPasswordException;
import com.dmantz.lms.exceptions.OtpSendingException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.StudentNotFoundException;
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

	@Value("${strapi.upload.folder-id:2}")
	private Long strapiUploadFolderId;

	private final RestTemplate restTemplate = new RestTemplate();

	public StudentServiceImpl(StudentRepository studentRepository, StudentOtpRepository otpRepository,
			StudentMapper studentMapper, BCryptPasswordEncoder passwordEncoder, EmailService emailService,
			StudentOtpRepository studentOtpRepository, JwtUtil jwtUtil, SmsService smsService,
			StudentRegistrationOtpRepository studentRegistrationOtpRepository,
			StudentRegistrationOtpMapper studentRegistrationOtpMapper) {
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

		// remove any old/stale pending registration rows for this email or mobile
		List<StudentRegistrationOTP> oldPending = studentRegistrationOtpRepository
				.findAllByEmailIdOrMobileNum(request.getEmailId(), request.getMobileNum());
		if (!oldPending.isEmpty()) {
			studentRegistrationOtpRepository.deleteAll(oldPending);
			logger.info("Removed {} stale pending registration(s) for email/mobile: {} / {}", oldPending.size(),
					request.getEmailId(), request.getMobileNum());
		}

		// Save registration data
		StudentRegistrationOTP registration = studentRegistrationOtpMapper.toEntity(request);
		registration.setPassword(passwordEncoder.encode(request.getPassword()));
		registration.setCurrentStatus(request.getCurrentStatus());
		StudentRegistrationOTP savedRegistration = studentRegistrationOtpRepository.save(registration);
		logger.info("Registration data saved for email: {}", savedRegistration.getEmailId());

		// Generate OTP
		StudentOtp otp = generateOtp(savedRegistration);

		// Send the same OTP to both email and mobile number
		boolean emailSent = false;
		boolean smsSent = false;
		Exception lastError = null;

		try {
			emailService.sendOtpEmail(savedRegistration.getEmailId(), otp.getOtp(), OtpPurpose.REGISTRATION);
			emailSent = true;
			logger.info("Registration OTP sent via EMAIL to: {}", savedRegistration.getEmailId());
		} catch (Exception ex) {
			lastError = ex;
			logger.error("Failed to send registration OTP via EMAIL to: {}", savedRegistration.getEmailId(), ex);
		}

		try {
			smsService.sendOtpSms(savedRegistration.getMobileNum(), otp.getOtp(), OtpPurpose.REGISTRATION);
			smsSent = true;
			logger.info("Registration OTP sent via MOBILE to: {}", savedRegistration.getMobileNum());
		} catch (Exception ex) {
			lastError = ex;
			logger.error("Failed to send registration OTP via MOBILE to: {}", savedRegistration.getMobileNum(), ex);
		}

		otp.setUpdatedDt(LocalDateTime.now());

		// Fail only if the OTP could not be delivered to either email or mobile
		if (!emailSent && !smsSent) {
			otp.setStatus(OtpStatus.FAILED);
			otpRepository.save(otp);
			throw new OtpSendingException("Failed to send OTP to email and mobile number: " + lastError.getMessage(),
					lastError);
		}

		otp.setStatus(OtpStatus.SENT);
		otpRepository.save(otp);

		String sentVia = emailSent && smsSent ? "email and mobile number" : emailSent ? "email" : "mobile number";

		RegistrationResponse response = new RegistrationResponse();
		response.setEmailId(savedRegistration.getEmailId());
		response.setMobileNum(savedRegistration.getMobileNum());
		response.setStatus("SUCCESS");
		response.setMessage("OTP sent to " + sentVia);
		return response;
	}

	@Override
	@Transactional
	public StudentResponse verifyOtp(StudentOtpVerifyRequest request) {

		String identifier = request.getEmailIdOrMobileNo();
		logger.info("OTP verification started for identifier: {}", identifier);

		StudentOtp otpEntity = otpRepository.findTopByEmailIdOrMobileNumOrderByCreatedDtDesc(identifier, identifier)
				.orElseThrow(() -> new ResourceNotFoundException("OTP not found"));

		if (!otpEntity.getOtp().equals(request.getOtp())) {

			otpEntity.setAttemptsNum(otpEntity.getAttemptsNum() + 1);
			otpRepository.save(otpEntity);
			throw new RuntimeException("Invalid OTP");
		}

		StudentRegistrationOTP registration = studentRegistrationOtpRepository
				.findFirstByEmailIdOrMobileNumOrderByIdDesc(identifier, identifier)
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
		student.setState(null);
		student.setCountry(null);

		Student savedStudent = studentRepository.save(student);
		otpEntity.setStatus(OtpStatus.VERIFIED);
		otpEntity.setUpdatedDt(LocalDateTime.now());

		otpRepository.save(otpEntity);
		studentRegistrationOtpRepository.delete(registration);

		logger.info("Student registration completed successfully. StudentId={}", savedStudent.getStudentId());

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

	// Sends the OTP to the mobile number if the student entered their mobile number,
	// otherwise to the email. Returns where it was sent.
	private String sendOtpToIdentifier(String identifier, Student student, StudentOtp otp, OtpPurpose purpose) {

		try {
			if (identifier.equals(student.getMobileNum())) {
				smsService.sendOtpSms(student.getMobileNum(), otp.getOtp(), purpose);
				logger.info("{} OTP sent to mobile: {}", purpose, student.getMobileNum());
				markOtp(otp, OtpStatus.SENT);
				return "mobile number";
			}

			if (student.getEmailId() == null || student.getEmailId().isBlank()) {
				throw new BadRequestException("Email not available for this account. Please use your mobile number.");
			}
			emailService.sendOtpEmail(student.getEmailId(), otp.getOtp(), purpose);
			logger.info("{} OTP sent to email: {}", purpose, student.getEmailId());
			markOtp(otp, OtpStatus.SENT);
			return "email";

		} catch (BadRequestException ex) {
			markOtp(otp, OtpStatus.FAILED);
			throw ex;

		} catch (Exception ex) {
			markOtp(otp, OtpStatus.FAILED);
			logger.error("Failed to send {} OTP for identifier {}: {}", purpose, identifier, ex.getMessage(), ex);
			throw new OtpSendingException("Failed to send OTP: " + ex.getMessage(), ex);
		}
	}

	private void markOtp(StudentOtp otp, OtpStatus status) {
		otp.setStatus(status);
		otp.setUpdatedDt(LocalDateTime.now());
		otpRepository.save(otp);
	}

	@Override
	@Transactional
	public StudentLoginResponse login(StudentLoginRequest request) {

		logger.info("Login attempt for username: {}", request.getUsername());

		// ── 1. Look up student ───────────────────────────────────
		String username = request.getUsername();
		Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(username, username, username);

		if (student == null) {
			throw new RuntimeException("Invalid credentials");
		}

		if (!"Y".equalsIgnoreCase(student.getEnabled())) {
			throw new RuntimeException("Account is disabled");
		}

		// ── 2. Verify password ───────────────────────────────────
		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		// ── 3. Generate OTP ──────────────────────────────────────
		StudentOtp otp = generateOtp(student.getEmailId(), student.getMobileNum(), OtpPurpose.LOGIN);

		// ── 4. Send OTP to the entered email / mobile number ─────
		String sentTo = sendOtpToIdentifier(username, student, otp, OtpPurpose.LOGIN);

		// ── 5. Build response ────────────────────────────────────
		StudentLoginResponse response = studentMapper.toLoginResponse(student);
		response.setMessage("OTP sent to " + sentTo);
		return response;
	}

	@Override
	@Transactional
	public StudentLoginResponse verifyLoginOtp(StudentOtpVerifyRequest request) {

		String identifier = request.getEmailIdOrMobileNo();
		logger.info("Verifying login OTP for identifier: {}", identifier);

		// ── 1. Find student ──────────────────────────────────────
		Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(identifier, identifier, identifier);
		if (student == null) {
			throw new StudentNotFoundException("Student not found");
		}

		// ── 2. Find latest OTP ───────────────────────────────────
		StudentOtp otp = otpRepository.findLatestByIdentifier(identifier)
				.orElseThrow(() -> new RuntimeException("OTP not found"));

		// ── 3. Check already used ────────────────────────────────
		if (OtpStatus.VERIFIED.equals(otp.getStatus())) {
			throw new RuntimeException("OTP already used");
		}

		// ── 4. Validate OTP value ────────────────────────────────
		if (!otp.getOtp().equals(request.getOtp())) {
			otp.setAttemptsNum(otp.getAttemptsNum() == null ? 1 : otp.getAttemptsNum() + 1);
			otpRepository.save(otp);
			throw new RuntimeException("Invalid OTP");
		}

		// ── 5. Mark verified and generate JWT ────────────────────
		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());
		otpRepository.save(otp);

		String token = jwtUtil.generateToken(student.getEmailId(), "STUDENT", String.valueOf(student.getStudentId()));

		StudentLoginResponse response = studentMapper.toLoginResponse(student);
		response.setToken(token);
		response.setMessage("Login successful");
		return response;
	}

	private String uploadToStrapi(MultipartFile file) {
		try {
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
			file.transferTo(tempFile);
			try {
				MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
				body.add("files", new FileSystemResource(tempFile));

				// Save uploaded file inside Strapi Media Library -> LMS Content
				// (applied by lms-strapi/src/extensions/upload/strapi-server.js)
				body.add("fileInfo", "{\"folder\":" + strapiUploadFolderId + "}");

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
	public StudentResponse updateStudent(String studentId, StudentUpdateRequest request) {

		logger.info("Updating student profile for studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found for studentId: {}", studentId);
			return new RuntimeException("Student not found");
		});

		studentMapper.updateStudentFromDto(request, student);
		studentRepository.save(student);

		logger.info("Student profile updated successfully for studentId: {}", studentId);

		return studentMapper.toResponse(student);
	}

	@Override
	public StudentResponse updateProfileImage(String studentId, MultipartFile file) {

		logger.info("Updating profile image for studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found for studentId: {}", studentId);
			return new StudentNotFoundException("Student not found for studentId: " + studentId);
		});

		if (file == null || file.isEmpty()) {
			throw new BadRequestException("Profile image is required.");
		}

		// Delete old image from Strapi, if present
		String oldImgUrl = student.getProfileImg();
		if (oldImgUrl != null && !oldImgUrl.isBlank()) {
			deleteFromStrapiByUrl(oldImgUrl);
		}

		// Upload new image to Strapi
		String newImgUrl = uploadToStrapi(file);
		student.setProfileImg(newImgUrl);

		Student savedStudent = studentRepository.save(student);

		logger.info("Profile image updated successfully for studentId: {}", studentId);

		return studentMapper.toResponse(savedStudent);
	}

	@Override
	public List<StudentResponse> getAllStudents() {

		logger.info("Fetching all students");

		List<StudentResponse> students = studentRepository.findAll().stream().map(studentMapper::toResponse)
				.collect(Collectors.toList());

		logger.info("Total students fetched: {}", students.size());

		return students;
	}

	@Override
	public String forgotPassword(ForgotPasswordRequest request) {

		String identifier = request.getEmailIdOrMobileNo();
		logger.info("Forgot password requested for identifier: {}", identifier);

		Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(identifier, identifier, identifier);
		if (student == null) {
			throw new StudentNotFoundException("Student not found with given email or mobile number");
		}

		if (!"Y".equalsIgnoreCase(student.getEnabled())) {
			throw new RuntimeException("Account is disabled");
		}

		StudentOtp otp = generateOtp(student.getEmailId(), student.getMobileNum(), OtpPurpose.FORGOT_PASSWORD);

		String sentTo = sendOtpToIdentifier(identifier, student, otp, OtpPurpose.FORGOT_PASSWORD);

		logger.info("Forgot password OTP sent successfully to {} for identifier: {}", sentTo, identifier);
		return "OTP sent to " + sentTo;

	}

	@Override
	@Transactional
	public void resetPassword(ResetPasswordRequest request) {

		logger.info("Reset password started for emailIdOrMobileNo: {}", request.getEmailIdOrMobileNo());

		String emailIdOrMobileNo = request.getEmailIdOrMobileNo();

		Student student = studentRepository.findByEmailIdOrMobileNum(emailIdOrMobileNo, emailIdOrMobileNo)
				.orElseThrow(() -> {
					logger.error("Student not found for emailIdOrMobileNo: {}", emailIdOrMobileNo);
					return new RuntimeException("Student not found");
				});

		StudentOtp studentOtp = otpRepository
				.findTopByEmailIdOrMobileNumOrderByCreatedDtDesc(student.getEmailId(), student.getMobileNum())
				.orElseThrow(() -> {
					logger.error("OTP not found for emailIdOrMobileNo: {}", emailIdOrMobileNo);
					return new RuntimeException("OTP not found");
				});

		if (studentOtp.getStatus() == OtpStatus.VERIFIED) {
			logger.warn("OTP already used for emailIdOrMobileNo: {}", emailIdOrMobileNo);
			throw new RuntimeException("OTP already used");
		}

		if (studentOtp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(10))) {
			studentOtp.setStatus(OtpStatus.EXPIRED);
			studentOtp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(studentOtp);

			logger.warn("OTP expired for emailIdOrMobileNo: {}", emailIdOrMobileNo);
			throw new RuntimeException("OTP expired");
		}

		if (!studentOtp.getOtp().equals(request.getOtp())) {
			logger.warn("Invalid OTP entered for password reset, emailIdOrMobileNo: {}", emailIdOrMobileNo);
			throw new RuntimeException("Invalid OTP");
		}

		student.setPassword(passwordEncoder.encode(request.getNewPassword()));
		studentRepository.save(student);

		studentOtp.setStatus(OtpStatus.VERIFIED);
		studentOtp.setUpdatedDt(LocalDateTime.now());
		otpRepository.save(studentOtp);

		// Notify via EMAIL if available
		if (student.getEmailId() != null && !student.getEmailId().isBlank()) {
			try {
				emailService.sendOtpEmail(student.getEmailId(), null, OtpPurpose.PASSWORD_RESET_SUCCESS);
				logger.info("Password reset success email sent to: {}", student.getEmailId());
			} catch (Exception ex) {
				logger.error("Failed to send password reset success email to: {}", student.getEmailId(), ex);
			}
		}

		// Notify via SMS if available
		if (student.getMobileNum() != null && !student.getMobileNum().isBlank()) {
			try {
				smsService.sendOtpSms(student.getMobileNum(), null, OtpPurpose.PASSWORD_RESET_SUCCESS);
				logger.info("Password reset success SMS sent to: {}", student.getMobileNum());
			} catch (Exception ex) {
				logger.error("Failed to send password reset success SMS to: {}", student.getMobileNum(), ex);
			}
		}

		logger.info("Password reset successful for emailIdOrMobileNo: {}", emailIdOrMobileNo);
	}

	@Override
	public StudentResponse getStudentById(String studentId) {

		logger.info("Fetching student with studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found for studentId: {}", studentId);
			return new StudentNotFoundException("Student not found for studentId: " + studentId);
		});

		logger.info("Student fetched successfully for studentId: {}", studentId);

		return studentMapper.toResponse(student);
	}

	@Override
	@Transactional
	public RegistrationResponse resendOtp(ResendOtpRequest request) {

		logger.info("Resend OTP requested for purpose: {}", request.getPurpose());

		String emailId = request.getEmailId();
		String mobileNum = request.getMobileNum();

		if ((emailId == null || emailId.isBlank()) && (mobileNum == null || mobileNum.isBlank())) {
			throw new IllegalArgumentException("Either emailId or mobileNum must be provided");
		}

		boolean hasEmail = emailId != null && !emailId.isBlank();
		boolean hasMobile = mobileNum != null && !mobileNum.isBlank();

		// Generate new OTP
		StudentOtp otp = generateOtp(emailId, mobileNum, request.getPurpose());

		// Send the same OTP to whichever of email / mobile number was provided
		boolean emailSent = false;
		boolean smsSent = false;
		Exception lastError = null;

		if (hasEmail) {
			try {
				emailService.sendOtpEmail(emailId, otp.getOtp(), request.getPurpose());
				emailSent = true;
				logger.info("OTP resent to email: {}", emailId);
			} catch (Exception ex) {
				lastError = ex;
				logger.error("Failed to resend OTP to email: {}", emailId, ex);
			}
		}

		if (hasMobile) {
			try {
				smsService.sendOtpSms(mobileNum, otp.getOtp(), request.getPurpose());
				smsSent = true;
				logger.info("OTP resent to mobile: {}", mobileNum);
			} catch (Exception ex) {
				lastError = ex;
				logger.error("Failed to resend OTP to mobile: {}", mobileNum, ex);
			}
		}

		if (!emailSent && !smsSent) {
			markOtp(otp, OtpStatus.FAILED);
			throw new OtpSendingException("Failed to resend OTP: " + lastError.getMessage(), lastError);
		}

		markOtp(otp, OtpStatus.SENT);

		String sentTo = emailSent && smsSent ? "email and mobile number" : emailSent ? "email" : "mobile number";

		RegistrationResponse response = new RegistrationResponse();
		response.setEmailId(emailId);
		response.setMobileNum(mobileNum);
		response.setStatus("SUCCESS");
		response.setMessage("OTP sent to " + sentTo);
		return response;
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordRequest request) {

		String studentId = request.getStudentId();
		logger.info("Change password requested for studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> {
			logger.error("Student not found for studentId: {}", studentId);
			return new StudentNotFoundException("Student not found for studentId: " + studentId);
		});

		if (!"Y".equalsIgnoreCase(student.getEnabled())) {
			logger.warn("Change password blocked - account disabled for studentId: {}", studentId);
			throw new InvalidPasswordException("Account is disabled");
		}

		if (!passwordEncoder.matches(request.getOldPassword(), student.getPassword())) {
			logger.warn("Change password failed - incorrect old password for studentId: {}", studentId);
			throw new InvalidPasswordException("Old password is incorrect");
		}

		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			logger.warn("Change password failed - new password and confirm password do not match for studentId: {}",
					studentId);
			throw new InvalidPasswordException("New password and confirm password do not match");
		}

		if (passwordEncoder.matches(request.getNewPassword(), student.getPassword())) {
			logger.warn("Change password failed - new password same as old password for studentId: {}", studentId);
			throw new InvalidPasswordException("New password must be different from old password");
		}

		student.setPassword(passwordEncoder.encode(request.getNewPassword()));
		studentRepository.save(student);

		logger.info("Password changed successfully for studentId: {}", studentId);

		// Notify via EMAIL if available
		if (student.getEmailId() != null && !student.getEmailId().isBlank()) {
			try {
				emailService.sendOtpEmail(student.getEmailId(), null, OtpPurpose.PASSWORD_CHANGE_SUCCESS);
				logger.info("Password change notification email sent to: {}", student.getEmailId());
			} catch (Exception ex) {
				logger.error("Failed to send password change notification email to: {}", student.getEmailId(), ex);
			}
		}

		// Notify via SMS if available
		if (student.getMobileNum() != null && !student.getMobileNum().isBlank()) {
			try {
				smsService.sendOtpSms(student.getMobileNum(), null, OtpPurpose.PASSWORD_CHANGE_SUCCESS);
				logger.info("Password change notification SMS sent to: {}", student.getMobileNum());
			} catch (Exception ex) {
				logger.error("Failed to send password change notification SMS to: {}", student.getMobileNum(), ex);
			}
		}
	}
	@Override
	@Transactional
	public void deleteProfileImage(String studentId) {

	    logger.info("Deleting profile image for studentId: {}", studentId);

	    Student student = studentRepository.findByStudentId(studentId)
	            .orElseThrow(() ->
	                    new StudentNotFoundException(
	                            "Student not found for studentId: " + studentId
	                    )
	            );

	    String profileImg = student.getProfileImg();

	    if (profileImg == null || profileImg.isBlank()) {
	        throw new BadRequestException("Student profile image not found.");
	    }

	    // Delete image from Strapi
	    deleteFromStrapiByUrl(profileImg);

	    // Remove image URL from student table
	    student.setProfileImg(null);

	    studentRepository.save(student);

	    logger.info("Profile image deleted successfully for studentId: {}", studentId);
	}
}
