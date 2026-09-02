package com.dmantz.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.dmantz.lms.dto.response.EnrollmentResponse;
import com.dmantz.lms.entity.Enrollment;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EnrollmentMapper {

    @Mapping(source = "student.id", target = "studentId")

    @Mapping(
        expression = "java(enrollment.getStudent().getFirstNm() + \" \" + enrollment.getStudent().getLastNm())",
        target = "studentName"
    )

    @Mapping(
            source = "course.courseId",
            target = "courseId"
    )    @Mapping(source = "course.courseTitle", target = "courseTitle")

    @Mapping(source = "program.programId", target = "programId")

    // Change programName if your Program entity has a different field
    @Mapping(source = "program.programTitle", target = "programName")

    EnrollmentResponse toResponse(Enrollment enrollment);
}