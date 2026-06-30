package com.dmantz.lms.repository;

import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

	Optional<ClassBatch> findTopByCourse_CourseIdAndStatusOrderByStartDateDesc(String courseId, String status);

	Optional<ClassBatch> findTopByCourse_CourseIdOrderByStartDateDesc(String courseId);

}
