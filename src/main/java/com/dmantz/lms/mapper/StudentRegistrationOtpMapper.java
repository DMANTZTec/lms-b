package com.dmantz.lms.mapper;


import com.dmantz.lms.dto.request.StudentRegistrationRequest;
import com.dmantz.lms.entity.StudentRegistrationOTP;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentRegistrationOtpMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentStatus", ignore = true)
    @Mapping(target = "password", ignore = true)
    StudentRegistrationOTP toEntity(StudentRegistrationRequest request);
}
