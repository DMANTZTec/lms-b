package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.CourseStatus;
import com.dmantz.lms_b.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    Optional<Object> findByStudent_IdAndCourse_Id(Long id, Long id1);

    List<StudentCourse> findByStudent_StudentId(String studentId);

    List<StudentCourse> findByStudentStudentId(String studentId);

    List<StudentCourse> findByStudentStudentIdAndStatus(
            String studentId,
            CourseStatus status);

}
