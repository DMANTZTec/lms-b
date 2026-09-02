package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.StudentTaskRequest;
import com.dmantz.lms.dto.response.StudentTaskResponse;
import com.dmantz.lms.entity.StudentTask;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface StudentTaskMapper {

    // ================= REQUEST -> ENTITY =================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startDt", ignore = true)
    @Mapping(target = "endDt", ignore = true)
    @Mapping(target = "needHelp", ignore = true)
    StudentTask toEntity(StudentTaskRequest request);

    // ================= ENTITY -> RESPONSE =================
    @Mapping(target = "id", expression = "java(String.valueOf(task.getId()))")
    @Mapping(target = "tags", ignore = true)
    StudentTaskResponse toResponse(StudentTask task);

    @AfterMapping
    default void buildTags(StudentTask task, @MappingTarget StudentTaskResponse response) {
        var tags = new ArrayList<String>();
        if (task.getCourse() != null) {
            tags.add(task.getCourse().getCourseTitle());
        }
        if (task.getTopic() != null) {
            tags.add(task.getTopic().getTopicNm());
        }
        response.setTags(tags);
    }
}