package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.request.ClassScheduleRequest;
import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.entity.ClassSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ClassScheduleMapper {

    @Mapping(source = "id", target = "scheduleId")
    @Mapping(source = "classBatch.id", target = "batchId")
    @Mapping(source = "classBatch.className", target = "className")
    @Mapping(source = "staff.staffId", target = "staffId")
    @Mapping(target = "staffName",
            expression = "java(getStaffFullName(entity))")
    ClassScheduleResponse toResponse(ClassSchedule entity);

    List<ClassScheduleResponse> toDtoList(
            List<ClassSchedule> schedules);

    @Mapping(source = "classId", target = "classBatch.id")
    @Mapping(source = "staffId", target = "staff.id")
    ClassSchedule toEntity(ClassScheduleRequest request);

    default String getStaffFullName(ClassSchedule entity) {
        if (entity.getStaff() == null) {
            return null;
        }

        String first =
                entity.getStaff().getFirstNm() == null
                        ? ""
                        : entity.getStaff().getFirstNm();

        String last =
                entity.getStaff().getLastNm() == null
                        ? ""
                        : entity.getStaff().getLastNm();

        return (first + " " + last).trim();
    }
}