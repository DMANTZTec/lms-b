package com.dmantz.lms.service;

import java.time.LocalDate;
import java.util.List;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.ClassAdminStudentDetailsResponse;
import com.dmantz.lms.dto.response.ClassResponse;
import com.dmantz.lms.dto.response.ClassScheduleResponse;
import com.dmantz.lms.dto.response.ClassTopicResponse;
import jakarta.transaction.Transactional;

public interface ClassAdminService {

   ClassResponse addClass(String courseId, CreateClassRequest request);

   ClassResponse modifyClass(Long batchId, UpdateClassRequest request);

   ClassResponse cancelClass(Long batchId);

   ClassScheduleResponse addScheduleToClass(ClassScheduleRequest request);

   ClassScheduleResponse modifySchedule(Long scheduleId, ClassScheduleRequest request);

   ClassScheduleResponse cancelSchedule(Long scheduleId);
   
   ClassAdminStudentDetailsResponse viewStudentDetails(String studentId);
   
   List<ClassAdminStudentDetailsResponse> viewStudents();

   List<ClassScheduleResponse> getSchedulesByStaffId(String staffId);

   List<ClassScheduleResponse> getStaffDailySchedule(String staffId, LocalDate date);

   void addTopicsToClass(Long batchId, AddClassTopicRequest request);

   void removeTopicsFromClass(Long batchId, RemoveClassTopicRequest request);

   List<ClassTopicResponse> getTopicsByBatchId(Long batchId);

}
