package com.dmantz.lms.dto.response;

public class MentorPointsResponse {
	
	private int totalPoints;
	private int thisMonthPoints;

	// Add getters and setters
	public int getTotalPoints() {
	    return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
	    this.totalPoints = totalPoints;
	}

	public int getThisMonthPoints() {
	    return thisMonthPoints;
	}

	public void setThisMonthPoints(int thisMonthPoints) {
	    this.thisMonthPoints = thisMonthPoints;
	}

}
