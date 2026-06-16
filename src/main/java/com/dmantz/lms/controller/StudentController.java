package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.ForgotPasswordRequest;
import com.dmantz.lms.dto.request.OtpVerifyRequest;
import com.dmantz.lms.dto.request.ResetPasswordRequest;
import com.dmantz.lms.dto.request.StudentLoginRequest;
import com.dmantz.lms.dto.request.StudentRegistrationRequest;
import com.dmantz.lms.dto.request.StudentUpdateRequest;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.RegistrationResponse;
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
		logger.info("OTP verification request received for email: {}",
				request.getEmailIdOrMobileNo());
		return ResponseEntity.ok(studentService.verifyOtp(request));
	}

	@PostMapping("/login")
	public ResponseEntity<StudentLoginResponse> login(
			@RequestBody StudentLoginRequest request) {

		StudentLoginResponse response =
				studentService.login(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify-login-otp")
	public ResponseEntity<StudentLoginResponse> verifyLoginOtp(
			@RequestBody OtpVerifyRequest request) {

		StudentLoginResponse response =
				studentService.verifyLoginOtp(request);

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

	    logger.info("Reset password request received for studentId: {}", request.getStudentId());

	    studentService.resetPassword(request);

	    logger.info("Password reset successfully for studentId: {}", request.getStudentId());

	    return ResponseEntity.ok("Password reset successful");
	}

	// ================= UPDATE STUDENT PROFILE =================
	@PutMapping(value = "/update/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StudentResponse> updateProfile(@PathVariable String studentId,
			@RequestParam(value = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam String firstNm, @RequestParam String lastNm, @RequestParam String gender,
			@RequestParam String dob, @RequestParam(required = false) String addr1,
			@RequestParam(required = false) String addr2, @RequestParam(required = false) String city,
			@RequestParam(required = false) String state, @RequestParam(required = false) String country,
			@RequestParam(required = false) String pin, @RequestParam(required = false) String mobileNum,
			@RequestParam(required = false) String emergencyContactNm,
			@RequestParam(required = false) String emergencyContactNum) {

		StudentUpdateRequest request = new StudentUpdateRequest();
		request.setFirstNm(firstNm);
		request.setLastNm(lastNm);
		request.setGender(gender);
		request.setDob(LocalDate.parse(dob));
		request.setAddr1(addr1);
		request.setAddr2(addr2);
		request.setCity(city);
		request.setState(state);
		request.setCountry(country);
		request.setPin(pin);
		request.setMobileNum(mobileNum);
		request.setEmergencyContactNm(emergencyContactNm);
		request.setEmergencyContactNum(emergencyContactNum);
		request.setProfileImg(profileImg);

		return ResponseEntity.ok(studentService.updateStudentProfile(studentId, request));
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
	public ResponseEntity<RegistrationResponse> resendOtp(
			@RequestBody ResendOtpRequest request) {

		RegistrationResponse response = studentService.resendOtp(request);
		return ResponseEntity.ok(response);
	}

}