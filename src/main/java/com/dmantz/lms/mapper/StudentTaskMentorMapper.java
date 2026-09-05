package com.dmantz.lms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dmantz.lms.dto.request.StudentTaskMentorRequest;
import com.dmantz.lms.dto.response.StudentTaskMentorResponse;
import com.dmantz.lms.entity.StudentTaskMentor;

@Mapper(componentModel = "spring")
public interface StudentTaskMentorMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "minsSpent", source = "minsSpent")
    StudentTaskMentor toEntity(StudentTaskMentorRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "studentTask.id", target = "studentTaskId")
    @Mapping(source = "mentorStudent.studentId", target = "mentorStudentId") // ✅ FIXED
    @Mapping(source = "minsSpent", target = "minsSpent")
    @Mapping(source = "status", target = "status")
    StudentTaskMentorResponse toDto(StudentTaskMentor entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "minsSpent", source = "minsSpent")
    void updateFromRequest(StudentTaskMentorRequest request,
                           @MappingTarget StudentTaskMentor entity);
}

