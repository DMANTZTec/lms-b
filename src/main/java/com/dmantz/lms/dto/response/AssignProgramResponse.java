package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.ProgramEnrollmentStatus;

import java.util.List;

public class AssignProgramResponse {
    private Long enrollmentId;
    private String studentId;
    private String programId;
    private String programTitle;
    private ProgramEnrollmentStatus status;
    private List<String> enrolledCourseIds;

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

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

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public ProgramEnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(ProgramEnrollmentStatus status) {
        this.status = status;
    }

    public List<String> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public void setEnrolledCourseIds(List<String> enrolledCourseIds) {
        this.enrolledCourseIds = enrolledCourseIds;
    }
}
