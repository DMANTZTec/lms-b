package com.dmantz.lms_b.dto.response;

import com.dmantz.lms_b.entity.CourseStatus;

public class CourseProgressSummaryResponse {

	private Long courseId;
	private String courseName;

	private int totalChapters;
	private int completedChapters;

	private int totalTopics;
	private int completedTopics;

	private int totalReferences;
	private int completedReferences;

	private double coursePercentage;
	private boolean completed;
	
	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getTotalChapters() {
		return totalChapters;
	}

	public void setTotalChapters(int totalChapters) {
		this.totalChapters = totalChapters;
	}

	public int getCompletedChapters() {
		return completedChapters;
	}

	public void setCompletedChapters(int completedChapters) {
		this.completedChapters = completedChapters;
	}

	public int getTotalTopics() {
		return totalTopics;
	}

	public void setTotalTopics(int totalTopics) {
		this.totalTopics = totalTopics;
	}

	public int getCompletedTopics() {
		return completedTopics;
	}

	public void setCompletedTopics(int completedTopics) {
		this.completedTopics = completedTopics;
	}

	public int getTotalReferences() {
		return totalReferences;
	}

	public void setTotalReferences(int totalReferences) {
		this.totalReferences = totalReferences;
	}

	public int getCompletedReferences() {
		return completedReferences;
	}

	public void setCompletedReferences(int completedReferences) {
		this.completedReferences = completedReferences;
	}

	public double getCoursePercentage() {
		return coursePercentage;
	}

	public void setCoursePercentage(double coursePercentage) {
		this.coursePercentage = coursePercentage;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
