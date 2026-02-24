package com.dmantz.lms_b.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms_b.entity.ProgramCourse;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {

	boolean existsByProgram_ProgramIdAndCourse_CourseId(String programId, String courseId);

}
