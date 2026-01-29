package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /** Fetch all roles with IDs in the given set */
    Set<Role> findByIdIn(Set<Long> ids);

}
