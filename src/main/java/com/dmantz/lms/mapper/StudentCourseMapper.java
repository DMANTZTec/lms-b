package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.StudentCourseEnrollRequest;
import com.dmantz.lms.dto.response.MyCourseResponse;
import com.dmantz.lms.dto.response.StudentCourseResponse;
import com.dmantz.lms.entity.CourseStatus;
import com.dmantz.lms.entity.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentCourseMapper {

    // Request → Entity
    StudentCourse toEntity(StudentCourseEnrollRequest request);

    // Entity → Response
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "status", target = "status")
    StudentCourseResponse toResponse(StudentCourse entity);


    List<StudentCourseResponse> toResponseList(List<StudentCourse> courses);

    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.courseTitle", target = "courseName")
    @Mapping(source = "start_dt", target = "startDate")
    @Mapping(source = "completedDt", target = "endDate")
    @Mapping(target = "progress", expression = "java(calculateProgress(entity))")
    @Mapping(source = "status", target = "status")
    MyCourseResponse toDto(StudentCourse entity);

    default Integer map(Double progress) {
        return progress == null ? 0 : progress.intValue();
    }

    default String map(CourseStatus status) {
        return status != null ? status.name() : null;
    }

    default Integer calculateProgress(StudentCourse entity) {

        if (entity.getStatus() == null) return 0;

        return switch (entity.getStatus()) {
            case COMPLETED -> 100;
            case ONGOING -> 50; // temporary logic
            case PLANNED -> 0;
        };
    }

}
