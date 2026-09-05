package com.dmantz.lms.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class ProgramCourseRequest {

	@NotBlank(message = "Program ID is required")
	private String programId;

	@NotEmpty(message = "Course IDs list cannot be empty")
	private List<@NotBlank String> courseIds;

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public List<String> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(List<String> courseIds) {
		this.courseIds = courseIds;
	}

}
