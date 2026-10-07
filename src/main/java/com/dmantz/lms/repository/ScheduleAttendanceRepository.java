package com.dmantz.lms.repository;

import com.dmantz.lms.entity.AttendanceStatus;
import com.dmantz.lms.entity.ScheduleAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleAttendanceRepository extends JpaRepository<ScheduleAttendance, Long> {

    Optional<ScheduleAttendance> findByScheduleIdAndStudentStudentId(Long scheduleId, String studentId);

    List<ScheduleAttendance> findByScheduleIdOrderByStudentStudentIdAsc(Long scheduleId);

    @Query("select count(sa) from ScheduleAttendance sa where sa.schedule.id = :scheduleId and sa.status = :status")
    long countByScheduleIdAndStatus(@Param("scheduleId") Long scheduleId, @Param("status") AttendanceStatus status);

    @Query("select count(sa) from ScheduleAttendance sa where sa.schedule.id = :scheduleId")
    long countByScheduleId(@Param("scheduleId") Long scheduleId);

    @Modifying
    @Query("delete from ScheduleAttendance sa where sa.schedule.id = :scheduleId")
    void deleteByScheduleId(@Param("scheduleId") Long scheduleId);

    long countByStudent_StudentIdAndStatus(String studentId, AttendanceStatus status);

    long countByStudent_StudentIdAndStatusAndSchedule_ClassDateBetween(String studentId, AttendanceStatus status,
                                                                       LocalDate startDate, LocalDate endDate);
}
