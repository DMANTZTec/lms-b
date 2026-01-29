package com.dmantz.lms_b.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="staff")
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String staff_id;

	private String first_nm;
	private String last_nm;

	private String addr1;
	private String addr2;
	private String city;
	private String state;
	private String country;
	private String pin;

	private String email_id;
	private String mobile_num;

	private String password;

	private String status;
	private String enabled;

	private String designation;

	private String emergency_contact_nm;
	private String emergency_contact_num;

	@Lob
	private byte[] profile_img;

	private LocalDate dob;

	private Long created_by;
	private LocalDateTime created_dt;

	private Long updated_by;
	private LocalDateTime updated_dt;

	@ManyToMany
	@JoinTable(
			name = "staff_role",
			joinColumns = @JoinColumn(name = "staff_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public String getFirst_nm() {
		return first_nm;
	}

	public void setFirst_nm(String first_nm) {
		this.first_nm = first_nm;
	}

	public String getLast_nm() {
		return last_nm;
	}

	public void setLast_nm(String last_nm) {
		this.last_nm = last_nm;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getMobile_num() {
		return mobile_num;
	}

	public void setMobile_num(String mobile_num) {
		this.mobile_num = mobile_num;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmergency_contact_nm() {
		return emergency_contact_nm;
	}

	public void setEmergency_contact_nm(String emergency_contact_nm) {
		this.emergency_contact_nm = emergency_contact_nm;
	}

	public String getEmergency_contact_num() {
		return emergency_contact_num;
	}

	public void setEmergency_contact_num(String emergency_contact_num) {
		this.emergency_contact_num = emergency_contact_num;
	}

	public byte[] getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(byte[] profile_img) {
		this.profile_img = profile_img;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Staff{" +
				"id=" + id +
				", staff_id='" + staff_id + '\'' +
				", first_nm='" + first_nm + '\'' +
				", last_nm='" + last_nm + '\'' +
				", addr1='" + addr1 + '\'' +
				", addr2='" + addr2 + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", pin='" + pin + '\'' +
				", email_id='" + email_id + '\'' +
				", mobile_num='" + mobile_num + '\'' +
				", password='" + password + '\'' +
				", status='" + status + '\'' +
				", enabled='" + enabled + '\'' +
				", designation='" + designation + '\'' +
				", emergency_contact_nm='" + emergency_contact_nm + '\'' +
				", emergency_contact_num='" + emergency_contact_num + '\'' +
				", profile_img=" + Arrays.toString(profile_img) +
				", dob=" + dob +
				", created_by=" + created_by +
				", created_dt=" + created_dt +
				", updated_by=" + updated_by +
				", updated_dt=" + updated_dt +
				", roles=" + roles +
				'}';
	}
}