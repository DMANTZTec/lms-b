package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;

public class ProviderResponse {

	private Long id;
	private String providerName;
	private String providerOrgName;

	private Long createdBy;
	private LocalDateTime createdDt;
	private Long updatedBy;
	private LocalDateTime updatedDt;

	public ProviderResponse() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(LocalDateTime createdDt) {
		this.createdDt = createdDt;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(LocalDateTime updatedDt) {
		this.updatedDt = updatedDt;
	}
}
