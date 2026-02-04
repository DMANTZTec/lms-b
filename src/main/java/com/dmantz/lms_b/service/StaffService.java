package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.ForgotPasswordRequest;
import com.dmantz.lms_b.dto.request.StaffLoginRequest;
import com.dmantz.lms_b.dto.request.StaffRegistrationRequest;
import com.dmantz.lms_b.dto.request.StaffResetPasswordRequest;
import com.dmantz.lms_b.dto.response.StaffLoginResponse;
import com.dmantz.lms_b.dto.response.StaffPasswordResponse;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.Staff;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface StaffService {

    StaffResponse registerStaff(StaffRegistrationRequest request, Staff loggedInStaff);


    Optional<Staff> findByStaffId(String staffId);


    StaffLoginResponse login(StaffLoginRequest request);

    StaffLoginResponse verifyOtp(String staffId, String otpValue);


    StaffPasswordResponse forgotPassword(ForgotPasswordRequest request);

    StaffPasswordResponse resetPassword(StaffResetPasswordRequest request);

}
