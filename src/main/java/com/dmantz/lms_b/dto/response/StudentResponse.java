package com.dmantz.lms_b.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class StudentResponse {

    private Long id;
    private String studentid;          // from student_id
    private String loginid;            // from login_id

    private String firstnm;             // from first_nm
    private String lastnm;              // from last_nm
    private String gender;
    private LocalDate dob;

    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String country;
    private String pin;

    private String emailid;             // from email_id
    private String mobilenumber;        // from mobile_num

    private String status;
    private String enabled;

    private String emergencycontactnm;  // from emergency_contact_nm
    private String emergencycontactnum; // from emergency_contact_num

    private byte[] profileimg;          // from profile_img

    private Long createdby;
    private LocalDateTime createddt;
    private Long updatedby;
    private LocalDateTime updateddt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

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

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
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

    public String getEmergencycontactnm() {
        return emergencycontactnm;
    }

    public void setEmergencycontactnm(String emergencycontactnm) {
        this.emergencycontactnm = emergencycontactnm;
    }

    public String getEmergencycontactnum() {
        return emergencycontactnum;
    }

    public void setEmergencycontactnum(String emergencycontactnum) {
        this.emergencycontactnum = emergencycontactnum;
    }

    public byte[] getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(byte[] profileimg) {
        this.profileimg = profileimg;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public LocalDateTime getCreateddt() {
        return createddt;
    }

    public void setCreateddt(LocalDateTime createddt) {
        this.createddt = createddt;
    }

    public Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Long updatedby) {
        this.updatedby = updatedby;
    }

    public LocalDateTime getUpdateddt() {
        return updateddt;
    }

    public void setUpdateddt(LocalDateTime updateddt) {
        this.updateddt = updateddt;
    }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "id=" + id +
                ", studentid='" + studentid + '\'' +
                ", loginid='" + loginid + '\'' +
                ", firstnm='" + firstnm + '\'' +
                ", lastnm='" + lastnm + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pin='" + pin + '\'' +
                ", emailid='" + emailid + '\'' +
                ", mobilenumber='" + mobilenumber + '\'' +
                ", status='" + status + '\'' +
                ", enabled='" + enabled + '\'' +
                ", emergencycontactnm='" + emergencycontactnm + '\'' +
                ", emergencycontactnum='" + emergencycontactnum + '\'' +
                ", profileimg=" + Arrays.toString(profileimg) +
                ", createdby=" + createdby +
                ", createddt=" + createddt +
                ", updatedby=" + updatedby +
                ", updateddt=" + updateddt +
                '}';
    }
}
