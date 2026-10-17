package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class CreateClassRequest {

	@NotBlank
	private String batchName;

	@NotNull
	private LocalDate beginDate;

	@NotNull
	private LocalDate endDate;

	@NotEmpty
	private List<String> selectedDays;
	// ["Mon", "Tue", "Wed"]

	@NotEmpty
	private Map<String, DayTimeSlot> dayTimes;
	// {"Mon": {"start":"09:00","end":"11:00"}}

	@NotEmpty
	private List<String> selectedInstructors;

	// Maximum number of students the batch can hold; null means unlimited.
	@Min(1)
	private Integer capacity;

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public LocalDate getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public List<String> getSelectedDays() {
		return selectedDays;
	}

	public void setSelectedDays(List<String> selectedDays) {
		this.selectedDays = selectedDays;
	}

	public Map<String, DayTimeSlot> getDayTimes() {
		return dayTimes;
	}

	public void setDayTimes(Map<String, DayTimeSlot> dayTimes) {
		this.dayTimes = dayTimes;
	}

	public List<String> getSelectedInstructors() {
		return selectedInstructors;
	}

	public void setSelectedInstructors(List<String> selectedInstructors) {
		this.selectedInstructors = selectedInstructors;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	// Inner class for day time slot
	public static class DayTimeSlot {
		private LocalTime start;
		private LocalTime end;

		public LocalTime getStart() {
			return start;
		}

		public void setStart(LocalTime start) {
			this.start = start;
		}

		public LocalTime getEnd() {
			return end;
		}

		public void setEnd(LocalTime end) {
			this.end = end;
		}
	}
}