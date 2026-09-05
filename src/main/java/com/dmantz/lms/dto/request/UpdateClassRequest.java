package com.dmantz.lms.dto.request;

import java.time.LocalDate;

public class UpdateClassRequest {

    private String batchName;

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
   
  
}
