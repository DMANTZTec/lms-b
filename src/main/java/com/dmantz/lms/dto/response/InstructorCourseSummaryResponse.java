package com.dmantz.lms.dto.response;

public class InstructorCourseSummaryResponse {

	private String courseId;
	private String courseTitle;

	public InstructorCourseSummaryResponse() {
	}

	public InstructorCourseSummaryResponse(String courseId, String courseTitle) {
		this.courseId = courseId;
		this.courseTitle = courseTitle;
	}

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
}
