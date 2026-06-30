package com.dmantz.lms.dto.response;

import java.util.List;

public class CourseFeeSettingResponse {

    private String courseId;
    private String courseTitle;
    private String subjectNm;

    private String courseDuration;
    private String batchClassName;
    private String batchStatus;

    private CourseFeeHistoryResponse currentFee;
    private List<CourseFeeHistoryResponse> feeHistory;
    private int totalHistoryRecords;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getSubjectNm() {
        return subjectNm;
    }

    public void setSubjectNm(String subjectNm) {
        this.subjectNm = subjectNm;
    }

   

    public String getCourseDuration() {
		return courseDuration;
	}

	public void setCourseDuration(String courseDuration) {
		this.courseDuration = courseDuration;
	}

	public String getBatchClassName() {
        return batchClassName;
    }

    public void setBatchClassName(String batchClassName) {
        this.batchClassName = batchClassName;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public CourseFeeHistoryResponse getCurrentFee() {
        return currentFee;
    }

    public void setCurrentFee(CourseFeeHistoryResponse currentFee) {
        this.currentFee = currentFee;
    }

    public List<CourseFeeHistoryResponse> getFeeHistory() {
        return feeHistory;
    }

    public void setFeeHistory(List<CourseFeeHistoryResponse> feeHistory) {
        this.feeHistory = feeHistory;
    }

    public int getTotalHistoryRecords() {
        return totalHistoryRecords;
    }

    public void setTotalHistoryRecords(int totalHistoryRecords) {
        this.totalHistoryRecords = totalHistoryRecords;
    }
}