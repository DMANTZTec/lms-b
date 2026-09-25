package com.dmantz.lms.dto.request;

import java.util.List;

public class EnrollStudentRequest {

    private Long classBatchId;

    // staff → multiple students
    private List<String> studentIds;

    // self-enroll
    private String studentId;
    private boolean selfEnroll;

    public Long getClassBatchId() {
        return classBatchId;
    }

    public void setClassBatchId(Long classBatchId) {
        this.classBatchId = classBatchId;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isSelfEnroll() {
        return selfEnroll;
    }

    public void setSelfEnroll(boolean selfEnroll) {
        this.selfEnroll = selfEnroll;
    }

    @Override
    public String toString() {
        return "EnrollStudentRequest{" +
                "classBatchId=" + classBatchId +
                ", studentIds=" + studentIds +
                ", studentId='" + studentId + '\'' +
                ", selfEnroll=" + selfEnroll +
                '}';
    }
}
