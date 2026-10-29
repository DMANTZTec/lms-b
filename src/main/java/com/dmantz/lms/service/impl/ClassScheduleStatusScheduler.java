package com.dmantz.lms.service.impl;

import com.dmantz.lms.repository.ClassScheduleRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ClassScheduleStatusScheduler {

    private static final Logger logger =
            LogManager.getLogger(ClassScheduleStatusScheduler.class);

    private final ClassScheduleRepository classScheduleRepository;

    public ClassScheduleStatusScheduler(ClassScheduleRepository classScheduleRepository) {
        this.classScheduleRepository = classScheduleRepository;
    }

    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void markPastSchedulesCompleted() {

        int updatedCount = classScheduleRepository.markPastSchedulesCompleted(LocalDate.now(), LocalTime.now());

        logger.info("Marked {} class schedules as COMPLETED", updatedCount);
    }

}
