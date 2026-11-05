package com.dmantz.lms.entity;

public enum PaymentMethod {
    ONLINE,   // Razorpay
    OFFLINE,  // cash / bank transfer / cheque / UPI collected by admin
    WAIVER,   // fee waived by admin, amount 0, reason required
    FREE      // fee configured as 0
}
