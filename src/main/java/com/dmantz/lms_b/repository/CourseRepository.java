package com.dmantz.lms_b.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmantz.lms_b.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	boolean existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(String courseTitle, Long subjectId,
			Long providerId, String language);

	long countBySubject_SubjectShortCd(String subjectShortCd);

	List<Course> findBySubject_Id(Long subjectId);
}
