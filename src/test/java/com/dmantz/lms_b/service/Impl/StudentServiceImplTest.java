package com.dmantz.lms_b.service.Impl;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.StudentLoginResponse;
import com.dmantz.lms_b.dto.response.StudentResponse;
import com.dmantz.lms_b.entity.OtpStatus;
import com.dmantz.lms_b.entity.Student;
import com.dmantz.lms_b.entity.StudentOtp;
import com.dmantz.lms_b.mapper.StudentMapper;
import com.dmantz.lms_b.repository.StudentOtpRepository;
import com.dmantz.lms_b.repository.StudentRepository;
import com.dmantz.lms_b.service.EmailService;
import com.dmantz.lms_b.service.impl.StudentServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentOtpRepository otpRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Spy
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // REGISTER TEST
    @Test
    public void testRegisterSuccess() {

        StudentRegistrationRequest request = new StudentRegistrationRequest();
        request.setEmailId("test@gmail.com");
        request.setMobileNum("9999999999");
        request.setPassword("123456");

        Student student = new Student();
        Student savedStudent = new Student();
        savedStudent.setStudentId("S000001");

        StudentResponse response = new StudentResponse();
        response.setStudentId("S000001");

        when(studentRepository.existsByEmailId(anyString())).thenReturn(false);
        when(studentRepository.existsByMobileNum(anyString())).thenReturn(false);
        when(studentMapper.toEntity(request)).thenReturn(student);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
        when(studentMapper.toResponse(savedStudent)).thenReturn(response);

        StudentResponse result = studentService.register(request);

        assertNotNull(result);
        Assert.assertEquals(result.getStudentId(), "S000001");

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testRegisterEmailExists() {

        StudentRegistrationRequest request = new StudentRegistrationRequest();
        request.setEmailId("test@gmail.com");

        when(studentRepository.existsByEmailId("test@gmail.com")).thenReturn(true);

        studentService.register(request);
    }

    // VERIFY OTP
    @Test
    public void testVerifyOtpSuccess() {

        OtpVerifyRequest request = new OtpVerifyRequest();
        request.setStudentId("S000001");
        request.setOtp("123456");

        StudentOtp otp = new StudentOtp();
        otp.setOtp("123456");
        otp.setStatus(OtpStatus.SENT);
        otp.setCreatedDt(LocalDateTime.now());

        when(otpRepository.findByStudent_StudentIdOrderByCreatedDtDesc("S000001")).thenReturn(List.of(otp));

        var response = studentService.verifyOtp(request);

        Assert.assertTrue(response.isVerified());
    }

    // LOGIN TEST
    @Test
    public void testLoginSuccess() {

        StudentLoginRequest request = new StudentLoginRequest();
        request.setUsername("test@gmail.com");
        request.setPassword("1234");

        Student student = new Student();
        student.setEmailId("test@gmail.com");
        student.setPassword("encodedPassword");
        student.setEnabled("Y");

        StudentOtp otp = new StudentOtp();
        otp.setOtp("123456");
        StudentLoginResponse response = new StudentLoginResponse();

        when(studentRepository.findByEmailIdOrMobileNumOrLoginId(any(), any(), any())).thenReturn(student);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(studentMapper.toLoginResponse(student)).thenReturn(response);

        doReturn(otp).when(studentService).generateOtp(any(Student.class));

        StudentLoginResponse result = studentService.login(request);

        assertNotNull(result);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testLoginInvalidUser() {

        StudentLoginRequest request = new StudentLoginRequest();
        request.setUsername("wrong@gmail.com");

        when(studentRepository.findByEmailIdOrMobileNumOrLoginId(anyString(), anyString(), anyString()))
                .thenReturn(null);

        studentService.login(request);
    }

    // FORGOT PASSWORD
    @Test
    public void testForgotPasswordSuccess() {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@gmail.com");

        Student student = new Student();
        student.setStudentId("S000001");
        student.setLoginId("test@gmail.com");

        StudentOtp otp = new StudentOtp();
        otp.setOtp("123456");

        when(studentRepository.findByEmailId("test@gmail.com")).thenReturn(Optional.of(student));

        doReturn(otp).when(studentService).generateOtp(any(Student.class));

        studentService.forgotPassword(request);

        verify(studentRepository, times(1)).findByEmailId("test@gmail.com");
    }

    // RESET PASSWORD
    @Test
    public void testResetPasswordSuccess() {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setStudentId("S000001");
        request.setOtp("123456");
        request.setNewPassword("newPassword");

        StudentOtp otp = new StudentOtp();
        otp.setOtp("123456");
        otp.setStatus(OtpStatus.SENT);
        otp.setCreatedDt(LocalDateTime.now());

        Student student = new Student();
        student.setStudentId("S000001");

        when(otpRepository.findLatestOtpByStudentId("S000001")).thenReturn(List.of(otp));
        when(studentRepository.findByStudentId("S000001")).thenReturn(Optional.of(student));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        studentService.resetPassword(request);

        // verify password updated
        assertEquals("encodedPassword", student.getPassword());

        // verify student saved
        verify(studentRepository, times(1)).save(student);

        verify(otpRepository, atLeastOnce()).save(any(StudentOtp.class));
    }

    // GET ALL STUDENTS
    @Test
    public void testGetAllStudents() {

        Student student = new Student();
        student.setStudentId("S000001");

        StudentResponse response = new StudentResponse();
        response.setStudentId("S000001");

        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(studentMapper.toResponse(student)).thenReturn(response);

        List<StudentResponse> result = studentService.getAllStudents();

        Assert.assertEquals(result.size(), 1);
    }

}
