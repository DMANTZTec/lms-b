package com.dmantz.lms_b.mapper;

import org.mapstruct.Mapper;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

	Subject toEntity(SubjectRequest dto);

	SubjectResponse toDto(Subject subject);

}
