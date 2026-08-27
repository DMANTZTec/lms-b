package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.EnrollmentRequest;
import com.dmantz.lms.dto.response.EnrollmentResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.EnrollmentType;
import com.dmantz.lms.entity.Program;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.mapper.EnrollmentMapper;
import com.dmantz.lms.repository.CourseRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.ProgramRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.EnrollmentService;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            ProgramRepository programRepository,
            EnrollmentMapper enrollmentMapper) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.programRepository = programRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    // ============================================================
    // CREATE ENROLLMENT
    // ============================================================

    @Override
    public EnrollmentResponse createEnrollment(
            EnrollmentRequest request) {

        validateEnrollmentRequest(request);

        Student student = studentRepository
                .findByStudentId(request.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);

        enrollment.setEnrollmentDate(LocalDateTime.now());

        enrollment.setStatus(EnrollmentStatus.PENDING);

        enrollment.setPaymentStatus("PENDING");


        // ==========================
        // COURSE ENROLLMENT
        // ==========================

        if (request.getCourseId() != null) {

            boolean alreadyEnrolled =
                    enrollmentRepository
                            .existsByStudentStudentIdAndCourseCourseId(
                                    request.getStudentId(),
                                    request.getCourseId()
                            );

            if (alreadyEnrolled) {
                throw new RuntimeException(
                        "Student is already enrolled in this course"
                );
            }

            Course course = courseRepository
                    .findByCourseIdAndIsDeletedFalse(request.getCourseId())
                    .orElseThrow(() ->
                            new RuntimeException("Course not found"));

            enrollment.setCourse(course);

            // Program must be null for course enrollment
            enrollment.setProgram(null);

            enrollment.setEnrollmentType(
                    EnrollmentType.COURSE
            );
        }


        // ==========================
        // PROGRAM ENROLLMENT
        // ==========================

        if (request.getProgramId() != null) {

            boolean alreadyEnrolled =
                    enrollmentRepository
                            .existsByStudentStudentIdAndProgramProgramId(
                                    request.getStudentId(),
                                    request.getProgramId()
                            );

            if (alreadyEnrolled) {
                throw new RuntimeException(
                        "Student is already enrolled in this program"
                );
            }

            Program program = programRepository
                    .findByProgramId(request.getProgramId())
                    .orElseThrow(() ->
                            new RuntimeException("Program not found"));

            enrollment.setProgram(program);

            // Course must be null for program enrollment
            enrollment.setCourse(null);

            enrollment.setEnrollmentType(
                    EnrollmentType.PROGRAM
            );
        }


        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponse(savedEnrollment);
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
                .orElseThrow(() ->
                        new RuntimeException("Enrollment not found"));

        return enrollmentMapper.toResponse(enrollment);
    }


    // ============================================================
    // GET ENROLLMENTS BY STUDENT ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(
            String studentId) {

        // Optional: check student exists first
        studentRepository
                .findByStudentId(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return enrollmentRepository
                .findByStudentStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }


    // ============================================================
    // UPDATE ENROLLMENT
    // ============================================================

    @Override
    public EnrollmentResponse updateEnrollment(
            Long id,
            EnrollmentRequest request) {

        validateEnrollmentRequest(request);

        Enrollment enrollment = enrollmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Enrollment not found"));

        Student student = studentRepository
                .findByStudentId(request.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        enrollment.setStudent(student);


        // ==========================
        // UPDATE TO COURSE
        // ==========================

        if (request.getCourseId() != null) {

            Course course = courseRepository
                    .findByCourseIdAndIsDeletedFalse(request.getCourseId())
                    .orElseThrow(() ->
                            new RuntimeException("Course not found"));

            enrollment.setCourse(course);
            enrollment.setProgram(null);

            enrollment.setEnrollmentType(
                    EnrollmentType.COURSE
            );
        }


        // ==========================
        // UPDATE TO PROGRAM
        // ==========================

        if (request.getProgramId() != null) {

            Program program = programRepository
                    .findByProgramId(request.getProgramId())
                    .orElseThrow(() ->
                            new RuntimeException("Program not found"));

            enrollment.setProgram(program);
            enrollment.setCourse(null);

            enrollment.setEnrollmentType(
                    EnrollmentType.PROGRAM
            );
        }

        Enrollment updatedEnrollment =
                enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponse(updatedEnrollment);
    }


    // ============================================================
    // DELETE ENROLLMENT
    // ============================================================

    @Override
    public void deleteEnrollment(Long id) {

        Enrollment enrollment = enrollmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Enrollment not found"));

        enrollmentRepository.delete(enrollment);
    }


    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateEnrollmentRequest(
            EnrollmentRequest request) {

        if (request.getStudentId() == null
                || request.getStudentId().isBlank()) {

            throw new RuntimeException(
                    "Student ID is required"
            );
        }

        boolean hasCourse =
                request.getCourseId() != null
                && !request.getCourseId().isBlank();

        boolean hasProgram =
                request.getProgramId() != null
                && !request.getProgramId().isBlank();


        // Neither course nor program

        if (!hasCourse && !hasProgram) {

            throw new RuntimeException(
                    "Either course ID or program ID is required"
            );
        }


        // Both course and program

        if (hasCourse && hasProgram) {

            throw new RuntimeException(
                    "Student can enroll in either a course or a program, not both"
            );
        }
    }
}