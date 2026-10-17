package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlanClassTopicsRequest {

	@NotBlank(message = "Staff ID is required")
	private String staffId;

	@NotNull(message = "Topic IDs are required")
	private List<Long> topicIds;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public List<Long> getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(List<Long> topicIds) {
		this.topicIds = topicIds;
	}
}
