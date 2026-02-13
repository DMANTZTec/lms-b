package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.StudentRegistrationRequest;
import com.dmantz.lms_b.dto.request.StudentUpdateRequest;
import com.dmantz.lms_b.dto.response.StudentLoginResponse;
import com.dmantz.lms_b.dto.response.StudentResponse;
import com.dmantz.lms_b.entity.Student;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentMapper {

    // ================= CREATE =================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    Student toEntity(StudentRegistrationRequest request);

    // ================= UPDATE PROFILE =================
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstNm", source = "firstNm")
    @Mapping(target = "lastNm", source = "lastNm")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "addr1", source = "addr1")
    @Mapping(target = "addr2", source = "addr2")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "pin", source = "pin")
    @Mapping(target = "mobileNum", source = "mobileNum")
    @Mapping(target = "emergencyContactNm", source = "emergencyContactNm")
    @Mapping(target = "emergencyContactNum", source = "emergencyContactNum")
    void updateStudentFromDto(StudentUpdateRequest request,
                              @MappingTarget Student entity);

    // ================= RESPONSE =================
    StudentResponse toResponse(Student student);

    // ================= LOGIN RESPONSE =================
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "emailId", target = "email")
    StudentLoginResponse toLoginResponse(Student student);
}
