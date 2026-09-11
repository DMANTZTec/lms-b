package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class TopicRequestDto {
    @NotNull(message = "Chapter ID is required")
    @Positive(message = "Chapter ID must be a positive number")
    private Long chapterId;

    @NotBlank(message = "Topic name is required")
    @Size(min = 3, max = 100, message = "Topic name must be between 3 and 100 characters")
    private String topicName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Expected time is required")
    @Positive(message = "Expected time must be greater than 0 minutes")
    private Long expectedTimeMin;

    @NotBlank(message = "Staff ID is required")
    private String staffId;

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getExpectedTimeMin() {
        return expectedTimeMin;
    }

    public void setExpectedTimeMin(Long expectedTimeMin) {
        this.expectedTimeMin = expectedTimeMin;
    }

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

   
}
