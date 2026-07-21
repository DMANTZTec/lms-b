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
    private String email;
    private String mobileNum;
    private LocalDate dateOfJoining;
    private String designation;
    private String profileImg;
    private Gender gender;
    private String status;
    private String enabled;
    private LocalDate dob;
    private LocalDateTime createdDt;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
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

    @Override
    public String toString() {
        return "StaffResponse{" +
                "id=" + id +
                ", staffId='" + staffId + '\'' +
                ", firstNm='" + firstNm + '\'' +
                ", lastNm='" + lastNm + '\'' +
                ", email='" + email + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", dateOfJoining=" + dateOfJoining +
                ", designation='" + designation + '\'' +
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
