package com.dmantz.lms.service.impl;

import com.dmantz.lms.repository.ClassBatchRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClassBatchStatusScheduler {

    private static final Logger logger =
            LogManager.getLogger(ClassBatchStatusScheduler.class);

    private final ClassBatchRepository classBatchRepository;

    public ClassBatchStatusScheduler(ClassBatchRepository classBatchRepository) {
        this.classBatchRepository = classBatchRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void markPastBatchesCompleted() {

        int updatedCount = classBatchRepository.markPastBatchesCompleted(LocalDate.now());

        logger.info("Marked {} class batches as COMPLETED", updatedCount);
    }

}
