package com.dmantz.lms.dto.response;

import java.util.ArrayList;
import java.util.List;

public class AttendanceStudentResponse {

    private String studentId;
    private String firstNm;
    private String lastNm;
    private String emailId;
    private String mobileNum;
    private String profileImg;
    private String enabled;
    private String status; // UNMARKED | PRESENT | ABSENT
    private List<StudentTaskStatusResponse> taskStatuses = new ArrayList<>();

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstNm() {
        return firstNm;
    }

    public void setFirstNm(String firstNm) {
        this.firstNm = firstNm;
    }

    public String getLastNm() {
        return lastNm;
    }

    public void setLastNm(String lastNm) {
        this.lastNm = lastNm;
    }

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

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StudentTaskStatusResponse> getTaskStatuses() {
        return taskStatuses;
    }

    public void setTaskStatuses(List<StudentTaskStatusResponse> taskStatuses) {
        this.taskStatuses = taskStatuses;
    }
}
