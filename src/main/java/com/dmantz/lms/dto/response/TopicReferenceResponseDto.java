package com.dmantz.lms.dto.response;

public class TopicReferenceResponseDto {
	private boolean success;
	private String message;
	private TopicReferenceDataDto data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TopicReferenceDataDto getData() {
		return data;
	}

	public void setData(TopicReferenceDataDto data) {
		this.data = data;
	}
}