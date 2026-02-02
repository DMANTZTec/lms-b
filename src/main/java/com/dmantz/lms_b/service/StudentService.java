package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StudentLoginResponse;
import com.dmantz.lms_b.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse register(StudentRegistrationRequest request);

    StudentLoginResponse login(StudentLoginRequest request);

    OtpVerifyResponse verifyOtp(OtpVerifyRequest request);

    List<StudentResponse> getAllStudents();

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
