package com.dmantz.lms.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.coyote.BadRequestException;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;
import jakarta.transaction.Transactional;

public interface ClassAdminService {

	ClassResponse addClass(String courseId, CreateClassRequest request);

	ClassResponse modifyClass(Long batchId, UpdateClassRequest request);

	ClassResponse cancelClass(Long batchId);

	ClassScheduleResponse addScheduleToClass(AddScheduleRequest request);

	ClassScheduleResponse cancelSchedule(Long scheduleId);

	ClassAdminStudentDetailsResponse viewStudentDetails(String studentId);

	List<ClassAdminStudentDetailsResponse> viewStudents();

	List<ClassScheduleResponse> getSchedulesByStaffId(String staffId);

	List<ClassScheduleResponse> getStaffDailySchedule(String staffId, LocalDate date);

	void addTopicsToClass(Long batchId, AddClassTopicRequest request);

	void removeTopicsFromClass(Long batchId, RemoveClassTopicRequest request);

	List<ClassTopicResponse> getTopicsByBatchId(Long batchId);

// Add these 2 new methods
	List<ClassResponse> getClassesByCourse(String courseId);

	List<ClassScheduleResponse> getSchedulesByBatch(Long batchId);
	
	List<ClassScheduleResponse> getAllSchedules();
	
	void assignInstructor(Long scheduleId,
            AssignInstructorRequest request) throws BadRequestException;

	ClassScheduleResponse modifySchedule(Long scheduleId, AddScheduleRequest request);
	
	// ClassAdminService.java
	List<BatchInstructorResponse> getInstructorsByBatchId(Long batchId);
	
	ClassScheduleResponse getScheduleById(Long scheduleId);
	
	ClassResponse getBatchById(Long batchId);
	
	List<BatchInstructorResponse> addInstructorsToBatch(Long batchId, BatchInstructorRequest request);
	List<BatchInstructorResponse> updateInstructorsForBatch(Long batchId, BatchInstructorRequest request);

}
