package com.dmantz.lms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmantz.lms.dto.response.ProgramCourseResponse;
import com.dmantz.lms.entity.ProgramCourse;

@Mapper(componentModel = "spring")
public interface ProgramCourseMapper {

	@Mapping(source = "id", target = "id")
	@Mapping(source = "program.programId", target = "programId")
	@Mapping(source = "course.courseId", target = "courseId")
	ProgramCourseResponse toResponse(ProgramCourse entity);

	List<ProgramCourseResponse> toResponseList(List<ProgramCourse> entities);
}
