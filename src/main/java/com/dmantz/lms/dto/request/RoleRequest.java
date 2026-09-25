package com.dmantz.lms.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String roleNm;

    @Size(max = 255, message = "Role description must not exceed 255 characters")
    private String roleDesc;

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
}
