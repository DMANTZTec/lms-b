package com.dmantz.lms.entity;

import java.time.LocalDateTime;

import com.dmantz.lms.entity.base.AuditFields;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "provider")
public class Provider extends AuditFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_nm")
    private String providerName;

    @Column(name = "provider_org_nm")
    private String providerOrgName;

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

}
