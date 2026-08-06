package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SuccessStoryRequest{
	
	 @NotNull 
	 private Long studentId;
	    @NotBlank 
	    private String placedCompany;
	    @NotBlank 
	    private String placedDesignation;
	    @NotBlank 
	    private String reviewMsg;
	    Integer displayOrder;
		public Long getStudentId() {
			return studentId;
		}
		public void setStudentId(Long studentId) {
			this.studentId = studentId;
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
		public Integer getDisplayOrder() {
			return displayOrder;
		}
		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}
	    
	    
	    
	
}


