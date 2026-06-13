package com.dmantz.lms.repository;

import com.dmantz.lms.entity.StudentRegistrationOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRegistrationOtpRepository extends JpaRepository<StudentRegistrationOTP, Long> {

    Optional<StudentRegistrationOTP> findByEmailId(String emailId);

    Optional<StudentRegistrationOTP> findByMobileNum(String mobileNum);

}
