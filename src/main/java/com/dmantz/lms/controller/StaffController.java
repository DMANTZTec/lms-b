package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.service.StaffService;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	private static final Logger logger = LogManager.getLogger(StaffController.class);

	private final StaffService staffService;

	public StaffController(StaffService staffService) {
		this.staffService = staffService;
	}

	@PostMapping("/register")
	public ResponseEntity<StaffResponse> registerStaff(@RequestBody StaffRegistrationRequest request,
			@RequestParam(required = false) String loggedInStaffId) {

		logger.info("Received request to register staff with email: {}", request.getEmailId());

		Staff loggedInStaff = null;

		if (loggedInStaffId != null) {

			logger.info("Fetching logged-in staff with staffId: {}", loggedInStaffId);

			loggedInStaff = staffService.findByStaffId(loggedInStaffId).orElseThrow(() -> {

				logger.error("Logged-in staff not found with staffId: {}", loggedInStaffId);

				return new RuntimeException("Logged-in staff not found");
			});
		}

		StaffResponse response = staffService.registerStaff(request, loggedInStaff);

		logger.info("Staff registered successfully with email: {}", request.getEmailId());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<OtpVerifyResponse> verifyOtp(@RequestBody StaffOtpVerifyRequest request) {

		logger.info("OTP verification request received for staffId: {}", request.getStaffId());

		OtpVerifyResponse response = staffService.verifyStaffOtp(request);

		logger.info("OTP verified successfully for staffId: {}", request.getStaffId());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<StaffPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {

		logger.info("Forgot password request received for email: {}", request.getEmail());

		StaffPasswordResponse response = staffService.forgotPassword(request);

		logger.info("Forgot password OTP sent successfully to email: {}", request.getEmail());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<StaffPasswordResponse> resetPassword(@RequestBody StaffResetPasswordRequest request) {

		logger.info("Reset password request received for staffId: {}", request.getStaffId());

		StaffPasswordResponse response = staffService.resetPassword(request);

		logger.info("Password reset completed successfully for staffId: {}", request.getStaffId());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/view-staff")
	public ResponseEntity<List<StaffResponse>> getAllStaff() {

		logger.info("Request received to fetch all staff");

		List<StaffResponse> response = staffService.getAllStaff();

		logger.info("Total staff fetched successfully: {}", response.size());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{staffId}")
	public ResponseEntity<StaffResponse> getStaffById(@PathVariable String staffId) {

		logger.info("Request received to fetch staff by staffId: {}", staffId);

		StaffResponse response = staffService.getStaffByStaffId(staffId);

		logger.info("Staff fetched successfully for staffId: {}", staffId);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/admin-register")
	public ResponseEntity<StaffResponse> registerInitialAdmin(@RequestBody @Valid StaffRegistrationRequest request) {

		logger.info("Initial admin registration request received for email: {}", request.getEmailId());

		StaffResponse response = staffService.registerInitialAdmin(request);

		logger.info("Initial admin registered successfully with email: {}", request.getEmailId());

		return ResponseEntity.ok(response);
	}

}