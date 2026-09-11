package com.dmantz.lms.dto.response;

public class OverallProgressResponse {

	private int totalReferences;
	private int completedReferences;
	private double overallPercentage;
	private boolean completed;

	public int getTotalReferences() {
		return totalReferences;
	}

	public void setTotalReferences(int totalReferences) {
		this.totalReferences = totalReferences;
	}

	public int getCompletedReferences() {
		return completedReferences;
	}

	public void setCompletedReferences(int completedReferences) {
		this.completedReferences = completedReferences;
	}

	public double getOverallPercentage() {
		return overallPercentage;
	}

	public void setOverallPercentage(double overallPercentage) {
		this.overallPercentage = overallPercentage;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}