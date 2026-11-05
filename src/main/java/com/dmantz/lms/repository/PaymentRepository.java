package com.dmantz.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.Payment;

import jakarta.persistence.LockModeType;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Row lock so verify and webhook cannot update the same payment concurrently.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.razorpayOrderId = :orderId")
    Optional<Payment> findByRazorpayOrderIdForUpdate(@Param("orderId") String orderId);

    List<Payment> findByEnrollment_IdOrderByCreatedDtDescIdDesc(Long enrollmentId);
}
