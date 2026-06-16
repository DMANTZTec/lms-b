package com.dmantz.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.ProgramCourse;
import com.dmantz.lms.entity.Subject;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {

	boolean existsByProgram_ProgramIdAndCourse_CourseId(String programId, String courseId);

	Optional<ProgramCourse> findByProgram_ProgramIdAndCourse_CourseId(String programId, String courseId);

}
