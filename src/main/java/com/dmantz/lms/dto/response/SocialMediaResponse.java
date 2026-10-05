package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.SocialPlatform;

public class SocialMediaResponse {

	private Long id;
	private SocialPlatform platform;
	private String url;
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SocialPlatform getPlatform() {
		return platform;
	}

	public void setPlatform(SocialPlatform platform) {
		this.platform = platform;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}