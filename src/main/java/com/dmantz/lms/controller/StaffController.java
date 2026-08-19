package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.ResendOtpResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
	public ResponseEntity<StaffResponse> createStaff(@ModelAttribute StaffCreateRequest request) {

		StaffResponse response = staffService.createStaff(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/set-Newpassword")
	public ResponseEntity<?> setPassword(@Valid @RequestBody SetStaffPasswordRequest request) {
		staffService.setPassword(request);
		return ResponseEntity.ok("Password created successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<StaffLoginResponse> staffLogin(@RequestBody StaffLoginRequest request) {
		StaffLoginResponse response = authService.staffLogin(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login-verification-otp")
	public ResponseEntity<StaffLoginResponse> verifyStaffOtp(@RequestBody @Valid OtpVerifyRequest request) {

		StaffLoginResponse response = staffService.verifyStaffOtp(request);
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

	@GetMapping("/active")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<StaffResponse>> getActiveStaff(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(staffService.getActiveStaff(page, size));
	}

	@GetMapping("/pagination")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<StaffResponse>> getAllStaff(@RequestParam(defaultValue = "0") int page,
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

	@PutMapping("/{staffId}")
	public ResponseEntity<StaffResponse> updateStaff(@PathVariable String staffId,
			@Valid @RequestBody StaffUpdateRequest request) {

		logger.info("Request received to update staff with staffId: {}", staffId);
		StaffResponse response = staffService.updateStaff(staffId, request);

		logger.info("Staff updated successfully with staffId: {}", staffId);
		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "/{staffId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<StaffResponse> updateProfileImage(@PathVariable String staffId,
			@RequestParam("file") MultipartFile file) {

		logger.info("Request received to update profile image for staffId: {}", staffId);

		StaffResponse response = staffService.updateProfileImage(staffId, file);

		logger.info("Profile image updated successfully for staffId: {}", staffId);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

		logger.info("Received forgot password request for: {}", request.getEmailIdOrMobileNo());

		staffService.forgotPassword(request);

		String identifier = request.getEmailIdOrMobileNo();

		if (identifier.contains("@")) {
			logger.info("Password reset link sent successfully through email.");
			return ResponseEntity.ok("Password reset link has been sent to your registered email.");
		} else {
			logger.info("Password reset link sent successfully through SMS.");
			return ResponseEntity.ok("Password reset link has been sent to your registered mobile number.");
		}
	}

	@GetMapping("/reset-password/validate")
	public ResponseEntity<String> validateResetToken(@RequestParam String token) {

		logger.info("Validating password reset token.");
		staffService.validateResetToken(token);

		logger.info("Password reset token validated successfully.");
		return ResponseEntity.ok("Reset token is valid.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody SetStaffPasswordRequest request) {

		logger.info("Received reset password request.");
		staffService.resetPassword(request);

		logger.info("Password reset successfully.");
		return ResponseEntity.ok("Password has been reset successfully.");
	}

	@PostMapping("/resend-login-otp")
	public ResponseEntity<ResendOtpResponse> resendLoginOtp(@Valid @RequestBody ResendStaffOtpRequest request) {

		logger.info("Received request to resend login OTP for: {}", request.getEmailIdOrMobileNo());

		ResendOtpResponse response = staffService.resendLoginOtp(request);

		logger.info("Login OTP resent successfully for: {}", request.getEmailIdOrMobileNo());

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/profile-image/{staffId}")
	public ResponseEntity<?> deleteProfileImage(@PathVariable String staffId) {

		staffService.deleteProfileImage(staffId);

		return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Profile image deleted successfully"));
	}

}