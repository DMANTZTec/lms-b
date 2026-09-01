package com.dmantz.lms.service;

import com.dmantz.lms.dto.request.AssignStudentToBatchRequest;
import com.dmantz.lms.dto.response.EnrollmentBatchResponse;
import com.dmantz.lms.dto.response.StudentWeeklyScheduleResponse;

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

    List<StudentWeeklyScheduleResponse>
    getStudentWeeklySchedule(
            String studentId,
            LocalDate startDate
    );
}