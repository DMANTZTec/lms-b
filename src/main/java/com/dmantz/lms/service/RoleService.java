package com.dmantz.lms.service;

import java.util.List;

import com.dmantz.lms.dto.request.RoleRequest;
import com.dmantz.lms.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse createRole(RoleRequest request, String staffId);

    RoleResponse getRoleById(Long id);

    List<RoleResponse> getAllRoles();

    RoleResponse updateRole(Long id, RoleRequest request, String staffId);

    void deleteRole(Long id);
}