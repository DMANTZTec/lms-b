package com.dmantz.lms.entity;

/*
 * An enrollment row exists only after its fee is settled (paid online, paid
 * offline, waived or free), so there is no pending state here. Unpaid attempts
 * live in the payment table.
 */
public enum EnrollmentStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}
