package com.dmantz.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.dmantz.lms.dto.response.StaffRoleResponse;
import com.dmantz.lms.entity.StaffRole;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffRoleMapper {

    @Mapping(target = "staffId", source = "staff.staffId")
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleNm", source = "role.roleNm")
    StaffRoleResponse toResponse(StaffRole staffRole);
}