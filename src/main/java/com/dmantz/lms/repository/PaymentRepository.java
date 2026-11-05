package com.dmantz.lms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dmantz.lms.entity.Payment;
import com.dmantz.lms.entity.PaymentMethod;
import com.dmantz.lms.entity.PaymentStatus;

import jakarta.persistence.LockModeType;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Row locks so verify, webhook, scheduler and admin actions cannot update the same payment concurrently.

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.razorpayOrderId = :orderId")
    Optional<Payment> findByRazorpayOrderIdForUpdate(@Param("orderId") String orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.razorpayPaymentId = :paymentId")
    Optional<Payment> findByRazorpayPaymentIdForUpdate(@Param("paymentId") String paymentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.id = :id")
    Optional<Payment> findByIdForUpdate(@Param("id") Long id);

    List<Payment> findByEnrollment_IdOrderByCreatedDtDescIdDesc(Long enrollmentId);

    List<Payment> findByStudent_StudentIdOrderByIdDesc(String studentId);

    boolean existsByReferenceNo(String referenceNo);

    // Open (unpaid, not expired) requests for the same student and course / program

    List<Payment> findByStudent_StudentIdAndCourse_CourseIdAndStatusInOrderByIdDesc(
            String studentId, String courseId, List<PaymentStatus> statuses);

    List<Payment> findByStudent_StudentIdAndProgram_ProgramIdAndStatusInOrderByIdDesc(
            String studentId, String programId, List<PaymentStatus> statuses);

    // Online orders whose expiry has passed (for the expiry scheduler)

    @Query("""
            SELECT p.id FROM Payment p
            WHERE p.method = com.dmantz.lms.entity.PaymentMethod.ONLINE
            AND p.status IN (com.dmantz.lms.entity.PaymentStatus.CREATED, com.dmantz.lms.entity.PaymentStatus.FAILED)
            AND p.expiresAt < :now
            """)
    List<Long> findExpiredOnlineOrderIds(@Param("now") LocalDateTime now);

    // Admin report / cash reconciliation. Every filter is optional.

    @Query("""
            SELECT p FROM Payment p
            WHERE (:method IS NULL OR p.method = :method)
            AND (:status IS NULL OR p.status = :status)
            AND (:studentId IS NULL OR p.student.studentId = :studentId)
            AND (:recordedBy IS NULL OR p.recordedBy = :recordedBy)
            AND (:from IS NULL OR p.createdDt >= :from)
            AND (:to IS NULL OR p.createdDt < :to)
            """)
    Page<Payment> search(
            @Param("method") PaymentMethod method,
            @Param("status") PaymentStatus status,
            @Param("studentId") String studentId,
            @Param("recordedBy") Long recordedBy,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);
}
