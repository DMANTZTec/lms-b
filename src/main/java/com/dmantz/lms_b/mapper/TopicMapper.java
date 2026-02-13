package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.TopicRequestDto;
import com.dmantz.lms_b.dto.response.TopicResponseDto;
import com.dmantz.lms_b.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topicNum", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDt", ignore = true)
    @Mapping(source = "chapterId", target = "chapter.id")
    @Mapping(source = "topicName", target = "topicNm")
    @Mapping(source = "staffId", target = "createdBy")
    Topic toEntity(TopicRequestDto request);

    @Mapping(source = "chapter.id", target = "chapterId")
    @Mapping(source = "chapter.chapterNm", target = "chapterName")
    @Mapping(source = "topicNm", target = "topicName")
    @Mapping(source = "createdDt", target = "createdDate")
    @Mapping(source = "updatedDt", target = "updatedDate")
    @Mapping(source = "topicNum", target = "topicNumber")
    TopicResponseDto toResponseDto(Topic topic);
}
