package com.dmantz.lms.dto.response;

public class SuccessStoryResponse {
	
	    private Long id;
	    private Long studentId;
	    private String studentName;    // firstNm + lastNm
	    private String profileImg;      // straight from student.profileImg
	    private String placedCompany;
	    private String placedDesignation;
	    private String reviewMsg;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getStudentId() {
			return studentId;
		}
		public void setStudentId(Long studentId) {
			this.studentId = studentId;
		}
		public String getStudentName() {
			return studentName;
		}
		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}
		public String getProfileImg() {
			return profileImg;
		}
		public void setProfileImg(String profileImg) {
			this.profileImg = profileImg;
		}
		public String getPlacedCompany() {
			return placedCompany;
		}
		public void setPlacedCompany(String placedCompany) {
			this.placedCompany = placedCompany;
		}
		public String getPlacedDesignation() {
			return placedDesignation;
		}
		public void setPlacedDesignation(String placedDesignation) {
			this.placedDesignation = placedDesignation;
		}
		public String getReviewMsg() {
			return reviewMsg;
		}
		public void setReviewMsg(String reviewMsg) {
			this.reviewMsg = reviewMsg;
		}
	    
	    
	    

}
