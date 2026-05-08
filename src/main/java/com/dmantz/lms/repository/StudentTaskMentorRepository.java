package com.dmantz.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.StudentTaskMentor;

public interface StudentTaskMentorRepository extends JpaRepository<StudentTaskMentor, Long> {

	boolean existsByStudentTask_IdAndMentorStudent_StudentId(Long studentTaskId, String mentorStudentId);

}
