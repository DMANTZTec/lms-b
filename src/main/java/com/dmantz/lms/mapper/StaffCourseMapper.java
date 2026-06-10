package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.InstructorResponse;  // ✅ updated
import com.dmantz.lms.dto.response.StaffCourseResponse;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.StaffCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffCourseMapper {

    // StaffCourse → StaffCourseResponse
    @Mapping(source = "staff.staffId", target = "staffId")
    @Mapping(source = "staff.firstNm", target = "staffFirstNm")
    @Mapping(source = "staff.lastNm", target = "staffLastNm")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.courseTitle", target = "courseTitle")
    StaffCourseResponse toResponse(StaffCourse staffCourse);

    List<StaffCourseResponse> toResponseList(List<StaffCourse> staffCourses);

    // Staff → InstructorResponse ✅ updated
    @Mapping(source = "staffId", target = "staffId")
    @Mapping(source = "firstNm", target = "firstNm")
    @Mapping(source = "lastNm", target = "lastNm")
    @Mapping(source = "designation", target = "designation")
    InstructorResponse toChipResponse(Staff staff);  // ✅ updated

    List<InstructorResponse> toChipResponseList(List<Staff> staffList);  // ✅ updated
}