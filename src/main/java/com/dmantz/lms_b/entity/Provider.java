package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "provider")
public class Provider {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "provider_nm")
<<<<<<< HEAD
	private String providerName;

	@Column(name = "provider_org_nm")
	private String providerOrgName;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_dt")
	private LocalDateTime createdDt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_dt")
	private LocalDateTime updatedDt;

=======
	private String provider_nm;

	@Column(name = "provider_org_nm")
	private String provider_org_nm;

	@Column(name = "created_by")
	private Long created_by;

	@Column(name = "created_dt")
	private LocalDateTime created_dt;

	@Column(name = "updated_by")
	private Long updated_by;

	@Column(name = "updated_dt")
	private LocalDateTime updated_dt;

>>>>>>> 23d046e2cbc246a38f1509fcac18c9ef45933632
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
<<<<<<< HEAD

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

=======

	public String getProvider_nm() {
		return provider_nm;
	}

	public void setProvider_nm(String provider_nm) {
		this.provider_nm = provider_nm;
	}

	public String getProvider_org_nm() {
		return provider_org_nm;
	}

	public void setProvider_org_nm(String provider_org_nm) {
		this.provider_org_nm = provider_org_nm;
	}

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public LocalDateTime getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(LocalDateTime created_dt) {
		this.created_dt = created_dt;
	}

	public Long getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Long updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDateTime getUpdated_dt() {
		return updated_dt;
	}

	public void setUpdated_dt(LocalDateTime updated_dt) {
		this.updated_dt = updated_dt;
	}

	@Override
	public String toString() {
		return "Provider [id=" + id + ", provider_nm=" + provider_nm + ", provider_org_nm=" + provider_org_nm
				+ ", created_by=" + created_by + ", created_dt=" + created_dt + ", updated_by=" + updated_by
				+ ", updated_dt=" + updated_dt + "]";
	}

>>>>>>> 23d046e2cbc246a38f1509fcac18c9ef45933632
}
