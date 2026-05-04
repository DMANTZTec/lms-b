package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.dto.response.StudentResponse;
import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.entity.OtpStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentOtp;
import com.dmantz.lms.exceptions.OtpExpiredException;
import com.dmantz.lms.exceptions.OtpInvalidException;
import com.dmantz.lms.exceptions.OtpNotFoundException;
import com.dmantz.lms.mapper.StudentMapper;
import com.dmantz.lms.repository.StudentOtpRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.EmailService;
import com.dmantz.lms.service.StudentService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger logger =
			LogManager.getLogger(StudentServiceImpl.class);

	private final StudentRepository studentRepository;
	private final StudentOtpRepository otpRepository;
	private final StudentMapper studentMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final StudentOtpRepository studentOtpRepository;

	public StudentServiceImpl(StudentRepository studentRepository, StudentOtpRepository otpRepository,
			StudentMapper studentMapper, BCryptPasswordEncoder passwordEncoder, EmailService emailService,
			StudentOtpRepository studentOtpRepository) {
		this.studentRepository = studentRepository;
		this.otpRepository = otpRepository;
		this.studentMapper = studentMapper;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.studentOtpRepository = studentOtpRepository;
	}

	@Override
	public StudentResponse register(StudentRegistrationRequest request) {

		logger.info("Student registration started for email: {}", request.getEmailId());

		if (studentRepository.existsByEmailId(request.getEmailId())) {
			logger.error("Email already exists: {}", request.getEmailId());
			throw new RuntimeException("email already exists");
		}
		if (studentRepository.existsByMobileNum(request.getMobileNum())) {
			logger.error("Mobile number already exists: {}", request.getMobileNum());
			throw new RuntimeException("mobile number already exists");
		}

		// DTO → Entity
		Student student = studentMapper.toEntity(request);

		student.setStudentId(generateStudentId());
		student.setLoginId(request.getEmailId());

		student.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password

		// System fields
		student.setStatus("ACTIVE");
		student.setEnabled("Y");
		student.setProfileImg(request.getProfileImg());

		Student savedStudent = studentRepository.save(student);

		logger.info("Student registered successfully with studentId: {}",
				savedStudent.getStudentId());

		generateOtp(savedStudent);
		return studentMapper.toResponse(savedStudent);
	}

	private String generateStudentId() {

		Long count = studentRepository.count() + 1; // Get total count of students
		return String.format("S%06d", count); // Format as S + 6-digit number → always 7 characters
	}

	public StudentOtp generateOtp(Student student) {

		logger.info("Generating OTP for studentId: {}", student.getStudentId());

		StudentOtp otp = new StudentOtp();

		otp.setStudent(student);
		otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));
		otp.setStatus(OtpStatus.NEW);
		otp.setAttemptsNum(0);
		otp.setCreatedDt(LocalDateTime.now());

		StudentOtp savedOtp = otpRepository.save(otp);

		logger.info("OTP generated successfully for studentId: {}",
				student.getStudentId());

		return savedOtp;
	}

	@Override
	public StudentLoginResponse login(StudentLoginRequest request) {

		logger.info("Login attempt for username: {}", request.getUsername());

		String username = request.getUsername();

		Student student = studentRepository
				.findByEmailIdOrMobileNumOrLoginId(username, username, username);

		if (student == null) {
			logger.warn("Invalid login credentials for username: {}", username);
			throw new RuntimeException("Invalid login credentials");
		}

		if (!"Y".equals(student.getEnabled())) {
			logger.warn("Account disabled for studentId: {}", student.getStudentId());
			throw new RuntimeException("Account disabled");
		}

		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
			logger.warn("Wrong password attempt for studentId: {}", student.getStudentId());
			throw new RuntimeException("Invalid login credentials");
		}

		StudentOtp otp = generateOtp(student);

		try {
			emailService.sendOtpEmail(
					student.getEmailId(),
					otp.getOtp(),
					OtpPurpose.LOGIN
			);

			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(otp);

			logger.info("Login OTP sent successfully to email: {}",
					student.getEmailId());

		} catch (Exception e) {

			logger.error("Failed to send login OTP to email: {}",
					student.getEmailId(), e);

			otp.setStatus(OtpStatus.FAILED);
			otpRepository.save(otp);
		}

		StudentLoginResponse response = studentMapper.toLoginResponse(student);
		response.setMessage("OTP sent to your registered email");

		return response;
	}

	@Override
	public OtpVerifyResponse verifyOtp(OtpVerifyRequest request) {

		logger.info("OTP verification started for studentId: {}", request.getStudentId());

		StudentOtp otp = studentOtpRepository
				.findByStudent_StudentIdOrderByCreatedDtDesc(request.getStudentId())
				.stream()
				.findFirst()
				.orElseThrow(() -> {
					logger.error("OTP not found for studentId: {}", request.getStudentId());
					return new OtpNotFoundException("OTP not found");
				});

		// Only SENT OTPs are valid
		if (otp.getStatus() != OtpStatus.SENT) {
			logger.warn("Invalid OTP status for studentId: {}", request.getStudentId());
			throw new OtpInvalidException("OTP is not valid");
		}

		// Expiry check (5 mins)
		if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {
			otp.setStatus(OtpStatus.EXPIRED);
			otp.setUpdatedDt(LocalDateTime.now());
			studentOtpRepository.save(otp);

			logger.warn("OTP expired for studentId: {}", request.getStudentId());
			throw new OtpExpiredException("OTP expired");
		}

		// Match OTP
		if (!otp.getOtp().equals(request.getOtp())) {
			otp.setAttemptsNum(otp.getAttemptsNum() + 1);
			otp.setUpdatedDt(LocalDateTime.now());
			studentOtpRepository.save(otp);

			logger.warn("Invalid OTP entered for studentId: {}", request.getStudentId());
			throw new OtpInvalidException("Invalid OTP");
		}

		// Success
		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());
		studentOtpRepository.save(otp);

		logger.info("OTP verified successfully for studentId: {}", request.getStudentId());

		OtpVerifyResponse response = new OtpVerifyResponse();
		response.setVerified(true);
		response.setMessage("OTP verified successfully");

		return response;
	}

	@Override
	public List<StudentResponse> getAllStudents() {

		logger.info("Fetching all students");

		List<StudentResponse> students = studentRepository.findAll()
				.stream()
				.map(studentMapper::toResponse)
				.collect(Collectors.toList());

		logger.info("Total students fetched: {}", students.size());

		return students;
	}

	@Override
	public void forgotPassword(ForgotPasswordRequest request) {

		logger.info("Forgot password request started for email: {}", request.getEmail());

		Student student = studentRepository.findByEmailId(request.getEmail())
				.orElseThrow(() -> {
					logger.error("Student not found for email: {}", request.getEmail());
					return new RuntimeException("Student not found");
				});

		StudentOtp otp = generateOtp(student);

		try {
			emailService.sendOtpEmail(
					student.getLoginId(),
					otp.getOtp(),
					OtpPurpose.FORGOT_PASSWORD
			);

			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(otp);

			logger.info("Forgot password OTP sent successfully to email: {}", student.getEmailId());

		} catch (Exception e) {
			otp.setStatus(OtpStatus.FAILED);
			otpRepository.save(otp);

			logger.error("Failed to send forgot password OTP to email: {}", student.getEmailId(), e);
			throw new RuntimeException("Failed to send OTP");
		}
	}

	@Override
	@Transactional
	public void resetPassword(ResetPasswordRequest request) {

		logger.info("Reset password started for studentId: {}", request.getStudentId());

		String studentId = request.getStudentId();

		StudentOtp studentOtp = otpRepository.findLatestOtpByStudentId(studentId)
				.stream()
				.findFirst()
				.orElseThrow(() -> {
					logger.error("OTP not found for studentId: {}", studentId);
					return new RuntimeException("OTP not found");
				});

		if (studentOtp.getStatus() == OtpStatus.VERIFIED) {
			logger.warn("OTP already used for studentId: {}", studentId);
			throw new RuntimeException("OTP already used");
		}

		if (studentOtp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(10))) {
			studentOtp.setStatus(OtpStatus.EXPIRED);
			otpRepository.save(studentOtp);

			logger.warn("OTP expired for studentId: {}", studentId);
			throw new RuntimeException("OTP expired");
		}

		if (!studentOtp.getOtp().equals(request.getOtp())) {
			logger.warn("Invalid OTP entered for password reset, studentId: {}", studentId);
			throw new RuntimeException("Invalid OTP");
		}

		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> {
					logger.error("Student not found for studentId: {}", studentId);
					return new RuntimeException("Student not found");
				});

		student.setPassword(passwordEncoder.encode(request.getNewPassword()));
		studentRepository.save(student);

		studentOtp.setStatus(OtpStatus.VERIFIED);
		studentOtp.setUpdatedDt(LocalDateTime.now());
		otpRepository.save(studentOtp);

		emailService.sendOtpEmail(
				student.getEmailId(),
				null,
				OtpPurpose.PASSWORD_RESET_SUCCESS
		);

		logger.info("Password reset successful for studentId: {}", studentId);
	}

	@Override
	public StudentResponse updateStudentProfile(String studentId, StudentUpdateRequest request) {

		logger.info("Updating student profile for studentId: {}", studentId);

		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> {
					logger.error("Student not found for studentId: {}", studentId);
					return new RuntimeException("Student not found");
				});

		studentMapper.updateStudentFromDto(request, student);

		studentRepository.save(student);

		logger.info("Student profile updated successfully for studentId: {}", studentId);

		return studentMapper.toResponse(student);
	}

}
