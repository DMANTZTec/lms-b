package com.dmantz.lms_b.service;

import com.dmantz.lms_b.dto.request.OtpVerifyRequest;
import com.dmantz.lms_b.dto.request.StudentLoginRequest;
import com.dmantz.lms_b.dto.request.StudentRegistrationRequest;
import com.dmantz.lms_b.dto.response.StudentResponse;

public interface StudentService {

    // Register a student
    StudentResponse register(StudentRegistrationRequest request);

    // Login a student (by email / mobile / loginId)
    StudentResponse login(StudentLoginRequest request);


//    // Verify OTP
//    String verifyOtp(OtpVerifyRequest request);
}
