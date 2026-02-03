package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.StaffRegistrationRequest;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.*;
import com.dmantz.lms_b.mapper.StaffMapper;
import com.dmantz.lms_b.repository.RoleRepository;
import com.dmantz.lms_b.repository.StaffOtpRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.repository.StaffRoleRepository;
import com.dmantz.lms_b.service.StaffService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.Set;

@Service
public class StaffServiceImpl implements StaffService {

//    private final StaffRepository staffRepository;
//    private final RoleRepository roleRepository;
//    private final StaffRoleRepository staffRoleRepository;
//    private final StaffMapper staffMapper;
//    private final PasswordEncoder passwordEncoder;
//    private final StaffOtpRepository staffOtpRepository;
//
//    public StaffServiceImpl(StaffRepository staffRepository, RoleRepository roleRepository, StaffRoleRepository staffRoleRepository, StaffMapper staffMapper, PasswordEncoder passwordEncoder, StaffOtpRepository staffOtpRepository) {
//        this.staffRepository = staffRepository;
//        this.roleRepository = roleRepository;
//        this.staffRoleRepository = staffRoleRepository;
//        this.staffMapper = staffMapper;
//        this.passwordEncoder = passwordEncoder;
//        this.staffOtpRepository = staffOtpRepository;
//    }

//    @Override
//    @Transactional
//    public StaffResponse register_staff(StaffRegistrationRequest request) {
//
//        //  Check if email already exists
//        staffRepository.findByEmailId(request.getEmail_id())
//                .ifPresent(s -> {
//                    throw new RuntimeException("Email already exists");
//                });
//
//        //  Map DTO → Entity
//        Staff staff = staffMapper.toEntity(request);
//
//        //  Generate Staff ID
//        staff.setStaffId(generateStaffId());
//
//        //  Encode password
//        staff.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        // Handle profile image (Base64 → byte[])
//        if (request.getProfile_img() != null && !request.getProfile_img().isBlank()) {
//            staff.setProfileImg(
//                    Base64.getDecoder().decode(request.getProfile_img())
//            );
//        }
//
//        //  Default values
//        staff.setStatus("ACTIVE");
//        staff.setEnabled("Y");
//        staff.setCreatedDt(LocalDateTime.now());
//
//        //Save staff only (NO roles)
//        Staff savedStaff = staffRepository.save(staff);
//
//        // Return response
//        return staffMapper.toResponse(savedStaff);
//    }
//
//    private String generateStaffId() {
//        Long count = staffRepository.count() + 1;
//        return String.format("SF%06d", count); // SF000001, SF000002, etc.
//    }
//
//    private StaffOtp generateStaffOtp(Long staffId) {
//
//        StaffOtp otp = new StaffOtp();
//
//        otp.setStaffId(staffId);
//        otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));
//        otp.setStatus(OtpStatus.NEW);
//        otp.setAttemptsNum(0);
//        otp.setCreatedDt(LocalDateTime.now());
//
//        return staffOtpRepository.save(otp);
//    }

}

