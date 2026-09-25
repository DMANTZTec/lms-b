package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.EnrollStudentResponse;
import com.dmantz.lms.entity.ClassStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassStudentMapper {

    @Mapping(source = "classBatch.id", target = "classBatchId")
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.firstNm", target = "studentName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "enrolledDate", target = "enrolledDate")
    EnrollStudentResponse toDto(ClassStudent entity);
}
