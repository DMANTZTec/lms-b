package com.dmantz.lms.repository;

import com.dmantz.lms.entity.StaffCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffCourseRepository extends JpaRepository<StaffCourse, Long> {

	List<StaffCourse> findByCourse_CourseId(String courseId);

	List<StaffCourse> findByStaff_StaffId(String staffId);

	boolean existsByStaff_StaffIdAndCourse_CourseId(String staffId, String courseId);

	Optional<StaffCourse> findByStaff_StaffIdAndCourse_CourseId(String staffId, String courseId);
}