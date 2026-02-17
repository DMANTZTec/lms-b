package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms_b.dto.response.StudentCourseResponse;
import com.dmantz.lms_b.dto.response.StudentDashboardResponse;
import com.dmantz.lms_b.dto.response.StudentSummaryResponse;
import com.dmantz.lms_b.entity.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentCourseMapper {

    // Request → Entity
    StudentCourse toEntity(StudentCourseEnrollRequest request);

    // Entity → Response
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "status", target = "status")
    StudentCourseResponse toResponse(StudentCourse entity);


    List<StudentCourseResponse> toResponseList(List<StudentCourse> courses);

    // Build summary DTO
    default StudentSummaryResponse toSummary(List<StudentCourse> courses) {
        StudentSummaryResponse summary = new StudentSummaryResponse();
        summary.setTotalEnrolled(courses.size());
        summary.setPlanned(courses.stream()
                .filter(c -> c.getStatus() != null && c.getStatus().name().equals("PLANNED"))
                .count());
        summary.setOngoing(courses.stream()
                .filter(c -> c.getStatus() != null && c.getStatus().name().equals("ONGOING"))
                .count());
        summary.setCompleted(courses.stream()
                .filter(c -> c.getStatus() != null && c.getStatus().name().equals("COMPLETED"))
                .count());
        summary.setAverageProgress(courses.stream()
                .mapToDouble(StudentCourse::getProgressPercentage)
                .average().orElse(0));
        return summary;
    }

    // Build full dashboard DTO
    default StudentDashboardResponse toDashboard(List<StudentCourse> courses) {
        StudentDashboardResponse dashboard = new StudentDashboardResponse();
        dashboard.setCourses(toResponseList(courses));
        dashboard.setSummary(toSummary(courses));
        return dashboard;
    }

}
