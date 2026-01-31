package com.dmantz.lms_b.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmantz.lms_b.entity.StaffOtp;

public interface StaffOtpRepository extends JpaRepository<StaffOtp, UUID> {

}
