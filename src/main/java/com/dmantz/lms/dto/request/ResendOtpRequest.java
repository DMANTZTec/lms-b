package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.OtpPurpose;

public class ResendOtpRequest {

    private String emailId;
    private String mobileNum;
    private OtpPurpose purpose;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "ResendOtpRequest{" +
                "emailId='" + emailId + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", purpose=" + purpose +
                '}';
    }
}
