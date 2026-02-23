package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.ClassScheduleRequest;
import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.request.UpdateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;
import com.dmantz.lms_b.dto.response.ClassScheduleResponse;
import com.dmantz.lms_b.entity.*;
import com.dmantz.lms_b.mapper.ClassBatchMapper;
import com.dmantz.lms_b.mapper.ClassScheduleMapper;
import com.dmantz.lms_b.repository.ClassBatchRepository;
import com.dmantz.lms_b.repository.ClassScheduleRepository;
import com.dmantz.lms_b.repository.CourseRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.service.ClassAdminService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClassAdminServiceImpl implements ClassAdminService {

    private final CourseRepository courseRepository;
    private final ClassBatchRepository classBatchRepository;
    private final ClassBatchMapper classBatchMapper;
    private final ClassScheduleMapper classScheduleMapper;
    private final StaffRepository staffRepository;
    private final ClassScheduleRepository classScheduleRepository;

    public ClassAdminServiceImpl(CourseRepository courseRepository, ClassBatchRepository classBatchRepository, ClassBatchMapper classBatchMapper, ClassScheduleMapper classScheduleMapper, StaffRepository staffRepository, ClassScheduleRepository classScheduleRepository) {
        this.courseRepository = courseRepository;
        this.classBatchRepository = classBatchRepository;
        this.classBatchMapper = classBatchMapper;
        this.classScheduleMapper = classScheduleMapper;
        this.staffRepository = staffRepository;
        this.classScheduleRepository = classScheduleRepository;
    }


    @Override
    public ClassResponse addClass(String courseId, CreateClassRequest request) {

        // Fetch course using business ID
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Map request to entity
        ClassBatch classBatch = classBatchMapper.toEntity(request);

        // IMPORTANT: set relation
        classBatch.setCourse(course);

        if (classBatch.getStatus() == null) {
            classBatch.setStatus(String.valueOf(ClassStatus.SCHEDULED));
        }

        // Save
        classBatch = classBatchRepository.save(classBatch);

        return classBatchMapper.toResponse(classBatch);
    }

    @Override
    public ClassResponse modifyClass(Long batchId, UpdateClassRequest request) {

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Update only allowed fields
        classBatchMapper.updateClassFromDto(request, classBatch);

        classBatch = classBatchRepository.save(classBatch);

        return classBatchMapper.toResponse(classBatch);
    }

    @Override
    public ClassResponse cancelClass(Long batchId) {

        ClassBatch classBatch = classBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        classBatch.setStatus("CANCELLED");

        classBatch = classBatchRepository.save(classBatch);

        return classBatchMapper.toResponse(classBatch);
    }


    @Override
    public ClassScheduleResponse addScheduleToClass(ClassScheduleRequest request) {

        ClassSchedule schedule = classScheduleMapper.toEntity(request);

        if (schedule.getStatus() == null) {
            schedule.setStatus(ClassStatus.SCHEDULED);
        }

        ClassBatch batch = classBatchRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        schedule.setClassBatch(batch);
        schedule.setStaff(staff);

        ClassSchedule saved = classScheduleRepository.save(schedule);
        return classScheduleMapper.toResponse(saved);
    }

    @Override
    public ClassScheduleResponse modifySchedule(Long scheduleId,
                                                ClassScheduleRequest request) {

        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // update fields
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        // update staff if changed
        if (request.getStaffId() != null) {
            Staff staff = staffRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            schedule.setStaff(staff);
        }

        // update class if changed
        if (request.getClassId() != null) {
            ClassBatch batch = classBatchRepository.findById(request.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            schedule.setClassBatch(batch);
        }

        ClassSchedule updated = classScheduleRepository.save(schedule);
        return classScheduleMapper.toResponse(updated);
    }

    @Override
    public ClassScheduleResponse cancelSchedule(Long scheduleId) {

        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setStatus(ClassStatus.CANCELLED);

        ClassSchedule updated = classScheduleRepository.save(schedule);
        return classScheduleMapper.toResponse(updated);
    }

}


