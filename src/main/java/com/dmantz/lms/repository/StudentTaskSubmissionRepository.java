package com.dmantz.lms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms.entity.Provider;
import com.dmantz.lms.entity.ReviewStatus;
import com.dmantz.lms.entity.StudentTaskSubmission;

public interface StudentTaskSubmissionRepository extends JpaRepository<StudentTaskSubmission, Long>{


	List<StudentTaskSubmission> findByStudentTask_CourseId(String courseId);

	List<StudentTaskSubmission> findByStudentTask_CourseIdIn(List<String> courseIds);

    List<StudentTaskSubmission> findByStudentTask_CourseIdInAndReviewStatus(
            List<String> courseIds, ReviewStatus reviewStatus);

    List<StudentTaskSubmission> findByStudent_StudentIdAndSubmittedAtBetween(
            String studentId, LocalDateTime start, LocalDateTime end);

    List<StudentTaskSubmission> findByStudent_StudentIdAndReviewStatusAndOverallRatingIsNotNullAndReviewedAtBetween(
            String studentId, ReviewStatus reviewStatus, LocalDateTime start, LocalDateTime end);
    
    List<StudentTaskSubmission> findByStudentTask_IdIn(List<Long> taskIds);

    @Query("select coalesce(sum(s.timeSpentInMinutes), 0) from StudentTaskSubmission s "
            + "where s.student.studentId = :studentId and s.timeSpentInMinutes > 0")
    long sumTimeSpentInMinutesByStudentId(@Param("studentId") String studentId);
}
