package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
import com.dmantz.lms_b.dto.request.UpdateClassRequest;
import com.dmantz.lms_b.dto.response.ClassResponse;
import com.dmantz.lms_b.entity.ClassBatch;
import com.dmantz.lms_b.entity.Course;
import com.dmantz.lms_b.mapper.ClassBatchMapper;
import com.dmantz.lms_b.repository.ClassBatchRepository;
import com.dmantz.lms_b.repository.CourseRepository;
import com.dmantz.lms_b.service.ClassAdminService;
import org.springframework.stereotype.Service;

@Service
public class ClassAdminServiceImpl implements ClassAdminService {

    private final CourseRepository courseRepository;
    private final ClassBatchRepository classBatchRepository;
    private final ClassBatchMapper classBatchMapper;

    public ClassAdminServiceImpl(CourseRepository courseRepository, ClassBatchRepository classBatchRepository, ClassBatchMapper classBatchMapper) {
        this.courseRepository = courseRepository;
        this.classBatchRepository = classBatchRepository;
        this.classBatchMapper = classBatchMapper;
    }


    public ClassResponse addClass(String courseId, CreateClassRequest request) {

        // 🔥 Fetch course using business ID (SE001)
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Map request to entity
        ClassBatch classBatch = classBatchMapper.toEntity(request);

        // Set relation (VERY IMPORTANT)
        classBatch.setCourse(course);

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

}


