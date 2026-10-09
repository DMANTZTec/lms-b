package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.MarkAttendanceRequest;
import com.dmantz.lms.dto.response.AttendanceStudentResponse;
import com.dmantz.lms.dto.response.MarkAttendanceResponse;
import com.dmantz.lms.dto.response.ScheduleAttendanceResponse;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.mapper.AttendanceMapper;
import com.dmantz.lms.repository.ClassScheduleRepository;
import com.dmantz.lms.repository.EnrollmentBatchRepository;
import com.dmantz.lms.repository.ScheduleAttendanceRepository;
import com.dmantz.lms.repository.StaffRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.AttendanceService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LogManager.getLogger(AttendanceServiceImpl.class);

    private final ClassScheduleRepository classScheduleRepository;
    private final ScheduleAttendanceRepository scheduleAttendanceRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final EnrollmentBatchRepository enrollmentBatchRepository;

    public AttendanceServiceImpl(ClassScheduleRepository classScheduleRepository,
                                 ScheduleAttendanceRepository scheduleAttendanceRepository,
                                 StudentRepository studentRepository,
                                 StaffRepository staffRepository,
                                 EnrollmentBatchRepository enrollmentBatchRepository) {
        this.classScheduleRepository = classScheduleRepository;
        this.scheduleAttendanceRepository = scheduleAttendanceRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.enrollmentBatchRepository = enrollmentBatchRepository;
    }

    @Override
    @Transactional
    public ScheduleAttendanceResponse getScheduleAttendance(Long scheduleId) {
        logger.info("Fetching schedule attendance for scheduleId: {}", scheduleId);

        ClassSchedule cs = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));

        ensureAttendanceRows(cs);

        List<ScheduleAttendance> attendanceList = scheduleAttendanceRepository.findByScheduleIdOrderByStudentStudentIdAsc(scheduleId);

        List<AttendanceStudentResponse> students = attendanceList.stream()
                .map(AttendanceMapper::toStudentResponse)
                .collect(Collectors.toList());

        int total = students.size();
        int present = (int) attendanceList.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        int absent = (int) attendanceList.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        int unmarked = total - present - absent;

        SessionStatus sessionStatus = deriveSessionStatus(scheduleId);
        LocalDateTime markedAt = findLatestMarkedAt(attendanceList);
        String markedByStaffId = findLatestMarkedByStaffId(attendanceList);

        double rate = AttendanceMapper.computeAttendanceRate(present, total);

        String batchName = cs.getClassBatch() != null ? cs.getClassBatch().getClassName() : null;
        String course = null;
        if (cs.getClassBatch() != null && cs.getClassBatch().getCourse() != null) {
            course = cs.getClassBatch().getCourse().getCourseTitle();
        }
        if (course == null) {
            course = cs.getClassName();
        }

        return AttendanceMapper.toScheduleAttendanceResponse(
                scheduleId,
                batchName,
                course,
                sessionStatus,
                total,
                present,
                absent,
                unmarked,
                rate,
                markedAt,
                markedByStaffId,
                students
        );
    }

    @Override
    @Transactional
    public MarkAttendanceResponse markAttendance(Long scheduleId, MarkAttendanceRequest request) {
        logger.info("Marking attendance for scheduleId: {}, studentId: {}, status: {}, staffId: {}",
                scheduleId, request.getStudentId(), request.getStatus(), request.getStaffId());

        ClassSchedule cs = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));

        Student student = studentRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + request.getStudentId()));

        Staff marker = staffRepository.findByStaffId(request.getStaffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + request.getStaffId()));

        ensureAttendanceRows(cs);

        ScheduleAttendance sa = scheduleAttendanceRepository.findByScheduleIdAndStudentStudentId(scheduleId, request.getStudentId())
                .orElseGet(() -> {
                    ScheduleAttendance na = new ScheduleAttendance();
                    na.setSchedule(cs);
                    na.setStudent(student);
                    na.setStatus(AttendanceStatus.UNMARKED);
                    return na;
                });

        AttendanceStatus newStatus;
        String sReq = request.getStatus() == null ? "" : request.getStatus().trim().toUpperCase();
        switch (sReq) {
            case "PRESENT":
                newStatus = AttendanceStatus.PRESENT;
                break;
            case "ABSENT":
                newStatus = AttendanceStatus.ABSENT;
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + request.getStatus() + ". Allowed: PRESENT, ABSENT");
        }

        sa.setStatus(newStatus);
        sa.setMarkedBy(marker);
        sa.setMarkedAt(LocalDateTime.now());

        scheduleAttendanceRepository.save(sa);

        List<ScheduleAttendance> all = scheduleAttendanceRepository.findByScheduleIdOrderByStudentStudentIdAsc(scheduleId);
        int total = all.size();
        int present = (int) all.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        int absent = (int) all.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        int unmarked = total - present - absent;

        SessionStatus sessionStatus = deriveSessionStatus(scheduleId);
        LocalDateTime latestMarkedAt = findLatestMarkedAt(all);
        LocalDateTime markedAtResp = latestMarkedAt != null ? latestMarkedAt : sa.getMarkedAt();

        double rate = AttendanceMapper.computeAttendanceRate(present, total);

        return AttendanceMapper.toMarkAttendanceResponse(
                scheduleId,
                sessionStatus,
                total,
                present,
                absent,
                unmarked,
                rate,
                markedAtResp,
                marker.getStaffId()
        );
    }

    @Transactional
    protected void ensureAttendanceRows(ClassSchedule cs) {
        ClassBatch cb = cs.getClassBatch();
        if (cb == null) return;

        Set<Student> students = new java.util.HashSet<>();

        for (EnrollmentBatch eb : enrollmentBatchRepository.findByClassBatchId(cb.getId())) {
            if (eb.getEnrollment() != null && eb.getEnrollment().getStudent() != null) {
                students.add(eb.getEnrollment().getStudent());
            }
        }

        if (students.isEmpty()) return;

        for (Student s : students) {
            if (s == null || s.getStudentId() == null) continue;
            boolean exists = scheduleAttendanceRepository.findByScheduleIdAndStudentStudentId(cs.getId(), s.getStudentId()).isPresent();
            if (!exists) {
                ScheduleAttendance na = new ScheduleAttendance();
                na.setSchedule(cs);
                na.setStudent(s);
                na.setStatus(AttendanceStatus.UNMARKED);
                scheduleAttendanceRepository.save(na);
            }
        }
    }

    @Override
    public SessionStatus getSessionStatus(Long scheduleId) {
        return deriveSessionStatus(scheduleId);
    }

    @Override
    public int getPresentCount(Long scheduleId) {
        return (int) scheduleAttendanceRepository.countByScheduleIdAndStatus(scheduleId, AttendanceStatus.PRESENT);
    }

    @Override
    public double getAttendanceRate(Long scheduleId) {
        int total = (int) scheduleAttendanceRepository.countByScheduleId(scheduleId);
        return AttendanceMapper.computeAttendanceRate(getPresentCount(scheduleId), total);
    }

    private SessionStatus deriveSessionStatus(Long scheduleId) {
        List<ScheduleAttendance> all = scheduleAttendanceRepository.findByScheduleIdOrderByStudentStudentIdAsc(scheduleId);
        if (all.isEmpty()) {
            return SessionStatus.NOT_STARTED;
        }
        int total = all.size();
        int marked = (int) all.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.ABSENT)
                .count();
        if (marked == 0) {
            return SessionStatus.NOT_STARTED;
        }
        if (marked < total) {
            return SessionStatus.IN_PROGRESS;
        }
        return SessionStatus.MARKED;
    }

    private LocalDateTime findLatestMarkedAt(List<ScheduleAttendance> list) {
        return list.stream()
                .map(ScheduleAttendance::getMarkedAt)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private String findLatestMarkedByStaffId(List<ScheduleAttendance> list) {
        ScheduleAttendance latest = list.stream()
                .filter(a -> a.getMarkedAt() != null && a.getMarkedBy() != null)
                .max(Comparator.comparing(ScheduleAttendance::getMarkedAt))
                .orElse(null);
        if (latest == null || latest.getMarkedBy() == null) return null;
        return latest.getMarkedBy().getStaffId();
    }
}
