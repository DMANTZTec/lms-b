package com.dmantz.lms_b.dto.request;

import jakarta.validation.constraints.NotBlank;

public class StaffLoginRequest {

    @NotBlank
    private String emailId;

    @NotBlank
    private String password;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "StaffLoginRequest{" +
                "emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
