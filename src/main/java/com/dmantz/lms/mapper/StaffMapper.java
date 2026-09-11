package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.StaffCreateRequest;
import com.dmantz.lms.dto.request.StaffRegistrationRequest;
import com.dmantz.lms.dto.request.StaffUpdateReq1;
import com.dmantz.lms.dto.request.StaffUpdateRequest;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import com.dmantz.lms.entity.Role;
import com.dmantz.lms.entity.Staff;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StaffMapper {

    // Registration → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "staffId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profileImg", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDt", ignore = true)
    Staff toEntity(StaffRegistrationRequest request);


    // Create → Entity
    @Mapping(target = "profileImg", ignore = true)
    Staff toEntity(StaffCreateRequest request);

 // Update API 1
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "staffId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profileImg", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDt", ignore = true)
    void updateEntity(
            StaffUpdateRequest request,
            @MappingTarget Staff staff
    );


    // Update API 2
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "staffId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profileImg", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDt", ignore = true)
    void updateEntity(
            StaffUpdateReq1 request,
            @MappingTarget Staff staff
    );


    // Entity → Response
    @Mapping(source = "emailId", target = "email")
    StaffResponse toResponse(Staff staff);


    StaffLoginResponse toLoginResponse(Staff staff);


    StaffPasswordResponse toPasswordResponse(Staff staff);


    // Role → String
    default Set<String> mapRoles(Set<Role> roles) {
        return roles == null
                ? Set.of()
                : roles.stream()
                .map(Role::getRoleNm)
                .collect(Collectors.toSet());
    }


    List<StaffResponse> toResponseList(List<Staff> staffList);
}