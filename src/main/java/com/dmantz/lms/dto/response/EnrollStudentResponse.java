package com.dmantz.lms.dto.response;

import java.time.LocalDate;

public class EnrollStudentResponse {

    private Long classBatchId;
    private String studentId;
    private String studentName;
    private String status;
    private LocalDate enrolledDate;

    public Long getClassBatchId() {
        return classBatchId;
    }

    public void setClassBatchId(Long classBatchId) {
        this.classBatchId = classBatchId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    @Override
    public String toString() {
        return "EnrollStudentResponse{" +
                "classBatchId=" + classBatchId +
                ", studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", status='" + status + '\'' +
                ", enrolledDate=" + enrolledDate +
                '}';
    }
}
