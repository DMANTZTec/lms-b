package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;

public class DeleteProgramCourseRequest {

	    @NotBlank(message = "Program ID is required")
	    private String programId;

	    @NotBlank(message = "Course ID is required")
	    private String courseId;

		public String getProgramId() {
			return programId;
		}

		public void setProgramId(String programId) {
			this.programId = programId;
		}

		public String getCourseId() {
			return courseId;
		}

		public void setCourseId(String courseId) {
			this.courseId = courseId;
		}
	    

	}


