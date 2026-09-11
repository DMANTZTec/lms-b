package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.StaffLoginRequest;
import com.dmantz.lms.dto.request.StudentLoginRequest;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StudentLoginResponse;

public interface AuthService {

//    StudentLoginResponse studentLogin(
//            StudentLoginRequest studentLoginRequest);

    StaffLoginResponse staffLogin(
            StaffLoginRequest request);
}
