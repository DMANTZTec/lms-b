package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import com.dmantz.lms.service.AuthService;
import com.dmantz.lms.service.StaffService;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	private static final Logger logger = LogManager.getLogger(StaffController.class);

	private final StaffService staffService;
	private final AuthService authService;

	public StaffController(StaffService staffService, AuthService authService) {
		this.staffService = staffService;
        this.authService = authService;
    }

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StaffResponse> createStaff(
			@ModelAttribute StaffCreateRequest request) {

		StaffResponse response = staffService.createStaff(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/set-password")
	public ResponseEntity<?> setPassword(@Valid @RequestBody SetStaffPasswordRequest request) {
		staffService.setPassword(request);
		return ResponseEntity.ok("Password created successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<StaffLoginResponse> staffLogin(@RequestBody StaffLoginRequest request) {
		StaffLoginResponse response = authService.staffLogin(request);
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

	@PostMapping("/login-verification-otp")
	public ResponseEntity<StaffLoginResponse> verifyStaffOtp(
			@RequestBody @Valid StaffOtpVerifyRequest request) {

		StaffLoginResponse response = staffService.verifyStaffOtp(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/active")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<StaffResponse>> getActiveStaff(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(staffService.getActiveStaff(page, size));
	}

	@GetMapping("/pagination")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<StaffResponse>> getAllStaff(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(staffService.getAllStaff(page, size));
	}

	@PostMapping("/admin-register")
	public ResponseEntity<StaffResponse> registerInitialAdmin(@RequestBody @Valid StaffRegistrationRequest request) {

		logger.info("Initial admin registration request received for email: {}", request.getEmailId());
		StaffResponse response = staffService.registerInitialAdmin(request);

		logger.info("Initial admin registered successfully with email: {}", request.getEmailId());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<StaffPasswordResponse> resetPassword(@RequestBody StaffResetPasswordRequest request) {

		logger.info("Reset password request received for staffId: {}", request.getStaffId());

		StaffPasswordResponse response = staffService.resetPassword(request);

		logger.info("Password reset completed successfully for staffId: {}", request.getStaffId());

		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "/{staffId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StaffResponse> updateStaff(
			@PathVariable String staffId,
			@ModelAttribute StaffUpdateRequest request) {

		return ResponseEntity.ok(staffService.updateStaff(staffId, request));
	}

}