package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRoleRepository extends JpaRepository<StaffRole, Long> {

}
