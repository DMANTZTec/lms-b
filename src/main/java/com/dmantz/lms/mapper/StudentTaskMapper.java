package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.entity.StudentTask;
import com.dmantz.lms.entity.TopicReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface StudentTaskMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.topicNm", target = "topicName")
    @Mapping(source = "topic.references", target = "topicReferences")
    StudentTaskResponse toResponse(StudentTask task);

    default List<Map<String, Object>> mapTopicReferences(List<TopicReference> references) {

        return references == null
                ? List.of()
                : references.stream()
                .map(TopicReference::getRefValue)
                .filter(Objects::nonNull)
                .toList();
    }
}