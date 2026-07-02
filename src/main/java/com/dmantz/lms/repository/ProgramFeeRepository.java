package com.dmantz.lms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.ProgramFee;

public interface ProgramFeeRepository extends JpaRepository<ProgramFee, Long> {

    List<ProgramFee> findByProgram_IdOrderByEffectiveDateAsc(Long programId);

    Optional<ProgramFee> findTopByProgram_IdOrderByEffectiveDateDesc(Long programId);

    boolean existsByProgram_IdAndEffectiveDate(Long programId, LocalDate effectiveDate);
}