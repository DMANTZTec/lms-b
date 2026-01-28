package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.OtpVerifyResponse;
import com.dmantz.lms_b.dto.response.StudentLoginResponse;
import com.dmantz.lms_b.dto.response.StudentResponse;
import com.dmantz.lms_b.entity.OtpStatus;
import com.dmantz.lms_b.entity.Student;
import com.dmantz.lms_b.entity.StudentOtp;
import com.dmantz.lms_b.exceptions.OtpExpiredException;
import com.dmantz.lms_b.exceptions.OtpInvalidException;
import com.dmantz.lms_b.exceptions.OtpNotFoundException;
import com.dmantz.lms_b.mapper.StudentMapper;
import com.dmantz.lms_b.repository.StudentOtpRepository;
import com.dmantz.lms_b.repository.StudentRepository;
import com.dmantz.lms_b.service.EmailService;
import com.dmantz.lms_b.service.StudentService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentOtpRepository otpRepository;
    private final StudentMapper studentMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final StudentOtpRepository studentOtpRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              StudentOtpRepository otpRepository,
                              StudentMapper studentMapper,
                              BCryptPasswordEncoder passwordEncoder, EmailService emailService, StudentOtpRepository studentOtpRepository) {
        this.studentRepository = studentRepository;
        this.otpRepository = otpRepository;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.studentOtpRepository = studentOtpRepository;
    }

    @Override
    public StudentResponse register(StudentRegistrationRequest request) {

        if (studentRepository.existsByEmail(request.getEmail_id())) {
            throw new RuntimeException("email already exists");
        }
        if (studentRepository.existsByMobile(request.getMobile_num())) {
            throw new RuntimeException("mobile number already exists");
        }

        // DTO → Entity
        Student student = studentMapper.toEntity(request);

        student.setStudent_id(generateStudentId());
        student.setLogin_id(request.getEmail_id());

        student.setPassword(passwordEncoder.encode(request.getPassword()));    //  Encrypt password

        //  System fields
        student.setStatus("ACTIVE");
        student.setEnabled("Y");
        student.setCreated_dt(LocalDateTime.now());

        Student savedStudent = studentRepository.save(student);  // Save
        generateOtp(savedStudent);
        return studentMapper.toResponse(savedStudent);
    }

    private String generateStudentId() {

        Long count = studentRepository.count() + 1; // Get total count of students
        return String.format("S%06d", count); // Format as S + 6-digit number → always 7 characters
    }


    private StudentOtp generateOtp(Student student) {

        StudentOtp otp = new StudentOtp();

        otp.setStudent(student);
        otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000)); // 6-digit OTP
        otp.setStatus(OtpStatus.valueOf(String.valueOf(OtpStatus.NEW)));
        otp.setAttemptsNum(0);
        otp.setCreatedDt(LocalDateTime.now());

        return otpRepository.save(otp);
    }

    @Override
    public StudentLoginResponse login(StudentLoginRequest request) {

        Student student = studentRepository.findByUsername(request.getUsername());
        if (student == null) {
            throw new RuntimeException("Invalid login credentials");
        }

        //  Check enabled
        if (!"Y".equals(student.getEnabled())) {
            throw new RuntimeException("Account disabled");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid login credentials");
        }

        //  Generate OTP
        StudentOtp otp = generateOtp(student);

        // Send OTP email
        try {
            emailService.sendOtpEmail(student.getEmail_id(), otp.getOtp());

            otp.setStatus(OtpStatus.SENT);
            otp.setUpdatedDt(LocalDateTime.now());
            otpRepository.save(otp);

        } catch (Exception e) {
            System.out.println("OTP email failed");
            // optional
            otp.setStatus(OtpStatus.FAILED);
            otpRepository.save(otp);
        }
        //  Response
        StudentLoginResponse response = studentMapper.toLoginResponse(student);
        response.setMessage("OTP sent to your registered email");

        return response;
    }


    @Override
    public OtpVerifyResponse verifyOtp(OtpVerifyRequest request) {

        StudentOtp otp = studentOtpRepository
                .findByStudentIdOrderByCreatedDtDesc(request.getStudentId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new OtpNotFoundException("OTP not found"));

        // Only SENT OTPs are valid
        if (otp.getStatus() != OtpStatus.SENT) {
            throw new OtpInvalidException("OTP is not valid");
        }

        // Expiry check (5 mins)
        if (otp.getCreatedDt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            otp.setStatus(OtpStatus.EXPIRED);
            otp.setUpdatedDt(LocalDateTime.now());
            studentOtpRepository.save(otp);
            throw new OtpExpiredException("OTP expired");
        }

        // Match OTP
        if (!otp.getOtp().equals(request.getOtp())) {
            otp.setAttemptsNum(otp.getAttemptsNum() + 1);
            otp.setUpdatedDt(LocalDateTime.now());
            studentOtpRepository.save(otp);
            throw new OtpInvalidException("Invalid OTP");
        }

        // Success
        otp.setStatus(OtpStatus.VERIFIED);
        otp.setUpdatedDt(LocalDateTime.now());
        studentOtpRepository.save(otp);

        OtpVerifyResponse response = new OtpVerifyResponse();
        response.setVerified(true);
        response.setMessage("OTP verified successfully");

        return response;
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse) // map entity to DTO
                .collect(Collectors.toList());
    }
}




