package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.dto.response.StudentResponse;
import com.dmantz.lms.service.StudentService;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

	private static final Logger logger = LogManager.getLogger(StudentController.class);

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	// ================= REGISTER STUDENT =================
	// ================= REGISTER STUDENT =================
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StudentResponse> registerStudent(@Valid @ModelAttribute StudentRegistrationRequest request) {

		logger.info("Student registration request received for email: {}", request.getEmailId());

		StudentResponse response = studentService.register(request);

		logger.info("Student registered successfully with studentId: {}", response.getStudentId());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ================= LOGIN =================
	@PostMapping("/login")
	public ResponseEntity<StudentLoginResponse> login(@Valid @RequestBody StudentLoginRequest request) {

		logger.info("Student login request received for loginId: {}", request.getUsername());

		StudentLoginResponse response = studentService.login(request);

		logger.info("OTP sent successfully for student loginId: {}", request.getUsername());

		return ResponseEntity.ok(response);
	}

	// ================= VERIFY OTP =================
	@PostMapping("/otp-verify")
	public ResponseEntity<StudentLoginResponse> verifyOtp(@RequestBody OtpVerifyRequest request) {

		logger.info("OTP verification request received for studentId: {}", request.getStudentId());

		StudentLoginResponse response = studentService.verifyLoginOtp(request);

		logger.info("OTP verified successfully for studentId: {}", request.getStudentId());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// ================= GET ALL STUDENTS =================
	@GetMapping("view-students")
	public ResponseEntity<List<StudentResponse>> getAllStudents() {

		logger.info("Fetching all students");

		List<StudentResponse> students = studentService.getAllStudents();

		logger.info("Fetched {} students successfully", students.size());

		return ResponseEntity.ok(students);
	}

	// ================= FORGOT PASSWORD =================
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {

		logger.info("Forgot password request received for email: {}", request.getEmail());

		studentService.forgotPassword(request);

		logger.info("Forgot password OTP sent successfully for email: {}", request.getEmail());

		return ResponseEntity.ok("OTP sent successfully");
	}

	// ================= RESET PASSWORD =================
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

		logger.info("Reset password request received for studentId: {}", request.getStudentId());

		studentService.resetPassword(request);

		logger.info("Password reset successfully for studentId: {}", request.getStudentId());

		return ResponseEntity.ok("Password reset successful");
	}

	// ================= UPDATE STUDENT PROFILE =================
	@PutMapping(value = "/update/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StudentResponse> updateStudentProfile(@PathVariable String studentId,
			@ModelAttribute StudentUpdateRequest request) {

		logger.info("Update student profile request received for studentId: {}", studentId);

		StudentResponse response = studentService.updateStudentProfile(studentId, request);

		logger.info("Student profile updated successfully for studentId: {}", studentId);

		return ResponseEntity.ok(response);
	}

}