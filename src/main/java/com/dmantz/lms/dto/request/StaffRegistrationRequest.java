package com.dmantz.lms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public class StaffRegistrationRequest {

    @NotBlank
    private String firstNm;
    private String lastNm;

    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String country;
    private String pin;

    @Email
    @NotBlank
    private String emailId;

    @NotBlank
    private String mobileNum;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    private String designation;

    private String emergencyContactNm;
    private String emergencyContactNum;

    private LocalDate dob;

    // Base64 image
    private String profileImgBase64;

    // Role names: ADMIN, STAFF, MENTOR
    private Set<String> roles;

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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getProfileImgBase64() {
        return profileImgBase64;
    }

    public void setProfileImgBase64(String profileImgBase64) {
        this.profileImgBase64 = profileImgBase64;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "StaffRegistrationRequest{" +
                "firstNm='" + firstNm + '\'' +
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
                ", designation='" + designation + '\'' +
                ", emergencyContactNm='" + emergencyContactNm + '\'' +
                ", emergencyContactNum='" + emergencyContactNum + '\'' +
                ", dob=" + dob +
                ", profileImgBase64='" + profileImgBase64 + '\'' +
                ", roles=" + roles +
                '}';
    }
}



