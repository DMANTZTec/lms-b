package com.dmantz.lms_b.dto.response;

public class OtpVerifyResponse {

    private String message;
    private boolean verified;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "OtpVerifyResponse{" +
                "message='" + message + '\'' +
                ", verified=" + verified +
                '}';
    }
}
