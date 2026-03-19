package com.dmantz.lms.dto.request;

public class StaffOtpVerifyRequest {

    private String staffId;  // "SF00006"
    private String otp;

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


    @Override
    public String toString() {
        return "StaffOtpVerifyRequest{" +
                "staffId='" + staffId + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
