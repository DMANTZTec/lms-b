package com.dmantz.lms.dto.request;

import java.util.List;

public class RemoveStudentRequest {

    private Long classBatchId;
    // Self remove
    private boolean selfRemove;
    private String studentId;
    // Staff remove
    private List<String> studentIds;

    public Long getClassBatchId() {
        return classBatchId;
    }

    public void setClassBatchId(Long classBatchId) {
        this.classBatchId = classBatchId;
    }

    public boolean isSelfRemove() {
        return selfRemove;
    }

    public void setSelfRemove(boolean selfRemove) {
        this.selfRemove = selfRemove;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}
