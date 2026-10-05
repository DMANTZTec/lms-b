package com.dmantz.lms.dto.response;

public class StaffCourseResponse {

	private Long id;
	private String staffId;
	private String staffFirstNm;
	private String staffLastNm;
	private String courseId;
	private String courseTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffFirstNm() {
		return staffFirstNm;
	}

	public void setStaffFirstNm(String staffFirstNm) {
		this.staffFirstNm = staffFirstNm;
	}

	public String getStaffLastNm() {
		return staffLastNm;
	}

	public void setStaffLastNm(String staffLastNm) {
		this.staffLastNm = staffLastNm;
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

	@Override
	public String toString() {
		return "StaffCourseResponse{" + "id=" + id + ", staffId='" + staffId + '\'' + ", staffFirstNm='" + staffFirstNm
				+ '\'' + ", staffLastNm='" + staffLastNm + '\'' + ", courseId='" + courseId + '\'' + ", courseTitle='"
				+ courseTitle + '\'' + '}';
	}
}