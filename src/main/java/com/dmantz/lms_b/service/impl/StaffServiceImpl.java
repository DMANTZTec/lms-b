package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StaffLoginResponse;
import com.dmantz.lms_b.dto.response.StaffPasswordResponse;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.*;
import com.dmantz.lms_b.mapper.StaffMapper;
import com.dmantz.lms_b.repository.RoleRepository;
import com.dmantz.lms_b.repository.StaffOtpRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.service.EmailService;
import com.dmantz.lms_b.service.StaffService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final StaffMapper staffMapper;
    private final PasswordEncoder passwordEncoder;
    private final StaffOtpRepository staffOtpRepository;
    private final EmailService emailService;

    public StaffServiceImpl(StaffRepository staffRepository,
                            RoleRepository roleRepository,
                            StaffMapper staffMapper,
                            PasswordEncoder passwordEncoder, StaffOtpRepository staffOtpRepository, EmailService emailService) {
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.staffMapper = staffMapper;
        this.passwordEncoder = passwordEncoder;
        this.staffOtpRepository = staffOtpRepository;
        this.emailService = emailService;
    }


    @Override
    @Transactional
    public StaffResponse registerStaff(StaffRegistrationRequest request, Staff loggedInStaff) {

        // Check email uniqueness
        staffRepository.findByEmailId(request.getEmailId())
                .ifPresent(s -> {
                    throw new RuntimeException("Staff already exists with this email");
                });

        boolean isFirstStaff = staffRepository.count() == 0;

        // Authorization check
        if (!isFirstStaff) {
            if (loggedInStaff == null) {
                throw new RuntimeException("Unauthorized access");
            }

            Staff dbStaff = staffRepository.findById(loggedInStaff.getId())
                    .orElseThrow(() -> new RuntimeException("Logged-in staff not found"));

            boolean isAdmin = dbStaff.getRoles().stream()
                    .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getRoleNm()));

            if (!isAdmin) {
                throw new RuntimeException("Only ADMIN can register staff");
            }
        }

        //  Validate roles
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            throw new RuntimeException("At least one role must be provided");
        }

        //  First staff must be ADMIN
        if (isFirstStaff && request.getRoles().stream()
                .noneMatch("ADMIN"::equalsIgnoreCase)) {
            throw new RuntimeException("First staff must have ADMIN role");
        }

        // Map entity
        Staff staff = staffMapper.toEntity(request);
        staff.setStaffId(generateStaffId());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setStatus("ACTIVE");
        staff.setEnabled("Y");
        staff.setCreatedDt(LocalDateTime.now());
        staff.setCreatedBy(isFirstStaff ? null : loggedInStaff.getId());

        // Profile image
        if (request.getProfileImgBase64() != null && !request.getProfileImgBase64().isBlank()) {
            staff.setProfileImg(Base64.getDecoder().decode(request.getProfileImgBase64()));
        }

        // Assign roles
        Set<Role> assignedRoles = request.getRoles().stream()
                .map(r -> r.trim().toUpperCase())
                .map(roleNm -> roleRepository.findByRoleNm(roleNm)
                        .orElseThrow(() -> new RuntimeException(roleNm + " role not found")))
                .collect(Collectors.toSet());

        staff.setRoles(assignedRoles);

        // Save
        Staff savedStaff = staffRepository.save(staff);
        return staffMapper.toResponse(savedStaff);
    }


    private String generateStaffId() {
        Long count = staffRepository.count() + 1;
        return String.format("SF%05d", count); // SF00001, SF00002
    }

    @Override
    public Optional<Staff> findByStaffId(String staffId) {
        return staffRepository.findByStaffId(staffId);
    }

    private StaffOtp generateStaffOtp(String staffId) {

        //  Check existing active OTP
        Optional<StaffOtp> existingOtp =
                staffOtpRepository.findTopByStaffIdAndStatusOrderByIdDesc(
                        staffId,
                        OtpStatus.NEW
                );

        if (existingOtp.isPresent()) {
            return existingOtp.get(); // reuse OTP (or you can throw exception)
        }

        // Generate new OTP
        StaffOtp otp = new StaffOtp();
        otp.setStaffId(staffId); //  STRING ONLY
        otp.setOtp(String.format("%06d",
                new SecureRandom().nextInt(1_000_000))); // 6-digit numeric OTP
        otp.setStatus(OtpStatus.NEW);
        otp.setAttemptsNum(0);
        otp.setCreatedDt(LocalDateTime.now());
        return staffOtpRepository.save(otp);
    }

    @Override
    public StaffLoginResponse login(StaffLoginRequest request) {

        Staff staff = staffRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!"Y".equalsIgnoreCase(staff.getEnabled())) {
            throw new RuntimeException("Staff account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), staff.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //  Generate / reuse OTP
        StaffOtp otp = generateStaffOtp(staff.getStaffId());

        try {
            emailService.sendOtpEmail(
                    staff.getEmailId(),
                    otp.getOtp(),
                    OtpPurpose.STAFF_LOGIN
            );

            otp.setStatus(OtpStatus.SENT);
            otp.setUpdatedDt(LocalDateTime.now());
            staffOtpRepository.save(otp);

        } catch (Exception e) {

            System.out.println("Staff OTP email failed");

            // optional but recommended
            otp.setStatus(OtpStatus.FAILED);
            otp.setUpdatedDt(LocalDateTime.now());
            staffOtpRepository.save(otp);

            throw new RuntimeException("Unable to send OTP. Please try again.");
        }

        StaffLoginResponse response = staffMapper.toLoginResponse(staff);
        response.setMessage("Login successfully Completed. OTP sent to your email.");

        return response;
    }


    @Override
    public OtpVerifyResponse verifyStaffOtp(StaffOtpVerifyRequest request) {

        StaffOtp otp = staffOtpRepository
                .findTopByStaffIdOrderByCreatedDtDesc(request.getStaffId())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otp.getStatus() != OtpStatus.SENT) {
            throw new RuntimeException("OTP is not valid");
        }

        if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            otp.setStatus(OtpStatus.EXPIRED);
            otp.setUpdatedDt(LocalDateTime.now());
            staffOtpRepository.save(otp);
            throw new RuntimeException("OTP expired");
        }

        if (!otp.getOtp().equals(request.getOtp())) {
            otp.setAttemptsNum(otp.getAttemptsNum() + 1);
            otp.setUpdatedDt(LocalDateTime.now());
            staffOtpRepository.save(otp);
            throw new RuntimeException("Invalid OTP");
        }

        otp.setStatus(OtpStatus.VERIFIED);
        otp.setUpdatedDt(LocalDateTime.now());
        staffOtpRepository.save(otp);

        OtpVerifyResponse response = new OtpVerifyResponse();
        response.setVerified(true);
        response.setMessage("OTP verified successfully");

        return response;
    }


    @Override
    public StaffPasswordResponse forgotPassword(ForgotPasswordRequest request) {

        Staff staff = staffRepository.findByEmailId(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Expire previous OTPs
        staffOtpRepository.expireActiveOtps(
                staff.getStaffId(),
                OtpStatus.EXPIRED,
                List.of(OtpStatus.NEW, OtpStatus.SENT)
        );

        StaffOtp otp = new StaffOtp();
        otp.setStaffId(staff.getStaffId());
        otp.setOtp(String.valueOf(100000 + new Random().nextInt(900000)));
        otp.setStatus(OtpStatus.SENT);
        otp.setCreatedDt(LocalDateTime.now());

        staffOtpRepository.save(otp);

        emailService.sendOtpEmail(
                staff.getEmailId(),
                otp.getOtp(),
                OtpPurpose.STAFF_FORGOT_PASSWORD
        );

        StaffPasswordResponse response = staffMapper.toPasswordResponse(staff);
        response.setMessage("OTP sent to your registered email.");

        return response;
    }


    @Override
    public StaffPasswordResponse resetPassword(StaffResetPasswordRequest request) {

        //  Fetch latest SENT OTP for staff
        StaffOtp otp = staffOtpRepository
                .findTopByStaffIdAndStatusOrderByCreatedDtDesc(
                        request.getStaffId(),
                        OtpStatus.SENT
                )
                .orElseThrow(() -> new RuntimeException("OTP not found or expired"));

        // Validate OTP value
        if (!otp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            otp.setStatus(OtpStatus.EXPIRED);
            staffOtpRepository.save(otp);
            throw new RuntimeException("OTP expired");
        }

        Staff staff = staffRepository.findByStaffId(request.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staff.setPassword(passwordEncoder.encode(request.getNewPassword()));
        staffRepository.save(staff);

        otp.setStatus(OtpStatus.VERIFIED);
        otp.setUpdatedDt(LocalDateTime.now());
        staffOtpRepository.save(otp);

        emailService.sendOtpEmail(
                staff.getEmailId(),
                null,
                OtpPurpose.STAFF_PASSWORD_RESET_SUCCESS
        );

        StaffPasswordResponse response = staffMapper.toPasswordResponse(staff);
        response.setMessage("Password reset successful.");

        return response;
    }

    @Override
    public List<StaffResponse> getAllStaff() {

        List<Staff> staffList = staffRepository.findAll();

        if (staffList.isEmpty()) {
            throw new RuntimeException("No staff found");
        }

        return staffMapper.toResponseList(staffList);
    }

    @Override
    public StaffResponse getStaffByStaffId(String staffId) {

        Staff staff = staffRepository.findByStaffId(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        return staffMapper.toResponse(staff);
    }

    @Transactional
    @Override
    public StaffResponse registerInitialAdmin(StaffRegistrationRequest request) {

        //  Check if staff already exists
        if (staffRepository.count() > 0) {
            throw new RuntimeException("Initial admin already created");
        }

        //  Validate ADMIN role
        if (request.getRoles() == null ||
                request.getRoles().stream()
                        .noneMatch(r -> "ADMIN".equalsIgnoreCase(r))) {

            throw new RuntimeException("Initial staff must have ADMIN role");
        }

        //  Email uniqueness
        staffRepository.findByEmailId(request.getEmailId())
                .ifPresent(s -> {
                    throw new RuntimeException("Email already exists");
                });


        Staff staff = staffMapper.toEntity(request);

        staff.setStaffId(generateStaffId());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setStatus("ACTIVE");
        staff.setEnabled("Y");
        staff.setCreatedDt(LocalDateTime.now());
        staff.setCreatedBy(null); // system created

        //  Profile image (optional)
        if (request.getProfileImgBase64() != null &&
                !request.getProfileImgBase64().isBlank()) {

            String base64 = request.getProfileImgBase64();
            if (base64.contains(",")) {
                base64 = base64.substring(base64.indexOf(",") + 1);
            }
            staff.setProfileImg(Base64.getDecoder().decode(base64));
        }

        // Assign ADMIN role
        Set<Role> roles = request.getRoles().stream()
                .map(r -> r.trim().toUpperCase())
                .map(roleNm -> roleRepository.findByRoleNm(roleNm)
                        .orElseThrow(() -> new RuntimeException(roleNm + " role not found")))
                .collect(Collectors.toSet());

        staff.setRoles(roles);

        Staff saved = staffRepository.save(staff);
        return staffMapper.toResponse(saved);
    }


}




