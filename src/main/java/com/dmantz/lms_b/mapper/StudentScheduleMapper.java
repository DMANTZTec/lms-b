package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.entity.ClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentScheduleMapper {

    @Mapping(source = "id", target = "scheduleId")
    @Mapping(source = "classBatch.id", target = "classId")
    @Mapping(source = "classBatch.className", target = "className")
    @Mapping(source = "classBatch.course.id", target = "courseId")
    @Mapping(source = "classBatch.course.courseTitle", target = "courseName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "status", target = "status")
    ClassScheduleResponse toResponse(ClassSchedule entity);

    List<ClassScheduleResponse> toDtoList(List<ClassSchedule> schedules);
}
