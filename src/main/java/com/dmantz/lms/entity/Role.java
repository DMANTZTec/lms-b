package com.dmantz.lms.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "role_nm")
	private String roleNm;

	@Column(name = "role_desc")
	private String roleDesc;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_dt")
	private LocalDateTime createdDt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_dt")
	private LocalDateTime updatedDt;

	@ManyToMany(mappedBy = "roles")
	private Set<Staff> staff;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleNm() {
		return roleNm;
	}

	public void setRoleNm(String roleNm) {
		this.roleNm = roleNm;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
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

	public Set<Staff> getStaff() {
		return staff;
	}

	public void setStaff(Set<Staff> staff) {
		this.staff = staff;
	}

	@Override
	public String toString() {
		return "Role{" + "id=" + id + ", roleNm='" + roleNm + '\'' + ", roleDesc='" + roleDesc + '\'' + ", createdBy="
				+ createdBy + ", createdDt=" + createdDt + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt
				+ ", staff=" + staff + '}';
	}
}
