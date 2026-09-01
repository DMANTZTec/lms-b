package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.EnrollmentBatchResponse;
import com.dmantz.lms.dto.response.StudentWeeklyScheduleResponse;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.EnrollmentBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentBatchMapper {

    @Mapping(
            target = "enrollmentId",
            source = "enrollment.id"
    )
    @Mapping(
            target = "studentId",
            source = "enrollment.student.studentId"
    )
    @Mapping(
            target = "batchId",
            source = "classBatch.id"
    )
    @Mapping(
            target = "batchName",
            source = "classBatch.className"
    )
    @Mapping(
            target = "courseId",
            source = "classBatch.course.courseId"
    )
    @Mapping(
            target = "assignedBy",
            source = "assignedBy.staffId"
    )
    @Mapping(
            target = "assignedDate",
            source = "assignedDate"
    )
    EnrollmentBatchResponse toResponse(
            EnrollmentBatch entity
    );

    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "studentId", source = "studentId")
    @Mapping(target = "batchId", source = "schedule.classBatch.id")
    @Mapping(target = "batchName", source = "schedule.classBatch.className")
    @Mapping(target = "courseId", source = "schedule.classBatch.course.courseId")
    @Mapping(target = "classDate", source = "schedule.classDate")
    @Mapping(target = "startTime", source = "schedule.startTime")
    @Mapping(target = "endTime", source = "schedule.endTime")
    StudentWeeklyScheduleResponse toScheduleResponse(
            ClassSchedule schedule,
            String studentId
    );
}