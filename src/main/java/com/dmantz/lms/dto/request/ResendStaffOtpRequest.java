package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ResendStaffOtpRequest {

    @NotBlank(message = "Email or mobile number is required")
    private String emailIdOrMobileNo;

    public String getEmailIdOrMobileNo() {
        return emailIdOrMobileNo;
    }

    public void setEmailIdOrMobileNo(String emailIdOrMobileNo) {
        this.emailIdOrMobileNo = emailIdOrMobileNo;
    }
}