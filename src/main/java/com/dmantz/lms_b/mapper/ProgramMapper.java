package com.dmantz.lms_b.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms_b.dto.request.ProgramRequest;
import com.dmantz.lms_b.dto.response.ProgramResponse;
import com.dmantz.lms_b.entity.Program;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "programTitle", target = "programTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "durationInMonths", target = "durationInMonths")
	Program toEntity(ProgramRequest request);

	@Mapping(source = "provider.id", target = "providerId")
	ProgramResponse toResponse(Program program);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "programTitle", target = "programTitle")
	@Mapping(source = "description", target = "description")
	@Mapping(source = "durationInMonths", target = "durationInMonths")
	void updateEntityFromRequest(ProgramRequest request, @MappingTarget Program program);
}
