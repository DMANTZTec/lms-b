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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

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

		if (studentRepository.existsByEmailId(request.getEmailId())) {
			throw new RuntimeException("email already exists");
		}
		if (studentRepository.existsByMobileNum(request.getMobileNum())) {
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

		Student savedStudent = studentRepository.save(student); // Save
		generateOtp(savedStudent);
		return studentMapper.toResponse(savedStudent);
	}

	private String generateStudentId() {

		Long count = studentRepository.count() + 1; // Get total count of students
		return String.format("S%06d", count); // Format as S + 6-digit number → always 7 characters
	}

	public StudentOtp generateOtp(Student student) {

		StudentOtp otp = new StudentOtp();

		otp.setStudent(student);
		otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000)); // 6-digit OTP
		otp.setStatus(OtpStatus.valueOf(String.valueOf(OtpStatus.NEW)));
		otp.setAttemptsNum(0);
		otp.setCreatedDt(LocalDateTime.now());

		return otpRepository.save(otp);
	}

	@Override
	public StudentLoginResponse login(StudentLoginRequest request) {

		// Extract username from request
		String username = request.getUsername();

		// Find student by email, mobile, or login ID
		Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(username, username, username);

		if (student == null) {
			throw new RuntimeException("Invalid login credentials");
		}

		// Check enabled
		if (!"Y".equals(student.getEnabled())) {
			throw new RuntimeException("Account disabled");
		}

		// Verify password
		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
			throw new RuntimeException("Invalid login credentials");
		}

		// Generate OTP
		StudentOtp otp = generateOtp(student);

		// Send OTP email
		try {
			emailService.sendOtpEmail(student.getEmailId(), otp.getOtp(), OtpPurpose.LOGIN);
			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(otp);

		} catch (Exception e) {
			System.out.println("OTP email failed");
			// optional
			otp.setStatus(OtpStatus.FAILED);
			otpRepository.save(otp);
		}
		// Response
		StudentLoginResponse response = studentMapper.toLoginResponse(student);
		response.setMessage("OTP sent to your registered email");

		return response;
	}

	@Override
	public OtpVerifyResponse verifyOtp(OtpVerifyRequest request) {

		StudentOtp otp = studentOtpRepository.findByStudent_StudentIdOrderByCreatedDtDesc(request.getStudentId())
				.stream().findFirst().orElseThrow(() -> new OtpNotFoundException("OTP not found"));

		// Only SENT OTPs are valid
		if (otp.getStatus() != OtpStatus.SENT) {
			throw new OtpInvalidException("OTP is not valid");
		}

		// Expiry check (5 mins)
		if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {
			otp.setStatus(OtpStatus.EXPIRED);
			otp.setUpdatedDt(LocalDateTime.now());
			studentOtpRepository.save(otp);
			throw new OtpExpiredException("OTP expired");
		}

		// Match OTP
		if (!otp.getOtp().equals(request.getOtp())) {
			otp.setAttemptsNum(otp.getAttemptsNum() + 1);
			otp.setUpdatedDt(LocalDateTime.now());
			studentOtpRepository.save(otp);
			throw new OtpInvalidException("Invalid OTP");
		}

		// Success
		otp.setStatus(OtpStatus.VERIFIED);
		otp.setUpdatedDt(LocalDateTime.now());
		studentOtpRepository.save(otp);

		OtpVerifyResponse response = new OtpVerifyResponse();
		response.setVerified(true);
		response.setMessage("OTP verified successfully");

		return response;
	}

	@Override
	public List<StudentResponse> getAllStudents() {
		return studentRepository.findAll().stream().map(studentMapper::toResponse) // map entity to DTO
				.collect(Collectors.toList());
	}

	@Override
	public void forgotPassword(ForgotPasswordRequest request) {

		// Make sure we use student_id here
		Student student = studentRepository.findByEmailId(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		// Generate OTP
		StudentOtp otp = generateOtp(student);

		try {
			emailService.sendOtpEmail(student.getLoginId(), // assuming login_id is the email
					otp.getOtp(), OtpPurpose.FORGOT_PASSWORD);

			otp.setStatus(OtpStatus.SENT);
			otp.setUpdatedDt(LocalDateTime.now());
			otpRepository.save(otp);

		} catch (Exception e) {
			otp.setStatus(OtpStatus.FAILED);
			otpRepository.save(otp);
			throw new RuntimeException("Failed to send OTP");
		}
	}

	@Override
	@Transactional
	public void resetPassword(ResetPasswordRequest request) {

		// Extract studentId from request
		String studentId = request.getStudentId(); // e.g. S000002

		// Get latest OTP for student
		StudentOtp studentOtp = otpRepository.findLatestOtpByStudentId(studentId).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("OTP not found"));

		// OTP already used
		if (studentOtp.getStatus() == OtpStatus.VERIFIED) {
			throw new RuntimeException("OTP already used");
		}

		// OTP expired (10 minutes)
		if (studentOtp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(10))) {

			studentOtp.setStatus(OtpStatus.EXPIRED);
			otpRepository.save(studentOtp);
			throw new RuntimeException("OTP expired");
		}

		// OTP mismatch
		if (!studentOtp.getOtp().equals(request.getOtp())) {
			throw new RuntimeException("Invalid OTP");
		}

		// Get student (UNIQUE by student_id)
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		// Update password
		student.setPassword(passwordEncoder.encode(request.getNewPassword()));
		studentRepository.save(student);

		studentOtp.setStatus(OtpStatus.VERIFIED);
		studentOtp.setUpdatedDt(LocalDateTime.now());
		otpRepository.save(studentOtp);

		// Send confirmation email
		emailService.sendOtpEmail(student.getEmailId(), null, OtpPurpose.PASSWORD_RESET_SUCCESS);
	}

	@Override
	public StudentResponse updateStudentProfile(String studentId, StudentUpdateRequest request) {

		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		studentMapper.updateStudentFromDto(request, student);

		studentRepository.save(student);

		return studentMapper.toResponse(student);
	}

}
