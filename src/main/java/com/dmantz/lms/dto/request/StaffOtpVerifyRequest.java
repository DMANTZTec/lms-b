package com.dmantz.lms.dto.request;

public class StaffOtpVerifyRequest {

    private String emailId;
    private String otp;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "StaffOtpVerifyRequest{" +
                "emailId='" + emailId + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
