package com.dmantz.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentStudentId(String studentId);

    boolean existsByStudentStudentIdAndCourseCourseId(
            String studentId,
            String courseId
    );

    boolean existsByStudentStudentIdAndProgramProgramId(
            String studentId,
            String programId
    );
}