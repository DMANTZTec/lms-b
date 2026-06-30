package com.dmantz.lms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.CourseFee;

@Repository
public interface CourseFeeRepository extends JpaRepository<CourseFee, Long> {

    List<CourseFee> findByCourse_IdOrderByEffectiveDateAsc(Long courseId);

    Optional<CourseFee> findTopByCourse_IdOrderByEffectiveDateDesc(Long courseId);

    boolean existsByCourse_IdAndEffectiveDate(Long courseId, LocalDate effectiveDate);
}