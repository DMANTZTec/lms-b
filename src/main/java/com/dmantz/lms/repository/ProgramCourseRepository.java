package com.dmantz.lms.repository;

import java.util.List;
import java.util.Optional;

import com.dmantz.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.ProgramCourse;
import com.dmantz.lms.entity.Subject;
import org.springframework.data.jpa.repository.Query;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {

	boolean existsByProgram_ProgramIdAndCourse_CourseId(String programId, String courseId);

	Optional<ProgramCourse> findByProgram_ProgramIdAndCourse_CourseId(String programId, String courseId);

	@Query("SELECT pc.course FROM ProgramCourse pc WHERE pc.program.programId = :programId")
	List<Course> findCoursesByProgramId(String programId);

}
