package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.StaffRegistrationRequest;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.Role;
import com.dmantz.lms_b.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "profile_img", ignore = true), // base64 → byte[] in service
            @Mapping(target = "status", constant = "ACTIVE"),
            @Mapping(target = "enabled", constant = "Y"),
            @Mapping(target = "created_dt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "updated_dt", ignore = true),
            @Mapping(target = "created_by", ignore = true),
            @Mapping(target = "updated_by", ignore = true)
    })
    Staff toEntity(StaffRegistrationRequest request);

    StaffResponse toResponse(Staff staff);

}
