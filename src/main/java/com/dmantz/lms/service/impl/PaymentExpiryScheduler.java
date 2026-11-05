package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dmantz.lms.repository.PaymentRepository;

/*
 * Expires unpaid online orders. Before expiring, Razorpay is asked whether the order was
 * actually paid, so a payment whose webhook was missed is still settled.
 */
@Component
public class PaymentExpiryScheduler {

    private static final Logger logger = LogManager.getLogger(PaymentExpiryScheduler.class);

    private final PaymentRepository paymentRepository;
    private final PaymentWorkflow workflow;

    public PaymentExpiryScheduler(PaymentRepository paymentRepository, PaymentWorkflow workflow) {
        this.paymentRepository = paymentRepository;
        this.workflow = workflow;
    }

    @Scheduled(cron = "${payment.expiry-cron:0 */30 * * * *}")
    public void expireUnpaidOrders() {

        List<Long> ids = paymentRepository.findExpiredOnlineOrderIds(LocalDateTime.now());
        if (ids.isEmpty()) {
            return;
        }

        logger.info("Payment expiry job: {} unpaid online order(s) past expiry", ids.size());

        for (Long id : ids) {
            try {
                // Each payment in its own transaction (PaymentWorkflow is @Transactional)
                workflow.expireOrRecover(id);
            } catch (RuntimeException e) {
                // e.g. Razorpay unreachable: try again on the next run
                logger.error("Payment expiry job failed for payment #{}", id, e);
            }
        }
    }
}
