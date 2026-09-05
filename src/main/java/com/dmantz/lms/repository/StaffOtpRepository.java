package com.dmantz.lms.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.entity.OtpStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms.entity.StaffOtp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffOtpRepository extends JpaRepository<StaffOtp, UUID> {

    Optional<StaffOtp> findTopByStaffIdAndStatusOrderByIdDesc(String staffId, OtpStatus status);

    Optional<StaffOtp> findTopByStaffIdOrderByIdDesc(String staffId);


    @Modifying
    @Transactional
    @Query("""
        UPDATE StaffOtp o
        SET o.status = :expiredStatus
        WHERE o.staffId = :staffId
          AND o.status IN (:activeStatuses)
    """)
    void expireActiveOtps(@Param("staffId") String staffId, @Param("expiredStatus") OtpStatus expiredStatus,
            @Param("activeStatuses") List<OtpStatus> activeStatuses);

    Optional<StaffOtp> findTopByStaffIdAndStatusOrderByCreatedDtDesc(String staffId, OtpStatus status);

    Optional<StaffOtp> findTopByStaffIdOrderByCreatedDtDesc(String staffId);


}


