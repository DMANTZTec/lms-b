package com.dmantz.lms_b.service.Impl;

import com.dmantz.lms_b.dto.request.OtpVerifyRequest;
import com.dmantz.lms_b.dto.request.StudentLoginRequest;
import com.dmantz.lms_b.dto.request.StudentRegistrationRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        Assert.assertNotNull(result);
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

        when(otpRepository.findByStudent_StudentIdOrderByCreatedDtDesc("S000001"))
                .thenReturn(List.of(otp));

        var response = studentService.verifyOtp(request);

        Assert.assertTrue(response.isVerified());
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
