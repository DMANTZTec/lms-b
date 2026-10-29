package com.dmantz.lms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmantz.lms.dto.request.StaffRoleRequest;
import com.dmantz.lms.dto.response.StaffRoleResponse;
import com.dmantz.lms.entity.Role;
import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.StaffRole;
import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.mapper.StaffRoleMapper;
import com.dmantz.lms.repository.RoleRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StaffRoleRepository;
import com.dmantz.lms.service.StaffRoleService;

@Service
public class StaffRoleServiceImpl implements StaffRoleService {

    private final StaffRoleRepository staffRoleRepository;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final StaffRoleMapper staffRoleMapper;

    public StaffRoleServiceImpl(StaffRoleRepository staffRoleRepository,
                                 StaffRepository staffRepository,
                                 RoleRepository roleRepository,
                                 StaffRoleMapper staffRoleMapper) {
        this.staffRoleRepository = staffRoleRepository;
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.staffRoleMapper = staffRoleMapper;
    }

    @Override
    @Transactional
    public StaffRoleResponse assignRole(StaffRoleRequest request) {
        Staff staff = resolveStaff(request.getStaffId());

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

        if (staffRoleRepository.existsByStaff_IdAndRole_Id(staff.getId(), role.getId())) {
            throw new DuplicateValuesException("Role already assigned to this staff member");
        }

        StaffRole staffRole = new StaffRole();
        staffRole.setStaff(staff);
        staffRole.setRole(role);

        StaffRole saved = staffRoleRepository.save(staffRole);
        return staffRoleMapper.toResponse(saved);
    }

    @Override
    public StaffRoleResponse getById(Long id) {
        StaffRole staffRole = staffRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff role not found with id: " + id));
        return staffRoleMapper.toResponse(staffRole);
    }

    @Override
    public List<StaffRoleResponse> getAll() {
        return staffRoleRepository.findAll()
                .stream()
                .map(staffRoleMapper::toResponse)
                .toList();
    }

    @Override
    public List<StaffRoleResponse> getRolesByStaffId(String staffId) {
        Staff staff = resolveStaff(staffId);
        return staffRoleRepository.findByStaff_Id(staff.getId())
                .stream()
                .map(staffRoleMapper::toResponse)
                .toList();
    }

    @Override
    public List<StaffRoleResponse> getStaffByRoleId(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found with id: " + roleId);
        }
        return staffRoleRepository.findByRole_Id(roleId)
                .stream()
                .map(staffRoleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public StaffRoleResponse updateStaffRole(Long id, StaffRoleRequest request) {
        StaffRole staffRole = staffRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff role not found with id: " + id));

        Staff staff = resolveStaff(request.getStaffId());

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

        staffRoleRepository.findByStaff_IdAndRole_Id(staff.getId(), role.getId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateValuesException("Role already assigned to this staff member");
                });

        staffRole.setStaff(staff);
        staffRole.setRole(role);

        StaffRole updated = staffRoleRepository.save(staffRole);
        return staffRoleMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void removeRole(Long id) {
        if (!staffRoleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Staff role not found with id: " + id);
        }
        staffRoleRepository.deleteById(id);
    }

    private Staff resolveStaff(String staffId) {
        if (staffId == null || staffId.isBlank()) {
            throw new IllegalArgumentException("Staff id is required");
        }
        return staffRepository.findByStaffId(staffId.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with staff id: " + staffId));
    }
}