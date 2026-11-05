package com.dmantz.lms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.response.EnrollmentResponse;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.exceptions.BadRequestException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.EnrollmentMapper;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.EnrollmentService;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger logger = LogManager.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final PaymentWorkflow paymentWorkflow;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            EnrollmentMapper enrollmentMapper,
            PaymentWorkflow paymentWorkflow) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.paymentWorkflow = paymentWorkflow;
    }

    // ============================================================
    // GET ALL ENROLLMENTS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {

        return enrollmentRepository
                .findAll()
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET ENROLLMENT BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(Long id) {

        Enrollment enrollment = enrollmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        return enrollmentMapper.toResponse(enrollment);
    }

    // ============================================================
    // GET ENROLLMENTS BY STUDENT ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(String studentId) {

        studentRepository
                .findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return enrollmentRepository
                .findByStudentStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ============================================================
    // CANCEL ENROLLMENT (no refund)
    // ============================================================

    @Override
    public EnrollmentResponse cancelEnrollment(Long id) {

        Enrollment enrollment = enrollmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new BadRequestException("Enrollment #" + id + " is already cancelled");
        }

        // Rows are kept (payments reference them); access is removed
        paymentWorkflow.cancelEnrollment(enrollment, enrollment.getPaymentStatus());

        logger.info("Enrollment #{} cancelled by staff {}", id, PaymentWorkflow.currentStaffId());

        return enrollmentMapper.toResponse(enrollment);
    }
}
