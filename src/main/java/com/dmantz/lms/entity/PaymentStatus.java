package com.dmantz.lms.entity;

public enum PaymentStatus {
    CREATED,          // online order created, waiting for the student to pay
    PAID,             // money received (or fee waived / free)
    FAILED,           // last online attempt failed; the order can still be paid until it expires
    EXPIRED,          // online order not paid in time
    CANCELLED,        // request cancelled by admin, or replaced by another settled payment
    REFUND_PENDING,   // refund requested from Razorpay, waiting for refund.processed
    REFUNDED
}
