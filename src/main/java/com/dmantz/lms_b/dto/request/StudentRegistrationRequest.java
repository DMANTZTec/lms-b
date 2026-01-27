package com.dmantz.lms_b.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class StudentRegistrationRequest {
    @NotBlank(message = "First name is required")
    private String firstnm;

    private String lastnm;

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
    private String email_id;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 15, message = "Mobile number must be 10-15 digits")
    private String mobile_num;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    private String emergencyContactnm;
    private String emergencyContactnum;

    public String getFirstnm() {
        return firstnm;
    }

    public void setFirstnm(String firstnm) {
        this.firstnm = firstnm;
    }

    public String getLastnm() {
        return lastnm;
    }

    public void setLastnm(String lastnm) {
        this.lastnm = lastnm;
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

    public String getEmergencyContactnm() {
        return emergencyContactnm;
    }

    public void setEmergencyContactnm(String emergencyContactnm) {
        this.emergencyContactnm = emergencyContactnm;
    }

    public String getEmergencyContactnum() {
        return emergencyContactnum;
    }

    public void setEmergencyContactnum(String emergencyContactnum) {
        this.emergencyContactnum = emergencyContactnum;
    }

    @Override
    public String toString() {
        return "StudentRegistrationRequest{" +
                "firstnm='" + firstnm + '\'' +
                ", lastnm='" + lastnm + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pin='" + pin + '\'' +
                ", email_id='" + email_id + '\'' +
                ", mobile_num='" + mobile_num + '\'' +
                ", password='" + password + '\'' +
                ", emergencyContactnm='" + emergencyContactnm + '\'' +
                ", emergencyContactnum='" + emergencyContactnum + '\'' +
                '}';
    }
}
