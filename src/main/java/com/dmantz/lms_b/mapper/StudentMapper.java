package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.request.StudentRegistrationRequest;
import com.dmantz.lms_b.dto.response.StudentLoginResponse;
import com.dmantz.lms_b.dto.response.StudentResponse;
import com.dmantz.lms_b.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // ---------- DTO → Entity ----------
    @Mappings({
            @Mapping(source = "firstnm", target = "first_nm"),
            @Mapping(source = "lastnm", target = "last_nm"),

            @Mapping(source = "email_id", target = "email_id"),
            @Mapping(source = "mobile_num", target = "mobile_num"),

            @Mapping(source = "emergencyContactnm", target = "emergency_contact_nm"),
            @Mapping(source = "emergencyContactnum", target = "emergency_contact_num")
    })
    Student toEntity(StudentRegistrationRequest request);

    // ---------- Entity → Response ----------
    @Mappings({
            @Mapping(source = "student_id", target = "studentid"),
            @Mapping(source = "login_id", target = "loginid"),

            @Mapping(source = "first_nm", target = "firstnm"),
            @Mapping(source = "last_nm", target = "lastnm"),

            @Mapping(source = "email_id", target = "emailid"),
            @Mapping(source = "mobile_num", target = "mobilenumber"),

            @Mapping(source = "emergency_contact_nm", target = "emergencycontactnm"),
            @Mapping(source = "emergency_contact_num", target = "emergencycontactnum"),

            @Mapping(source = "status", target = "status"),
            @Mapping(source = "enabled", target = "enabled")
    })
    StudentResponse toResponse(Student student);



    @Mapping(source = "student_id", target = "studentId")
    @Mapping(source = "email_id", target = "email")
    StudentLoginResponse toLoginResponse(Student student);

}


