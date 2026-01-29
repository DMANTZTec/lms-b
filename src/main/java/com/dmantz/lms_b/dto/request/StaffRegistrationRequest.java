package com.dmantz.lms_b.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public class StaffRegistrationRequest {


    @NotBlank
    private String first_nm;
    private String last_nm;

    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String country;
    private String pin;

    @Email
    @NotBlank
    private String email_id;

    @NotBlank
    private String mobile_num;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    private String profile_img;

    @NotBlank
    private String designation;

    private String emergency_contact_nm;
    private String emergency_contact_num;

    private LocalDate dob;


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

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }


    @Override
    public String toString() {
        return "StaffRegistrationRequest{" +
                "first_nm='" + first_nm + '\'' +
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
                ", profile_img='" + profile_img + '\'' +
                ", designation='" + designation + '\'' +
                ", emergency_contact_nm='" + emergency_contact_nm + '\'' +
                ", emergency_contact_num='" + emergency_contact_num + '\'' +
                ", dob=" + dob +
                '}';
    }
}
