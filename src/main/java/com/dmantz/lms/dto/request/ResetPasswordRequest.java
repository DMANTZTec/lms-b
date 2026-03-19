package com.dmantz.lms.dto.request;

public class ResetPasswordRequest {

    private String studentId;
    private String otp;
    private String newPassword;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
        return "ResetPasswordRequest{" +
                "studentId='" + studentId + '\'' +
                ", otp='" + otp + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
