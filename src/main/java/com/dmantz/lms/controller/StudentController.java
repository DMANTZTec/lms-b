package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.RegistrationResponse;
import com.dmantz.lms.dto.response.StaffResponse;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

	private static final Logger logger = LogManager.getLogger(StudentController.class);

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping("/register")
	public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody StudentRegistrationRequest request) {
		return ResponseEntity.ok(studentService.register(request));
	}

	@PostMapping("/registration/verify-otp")
	public ResponseEntity<StudentResponse> verifyOtp(@RequestBody OtpVerifyRequest request) {
		logger.info("OTP verification request received for email: {}", request.getEmailIdOrMobileNo());
		return ResponseEntity.ok(studentService.verifyOtp(request));
	}

	@PostMapping("/login")
	public ResponseEntity<StudentLoginResponse> login(@RequestBody StudentLoginRequest request) {

		logger.info("Login request received for username: {} via channel: {}", request.getUsername(),
				request.getOtpChannel());

		StudentLoginResponse response = studentService.login(request);

		logger.info("Login OTP sent successfully for username: {}", request.getUsername());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify-login-otp")
	public ResponseEntity<StudentLoginResponse> verifyLoginOtp(@RequestBody OtpVerifyRequest request) {

		logger.info("Login OTP verification request received for identifier: {}", request.getEmailIdOrMobileNo());

		StudentLoginResponse response = studentService.verifyLoginOtp(request);

		logger.info("Login OTP verified successfully for identifier: {}", request.getEmailIdOrMobileNo());

		return ResponseEntity.ok(response);
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
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

		logger.info("Forgot password request received for iudentifier: {} via channel: {}",
				request.getGetEmailIdOrMobileNo(), request.getOtpChannel());

		studentService.forgotPassword(request);

		logger.info("Forgot password OTP sent successfully for identifier: {}", request.getGetEmailIdOrMobileNo());

		return ResponseEntity.ok("OTP sent successfully via " + request.getOtpChannel());
	}

	// ================= RESET PASSWORD =================
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

		studentService.resetPassword(request);

		return ResponseEntity.ok("Password reset successful");
	}

	// ================= UPDATE STUDENT =================
	@PutMapping(value = "/{studentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StudentResponse> updateProfile(@PathVariable String studentId,
			@RequestBody StudentUpdateRequest request) {

		logger.info("Request received to update student with studentId: {}", studentId);
		StudentResponse response = studentService.updateStudent(studentId, request);

		logger.info("Student updated successfully with studentId: {}", studentId);
		return ResponseEntity.ok(response);
	}

	// ================= UPDATE STUDENT PROFILE IMAGE =================
	@PutMapping(value = "/{studentId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StudentResponse> updateProfileImage(@PathVariable String studentId,
			@RequestParam("profileImg") MultipartFile profileImg) {

		logger.info("Profile image update request received for studentId: {}", studentId);

		StudentResponse response = studentService.updateProfileImage(studentId, profileImg);

		logger.info("Profile image updated successfully for studentId: {}", studentId);

		return ResponseEntity.ok(response);
	}

	// GET STUDENT BY StudentId
	@GetMapping("/{studentId}")
	public ResponseEntity<StudentResponse> getStudentById(@PathVariable String studentId) {
		logger.info("Request received to fetch student with studentId: {}", studentId);
		StudentResponse response = studentService.getStudentById(studentId);
		logger.info("Student fetched successfully for studentId: {}", studentId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/resend-otp")
	public ResponseEntity<RegistrationResponse> resendOtp(@RequestBody ResendOtpRequest request) {

		RegistrationResponse response = studentService.resendOtp(request);
		return ResponseEntity.ok(response);
	}

	// ================= CHANGE PASSWORD =================
	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

		logger.info("Change password request received for studentId: {}", request.getStudentId());

		studentService.changePassword(request);

		logger.info("Password changed successfully for studentId: {}", request.getStudentId());

		return ResponseEntity.ok("Password changed successfully");
	}
	
	@DeleteMapping("/profile-image/{studentId}")
	public ResponseEntity<?> deleteProfileImage(
	        @PathVariable String studentId) {

	    studentService.deleteProfileImage(studentId);

	    return ResponseEntity.ok(
	            Map.of(
	                    "status", "SUCCESS",
	                    "message", "Profile image deleted successfully"
	            )
	    );
	}

}