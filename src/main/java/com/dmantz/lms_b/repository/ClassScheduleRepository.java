package com.dmantz.lms_b.repository;

import com.dmantz.lms_b.entity.ClassSchedule;
import com.dmantz.lms_b.entity.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

    List<ClassSchedule> findByCourseIdInAndClassDateBetweenAndStatusNotOrderByClassDateAscStartTimeAsc(
            List<Long> courseIds, LocalDate startDate,
            LocalDate endDate, ClassStatus status);


}
