package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.AssignStudentToBatchRequest;
import com.dmantz.lms.dto.response.DailyScheduleResponse;
import com.dmantz.lms.dto.response.EnrollmentBatchResponse;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentBatchService {

    EnrollmentBatchResponse
    assignStudentToBatch(
            AssignStudentToBatchRequest request
    );

    List<EnrollmentBatchResponse>
    getStudentsByBatch(
            Long batchId
    );

    EnrollmentBatchResponse
    getEnrollmentBatch(
            Long enrollmentBatchId
    );

    void removeStudentFromBatch(
            Long enrollmentBatchId
    );

    List<DailyScheduleResponse> getStudentWeeklySchedule(
            String studentId
    );
    
    List<EnrollmentBatchResponse> getEnrolledBatchesByStudentId(
            String studentId
    );
}