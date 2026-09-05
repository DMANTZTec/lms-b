package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.CreateClassRequest;
import com.dmantz.lms.dto.request.UpdateClassRequest;
import com.dmantz.lms.dto.response.ClassResponse;
import com.dmantz.lms.dto.response.StudentClassResponse;
import com.dmantz.lms.entity.ClassBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClassBatchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "batchName", target = "className")
    @Mapping(source = "beginDate", target = "startDate")
    ClassBatch toEntity(CreateClassRequest request);

    @Mapping(source = "id", target = "batchId")
    @Mapping(source = "className", target = "batchName")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.courseTitle", target = "courseName")
    ClassResponse toResponse(ClassBatch entity);

    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.courseTitle", target = "courseName")
    StudentClassResponse toDto(ClassBatch entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(source = "batchName", target = "className")
    void updateClassFromDto(UpdateClassRequest request, @MappingTarget ClassBatch entity);

}