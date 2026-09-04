package com.dmantz.lms.repository;

import com.dmantz.lms.entity.EnrollmentBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentBatchRepository extends JpaRepository<EnrollmentBatch, Long> {

	boolean existsByEnrollmentIdAndClassBatchId(Long enrollmentId, Long batchId);

	Optional<EnrollmentBatch> findByEnrollmentIdAndClassBatchId(Long enrollmentId, Long batchId);

	List<EnrollmentBatch> findByClassBatchId(Long batchId);

	List<EnrollmentBatch> findByEnrollmentId(Long enrollmentId);

	List<EnrollmentBatch> findByEnrollmentStudentStudentId(String studentId);

	long countByClassBatchId(Long batchId);

	@Query("""
			SELECT eb
			FROM EnrollmentBatch eb
			JOIN FETCH eb.enrollment e
			JOIN FETCH e.student
			WHERE eb.classBatch.id = :batchId
			""")
	List<EnrollmentBatch> findWithStudentsByClassBatchId(@Param("batchId") Long batchId);

	boolean existsByEnrollment_Student_StudentIdAndClassBatch_Id(String studentId, Long batchId);

}