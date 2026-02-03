package com.dmantz.lms_b.controller;

import com.dmantz.lms_b.dto.request.StaffRegistrationRequest;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

//    private final StaffService staffService;
//
//    public StaffController(StaffService staffService) {
//        this.staffService = staffService;
//    }
//
//
//    @PostMapping("/register")
//    public ResponseEntity<StaffResponse> registerStaff(
//            @Valid @RequestBody StaffRegistrationRequest request) {
//
//        StaffResponse response = staffService.register_staff(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//    }

}