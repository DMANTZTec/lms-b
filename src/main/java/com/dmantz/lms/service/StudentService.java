package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.OtpVerifyResponse;
import com.dmantz.lms.dto.response.RegistrationResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.dto.response.StudentResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

	RegistrationResponse register(StudentRegistrationRequest request);

	StudentResponse verifyOtp(OtpVerifyRequest request);

	StudentLoginResponse login(StudentLoginRequest request);

	StudentLoginResponse verifyLoginOtp(OtpVerifyRequest request);

	List<StudentResponse> getAllStudents();

	void forgotPassword(ForgotPasswordRequest request);

	void resetPassword(ResetPasswordRequest request);

	StudentResponse updateStudent(String studentId, StudentUpdateRequest request);
	
	StudentResponse getStudentById(String studentId);

	RegistrationResponse resendOtp(ResendOtpRequest request);
	
	void changePassword(ChangePasswordRequest request);
	
	StudentResponse updateProfileImage(String studentId, MultipartFile file);
}
