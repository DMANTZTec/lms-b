package com.dmantz.lms.repository;

import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.ClassStatus;
import com.dmantz.lms.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassBatchRepository extends JpaRepository<ClassBatch, Long> {
	@Query("""
			SELECT cb
			FROM ClassBatch cb
			JOIN StudentCourse sc ON sc.course.id = cb.course.id
			WHERE sc.student.id = :studentId
			""")
	List<ClassBatch> findByStudentId(Long studentId);

	// ClassBatchRepository
	List<ClassBatch> findByCourse_CourseId(String courseId);

	boolean existsByCourse(Course course);

	Optional<ClassBatch> findTopByCourse_CourseIdAndStatusOrderByStartDateDesc(String courseId, ClassStatus status);

	Optional<ClassBatch> findTopByCourse_CourseIdOrderByStartDateDesc(String courseId);

	@Query("""
			SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
			FROM ClassBatch b
			JOIN b.instructors instructor
			WHERE b.id = :batchId
			AND instructor.staffId = :staffId
			""")
	boolean existsByIdAndInstructorsStaffId(@Param("batchId") Long batchId, @Param("staffId") String staffId);

	List<ClassBatch> findByInstructors_StaffId(String staffId);

	// Flips any still-SCHEDULED batch whose end date has passed to COMPLETED.
	// Driven by a periodic job (ClassBatchStatusScheduler) — never touches CANCELLED rows.
	@Modifying
	@Transactional
	@Query("""
			UPDATE ClassBatch cb
			SET cb.status = com.dmantz.lms.entity.ClassStatus.COMPLETED
			WHERE cb.status = com.dmantz.lms.entity.ClassStatus.SCHEDULED
			AND cb.endDate < :today
			""")
	int markPastBatchesCompleted(@Param("today") LocalDate today);

}
