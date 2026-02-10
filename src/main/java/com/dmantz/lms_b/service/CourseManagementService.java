package com.dmantz.lms_b.service;

import java.util.List;

import com.dmantz.lms_b.dto.request.CourseRequest;
import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.CourseResponse;
import com.dmantz.lms_b.dto.response.SubjectResponse;

public interface CourseManagementService {
//	create a subject
	SubjectResponse createSubject(SubjectRequest requestDto, Long staffID);

//  view all subject
	List<SubjectResponse> viewAllSubjects();

// update an existing subject
	SubjectResponse updateSubject(Long subjectId, SubjectRequest request, Long staffId);

//	delete subject
	void deleteSubject(Long subjectId, Long staffId);

//	create a course
	CourseResponse createCourse(CourseRequest requestDto, Long staffId);

//	 view all courses
	List<CourseResponse> viewAllCourses();

//	 update an existing course
	CourseResponse updateCourse(Long courseId, CourseRequest request, Long staffId);

//		delete subject
	void deleteCourse(Long courseId, Long staffId);

//	view Courses by subjects 
	List<CourseResponse> viewCoursesBySubject(Long subjectId);

}
