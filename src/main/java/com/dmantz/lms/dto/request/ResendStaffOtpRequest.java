package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ResendStaffOtpRequest {

    @NotBlank(message = "Email is required")
    private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
