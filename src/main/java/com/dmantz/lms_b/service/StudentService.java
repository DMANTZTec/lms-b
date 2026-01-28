package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.OtpVerifyRequest;
import com.dmantz.lms_b.dto.request.StudentLoginRequest;
import com.dmantz.lms_b.dto.request.StudentRegistrationRequest;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StudentLoginResponse;
import com.dmantz.lms_b.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {

    // Register a student
    StudentResponse register(StudentRegistrationRequest request);

    // Login a student (by email / mobile / loginId)
    StudentLoginResponse login(StudentLoginRequest request);


//    // Verify OTP
    OtpVerifyResponse verifyOtp(OtpVerifyRequest request);

    List<StudentResponse> getAllStudents();

}
