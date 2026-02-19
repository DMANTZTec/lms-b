package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.request.UpdateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;
import com.dmantz.lms_b.entity.ClassBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClassBatchMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    ClassBatch toEntity(CreateClassRequest request);

    @Mapping(source = "id", target = "batchId")   // ✅ FIXED
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.courseTitle", target = "courseName")
    ClassResponse toResponse(ClassBatch entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateClassFromDto(UpdateClassRequest request,
                            @MappingTarget ClassBatch entity);

}

