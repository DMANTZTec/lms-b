package com.dmantz.lms_b.service.Impl;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StaffLoginResponse;
import com.dmantz.lms_b.dto.response.StaffPasswordResponse;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.OtpStatus;
import com.dmantz.lms_b.entity.Role;
import com.dmantz.lms_b.entity.Staff;
import com.dmantz.lms_b.entity.StaffOtp;
import com.dmantz.lms_b.mapper.StaffMapper;
import com.dmantz.lms_b.repository.RoleRepository;
import com.dmantz.lms_b.repository.StaffOtpRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.service.EmailService;
import com.dmantz.lms_b.service.impl.StaffServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class StaffServiceImplTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private StaffMapper staffMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StaffOtpRepository staffOtpRepository;

    @Mock
    private EmailService emailService;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterStaffSuccess() {

        StaffRegistrationRequest request = new StaffRegistrationRequest();
        request.setEmailId("test@gmail.com");
        request.setPassword("123456");
        request.setRoles(Set.of("ADMIN"));

        Staff staff = new Staff();
        staff.setId(1L);

        Role role = new Role();
        role.setRoleNm("ADMIN");

        when(staffRepository.findByEmailId("test@gmail.com")).thenReturn(Optional.empty());
        when(staffRepository.count()).thenReturn(0L);
        when(roleRepository.findByRoleNm("ADMIN")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("123456")).thenReturn("encodedPass");
        when(staffMapper.toEntity(request)).thenReturn(staff);
        when(staffRepository.save(any())).thenReturn(staff);
        when(staffMapper.toResponse(staff)).thenReturn(new StaffResponse());

        StaffResponse response = staffService.registerStaff(request, null);
        assertNotNull(response);
        verify(staffRepository).save(any());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testRegisterStaffEmailExists() {

        StaffRegistrationRequest request = new StaffRegistrationRequest();
        request.setEmailId("test@gmail.com");

        when(staffRepository.findByEmailId("test@gmail.com")).thenReturn(Optional.of(new Staff()));

        staffService.registerStaff(request, null);
    }

    @Test
    public void testLoginSuccess() {

        StaffLoginRequest request = new StaffLoginRequest();
        request.setLoginId("admin@gmail.com");
        request.setPassword("123456");

        Staff staff = new Staff();
        staff.setStaffId("SF00001");
        staff.setEmailId("admin@gmail.com");
        staff.setPassword("encodedPass");
        staff.setEnabled("Y");

        StaffOtp otp = new StaffOtp();
        otp.setOtp("123456");
        otp.setStatus(OtpStatus.NEW);

        when(staffRepository.findByLoginId("admin@gmail.com")).thenReturn(Optional.of(staff));
        when(passwordEncoder.matches("123456", "encodedPass")).thenReturn(true);
        when(staffOtpRepository.findTopByStaffIdAndStatusOrderByIdDesc(any(), any())).thenReturn(Optional.empty());
        when(staffOtpRepository.save(any())).thenReturn(otp);
        when(staffMapper.toLoginResponse(staff)).thenReturn(new StaffLoginResponse());

        StaffLoginResponse response = staffService.login(request);

        assertNotNull(response);
        verify(emailService).sendOtpEmail(any(), any(), any());
    }

    @Test
    public void testVerifyStaffOtpSuccess() {

        StaffOtpVerifyRequest request = new StaffOtpVerifyRequest();
        request.setStaffId("SF00001");
        request.setOtp("123456");

        StaffOtp otp = new StaffOtp();
        otp.setOtp("123456");
        otp.setStatus(OtpStatus.SENT);
        otp.setCreatedDt(LocalDateTime.now());

        when(staffOtpRepository.findTopByStaffIdOrderByCreatedDtDesc("SF00001"))
                .thenReturn(Optional.of(otp));

        OtpVerifyResponse response = staffService.verifyStaffOtp(request);
        assertTrue(response.isVerified());
        verify(staffOtpRepository).save(any());
    }

    @Test
    public void testForgotPasswordSuccess() {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("admin@gmail.com");

        Staff staff = new Staff();
        staff.setStaffId("SF00001");
        staff.setEmailId("admin@gmail.com");

        when(staffRepository.findByEmailId("admin@gmail.com")).thenReturn(Optional.of(staff));
        when(staffMapper.toPasswordResponse(staff)).thenReturn(new StaffPasswordResponse());
        StaffPasswordResponse response = staffService.forgotPassword(request);

        assertNotNull(response);

        verify(emailService).sendOtpEmail(any(), any(), any());
    }

    @Test
    public void testResetPasswordSuccess() {

        StaffResetPasswordRequest request = new StaffResetPasswordRequest();

        request.setStaffId("SF00001");
        request.setOtp("123456");
        request.setNewPassword("newPass");

        StaffOtp otp = new StaffOtp();
        otp.setOtp("123456");
        otp.setStatus(OtpStatus.SENT);
        otp.setCreatedDt(LocalDateTime.now());

        Staff staff = new Staff();
        staff.setStaffId("SF00001");
        staff.setEmailId("admin@gmail.com");

        when(staffOtpRepository
                .findTopByStaffIdAndStatusOrderByCreatedDtDesc(any(), any()))
                .thenReturn(Optional.of(otp));

        when(staffRepository.findByStaffId("SF00001")).thenReturn(Optional.of(staff));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");
        when(staffMapper.toPasswordResponse(staff)).thenReturn(new StaffPasswordResponse());

        StaffPasswordResponse response = staffService.resetPassword(request);

        assertNotNull(response);
        verify(staffRepository).save(any());
    }

    @Test
    public void testGetAllStaffSuccess() {

        List<Staff> staffList = List.of(new Staff());

        when(staffRepository.findAll())
                .thenReturn(staffList);

        when(staffMapper.toResponseList(staffList))
                .thenReturn(List.of(new StaffResponse()));

        List<StaffResponse> response =
                staffService.getAllStaff();

        assertFalse(response.isEmpty());
    }

    @Test
    public void testGetStaffByIdSuccess() {

        Staff staff = new Staff();
        staff.setStaffId("SF00001");

        when(staffRepository.findByStaffId("SF00001")).thenReturn(Optional.of(staff));
        when(staffMapper.toResponse(staff)).thenReturn(new StaffResponse());

        StaffResponse response = staffService.getStaffByStaffId("SF00001");
        assertNotNull(response);
    }

}
