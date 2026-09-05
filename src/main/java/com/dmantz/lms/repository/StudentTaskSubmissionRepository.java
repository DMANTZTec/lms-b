package com.dmantz.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.Provider;
import com.dmantz.lms.entity.StudentTaskSubmission;

public interface StudentTaskSubmissionRepository extends JpaRepository<StudentTaskSubmission, Long>{

	
	List<StudentTaskSubmission> findByStudentTask_CourseId(String courseId);

	List<StudentTaskSubmission> findByStudentTask_CourseIdIn(List<String> courseIds);
}
