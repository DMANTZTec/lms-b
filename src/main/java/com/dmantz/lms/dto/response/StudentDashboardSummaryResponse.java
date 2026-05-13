package com.dmantz.lms.dto.response;

public class StudentDashboardSummaryResponse {

	private String studentId;
	private String studentName;
	private String profileImg;
	private StudentMyCoursesResponse courses; // reuse — has totalCourses, planned, ongoing, completed, courseList
	private WeeklyScheduleResponse weeklySchedule; // reuse — has weekStart, weekEnd, totalClasses, classList
	private OverallProgressResponse overallProgress;

	public OverallProgressResponse getOverallProgress() {
		return overallProgress;
	}

	public void setOverallProgress(OverallProgressResponse overallProgress) {
		this.overallProgress = overallProgress;
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

	public StudentMyCoursesResponse getCourses() {
		return courses;
	}

	public void setCourses(StudentMyCoursesResponse courses) {
		this.courses = courses;
	}

	public WeeklyScheduleResponse getWeeklySchedule() {
		return weeklySchedule;
	}

	public void setWeeklySchedule(WeeklyScheduleResponse weeklySchedule) {
		this.weeklySchedule = weeklySchedule;
	}

	public String getProfileImg() {
		return profileImg;
	}
	
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
}
