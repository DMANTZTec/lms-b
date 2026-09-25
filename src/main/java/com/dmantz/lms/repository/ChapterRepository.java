package com.dmantz.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.Chapter;

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
	
	// Get all chapters by courseId ordered by chapterNum
	List<Chapter> findByCourseIdOrderByChapterNumAsc(Long courseId);
	
	
	
}