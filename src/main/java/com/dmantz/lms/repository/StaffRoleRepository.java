package com.dmantz.lms.repository;

import com.dmantz.lms.entity.Staff;
import com.dmantz.lms.entity.StaffRole;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRoleRepository extends JpaRepository<StaffRole, Long> {
	
	@Query("""
		    SELECT DISTINCT s
		    FROM Staff s
		    JOIN s.roles r
		    WHERE r.roleNm = :roleNm
		""")
		List<Staff> findByRoleNm(@Param("roleNm") String roleNm);

}
