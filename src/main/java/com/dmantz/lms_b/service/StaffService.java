package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StaffLoginResponse;
import com.dmantz.lms_b.dto.response.StaffPasswordResponse;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.Staff;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface StaffService {

    StaffResponse registerStaff(StaffRegistrationRequest request, Staff loggedInStaff);


    Optional<Staff> findByStaffId(String staffId);


    StaffLoginResponse login(StaffLoginRequest request);

    OtpVerifyResponse verifyStaffOtp(StaffOtpVerifyRequest request);

    StaffPasswordResponse forgotPassword(ForgotPasswordRequest request);

    StaffPasswordResponse resetPassword(StaffResetPasswordRequest request);

    List<StaffResponse> getAllStaff();

    StaffResponse getStaffByStaffId(String staffId);

    @Transactional
    StaffResponse registerInitialAdmin(StaffRegistrationRequest request);
}

