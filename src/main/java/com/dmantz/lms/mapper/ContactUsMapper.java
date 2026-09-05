package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.ContactUsRequest;
import com.dmantz.lms.dto.response.ContactUsResponse;
import com.dmantz.lms.entity.ContactUs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactUsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    ContactUs toEntity(ContactUsRequest request);

    ContactUsResponse toResponse(ContactUs contactUs);

}
