package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.EnrollmentBatchResponse;
import com.dmantz.lms.entity.EnrollmentBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentBatchMapper {

	@Mapping(target = "enrollmentId", source = "enrollment.id")
	@Mapping(target = "studentId", source = "enrollment.student.studentId")
	@Mapping(target = "batchId", source = "classBatch.id")
	@Mapping(target = "batchName", source = "classBatch.className")
	@Mapping(target = "courseId", source = "classBatch.course.courseId")
	@Mapping(target = "assignedBy", source = "assignedBy.staffId")
	@Mapping(target = "assignedDate", source = "assignedDate")
	EnrollmentBatchResponse toResponse(EnrollmentBatch entity);
}