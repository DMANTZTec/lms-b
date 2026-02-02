package com.dmantz.lms_b.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class StudentRegistrationRequest {

    @NotBlank(message = "First name is required")
    private String firstNm;

    private String lastNm;

    @NotBlank(message = "Gender is required")
    private String gender;  // MALE / FEMALE / OTHER

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String country;
    private String pin;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String emailId;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 15, message = "Mobile number must be 10-15 digits")
    private String mobileNum;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    private String emergencyContactNm;
    private String emergencyContactNum;

    private String profile_img;


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

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    @Override
    public String toString() {
        return "StudentRegistrationRequest{" +
                "firstNm='" + firstNm + '\'' +
                ", lastNm='" + lastNm + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pin='" + pin + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", password='" + password + '\'' +
                ", emergencyContactNm='" + emergencyContactNm + '\'' +
                ", emergencyContactNum='" + emergencyContactNum + '\'' +
                ", profile_img='" + profile_img + '\'' +
                '}';
    }
}
