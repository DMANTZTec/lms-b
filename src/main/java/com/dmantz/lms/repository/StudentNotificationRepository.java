package com.dmantz.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms.entity.StudentNotification;

public interface StudentNotificationRepository extends JpaRepository<StudentNotification, Long> {

	// Only in-app messages are stored, so every row is a student-dashboard notification. Newest first.
	@Query("""
			SELECT n FROM StudentNotification n
			JOIN FETCH n.staff
			JOIN FETCH n.classBatch
			JOIN FETCH n.course
			WHERE n.student.studentId = :studentId
			ORDER BY n.sentAt DESC
			""")
	List<StudentNotification> findInAppForStudent(@Param("studentId") String studentId);

}
