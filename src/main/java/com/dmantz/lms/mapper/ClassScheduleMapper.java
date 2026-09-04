package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.AddScheduleRequest;
import com.dmantz.lms.dto.request.ClassScheduleRequest;
import com.dmantz.lms.dto.response.BatchInstructorResponse;
import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.entity.ClassSchedule;
import com.dmantz.lms.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClassScheduleMapper {

    @Mapping(source = "id", target = "scheduleId")
    @Mapping(source = "classBatch.id", target = "batchId")
    @Mapping(source = "classBatch.className", target = "batchName")
    @Mapping(source = "className", target = "className")
    @Mapping(target = "dayOfWeek", expression = "java(getDayOfWeek(entity))")
    @Mapping(target = "instructors", expression = "java(getBatchInstructors(entity))")
    ClassScheduleResponse toResponse(ClassSchedule entity);

    List<ClassScheduleResponse> toDtoList(List<ClassSchedule> schedules);

    @Mapping(source = "batchId", target = "classBatch.id")
    ClassSchedule toEntity(ClassScheduleRequest request);

    @Mapping(source = "batchId", target = "classBatch.id")
    ClassSchedule toEntity(AddScheduleRequest request);

    default String getDayOfWeek(ClassSchedule entity) {
        if (entity.getClassDate() == null) {
            return null;
        }
        return entity.getClassDate()
                .getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    default List<BatchInstructorResponse> getBatchInstructors(ClassSchedule entity) {
        Set<Staff> instructors = entity.getInstructors();

        // Legacy schedules created before per-schedule instructor storage existed
        // have no rows of their own yet — fall back to the batch's current
        // instructors so they don't show up empty.
        if ((instructors == null || instructors.isEmpty()) && entity.getClassBatch() != null) {
            instructors = entity.getClassBatch().getInstructors();
        }

        if (instructors == null) {
            return List.of();
        }

        return instructors.stream()
                .map(this::toBatchInstructorResponse)
                .toList();
    }

    default BatchInstructorResponse toBatchInstructorResponse(Staff staff) {
        BatchInstructorResponse r = new BatchInstructorResponse();
        r.setStaffId(staff.getStaffId());
        r.setFirstNm(staff.getFirstNm());
        r.setLastNm(staff.getLastNm());
        return r;
    }
}