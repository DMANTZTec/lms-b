package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.StudentLoginRequest;
import com.dmantz.lms.dto.response.StudentLoginResponse;

public interface AuthService {

    StudentLoginResponse studentLogin(
            StudentLoginRequest studentLoginRequest);

//    LoginResponse staffLogin(
//            LoginRequest request);
}
