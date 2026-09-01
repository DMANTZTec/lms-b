package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.AssignStudentToBatchRequest;
import com.dmantz.lms.dto.response.EnrollmentBatchResponse;
import com.dmantz.lms.dto.response.StudentWeeklyScheduleResponse;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentBatch;
import com.dmantz.lms.entity.EnrollmentStatus;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.mapper.EnrollmentBatchMapper;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.ClassScheduleRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.EnrollmentRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.EnrollmentBatchService;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EnrollmentBatchServiceImpl
        implements EnrollmentBatchService {

    private final EnrollmentBatchRepository enrollmentBatchRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassBatchRepository classBatchRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final StaffRepository staffRepository;
    private final EnrollmentBatchMapper mapper;

    public EnrollmentBatchServiceImpl(
            EnrollmentBatchRepository enrollmentBatchRepository,
            EnrollmentRepository enrollmentRepository,
            ClassBatchRepository classBatchRepository,
            ClassScheduleRepository classScheduleRepository,
            StaffRepository staffRepository,
            EnrollmentBatchMapper mapper) {

        this.enrollmentBatchRepository = enrollmentBatchRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.classBatchRepository = classBatchRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.staffRepository = staffRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // ASSIGN ENROLLED STUDENT TO BATCH
    // =========================================================

    @Override
    public EnrollmentBatchResponse assignStudentToBatch(
            AssignStudentToBatchRequest request) {

        /*
         * 1. Find enrollment
         */
        Enrollment enrollment =
                enrollmentRepository.findById(
                        request.getEnrollmentId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Enrollment not found with id: "
                                        + request.getEnrollmentId()
                        )
                );

        /*
         * 2. Find batch
         */
        ClassBatch batch =
                classBatchRepository.findById(
                        request.getBatchId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Batch not found with id: "
                                        + request.getBatchId()
                        )
                );

        /*
         * 3. Enrollment must be ACTIVE
         */
        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {

            throw new RuntimeException(
                    "Only ACTIVE enrollment can be assigned to a batch"
            );
        }

        /*
         * 4. Validate that student's enrollment
         *    belongs to the course of this batch.
         */
        validateEnrollmentForBatch(
                enrollment,
                batch
        );

        /*
         * 5. Prevent duplicate assignment
         */
        if (enrollmentBatchRepository
                .existsByEnrollmentIdAndClassBatchId(
                        enrollment.getId(),
                        batch.getId())) {

            throw new RuntimeException(
                    "Student is already assigned to this batch"
            );
        }

        /*
         * 6. Check batch capacity
         */
        if (batch.getCapacity() != null) {

            long currentStudentCount =
                    enrollmentBatchRepository
                            .countByClassBatchId(
                                    batch.getId()
                            );

            if (currentStudentCount >= batch.getCapacity()) {

                throw new RuntimeException(
                        "Batch capacity is full"
                );
            }
        }

        /*
         * 7. Get staff from existing authentication
         *
         * Request does NOT contain staffId.
         */
        Staff authenticatedStaff =
                getAuthenticatedStaff();

        /*
         * 8. Create EnrollmentBatch
         */
        EnrollmentBatch enrollmentBatch =
                new EnrollmentBatch();

        enrollmentBatch.setEnrollment(
                enrollment
        );

        enrollmentBatch.setClassBatch(
                batch
        );

        enrollmentBatch.setAssignedBy(
                authenticatedStaff
        );

        enrollmentBatch.setAssignedDate(
                LocalDateTime.now()
        );

        /*
         * 9. Save
         */
        EnrollmentBatch saved =
                enrollmentBatchRepository.save(
                        enrollmentBatch
                );

        /*
         * 10. Convert entity → response using MapStruct
         */
        return mapper.toResponse(saved);
    }

    // =========================================================
    // VALIDATE ENROLLMENT COURSE
    // =========================================================

    private void validateEnrollmentForBatch(
            Enrollment enrollment,
            ClassBatch batch) {

        if (batch.getCourse() == null) {

            throw new RuntimeException(
                    "Batch is not associated with a course"
            );
        }

        String batchCourseId =
                batch.getCourse().getCourseId();

        /*
         * CASE 1:
         * Student directly enrolled in a course.
         *
         * Enrollment
         *      ↓
         *    Course
         */
        if (enrollment.getCourse() != null) {

            String enrolledCourseId =
                    enrollment.getCourse().getCourseId();

            if (!batchCourseId.equals(enrolledCourseId)) {

                throw new RuntimeException(
                        "Student is not enrolled in the course "
                                + "of this batch"
                );
            }

            return;
        }

        /*
         * CASE 2:
         * Student enrolled through a program.
         *
         * Enrollment
         *      ↓
         *    Program
         *      ↓
         *    Courses
         */
        if (enrollment.getProgram() != null) {

            boolean courseExists =
                    enrollment.getProgram()
                            .getProgramCourses()
                            .stream()
                            .anyMatch(course ->
                                    batchCourseId.equals(
                                            course.getCourse()
                                    )
                            );

            if (!courseExists) {

                throw new RuntimeException(
                        "Batch course does not belong "
                                + "to student's enrolled program"
                );
            }

            return;
        }

        throw new RuntimeException(
                "Enrollment has neither course nor program"
        );
    }

    // =========================================================
    // GET ALL STUDENTS IN A BATCH
    // =========================================================

    @Override
    public List<EnrollmentBatchResponse> getStudentsByBatch(
            Long batchId) {

        /*
         * Make sure batch exists
         */
        classBatchRepository.findById(batchId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Batch not found with id: "
                                        + batchId
                        )
                );

        /*
         * Get all students assigned to batch
         */
        return enrollmentBatchRepository
                .findByClassBatchId(batchId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // =========================================================
    // GET ENROLLMENT-BATCH BY ID
    // =========================================================

    @Override
    public EnrollmentBatchResponse getEnrollmentBatch(
            Long enrollmentBatchId) {

        EnrollmentBatch enrollmentBatch =
                enrollmentBatchRepository.findById(
                        enrollmentBatchId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "EnrollmentBatch not found with id: "
                                        + enrollmentBatchId
                        )
                );

        return mapper.toResponse(
                enrollmentBatch
        );
    }

    // =========================================================
    // REMOVE STUDENT FROM BATCH
    // =========================================================

    @Override
    public void removeStudentFromBatch(
            Long enrollmentBatchId) {

        EnrollmentBatch enrollmentBatch =
                enrollmentBatchRepository.findById(
                        enrollmentBatchId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "EnrollmentBatch not found with id: "
                                        + enrollmentBatchId
                        )
                );

        enrollmentBatchRepository.delete(
                enrollmentBatch
        );
    }

    // =========================================================
    // GET STUDENT WEEKLY SCHEDULE
    // =========================================================

    @Override
    public List<StudentWeeklyScheduleResponse>
    getStudentWeeklySchedule(
            String studentId,
            LocalDate startDate) {

        /*
         * 1. Find all batches assigned to this student
         */
        List<EnrollmentBatch> assignments =
                enrollmentBatchRepository
                        .findByEnrollmentStudentStudentId(
                                studentId
                        );

        /*
         * Student has not been assigned
         * to any batch.
         */
        if (assignments.isEmpty()) {
            return List.of();
        }

        /*
         * 2. Get batch IDs
         */
        List<Long> batchIds =
                assignments.stream()
                        .map(assignment ->
                                assignment
                                        .getClassBatch()
                                        .getId()
                        )
                        .distinct()
                        .toList();

        /*
         * 3. Calculate weekly date range
         *
         * Example:
         *
         * startDate = 2026-09-07
         *
         * endDate = 2026-09-13
         */
        LocalDate endDate =
                startDate.plusDays(6);

        /*
         * 4. Get class schedules
         *    belonging to student's batches.
         */
        List<ClassSchedule> schedules =
                classScheduleRepository
                        .findByClassBatchIdInAndClassDateBetweenOrderByClassDateAscStartTimeAsc(
                                batchIds,
                                startDate,
                                endDate
                        );

        /*
         * 5. Convert entities → DTOs
         *    using MapStruct.
         */
        return schedules.stream()
                .map(schedule ->
                        mapper.toScheduleResponse(
                                schedule,
                                studentId
                        )
                )
                .toList();
    }

    // =========================================================
    // GET AUTHENTICATED STAFF
    // =========================================================

 // =========================================================
 // GET AUTHENTICATED STAFF
 // =========================================================

 private Staff getAuthenticatedStaff() {

     Authentication authentication =
             SecurityContextHolder
                     .getContext()
                     .getAuthentication();

     if (authentication == null
             || !authentication.isAuthenticated()) {

         throw new RuntimeException(
                 "Authenticated staff is required"
         );
     }

     // Your JWT authentication returns email
     String email = authentication.getName();

     return staffRepository
             .findByEmailId(email)
             .orElseThrow(() ->
                     new RuntimeException(
                             "Staff not found with email: "
                                     + email
                     )
             );
 }
}