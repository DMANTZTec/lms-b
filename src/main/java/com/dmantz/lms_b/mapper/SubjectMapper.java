package com.dmantz.lms_b.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    // Request → Entity
    @Mapping(source = "subjectNm", target = "subject_nm")
    @Mapping(source = "subjectShortCd", target = "subject_short_cd")
    @Mapping(source = "subjectCategory", target = "subject_category")
    @Mapping(source = "description", target = "description")
    Subject toEntity(SubjectRequest request);

    // Entity → Response
    @Mapping(source = "subject_nm", target = "subjectNm")
    @Mapping(source = "subject_short_cd", target = "subjectShortCd")
    @Mapping(source = "subject_category", target = "subjectCategory")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "created_by", target = "createdBy")
    @Mapping(source = "created_dt", target = "createdDt")
    @Mapping(source = "updated_by", target = "updatedBy")
    @Mapping(source = "updated_dt", target = "updatedDt")
    SubjectResponse toDto(Subject subject);

    // Update mapping
    @Mapping(source = "subjectNm", target = "subject_nm")
    @Mapping(source = "subjectShortCd", target = "subject_short_cd")
    @Mapping(source = "subjectCategory", target = "subject_category")
    @Mapping(source = "description", target = "description")
    void updateSubjectFromRequest(
            SubjectRequest request,
            @MappingTarget Subject subject
    );
}
