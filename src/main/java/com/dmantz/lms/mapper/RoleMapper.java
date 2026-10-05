package com.dmantz.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.dmantz.lms.dto.request.RoleRequest;
import com.dmantz.lms.dto.response.RoleResponse;
import com.dmantz.lms.entity.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleResponse toResponse(Role role);

    Role toEntity(RoleRequest request);
}