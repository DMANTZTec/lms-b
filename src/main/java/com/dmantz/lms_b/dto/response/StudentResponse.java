package com.dmantz.lms_b.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class StudentResponse {
    private Long id;

    private String studentId;
    private String loginId;

    private String firstNm;
    private String lastNm;

    private String gender;
    private LocalDate dob;

    private String addr1;
    private String city;
    private String state;
    private String country;
    private String pin;

    private String emailId;
    private String mobileNum;

    private String status;
    private String enabled;

    private String emergencyContactNm;
    private String emergencyContactNum;

    // Optional: return profile image URL instead of byte[]
    private String profile_img;

    private Long createdBy;
    private LocalDateTime createdDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
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

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", loginId='" + loginId + '\'' +
                ", firstNm='" + firstNm + '\'' +
                ", lastNm='" + lastNm + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", addr1='" + addr1 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pin='" + pin + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", status='" + status + '\'' +
                ", enabled='" + enabled + '\'' +
                ", emergencyContactNm='" + emergencyContactNm + '\'' +
                ", emergencyContactNum='" + emergencyContactNum + '\'' +
                ", profile_img='" + profile_img + '\'' +
                ", createdBy=" + createdBy +
                ", createdDt=" + createdDt +
                '}';
    }

}
