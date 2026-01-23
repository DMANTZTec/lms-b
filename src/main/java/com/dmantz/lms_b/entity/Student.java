package com.dmantz.lms_b.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name ="student")
	public class Student {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String student_id;
	    private String login_id;

	    private String first_nm;
	    private String last_nm;

	    private String gender;
	    private LocalDate dob;

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

	    private String emergency_contact_nm;
	    private String emergency_contact_num;

	    @Lob
	    private byte[] profile_img;

	    private Long created_by;
	    private LocalDateTime created_dt;

	    private Long updated_by;
	    private LocalDateTime updated_dt;
	    
		public Student() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Student(Long id, String student_id, String login_id, String first_nm, String last_nm, String gender,
				LocalDate dob, String addr1, String addr2, String city, String state, String country, String pin,
				String email_id, String mobile_num, String password, String status, String enabled,
				String emergency_contact_nm, String emergency_contact_num, byte[] profile_img, Long created_by,
				LocalDateTime created_dt, Long updated_by, LocalDateTime updated_dt) {
			super();
			this.id = id;
			this.student_id = student_id;
			this.login_id = login_id;
			this.first_nm = first_nm;
			this.last_nm = last_nm;
			this.gender = gender;
			this.dob = dob;
			this.addr1 = addr1;
			this.addr2 = addr2;
			this.city = city;
			this.state = state;
			this.country = country;
			this.pin = pin;
			this.email_id = email_id;
			this.mobile_num = mobile_num;
			this.password = password;
			this.status = status;
			this.enabled = enabled;
			this.emergency_contact_nm = emergency_contact_nm;
			this.emergency_contact_num = emergency_contact_num;
			this.profile_img = profile_img;
			this.created_by = created_by;
			this.created_dt = created_dt;
			this.updated_by = updated_by;
			this.updated_dt = updated_dt;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getStudent_id() {
			return student_id;
		}
		public void setStudent_id(String student_id) {
			this.student_id = student_id;
		}
		public String getLogin_id() {
			return login_id;
		}
		public void setLogin_id(String login_id) {
			this.login_id = login_id;
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
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public LocalDate getDob() {
			return dob;
		}
		public void setDob(LocalDate dob) {
			this.dob = dob;
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
	    
	}



