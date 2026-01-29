package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.StaffOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StaffOtpRepository extends JpaRepository<StaffOtp, UUID> {

}
