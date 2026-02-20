package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.TopicReferenceRequestDto;
import com.dmantz.lms_b.dto.response.TopicReferenceResponseDto;
import com.dmantz.lms_b.entity.TopicReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicReferenceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "refType", ignore = true)
    TopicReference toEntity(TopicReferenceRequestDto dto);

    @Mapping(source = "topic.id", target = "topicId")
    TopicReferenceResponseDto toDto(TopicReference entity);
}
