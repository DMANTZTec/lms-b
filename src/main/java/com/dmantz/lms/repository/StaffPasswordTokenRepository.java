package com.dmantz.lms.repository;

import com.dmantz.lms.entity.StaffPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffPasswordTokenRepository
        extends JpaRepository<StaffPasswordToken, Long> {

    Optional<StaffPasswordToken> findByToken(String token);
}
