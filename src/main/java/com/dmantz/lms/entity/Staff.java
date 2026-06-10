package com.dmantz.lms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.dmantz.lms.entity.base.AuditFields;
import jakarta.persistence.Column;
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
@Table(name = "staff")
public class Staff extends AuditFields {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "staff_id", unique = true, nullable = false)
	private String staffId;

	@Column(name = "first_nm")
	private String firstNm;

	@Column(name = "last_nm")
	private String lastNm;

	@Column(name = "addr1")
	private String addr1;

	@Column(name = "addr2")
	private String addr2;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;

	@Column(name = "pin")
	private String pin;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "mobile_num")
	private String mobileNum;

	@Column(name = "password")
	private String password;

	@Column(name = "status")
	private String status;

	@Column(name = "enabled")
	private String enabled;

	@Column(name = "designation")
	private String designation;

	@Column(name = "emergency_contact_nm")
	private String emergencyContactNm;

	@Column(name = "emergency_contact_num")
	private String emergencyContactNum;

	@Lob
	@Column(name = "profile_img")
	private byte[] profileImg;

	@Column(name = "dob")
	private LocalDate dob;

	@ManyToMany
	@JoinTable(name = "staff_role", joinColumns = @JoinColumn(name = "staff_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getFirstNm() {
		return firstNm;
	}

	public void setFirstNm(String firstNm) {
		this.firstNm = firstNm;
	}

	public String getLastNm() {
		return lastNm;
	}

	public void setLastNm(String lastNm) {
		this.lastNm = lastNm;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
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

	public String getEmergencyContactNm() {
		return emergencyContactNm;
	}

	public void setEmergencyContactNm(String emergencyContactNm) {
		this.emergencyContactNm = emergencyContactNm;
	}

	public String getEmergencyContactNum() {
		return emergencyContactNum;
	}

	public void setEmergencyContactNum(String emergencyContactNum) {
		this.emergencyContactNum = emergencyContactNum;
	}

	public byte[] getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(byte[] profileImg) {
		this.profileImg = profileImg;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
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
				", staffId='" + staffId + '\'' +
				", firstNm='" + firstNm + '\'' +
				", lastNm='" + lastNm + '\'' +
				", addr1='" + addr1 + '\'' +
				", addr2='" + addr2 + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", pin='" + pin + '\'' +
				", emailId='" + emailId + '\'' +
				", mobileNum='" + mobileNum + '\'' +
				", password='" + password + '\'' +
				", status='" + status + '\'' +
				", enabled='" + enabled + '\'' +
				", designation='" + designation + '\'' +
				", emergencyContactNm='" + emergencyContactNm + '\'' +
				", emergencyContactNum='" + emergencyContactNum + '\'' +
				", profileImg=" + Arrays.toString(profileImg) +
				", dob=" + dob +
				", roles=" + roles +
				'}';
	}

}