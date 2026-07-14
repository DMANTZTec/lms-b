package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StaffService {

    StaffResponse createStaff(StaffCreateRequest request);

    void setPassword(SetStaffPasswordRequest request);

    List<StaffResponse> getAllStaff();

    StaffResponse getStaffByStaffId(String staffId);

    @Transactional
    StaffResponse registerInitialAdmin(StaffRegistrationRequest request);

    StaffLoginResponse verifyStaffOtp(StaffOtpVerifyRequest request);

    StaffPasswordResponse resetPassword(StaffResetPasswordRequest request);

    Page<StaffResponse> getActiveStaff(int page, int size);

    Page<StaffResponse> getAllStaff(int page, int size);

}

