package com.dmantz.lms_b.dto.request;

public class StaffResetPasswordRequest {
    private String staffId;
    private String otp;
    private String newPassword;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "StaffResetPasswordRequest{" +
                "staffId='" + staffId + '\'' +
                ", otp='" + otp + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
