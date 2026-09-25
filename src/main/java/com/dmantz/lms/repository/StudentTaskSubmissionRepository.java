package com.dmantz.lms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
