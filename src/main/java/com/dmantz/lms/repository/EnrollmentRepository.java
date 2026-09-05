package com.dmantz.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.Enrollment;
import com.dmantz.lms.entity.EnrollmentStatus;

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

    // Students directly enrolled in one of the given courses.
    @Query("""
            SELECT DISTINCT e.student.studentId
            FROM Enrollment e
            WHERE e.enrollmentType = com.dmantz.lms.entity.EnrollmentType.COURSE
            AND e.course.id IN :courseIds
            """)
    List<String> findDistinctStudentIdsByCourseIds(@Param("courseIds") List<Long> courseIds);

    // Students enrolled in a program that includes one of the given courses.
    @Query("""
            SELECT DISTINCT e.student.studentId
            FROM Enrollment e
            JOIN ProgramCourse pc ON pc.program.id = e.program.id
            WHERE e.enrollmentType = com.dmantz.lms.entity.EnrollmentType.PROGRAM
            AND pc.course.id IN :courseIds
            """)
    List<String> findDistinctStudentIdsByProgramCourseIds(@Param("courseIds") List<Long> courseIds);

}