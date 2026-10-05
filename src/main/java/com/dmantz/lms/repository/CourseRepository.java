package com.dmantz.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
            String courseTitle,
            Long subjectId,
            Long providerId,
            String language);

    long countBySubject_SubjectShortCd(String subjectShortCd);

    List<Course> findBySubject_IdAndIsDeletedFalse(Long subjectId);

    Optional<Course> findByCourseIdAndIsDeletedFalse(String courseId);

    Optional<Course> findTopBySubject_SubjectShortCdAndIsDeletedFalseOrderByIdDesc(
            String subjectShortCd);

    boolean existsByCourseIdAndIsDeletedFalse(String courseId);
    
    List<Course> findByIsDeletedFalse();
    
    Optional<Course> findByCourseId(String courseId);
}