package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.StaffCreateRequest;
import com.dmantz.lms.dto.request.StaffRegistrationRequest;
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

    @Mapping(target = "profileImg", ignore = true)
    Staff toEntity(StaffCreateRequest request);


    // Update DTO → Existing Entity
    @Mapping(source = "firstName", target = "firstNm")
    @Mapping(source = "lastName", target = "lastNm")
    @Mapping(source = "mobileNumber", target = "mobileNum")
    @Mapping(source = "dateOfBirth", target = "dob")
    @Mapping(source = "addressOne", target = "addr1")
    @Mapping(source = "addressTwo", target = "addr2")
    @Mapping(source = "pincode", target = "pin")
    @Mapping(source = "emergencyContactName", target = "emergencyContactNm")
    @Mapping(source = "emergencyContactNumber", target = "emergencyContactNum")
    @Mapping(target = "id", ignore = true)
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


    // Entity → Response
    @Mapping(source = "firstNm", target = "firstName")
    @Mapping(source = "lastNm", target = "lastName")
    @Mapping(source = "mobileNum", target = "mobileNumber")
    @Mapping(source = "dob", target = "dateOfBirth")
    @Mapping(source = "addr1", target = "addressOne")
    @Mapping(source = "addr2", target = "addressTwo")
    @Mapping(source = "pin", target = "pincode")
    @Mapping(source = "emergencyContactNm", target = "emergencyContactName")
    @Mapping(source = "emergencyContactNum", target = "emergencyContactNumber")
    @Mapping(source = "createdDt", target = "createdDate")
    @Mapping(source = "updatedDt", target = "updatedDate")
    @Mapping(source = "profileImg", target = "profileImage")
    StaffResponse toResponse(Staff staff);


    // Login Response
    @Mapping(target = "staffId", source = "staffId")
    StaffLoginResponse toLoginResponse(Staff staff);


    // Password Response
    StaffPasswordResponse toPasswordResponse(Staff staff);


    // Role → String
    default Set<String> mapRoles(Set<Role> roles) {
        return roles == null
                ? Set.of()
                : roles.stream()
                        .map(Role::getRoleNm)
                        .collect(Collectors.toSet());
    }


    // List Mapping
    List<StaffResponse> toResponseList(List<Staff> staffList);
}