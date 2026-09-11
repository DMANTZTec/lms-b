package com.dmantz.lms.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms.entity.StudentTaskMentor;

public interface StudentTaskMentorRepository extends JpaRepository<StudentTaskMentor, Long> {

	boolean existsByStudentTask_IdAndMentorStudent_StudentId(Long studentTaskId, String mentorStudentId);
	
	@Query("""
		    SELECT COALESCE(SUM(
		        CASE
		            WHEN stm.minsSpent > 60 THEN 15
		            WHEN stm.minsSpent > 30 THEN 10
		            WHEN stm.minsSpent > 0  THEN 5
		            ELSE 0
		        END
		        +
		        CASE WHEN stm.studentAck = true THEN 5 ELSE 0 END
		    ), 0)
		    FROM StudentTaskMentor stm
		    WHERE stm.mentorStudent.studentId = :studentId
		""")
		Integer getTotalPoints(@Param("studentId") String studentId);

		@Query("""
		    SELECT COALESCE(SUM(
		        CASE
		            WHEN stm.minsSpent > 60 THEN 15
		            WHEN stm.minsSpent > 30 THEN 10
		            WHEN stm.minsSpent > 0  THEN 5
		            ELSE 0
		        END
		        +
		        CASE WHEN stm.studentAck = true THEN 5 ELSE 0 END
		    ), 0)
		    FROM StudentTaskMentor stm
		    WHERE stm.mentorStudent.studentId = :studentId
		    AND stm.createdDt >= :from
		""")
		Integer getPointsSince(@Param("studentId") String studentId, @Param("from") LocalDateTime from);

		boolean existsByMentorStudentStudentId(String studentId);
}