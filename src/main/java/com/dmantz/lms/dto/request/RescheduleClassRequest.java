package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class RescheduleClassRequest {

	@NotNull
	private String className;

	@NotNull
	private LocalDate classDate;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public LocalDate getClassDate() {
		return classDate;
	}

	public void setClassDate(LocalDate classDate) {
		this.classDate = classDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
