package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.RoleRequest;
import com.dmantz.lms.dto.response.RoleResponse;
import com.dmantz.lms.entity.Role;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.RoleMapper;
import com.dmantz.lms.repository.RoleRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final StaffRepository staffRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, StaffRepository staffRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.staffRepository = staffRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request, String staffId) {
        Long staffPkId = resolveStaffId(staffId);

        if (roleRepository.existsByRoleNm(request.getRoleNm())) {
            throw new DuplicateValuesException("Role already exists with name: " + request.getRoleNm());
        }

        Role role = roleMapper.toEntity(request);
        role.setCreatedBy(staffPkId);
        role.setCreatedDt(LocalDateTime.now());

        Role saved = roleRepository.save(role);
        return roleMapper.toResponse(saved);
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return roleMapper.toResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request, String staffId) {
        Long staffPkId = resolveStaffId(staffId);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        roleRepository.findByRoleNm(request.getRoleNm())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateValuesException("Role already exists with name: " + request.getRoleNm());
                });

        if (request.getRoleNm() != null) {
            role.setRoleNm(request.getRoleNm());
        }
        if (request.getRoleDesc() != null) {
            role.setRoleDesc(request.getRoleDesc());
        }
        role.setUpdatedBy(staffPkId);
        role.setUpdatedDt(LocalDateTime.now());

        Role updated = roleRepository.save(role);
        return roleMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    private Long resolveStaffId(String staffId) {
        if (staffId == null || staffId.isBlank()) {
            throw new IllegalArgumentException("Staff id is required");
        }

        Staff staff = staffRepository.findByStaffId(staffId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with staff id: " + staffId));

        return staff.getId();
    }
}