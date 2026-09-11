package com.dmantz.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmantz.lms.dto.request.StaffRoleRequest;
import com.dmantz.lms.dto.response.StaffRoleResponse;
import com.dmantz.lms.service.StaffRoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/staff-roles")
public class StaffRoleController {

    private final StaffRoleService staffRoleService;

    public StaffRoleController(StaffRoleService staffRoleService) {
        this.staffRoleService = staffRoleService;
    }

    @PostMapping
    public ResponseEntity<StaffRoleResponse> assignRole(@Valid @RequestBody StaffRoleRequest request) {
        StaffRoleResponse created = staffRoleService.assignRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffRoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffRoleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StaffRoleResponse>> getAll() {
        return ResponseEntity.ok(staffRoleService.getAll());
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffRoleResponse>> getRolesByStaffId(@PathVariable String staffId) {
        return ResponseEntity.ok(staffRoleService.getRolesByStaffId(staffId));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<StaffRoleResponse>> getStaffByRoleId(@PathVariable Long roleId) {
        return ResponseEntity.ok(staffRoleService.getStaffByRoleId(roleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffRoleResponse> updateStaffRole(
            @PathVariable Long id,
            @Valid @RequestBody StaffRoleRequest request) {
        return ResponseEntity.ok(staffRoleService.updateStaffRole(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeRole(@PathVariable Long id) {
        staffRoleService.removeRole(id);
        return ResponseEntity.noContent().build();
    }
}