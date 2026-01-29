package com.dmantz.lms_b.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class StaffResponse {

    private Long id;
    private String staff_id;

    private String first_nm;
    private String last_nm;

    private String email_id;
    private String mobile_num;

    private String designation;

    private String status;
    private String enabled;

    private LocalDate dob;

    private LocalDateTime created_dt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
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


    public LocalDateTime getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(LocalDateTime created_dt) {
        this.created_dt = created_dt;
    }

    @Override
    public String toString() {
        return "StaffResponse{" +
                "id=" + id +
                ", staff_id='" + staff_id + '\'' +
                ", first_nm='" + first_nm + '\'' +
                ", last_nm='" + last_nm + '\'' +
                ", email_id='" + email_id + '\'' +
                ", mobile_num='" + mobile_num + '\'' +
                ", designation='" + designation + '\'' +
                ", status='" + status + '\'' +
                ", enabled='" + enabled + '\'' +
                ", dob=" + dob +
                ", created_dt=" + created_dt +
                '}';
    }
}
