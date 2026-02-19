package com.dmantz.lms_b.service.impl;

import com.dmantz.lms_b.dto.request.CreateClassRequest;
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
    private final ClassBatchMapper mapper;

    public ClassAdminServiceImpl(CourseRepository courseRepository, ClassBatchRepository classBatchRepository, ClassBatchMapper mapper) {
        this.courseRepository = courseRepository;
        this.classBatchRepository = classBatchRepository;
        this.mapper = mapper;
    }

    @Override
    public ClassResponse addClass(Long courseId, CreateClassRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Validate dates
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        // Convert Request → Entity
        ClassBatch batch = mapper.toEntity(request);
        batch.setCourse(course);

        // Save
        classBatchRepository.save(batch);

        // Convert Entity → Response
        return mapper.toResponse(batch);
    }
}


