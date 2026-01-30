package com.dmantz.lms_b.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dmantz.lms_b.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

	@Query("SELECT s FROM Subject s WHERE s.subject_short_cd = :code")
	Optional<Subject> findBySubjectShortCd(@Param("code") String code);
}
