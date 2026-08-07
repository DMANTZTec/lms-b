package com.dmantz.lms.dto.request;

import com.dmantz.lms.entity.SocialPlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SocialMediaRequest {

	@NotNull
	private SocialPlatform platform;

	@NotBlank
	private String url;

	private Boolean isActive;

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