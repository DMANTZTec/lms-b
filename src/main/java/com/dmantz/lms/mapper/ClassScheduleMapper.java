package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.ClassScheduleRequest;
import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.entity.ClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)public interface ClassScheduleMapper {

    // ✅ Entity → Response
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


    @Mapping(source = "classId", target = "classBatch.id")
    @Mapping(source = "staffId", target = "staff.id")
    ClassSchedule toEntity(ClassScheduleRequest request);
}
