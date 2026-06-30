package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AssignProgramRequest {
    @NotBlank
    private String studentId;

    @NotBlank
    private String programId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
