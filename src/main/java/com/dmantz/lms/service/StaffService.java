package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.ResendOtpResponse;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {

    StaffResponse createStaff(StaffCreateRequest request);

    void setPassword(SetStaffPasswordRequest request);

    StaffLoginResponse verifyStaffOtp(StaffOtpVerifyRequest request);

    List<StaffResponse> getAllStaff();

    StaffResponse getStaffByStaffId(String staffId);

    Page<StaffResponse> getActiveStaff(int page, int size);

    Page<StaffResponse> getAllStaff(int page, int size);

    StaffResponse updateStaff(String staffId, StaffUpdateRequest request);

    StaffResponse updateProfileImage(String staffId, MultipartFile file);

    void forgotPassword(ForgotPasswordRequest request);

    void validateResetToken(String token);

    void resetPassword(SetStaffPasswordRequest request);

    ResendOtpResponse resendLoginOtp(ResendStaffOtpRequest request);

    @Transactional
    StaffResponse registerInitialAdmin(StaffRegistrationRequest request);

}

