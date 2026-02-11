package com.dmantz.lms_b.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmantz.lms_b.entity.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

	// Check for duplicate chapter name in a course (ignore case)
	Optional<Chapter> findByCourse_CourseIdAndChapterNmIgnoreCase(String courseId, String chapterNm);

	// Get the latest chapter number for a course
	Optional<Chapter> findTopByCourse_CourseIdOrderByChapterNumDesc(String courseId);

	// Find chapter by ID and courseId
	Optional<Chapter> findByIdAndCourse_CourseId(Long chapterId, String courseId);

	// Get all chapters by courseId
	List<Chapter> findByCourse_CourseId(String courseId);
}