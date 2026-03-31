
package com.dmantz.lms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dmantz.lms.dto.request.ChapterRequest;
import com.dmantz.lms.dto.response.ChapterResponse;
import com.dmantz.lms.entity.Chapter;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(source = "chapterNm", target = "chapterNm")
    @Mapping(source = "chapterDesc", target = "chapterDesc")
    Chapter toEntity(ChapterRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "chapterNm", target = "chapterNm")
    @Mapping(source = "chapterDesc", target = "chapterDesc")
    @Mapping(source = "chapterNum", target = "chapterNum")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "createdDt", target = "createdDt")
    @Mapping(source = "updatedBy", target = "updatedBy")
    @Mapping(source = "updatedDt", target = "updatedDt")
    ChapterResponse toResponse(Chapter chapter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "chapterNum", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(source = "chapterNm", target = "chapterNm")
    @Mapping(source = "chapterDesc", target = "chapterDesc")
    void updateEntityFromRequest(ChapterRequest request, @MappingTarget Chapter chapter);
}
