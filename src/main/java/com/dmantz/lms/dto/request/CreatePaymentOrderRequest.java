package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotNull;

public class CreatePaymentOrderRequest {

    @NotNull(message = "enrollmentId is required")
    private Long enrollmentId;

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
}
