package com.dmantz.lms_b.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmantz.lms_b.dto.response.StudentTopicReferenceProgressResponse;
import com.dmantz.lms_b.entity.StudentTopicReferenceProgress;

@Mapper(componentModel = "spring")
public interface StudentTopicReferenceProgressMapper {
	  // ENTITY → RESPONSE
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "topicReference.id", target = "referenceId")
    StudentTopicReferenceProgressResponse toResponse(StudentTopicReferenceProgress entity);

}
