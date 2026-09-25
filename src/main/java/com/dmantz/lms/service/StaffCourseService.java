package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.AssignInstructorToCourseRequest;
import com.dmantz.lms.dto.response.InstructorResponse;
import com.dmantz.lms.dto.response.StaffCourseResponse;

import java.util.List;

public interface StaffCourseService {

    // Assign instructors to course
    void assignInstructorsToCourse(String courseId,
            AssignInstructorToCourseRequest request);

    // Get instructors by course → for UI chips
    List<InstructorResponse> getInstructorsByCourse(String courseId);

    // Remove instructor from course
    void removeInstructorFromCourse(String courseId, String staffId);

    // Get all courses assigned to a staff
    List<StaffCourseResponse> getCoursesByStaff(String staffId);
    
    List<InstructorResponse> getAllInstructors();
}