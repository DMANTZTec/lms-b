package com.dmantz.lms_b.dto.request;

import jakarta.validation.constraints.NotBlank;

public class StaffLoginRequest {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
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
                "loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
