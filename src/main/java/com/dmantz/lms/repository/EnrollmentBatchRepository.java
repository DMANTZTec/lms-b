package com.dmantz.lms.repository;

import com.dmantz.lms.entity.EnrollmentBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentBatchRepository
        extends JpaRepository<EnrollmentBatch, Long> {

    boolean existsByEnrollmentIdAndClassBatchId(
            Long enrollmentId,
            Long batchId
    );

    Optional<EnrollmentBatch>
    findByEnrollmentIdAndClassBatchId(
            Long enrollmentId,
            Long batchId
    );

    List<EnrollmentBatch>
    findByClassBatchId(Long batchId);

    List<EnrollmentBatch>
    findByEnrollmentId(Long enrollmentId);

    List<EnrollmentBatch>
    findByEnrollmentStudentStudentId(String studentId);

    long countByClassBatchId(Long batchId);

}