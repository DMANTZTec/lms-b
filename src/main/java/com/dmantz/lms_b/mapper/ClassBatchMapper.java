package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;
import com.dmantz.lms_b.entity.ClassBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassBatchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    ClassBatch toEntity(CreateClassRequest request);

    @Mapping(source = "id", target = "classId")
    @Mapping(source = "course.id", target = "courseId")
    ClassResponse toResponse(ClassBatch entity);
}

