package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StaffLoginResponse;
import com.dmantz.lms_b.dto.response.StaffPasswordResponse;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.Staff;
import com.dmantz.lms_b.service.StaffService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

//    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<StaffResponse> registerStaff(
//            @RequestBody @Valid StaffRegistrationRequest request,
//            @AuthenticationPrincipal Staff loggedInStaff // Injected by Spring Security
//    ) {
//        StaffResponse response = staffService.registerStaff(request, loggedInStaff);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @PostMapping("/register")
    public ResponseEntity<StaffResponse> registerStaff(
            @RequestBody StaffRegistrationRequest request,
            @RequestParam(required = false) String loggedInStaffId  // string staffId
    ) {
        Staff loggedInStaff = null;
        if (loggedInStaffId != null) {
            loggedInStaff = staffService.findByStaffId(loggedInStaffId)
                    .orElseThrow(() -> new RuntimeException("Logged-in staff not found"));
        }
        StaffResponse response = staffService.registerStaff(request, loggedInStaff);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<StaffLoginResponse> login(@RequestBody @Valid StaffLoginRequest request) {
        return ResponseEntity.ok(staffService.login(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpVerifyResponse> verifyOtp(
            @RequestBody StaffOtpVerifyRequest request) {
        return ResponseEntity.ok(staffService.verifyStaffOtp(request));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<StaffPasswordResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(staffService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<StaffPasswordResponse> resetPassword(
            @RequestBody StaffResetPasswordRequest request) {
        return ResponseEntity.ok(staffService.resetPassword(request));
    }

    @GetMapping("/view-staff")
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable String staffId) {
        return ResponseEntity.ok(staffService.getStaffByStaffId(staffId));
    }

    @PostMapping("/admin-register")
    public ResponseEntity<StaffResponse> registerInitialAdmin(
            @RequestBody @Valid StaffRegistrationRequest request) {

        return ResponseEntity.ok(
                staffService.registerInitialAdmin(request)
        );
      }

    }