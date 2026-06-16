package com.dmantz.lms.dto.response;

import jakarta.validation.constraints.NotNull;

public class AssignInstructorRequest {
	
	 @NotNull(message = "Staff id is required")
	    private String staffId;
	 
	 public String getStaffId() {
		    return staffId;
		}

		public void setStaffId(String staffId) {
		    this.staffId = staffId;
		}

}
