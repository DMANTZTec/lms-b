package com.dmantz.lms.mapper;

import com.dmantz.lms.dto.response.AttendanceStudentResponse;
import com.dmantz.lms.dto.response.MarkAttendanceResponse;
import com.dmantz.lms.dto.response.ScheduleAttendanceResponse;
import com.dmantz.lms.entity.AttendanceStatus;
import com.dmantz.lms.entity.ScheduleAttendance;
import com.dmantz.lms.entity.SessionStatus;
import com.dmantz.lms.entity.Student;

import java.time.LocalDateTime;
import java.util.List;

public class AttendanceMapper {

    public static AttendanceStudentResponse toStudentResponse(ScheduleAttendance sa) {
        AttendanceStudentResponse r = new AttendanceStudentResponse();
        Student s = sa.getStudent();
        if (s != null) {
            r.setStudentId(s.getStudentId());
            r.setFirstNm(s.getFirstNm());
            r.setLastNm(s.getLastNm());
            r.setEmailId(s.getEmailId());
            r.setMobileNum(s.getMobileNum());
            r.setProfileImg(s.getProfileImg());
            r.setEnabled(s.getEnabled());
        }
        r.setStatus(sa.getStatus() == null ? AttendanceStatus.UNMARKED.name() : sa.getStatus().name());
        return r;
    }

    public static ScheduleAttendanceResponse toScheduleAttendanceResponse(
            Long scheduleId,
            String batchName,
            String course,
            SessionStatus sessionStatus,
            int totalStudents,
            int presentCount,
            int absentCount,
            int unmarkedCount,
            double attendanceRate,
            LocalDateTime markedAt,
            String markedByStaffId,
            List<AttendanceStudentResponse> students
    ) {
        ScheduleAttendanceResponse r = new ScheduleAttendanceResponse();
        r.setScheduleId(scheduleId);
        r.setBatchName(batchName);
        r.setCourse(course);
        r.setSessionStatus(sessionStatus == null ? SessionStatus.NOT_STARTED.name() : sessionStatus.name());
        r.setTotalStudents(totalStudents);
        r.setPresentCount(presentCount);
        r.setAbsentCount(absentCount);
        r.setUnmarkedCount(unmarkedCount);
        r.setAttendanceRate(attendanceRate);
        r.setMarkedAt(markedAt);
        r.setMarkedBy(markedByStaffId);
        r.setStudents(students);
        return r;
    }

    public static MarkAttendanceResponse toMarkAttendanceResponse(
            Long scheduleId,
            SessionStatus sessionStatus,
            int totalStudents,
            int presentCount,
            int absentCount,
            int unmarkedCount,
            double attendanceRate,
            LocalDateTime markedAt,
            String markedByStaffId
    ) {
        MarkAttendanceResponse r = new MarkAttendanceResponse();
        r.setScheduleId(scheduleId);
        r.setSessionStatus(sessionStatus == null ? SessionStatus.NOT_STARTED.name() : sessionStatus.name());
        r.setTotalStudents(totalStudents);
        r.setPresentCount(presentCount);
        r.setAbsentCount(absentCount);
        r.setUnmarkedCount(unmarkedCount);
        r.setAttendanceRate(attendanceRate);
        r.setMarkedAt(markedAt);
        r.setMarkedBy(markedByStaffId);
        return r;
    }

    public static double computeAttendanceRate(int presentCount, int totalStudents) {
        if (totalStudents <= 0) return 0.0d;
        double rate = (presentCount * 100.0d) / (double) totalStudents;
        return Math.round(rate * 10.0d) / 10.0d;
    }
}
