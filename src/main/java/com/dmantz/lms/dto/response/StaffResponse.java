package com.dmantz.lms.dto.response;

import com.dmantz.lms.entity.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class StaffResponse {

    private Long id;

    private String staffId;

    private String firstNm;

    private String lastNm;

    private String addr1;

    private String addr2;

    private String city;

    private String state;

    private String country;

    private String pin;

    private String email;

    private String mobileNum;

    private LocalDate dateOfJoining;

    private String designation;

    private String emergencyContactNm;

    private String emergencyContactNum;

    private String profileImg;

    private Gender gender;

    private String status;

    private String enabled;

    private LocalDate dob;

    private LocalDateTime createdDt;
    
    private LocalDateTime updatedDt;

    private Set<String> roles;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
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

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(LocalDateTime createdDt) {
        this.createdDt = createdDt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    

    public LocalDateTime getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(LocalDateTime updatedDt) {
		this.updatedDt = updatedDt;
	}

	@Override
    public String toString() {
        return "StaffResponse{" +
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
                ", email='" + email + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", dateOfJoining=" + dateOfJoining +
                ", designation='" + designation + '\'' +
                ", emergencyContactNm='" + emergencyContactNm + '\'' +
                ", emergencyContactNum='" + emergencyContactNum + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", gender=" + gender +
                ", status='" + status + '\'' +
                ", enabled='" + enabled + '\'' +
                ", dob=" + dob +
                ", createdDt=" + createdDt +
                ", roles=" + roles +
                '}';
    }
}