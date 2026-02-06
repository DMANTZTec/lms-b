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
	private Long id;

	@Column(name = "provider_nm")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

}
