package com.dmantz.lms_b.dto.response;

public class StaffPasswordResponse {

    private String staffId;
    private String message;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "StaffPasswordResponse{" +
                "staffId='" + staffId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
