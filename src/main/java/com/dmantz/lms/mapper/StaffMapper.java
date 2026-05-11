package com.dmantz.lms.mapper;
import com.dmantz.lms.dto.request.StaffRegistrationRequest;
import com.dmantz.lms.dto.response.StaffLoginResponse;
import com.dmantz.lms.dto.response.StaffPasswordResponse;
import com.dmantz.lms.dto.response.StaffResponse;
import com.dmantz.lms.entity.Role;
import com.dmantz.lms.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
