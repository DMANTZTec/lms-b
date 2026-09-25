package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.ClassTopicResponse;
import com.dmantz.lms.entity.ClassTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassTopicMapper {

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.topicNm", target = "topicName")
    ClassTopicResponse toResponse(ClassTopic entity);

    List<ClassTopicResponse> toResponseList(List<ClassTopic> entities);
}
