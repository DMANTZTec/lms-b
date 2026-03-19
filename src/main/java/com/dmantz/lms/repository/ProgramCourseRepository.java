package com.dmantz.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.ProgramCourse;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {

	boolean existsByProgram_ProgramIdAndCourse_CourseId(String programId, String courseId);

}
