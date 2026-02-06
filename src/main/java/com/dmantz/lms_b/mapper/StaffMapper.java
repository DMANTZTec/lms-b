package com.dmantz.lms_b.mapper;
import com.dmantz.lms_b.dto.request.StaffRegistrationRequest;
import com.dmantz.lms_b.dto.response.StaffLoginResponse;
import com.dmantz.lms_b.dto.response.StaffPasswordResponse;
import com.dmantz.lms_b.dto.response.StaffResponse;
import com.dmantz.lms_b.entity.Role;
import com.dmantz.lms_b.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    // DTO → Entity
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

    // Entity → Response
    @Mapping(source = "emailId", target = "email")
    StaffResponse toResponse(Staff staff);

    @Mapping(target = "staffId", source = "staffId")
    @Mapping(target = "name", expression = "java(staff.getFirstNm() + \" \" + staff.getLastNm())")
    StaffLoginResponse toLoginResponse(Staff staff);

    StaffPasswordResponse toPasswordResponse(Staff staff);


    // Role → String mapping (auto-used)
    default Set<String> mapRoles(Set<Role> roles) {
        return roles == null ? Set.of()
                : roles.stream()
                .map(Role::getRoleNm)
                .collect(java.util.stream.Collectors.toSet());
    }

    List<StaffResponse> toResponseList(List<Staff> staffList);


}
