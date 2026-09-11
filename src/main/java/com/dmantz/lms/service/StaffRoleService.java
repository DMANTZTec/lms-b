package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.StaffRoleRequest;
import com.dmantz.lms.dto.response.StaffRoleResponse;

public interface StaffRoleService {

    StaffRoleResponse assignRole(StaffRoleRequest request);

    StaffRoleResponse getById(Long id);

    List<StaffRoleResponse> getAll();

    List<StaffRoleResponse> getRolesByStaffId(String staffId);

    List<StaffRoleResponse> getStaffByRoleId(Long roleId);

    StaffRoleResponse updateStaffRole(Long id, StaffRoleRequest request);

    void removeRole(Long id);
}