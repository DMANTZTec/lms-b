package com.dmantz.lms_b.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="role")
	 public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String role_nm;
	private String role_desc;

	private Long created_by;
	private LocalDateTime created_dt;

	private Long updated_by;
	private LocalDateTime updated_dt;

	@ManyToMany(mappedBy = "roles")
	private Set<Staff> staff;

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(Long id, String role_nm, String role_desc, Long created_by, LocalDateTime created_dt, Long updated_by,
			LocalDateTime updated_dt, Set<Staff> staff) {
		super();
		this.id = id;
		this.role_nm = role_nm;
		this.role_desc = role_desc;
		this.created_by = created_by;
		this.created_dt = created_dt;
		this.updated_by = updated_by;
		this.updated_dt = updated_dt;
		this.staff = staff;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole_nm() {
		return role_nm;
	}

	public void setRole_nm(String role_nm) {
		this.role_nm = role_nm;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
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

	public Set<Staff> getStaff() {
		return staff;
	}

	public void setStaff(Set<Staff> staff) {
		this.staff = staff;
	}
	

	}



