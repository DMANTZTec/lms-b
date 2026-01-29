package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // JPQL query for email_id
    @Query("SELECT s FROM Staff s WHERE s.email_id = :email")
    Optional<Staff> findByEmailId(@Param("email") String email);

//    // JPQL query for staff_id
//    @Query("SELECT s FROM Staff s WHERE s.staff_id = :staffId")
//    Optional<Staff> findByStaffId(@Param("staffId") String staffId);
}