package com.dmantz.lms_b.mapper;

import com.dmantz.lms_b.dto.response.StudentScheduleResponse;
import com.dmantz.lms_b.entity.ClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentScheduleMapper {

    @Mapping(source = "id", target = "scheduleId")
    @Mapping(source = "course.courseTitle", target = "courseName")
    @Mapping(source = "mode", target = "mode")
    StudentScheduleResponse toDto(ClassSchedule schedule);

    List<StudentScheduleResponse> toDtoList(List<ClassSchedule> schedules);
}
