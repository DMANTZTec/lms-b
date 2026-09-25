package com.dmantz.lms.dto.response;

public class InstructorClassStatsResponse {

	private int classesTaken;
	private int scheduled;
	private double hoursSpent;

	public InstructorClassStatsResponse() {
	}

	public InstructorClassStatsResponse(int classesTaken, int scheduled, double hoursSpent) {
		this.classesTaken = classesTaken;
		this.scheduled = scheduled;
		this.hoursSpent = hoursSpent;
	}

	public int getClassesTaken() {
		return classesTaken;
	}

	public void setClassesTaken(int classesTaken) {
		this.classesTaken = classesTaken;
	}

	public int getScheduled() {
		return scheduled;
	}

	public void setScheduled(int scheduled) {
		this.scheduled = scheduled;
	}

	public double getHoursSpent() {
		return hoursSpent;
	}

	public void setHoursSpent(double hoursSpent) {
		this.hoursSpent = hoursSpent;
	}
}
