package com.dmantz.lms.dto.response;

public class HoursSpentResponse {
	
	    private int totalHours;
	    private String label;

	    public HoursSpentResponse() {}

	    public HoursSpentResponse(int totalHours) {
	        this.totalHours = totalHours;
	        this.label = "Total learning time";
	    }

	    public int getTotalHours() {
	        return totalHours;
	    }

	    public void setTotalHours(int totalHours) {
	        this.totalHours = totalHours;
	    }

	    public String getLabel() {
	        return label;
	    }

	    public void setLabel(String label) {
	        this.label = label;
	    }
	}
