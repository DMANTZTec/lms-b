package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public class UpdateClassRequest {

    private String batchName;

    @Min(1)
    private Integer capacity;

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
}
