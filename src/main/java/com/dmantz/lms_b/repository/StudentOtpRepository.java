package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.Student_otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentOtpRepository extends JpaRepository<Student_otp, UUID> {

}
