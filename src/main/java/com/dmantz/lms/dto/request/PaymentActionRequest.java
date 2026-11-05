package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Reason for an admin action on a payment (waive, cancel).
 */
public class PaymentActionRequest {

    @NotBlank(message = "reason is required")
    @Size(max = 255, message = "reason can be at most 255 characters")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
