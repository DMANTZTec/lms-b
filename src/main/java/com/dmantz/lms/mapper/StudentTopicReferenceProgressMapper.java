package com.dmantz.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.dmantz.lms.dto.response.StudentTopicReferenceProgressResponse;
import com.dmantz.lms.entity.StudentTopicReferenceProgress;

@Mapper(componentModel = "spring")
public interface StudentTopicReferenceProgressMapper {

    @Mapping(source = "student.studentId", target = "studentId")  // ← was student.id (Long), now student.studentId (String)
    @Mapping(source = "topicReference.id", target = "referenceId")
    StudentTopicReferenceProgressResponse toResponse(StudentTopicReferenceProgress entity);
}