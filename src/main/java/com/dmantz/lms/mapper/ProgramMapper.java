package com.dmantz.lms.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.dmantz.lms.dto.request.ProgramRequest;
import com.dmantz.lms.dto.response.CourseResponse;
import com.dmantz.lms.dto.response.ProgramResponse;
import com.dmantz.lms.entity.Course;
import com.dmantz.lms.entity.Program;
import com.dmantz.lms.entity.ProgramCourse;

@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface ProgramMapper {

	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "programTitle", target = "programTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "durationInMonths", target = "durationInMonths")
	Program toEntity(ProgramRequest request);

	@Mapping(source = "provider.id", target = "providerId")
	@Mapping(source = "programCourses", target = "coursesList") // ✅ let MapStruct handle it
	ProgramResponse toResponse(Program program);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "programTitle", target = "programTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "durationInMonths", target = "durationInMonths")
	void updateEntityFromRequest(ProgramRequest request, @MappingTarget Program program);

	// ✅ MapStruct will use CourseMapper.toDto() automatically via "uses"
	@Mapping(source = "course.id", target = "id")
	@Mapping(source = "course.courseId", target = "courseId")
	@Mapping(source = "course.courseTitle", target = "courseTitle")
	@Mapping(source = "course.description", target = "description")
	@Mapping(source = "course.language", target = "language")
	@Mapping(source = "course.skills", target = "skills")
	@Mapping(source = "course.subject.id", target = "subjectId")
	@Mapping(source = "course.subject.subjectNm", target = "subjectNm")
	@Mapping(source = "course.provider.id", target = "providerId")
	@Mapping(source = "course.level", target = "level")
	@Mapping(source = "course.courseImage", target = "courseImage")
	@Mapping(source = "course.introVideo", target = "introVideo")
	@Mapping(source = "course.createdBy", target = "createdBy")
	@Mapping(source = "course.createdDt", target = "createdDt")
	@Mapping(source = "course.updatedBy", target = "updatedBy")
	@Mapping(source = "course.updatedDt", target = "updatedDt")
	CourseResponse programCourseToResponse(ProgramCourse programCourse); // ✅ maps ProgramCourse → CourseResponse
}