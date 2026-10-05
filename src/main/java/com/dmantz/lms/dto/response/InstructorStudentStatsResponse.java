package com.dmantz.lms.dto.response;

public class InstructorStudentStatsResponse {

	private int activeStudents;
	private int totalStudents;

	public InstructorStudentStatsResponse() {
	}

	public InstructorStudentStatsResponse(int activeStudents, int totalStudents) {
		this.activeStudents = activeStudents;
		this.totalStudents = totalStudents;
	}

	public int getActiveStudents() {
		return activeStudents;
	}

	public void setActiveStudents(int activeStudents) {
		this.activeStudents = activeStudents;
	}

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}
}
