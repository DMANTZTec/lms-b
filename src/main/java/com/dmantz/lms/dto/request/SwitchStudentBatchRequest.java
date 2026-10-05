package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotNull;

public class SwitchStudentBatchRequest {

    @NotNull(message = "Enrollment ID is required")
    private Long enrollmentId;

    @NotNull(message = "Current batch ID is required")
    private Long fromBatchId;

    @NotNull(message = "New batch ID is required")
    private Long toBatchId;

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getFromBatchId() {
        return fromBatchId;
    }

    public void setFromBatchId(Long fromBatchId) {
        this.fromBatchId = fromBatchId;
    }

    public Long getToBatchId() {
        return toBatchId;
    }

    public void setToBatchId(Long toBatchId) {
        this.toBatchId = toBatchId;
    }
}