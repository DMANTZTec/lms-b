package com.dmantz.lms.controller;

import com.dmantz.lms.dto.request.StaffLoginRequest;
import com.dmantz.lms.dto.request.StudentLoginRequest;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

//	@PostMapping("/student/login")
//	public ResponseEntity<StudentLoginResponse> studentLogin(
//			@RequestBody StudentLoginRequest request) {
//
//		logger.info("OTP verification API called for studentId: {}",
//				request.getUsername());
//		StudentLoginResponse response = authService.studentLogin(request);
//
//		return ResponseEntity.ok(response);
//	}

//	@PostMapping("/staff/login")
//	public ResponseEntity<StaffLoginResponse> staffLogin(@RequestBody StaffLoginRequest request) {
//
//		StaffLoginResponse response = authService.staffLogin(request);
//
//		return ResponseEntity.ok(response);
//	}

}
