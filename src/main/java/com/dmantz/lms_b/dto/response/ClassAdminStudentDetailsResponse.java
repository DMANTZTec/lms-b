package com.dmantz.lms_b.dto.response;

import java.util.List;

public class ClassAdminStudentDetailsResponse {
	 // ===== Student basic info =====
    private Long id;
    private String studentId;
    private String firstNm;
    private String lastNm;
    private String emailId;
    private String mobileNum;
    private String status;
    private String enabled;

    // ===== Course details (from StudentMyCoursesResponse shape) =====
    private List<MyCourseResponse> courses;   // same type used inside StudentMyCoursesResponse

    // ===== Schedule details (using ClassScheduleResponse) =====
    private long totalSchedules;
    private long upcoming;
    private long completedSchedules;
    private List<ClassScheduleResponse> schedules;

    // ---- getters/setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

   
    public List<MyCourseResponse> getCourses() {
        return courses;
    }

    public void setCourses(List<MyCourseResponse> courses) {
        this.courses = courses;
    }

    public long getTotalSchedules() {
        return totalSchedules;
    }

    public void setTotalSchedules(long totalSchedules) {
        this.totalSchedules = totalSchedules;
    }

    public long getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(long upcoming) {
        this.upcoming = upcoming;
    }

    public long getCompletedSchedules() {
        return completedSchedules;
    }

    public void setCompletedSchedules(long completedSchedules) {
        this.completedSchedules = completedSchedules;
    }

    public List<ClassScheduleResponse> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ClassScheduleResponse> schedules) {
        this.schedules = schedules;
    }

}
