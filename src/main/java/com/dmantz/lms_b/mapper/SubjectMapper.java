package com.dmantz.lms_b.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

	 // Request → Entity (CREATE)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "subjectNm", target = "subjectNm")
    @Mapping(source = "subjectShortCd", target = "subjectShortCd")
    @Mapping(source = "subjectCategory", target = "subjectCategory")
    @Mapping(source = "description", target = "description")
    Subject toEntity(SubjectRequest request);

    // Entity → Response
    SubjectResponse toDto(Subject subject);

    // Update existing entity (UPDATE)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "subjectNm", target = "subjectNm")
    @Mapping(source = "subjectShortCd", target = "subjectShortCd")
    @Mapping(source = "subjectCategory", target = "subjectCategory")
    @Mapping(source = "description", target = "description")
    void updateSubjectFromRequest(
            SubjectRequest request,
            @MappingTarget Subject subject
    );
}
