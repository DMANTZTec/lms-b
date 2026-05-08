package com.dmantz.lms.service.impl;

import com.dmantz.lms.config.JwtUtil;
import com.dmantz.lms.dto.request.StudentLoginRequest;
import com.dmantz.lms.dto.response.StudentLoginResponse;
import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.entity.OtpStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentOtp;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StudentOtpRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.AuthService;
import com.dmantz.lms.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StudentOtpRepository otpRepository;
    private final EmailService emailService;


    public AuthServiceImpl(StudentRepository studentRepository, StaffRepository staffRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, StudentOtpRepository otpRepository, EmailService emailService) {
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Override
    public StudentLoginResponse studentLogin(StudentLoginRequest request) {

        logger.info("Login attempt for username: {}", request.getUsername());

        String username = request.getUsername();

        Student student = studentRepository.findByEmailIdOrMobileNumOrLoginId(
                        username, username, username);

        // INVALID USER
        if (student == null) {
            logger.warn("Invalid login credentials for username: {}", username);
            throw new RuntimeException("Invalid Credentials");
        }

        // ACCOUNT DISABLED
        if (!"Y".equals(student.getEnabled())) {
            logger.warn("Account disabled for studentId: {}", student.getStudentId());
            throw new RuntimeException("Account disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(),
                student.getPassword())) {

            logger.warn("Wrong password attempt for studentId: {}",
                    student.getStudentId()
            );

            throw new RuntimeException("Invalid Credentials");
        }

        // GENERATE JWT TOKEN
        String token = jwtUtil.generateToken(student.getEmailId(), "STUDENT",
                        student.getStudentId());

        // GENERATE OTP
        StudentOtp otp = generateOtp(student);

        try {
            // SEND OTP EMAIL
            emailService.sendOtpEmail(
                    student.getEmailId(),
                    otp.getOtp(),
                    OtpPurpose.LOGIN);

            otp.setStatus(OtpStatus.SENT);
            otp.setUpdatedDt(LocalDateTime.now());
            otpRepository.save(otp);

            logger.info("Login OTP sent successfully to email: {}",
                    student.getEmailId()
            );

        } catch (Exception e) {

            logger.error("Failed to send login OTP to email: {}",
                    student.getEmailId(), e );

            otp.setStatus(OtpStatus.FAILED);
            otpRepository.save(otp);
            throw new RuntimeException("Failed to send OTP");
        }

        StudentLoginResponse response = new StudentLoginResponse();
        response.setRole("STUDENT");
        response.setStudentId(student.getStudentId());
        response.setEmail(student.getEmailId());
        response.setToken(token);
        response.setMessage("Login Successful. OTP sent to registered email.");
        return response;
    }

    public StudentOtp generateOtp(Student student) {

        logger.info("Generating OTP for studentId: {}", student.getStudentId());

        StudentOtp otp = new StudentOtp();
        otp.setStudent(student);
        otp.setOtp(String.valueOf(new Random().nextInt(900000) + 100000));
        otp.setStatus(OtpStatus.NEW);
        otp.setAttemptsNum(0);
        otp.setCreatedDt(LocalDateTime.now());
        StudentOtp savedOtp = otpRepository.save(otp);

        logger.info("OTP generated successfully for studentId: {}",
                student.getStudentId());

        return savedOtp;
    }


}