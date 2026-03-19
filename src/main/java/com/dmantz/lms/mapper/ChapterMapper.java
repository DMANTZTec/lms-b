package com.dmantz.lms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.dmantz.lms.dto.request.ChapterRequest;
import com.dmantz.lms.dto.response.ChapterResponse;
import com.dmantz.lms.entity.Chapter;
import com.dmantz.lms.entity.Course;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "chapterNm", target = "chapterNm")
	@Mapping(source = "chapterDesc", target = "chapterDesc")
	Chapter toEntity(ChapterRequest request);

	// RESPONSE mapping stays same
	@BeanMapping(ignoreByDefault = true)
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

	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "chapterNm", target = "chapterNm")
	@Mapping(source = "chapterDesc", target = "chapterDesc")
	void updateEntityFromRequest(ChapterRequest request, @MappingTarget Chapter chapter);
}
