package com.dmantz.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmantz.lms.entity.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {

	boolean existsByProgramTitleAndProvider_Id(String programTitle, Long providerId);

	boolean existsByProgramTitleAndProvider_IdAndIdNot(String programTitle, Long providerId, Long id);

	Optional<Program> findByProgramId(String programId);

	Optional<Program> findTopByOrderByIdDesc();

	@Query("""
			    SELECT DISTINCT p FROM Program p
			    LEFT JOIN FETCH p.programCourses pc
			    LEFT JOIN FETCH pc.course c
			    LEFT JOIN FETCH c.subject s
			    LEFT JOIN FETCH c.provider cp
			""")
	List<Program> findAllWithCourses();

	@Query("""
			    SELECT p FROM Program p
			    LEFT JOIN FETCH p.programCourses pc
			    LEFT JOIN FETCH pc.course c
			    LEFT JOIN FETCH c.subject s
			    LEFT JOIN FETCH c.provider cp
			    WHERE p.id = :id
			""")
	Optional<Program> findByIdWithCourses(@Param("id") Long id);

	boolean existsByProgramId(String generatedId);
}