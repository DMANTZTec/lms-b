package com.dmantz.lms_b.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProviderRequest {

	@NotBlank(message = "Provider name must not be null or empty")
	@Size(min = 2, max = 100, message = "Provider name must be between 2 and 100 characters")
	@Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$", message = "Provider name must contain only letters and spaces")
	private String providerName;;

	@NotBlank(message = "Provider organization name must not be null or empty")
	@Size(min = 2, max = 150, message = "Organization name must be between 2 and 150 characters")
	private String providerOrgName;

	public ProviderRequest() {
		super();
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderOrgName() {
		return providerOrgName;
	}

	public void setProviderOrgName(String providerOrgName) {
		this.providerOrgName = providerOrgName;
	}

}
