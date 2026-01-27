package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.*;
import com.dmantz.lms_b.dto.response.StudentResponse;
import com.dmantz.lms_b.entity.Student;
import com.dmantz.lms_b.mapper.StudentMapper;
import com.dmantz.lms_b.repository.StudentOtpRepository;
import com.dmantz.lms_b.repository.StudentRepository;
import com.dmantz.lms_b.service.StudentService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentOtpRepository otpRepository;
    private final StudentMapper studentMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository,
                              StudentOtpRepository otpRepository,
                              StudentMapper studentMapper,
                              BCryptPasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.otpRepository = otpRepository;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
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


    private void generateOtp(Student student) {
        // your existing OTP logic
    }

    public StudentResponse login(StudentLoginRequest request) {

        // 1️⃣ Find student by email / mobile / loginId
        Student student = studentRepository.findByUsername(request.getUsername());

        if (student == null) {
            throw new RuntimeException("Invalid login credentials");
        }

        // 2️⃣ Check if enabled
        if (!"Y".equals(student.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        // 3️⃣ Verify password
        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid login credentials");
        }
        return studentMapper.toResponse(student);
    }
}




