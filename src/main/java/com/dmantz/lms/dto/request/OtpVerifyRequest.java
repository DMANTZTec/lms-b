package com.dmantz.lms.dto.request;


public class OtpVerifyRequest {
    private String studentId;
    private String otp;

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

    @Override
    public String toString() {
        return "OtpVerifyRequest{" +
                "studentId='" + studentId + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
