package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;

public class StaffRoleResponse {

    private Long id;
    private String staffId;
    private Long roleId;
    private String roleNm;
    private Long createdBy;
    private LocalDateTime createdDt;
    private Long updatedBy;
    private LocalDateTime updatedDt;

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
    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public String getRoleNm() {
        return roleNm;
    }
    public void setRoleNm(String roleNm) {
        this.roleNm = roleNm;
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
    public Long getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    public LocalDateTime getUpdatedDt() {
        return updatedDt;
    }
    public void setUpdatedDt(LocalDateTime updatedDt) {
        this.updatedDt = updatedDt;
    }
}