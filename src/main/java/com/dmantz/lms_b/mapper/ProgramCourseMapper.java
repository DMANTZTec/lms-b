package com.dmantz.lms_b.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmantz.lms_b.dto.response.ProgramCourseResponse;
import com.dmantz.lms_b.entity.ProgramCourse;

@Mapper(componentModel = "spring")
public interface ProgramCourseMapper {

	@Mapping(source = "id", target = "id")
	@Mapping(source = "program.programId", target = "programId")
	@Mapping(source = "course.courseId", target = "courseId")
	ProgramCourseResponse toResponse(ProgramCourse entity);

	List<ProgramCourseResponse> toResponseList(List<ProgramCourse> entities);
}
