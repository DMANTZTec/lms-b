package com.dmantz.lms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dmantz.lms.dto.request.SuccessStoryRequest;
import com.dmantz.lms.dto.response.SuccessStoryResponse;
import com.dmantz.lms.entity.SuccessStory;

@Mapper(componentModel = "spring")
public interface SuccessStoryMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", expression = "java(entity.getStudent().getFirstNm() + \" \" + entity.getStudent().getLastNm())")
    @Mapping(target = "profileImg", source = "student.profileImg")
    SuccessStoryResponse toResponse(SuccessStory entity);

    List<SuccessStoryResponse> toResponseList(List<SuccessStory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    SuccessStory toEntity(SuccessStoryRequest request);

}