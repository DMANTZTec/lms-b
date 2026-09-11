package com.dmantz.lms.dto.request;

import java.util.List;

public class BatchInstructorRequest {
	
    private List<String> staffIds;
    
    public List<String> getStaffIds() {
		return staffIds;
	}
    
    	public void setStaffIds(List<String> staffIds) {
		this.staffIds = staffIds;
	}


}
