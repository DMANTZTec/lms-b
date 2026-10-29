package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.MentorHelpStatus;

public class StudentTaskMentorResponse {

	    private Long id;
	    private Long studentTaskId;
	    private String mentorStudentId;
	    private int minsSpent;
	    private MentorHelpStatus status;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getStudentTaskId() {
			return studentTaskId;
		}
		public void setStudentTaskId(Long studentTaskId) {
			this.studentTaskId = studentTaskId;
		}
		public String getMentorStudentId() {
			return mentorStudentId;
		}
		public void setMentorStudentId(String mentorStudentId) {
			this.mentorStudentId = mentorStudentId;
		}
		public Integer getMinsSpent() {
			return minsSpent;
		}
		public void setMinsSpent(Integer minsSpent) {
			this.minsSpent = minsSpent;
		}
		public MentorHelpStatus getStatus() {
			return status;
		}
		public void setStatus(MentorHelpStatus status) {
			this.status = status;
		}	    
	 
	}


