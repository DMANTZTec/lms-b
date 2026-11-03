package com.dmantz.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InstructorStudentReportResponse {

	private String studentId;
	private String studentName;
	private String emailId;
	private String profileImg;
	private Long batchId;
	private String batchName;
	private String courseId;
	private String courseName;
	private int completedTopics;
	private int totalTopics;
	private int tasksCompleted;
	private int tasksTotal;
	private BigDecimal averageScore;
	private double attendancePercentage;
	private LocalDateTime joinedAt;
	private long joinedDaysAgo;
	private String status;
	private List<Criterion> criteria = new ArrayList<>();

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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCompletedTopics() {
		return completedTopics;
	}

	public void setCompletedTopics(int completedTopics) {
		this.completedTopics = completedTopics;
	}

	public int getTotalTopics() {
		return totalTopics;
	}

	public void setTotalTopics(int totalTopics) {
		this.totalTopics = totalTopics;
	}

	public int getTasksCompleted() {
		return tasksCompleted;
	}

	public void setTasksCompleted(int tasksCompleted) {
		this.tasksCompleted = tasksCompleted;
	}

	public int getTasksTotal() {
		return tasksTotal;
	}

	public void setTasksTotal(int tasksTotal) {
		this.tasksTotal = tasksTotal;
	}

	public BigDecimal getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(BigDecimal averageScore) {
		this.averageScore = averageScore;
	}

	public double getAttendancePercentage() {
		return attendancePercentage;
	}

	public void setAttendancePercentage(double attendancePercentage) {
		this.attendancePercentage = attendancePercentage;
	}

	public LocalDateTime getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(LocalDateTime joinedAt) {
		this.joinedAt = joinedAt;
	}

	public long getJoinedDaysAgo() {
		return joinedDaysAgo;
	}

	public void setJoinedDaysAgo(long joinedDaysAgo) {
		this.joinedDaysAgo = joinedDaysAgo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}

	public static class Criterion {

		private BigDecimal rating;
		private String criterion;

		public Criterion() {
		}

		public Criterion(BigDecimal rating, String criterion) {
			this.rating = rating;
			this.criterion = criterion;
		}

		public BigDecimal getRating() {
			return rating;
		}

		public void setRating(BigDecimal rating) {
			this.rating = rating;
		}

		public String getCriterion() {
			return criterion;
		}

		public void setCriterion(String criterion) {
			this.criterion = criterion;
		}
	}
}
