package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.dto.response.StudentResponse;
import com.dmantz.lms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping("/register")
	public ResponseEntity<StudentResponse> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {

		StudentResponse response = studentService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<StudentLoginResponse> login(@Valid @RequestBody StudentLoginRequest request) {

		StudentLoginResponse response = studentService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/otp-verify")
	public ResponseEntity<OtpVerifyResponse> verifyOtp(@RequestBody OtpVerifyRequest request) {
		OtpVerifyResponse response = studentService.verifyOtp(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("view-students")
	public ResponseEntity<List<StudentResponse>> getAllStudents() {
		List<StudentResponse> students = studentService.getAllStudents();
		return ResponseEntity.ok(students);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {

		studentService.forgotPassword(request);
		return ResponseEntity.ok("OTP sent successfully");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

		studentService.resetPassword(request);
		return ResponseEntity.ok("Password reset successful");
	}

	// ================= UPDATE STUDENT PROFILE =================
	@PutMapping("/update/{studentId}")
	public ResponseEntity<StudentResponse> updateStudentProfile(@PathVariable String studentId,
			@RequestBody StudentUpdateRequest request) {

		StudentResponse response = studentService.updateStudentProfile(studentId, request);

		return ResponseEntity.ok(response);
	}

}
