package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.SocialMediaRequest;
import com.dmantz.lms.dto.response.SocialMediaResponse;
import com.dmantz.lms.entity.SocialMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SocialMediaMapper {

	@Mapping(target = "id", ignore = true)
	SocialMedia toEntity(SocialMediaRequest request);

	SocialMediaResponse toResponse(SocialMedia socialMedia);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "platform", ignore = true)
	void updateEntity(SocialMediaRequest request, @MappingTarget SocialMedia socialMedia);
}