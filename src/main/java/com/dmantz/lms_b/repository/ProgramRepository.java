package com.dmantz.lms_b.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms_b.entity.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {

	boolean existsByProgramTitleAndProvider_Id(String programTitle, Long providerId);

	boolean existsByProgramTitleAndProvider_IdAndIdNot(String programTitle, Long providerId, Long id);

	Optional<Program> findByProgramId(String programId);

	Optional<Program> findTopByOrderByIdDesc();

	boolean existsByProgramId(String generatedId);

}
