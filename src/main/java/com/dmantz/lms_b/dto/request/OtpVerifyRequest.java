package com.dmantz.lms_b.dto.request;

import java.util.UUID;

public class OtpVerifyRequest {
    private UUID otpId;
    private String otp;

    public OtpVerifyRequest() {}

    public UUID getOtpId() {
        return otpId;
    }

    public void setOtpId(UUID otpId) {
        this.otpId = otpId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
