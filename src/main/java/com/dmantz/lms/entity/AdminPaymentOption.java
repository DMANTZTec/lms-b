package com.dmantz.lms.entity;

public enum AdminPaymentOption {
    CASH_RECEIVED,   // admin collected money offline; enrollment is created immediately
    WAIVER,          // fee waived; enrollment is created immediately
    PAY_LATER,       // payment request sent; enrollment is created when the student pays online
    COLLECT_ONLINE   // admin opens Razorpay checkout for the student right now
}
