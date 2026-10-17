package com.dmantz.lms.service.impl;

import com.dmantz.lms.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentCleanupScheduler {

    private static final Logger logger =
            LogManager.getLogger(StudentCleanupScheduler.class);

    private final StudentRepository studentRepository;

    public StudentCleanupScheduler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")
//    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void cleanupUnverifiedStudents() {

        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(1);

        int deletedCount = studentRepository.deleteUnverifiedStudents(cutoffTime);

        logger.info("Deleted {} unverified students older than 1 days", deletedCount);
    }

}
