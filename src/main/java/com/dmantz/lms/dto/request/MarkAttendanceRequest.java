package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MarkAttendanceRequest {

    @NotBlank(message = "studentId is required")
    private String studentId;

    @NotBlank(message = "status is required")
    @Pattern(regexp = "PRESENT|ABSENT", message = "status must be PRESENT or ABSENT")
    private String status;

    @NotBlank(message = "staffId is required")
    private String staffId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
