package com.dmantz.lms.dto.response;

import java.time.LocalDateTime;

public class RoleResponse {

    private Long id;
    private String roleNm;
    private String roleDesc;
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
    public String getRoleNm() {
        return roleNm;
    }
    public void setRoleNm(String roleNm) {
        this.roleNm = roleNm;
    }
    public String getRoleDesc() {
        return roleDesc;
    }
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
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