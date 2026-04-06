
package com.dmantz.lms.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms.dto.request.CourseRequest;
import com.dmantz.lms.dto.response.CourseResponse;
import com.dmantz.lms.entity.Course;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

	ObjectMapper mapper = new ObjectMapper(); // used to convert List<String> to JSON and vice versa

	// ---------- Request → Entity ----------
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "courseTitle", target = "courseTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "language", target = "language")
	@Mapping(target = "skills", expression = "java(toJson(request.getSkills()))")
	@Mapping(source = "level", target = "level")
	@Mapping(source = "courseImage", target = "courseImage")
	@Mapping(source = "introVideo", target = "introVideo")
	Course toEntity(CourseRequest request);

	// ---------- Entity → Response ----------
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "id", target = "id")
	@Mapping(source = "courseId", target = "courseId")
	@Mapping(source = "courseTitle", target = "courseTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "language", target = "language")
	@Mapping(target = "skills", expression = "java(fromJson(course.getSkills()))")
	@Mapping(source = "subject.id", target = "subjectId")
	@Mapping(source = "subject.subjectNm", target = "subjectNm")
	@Mapping(source = "provider.id", target = "providerId")
	@Mapping(source = "level", target = "level")
	@Mapping(source = "courseImage", target = "courseImage")
	@Mapping(source = "introVideo", target = "introVideo")
	@Mapping(source = "createdBy", target = "createdBy")
	@Mapping(source = "createdDt", target = "createdDt")
	@Mapping(source = "updatedBy", target = "updatedBy")
	@Mapping(source = "updatedDt", target = "updatedDt")
	CourseResponse toDto(Course course);

	// Update existing entity (UPDATE)
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "courseTitle", target = "courseTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "language", target = "language")
	@Mapping(target = "skills", expression = "java(toJson(request.getSkills()))")
	@Mapping(source = "level", target = "level")
	@Mapping(source = "courseImage", target = "courseImage")
	@Mapping(source = "introVideo", target = "introVideo")
	void updateCourseFromRequest(CourseRequest request, @MappingTarget Course course);

//	json format conversion
	default String toJson(List<String> skills) {
		try {
			if (skills == null || skills.isEmpty()) {
				return "[]";
			}
			return mapper.writeValueAsString(skills); // list of skills to json array
		} catch (Exception e) {
			throw new RuntimeException("Error converting skills to JSON", e);
		}
	}

//	from json to list<String>
	default List<String> fromJson(String skills) {
		try {
			if (skills == null || skills.isBlank()) {
				return List.of();
			}
			return mapper.readValue(skills, new TypeReference<List<String>>() { // tells object mapper that this json
																				// contains string
			}); // json array to list of skills
		} catch (Exception e) {
			throw new RuntimeException("Error converting skills from JSON", e);

		}
	}

}


