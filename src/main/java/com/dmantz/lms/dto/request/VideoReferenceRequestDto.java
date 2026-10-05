package com.dmantz.lms.dto.request;

public class VideoReferenceRequestDto {

	private String videoTitle;
	private String refBy;
	private String refById;

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getRefBy() {
		return refBy;
	}

	public void setRefBy(String refBy) {
		this.refBy = refBy;
	}

	public String getRefById() {
		return refById;
	}

	public void setRefById(String refById) {
		this.refById = refById;
	}
}