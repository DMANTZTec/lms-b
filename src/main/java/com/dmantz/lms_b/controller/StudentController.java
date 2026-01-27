package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.StudentLoginRequest;
import com.dmantz.lms_b.dto.request.StudentRegistrationRequest;
import com.dmantz.lms_b.dto.response.StudentResponse;
import com.dmantz.lms_b.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {

        StudentResponse response = studentService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<StudentResponse> login(
            @Valid @RequestBody StudentLoginRequest studentLoginRequestrequest) {
        StudentResponse response = studentService.login(studentLoginRequestrequest);
        return ResponseEntity.ok(response);
    }


//    @PostMapping("/verify-otp")
//    public String verifyOtp(@RequestBody OtpVerifyRequest request) {
//        return studentService.verifyOtp(request);
//    }
}
