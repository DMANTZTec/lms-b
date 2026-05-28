package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.TopicReferenceRequestDto;
import com.dmantz.lms.dto.response.TopicReferenceDataDto;
import com.dmantz.lms.entity.TopicReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicReferenceMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "topic", ignore = true)
	@Mapping(target = "refType", ignore = true)
	@Mapping(target = "refValue", ignore = true)
	TopicReference toEntity(TopicReferenceRequestDto dto);

	@Mapping(source = "topic.id", target = "topicId")
	TopicReferenceDataDto toDataDto(TopicReference entity);
}
