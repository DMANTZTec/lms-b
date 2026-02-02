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

    // DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedDt", ignore = true)
    Student toEntity(StudentRegistrationRequest request);

    // Entity → Response
    StudentResponse toResponse(Student student);

    //  Entity → Login Response
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "emailId", target = "email")
    StudentLoginResponse toLoginResponse(Student student);
}


