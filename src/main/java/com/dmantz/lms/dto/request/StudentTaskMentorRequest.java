package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.MentorHelpStatus;

public class StudentTaskMentorRequest {

	private Long studentTaskId;
	private String mentorStudentId;
	private Integer minsSpent;

	// getters & setters
	public void setStudentTaskId(Long studentTaskId) {
		this.studentTaskId = studentTaskId;
	}

	public Long getStudentTaskId() {
		return studentTaskId;
	}

	public void setMentorStudentId(String mentorStudentId) {
		this.mentorStudentId = mentorStudentId;
	}

	public String getMentorStudentId() {
		return mentorStudentId;
	}

	public void setMinsSpent(Integer minsSpent) {
		this.minsSpent = minsSpent;
	}

	public Integer getMinsSpent() {
		return minsSpent;
	}
}
