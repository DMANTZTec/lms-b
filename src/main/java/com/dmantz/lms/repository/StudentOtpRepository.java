package com.dmantz.lms.repository;

import com.dmantz.lms.entity.OtpPurpose;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.entity.StudentOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentOtpRepository extends JpaRepository<StudentOtp, UUID> {

	Optional<StudentOtp> findTopByEmailIdOrderByCreatedDtDesc(String emailId);

	Optional<StudentOtp> findTopByEmailIdOrMobileNumOrderByCreatedDtDesc(String emailId, String mobileNum);
	
	@Query(value = """
            SELECT * FROM student_otp
            WHERE email_id = :identifier OR mobile_num = :identifier
            ORDER BY created_dt DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<StudentOtp> findLatestByIdentifier(@Param("identifier") String identifier);

}
