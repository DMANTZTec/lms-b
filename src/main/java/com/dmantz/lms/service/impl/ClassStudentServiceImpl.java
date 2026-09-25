package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.EnrollStudentRequest;
import com.dmantz.lms.dto.request.RemoveStudentRequest;
import com.dmantz.lms.dto.response.EnrollStudentResponse;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassStudent;
import com.dmantz.lms.entity.ClassStudentStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.exceptions.StudentNotFoundException;
import com.dmantz.lms.mapper.ClassStudentMapper;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.ClassStudentRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.ClassStudentService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassStudentServiceImpl implements ClassStudentService {
	
	 private static final Logger logger = LogManager.getLogger(ClassStudentServiceImpl.class);
	

    private final ClassBatchRepository classBatchRepository;
    private final StudentRepository studentRepository;
    private final ClassStudentRepository classStudentRepository;
    private final ClassStudentMapper mapper;

    public ClassStudentServiceImpl(ClassBatchRepository classBatchRepository, StudentRepository studentRepository, ClassStudentRepository classStudentRepository, ClassStudentMapper mapper) {
        this.classBatchRepository = classBatchRepository;
        this.studentRepository = studentRepository;
        this.classStudentRepository = classStudentRepository;
        this.mapper = mapper;
    }

    @Override
    public List<EnrollStudentResponse> enrollStudents(EnrollStudentRequest request) {

        // Validate class
        logger.info("Enrolling student(s) into classBatchId: {} — selfEnroll: {}",
                request.getClassBatchId(), request.isSelfEnroll());

        ClassBatch classBatch = classBatchRepository.findById(request.getClassBatchId())
                .orElseThrow(() -> {
                    logger.warn("ClassBatch not found with id: {} during enrollStudents", request.getClassBatchId());
                    return new ResourceNotFoundException("Class not found with id: " + request.getClassBatchId());
                });

        List<Student> students = new ArrayList<>();

        // Self Enrollment
        if (request.isSelfEnroll()) {

            String studentId = request.getStudentId();

            if (studentId == null || studentId.isBlank()) {
                logger.warn("Self-enroll attempted without studentId for classBatchId: {}", request.getClassBatchId());
                throw new IllegalArgumentException("StudentId required for self enroll");
            }

            Student student = studentRepository.findByStudentId(studentId)
                    .orElseThrow(() -> {
                        logger.warn("Student not found with id: {} during self-enroll", studentId);
                        return new StudentNotFoundException("Student not found with id: " + studentId);
                    });

            students.add(student);
            logger.debug("Self-enroll: student {} resolved for classBatchId: {}", studentId, request.getClassBatchId());

        } else {

            //  Staff Enrollment
        	if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
                logger.warn("Staff-enroll attempted with empty studentIds for classBatchId: {}", request.getClassBatchId());
                throw new IllegalArgumentException("StudentIds required for staff enroll");
            }

            students = studentRepository.findByStudentIdIn(request.getStudentIds());
            if (students.isEmpty()) {
                logger.warn("No valid students found for provided studentIds during staff-enroll in classBatchId: {}",
                        request.getClassBatchId());
                throw new StudentNotFoundException("No valid students found for the provided IDs");
            }

            logger.debug("Staff-enroll: {} valid student(s) resolved for classBatchId: {}",
                    students.size(), request.getClassBatchId());
        }

        List<ClassStudent> mappings = new ArrayList<>();

        for (Student student : students) {

            //  Prevent duplicate enrollment
            boolean alreadyExists = classStudentRepository
                    .existsByClassBatchIdAndStudentId(
                            classBatch.getId(),
                            student.getId()   // NOTE: DB PK used here
                    );

            if (alreadyExists) {
            	 logger.debug("Student {} already enrolled in classBatchId: {}, skipping",
                         student.getStudentId(), classBatch.getId());
                continue; // skip duplicate
            }

            ClassStudent cs = new ClassStudent();
            cs.setClassBatch(classBatch);
            cs.setStudent(student);
            cs.setStartDate(LocalDate.now());
            cs.setStatus(ClassStudentStatus.ENROLLED);
            cs.setCreatedBy(1L);
            cs.setEnrolledDate(LocalDate.now());

            mappings.add(cs);
        }
        if (mappings.isEmpty()) {
            logger.warn("All students already enrolled in classBatchId: {}", classBatch.getId());
            throw new IllegalStateException("All students already enrolled");
        }

        List<ClassStudent> saved = classStudentRepository.saveAll(mappings);

        logger.info("Enrollment complete for classBatchId: {} — {} student(s) enrolled",
                classBatch.getId(), saved.size());

        return saved.stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    public List<String> removeStudents(RemoveStudentRequest request) {

        // Validate class
    	 logger.info("Removing student(s) from classBatchId: {} — selfRemove: {}",
                 request.getClassBatchId(), request.isSelfRemove());

         // Validate class
         ClassBatch classBatch = classBatchRepository.findById(request.getClassBatchId())
                 .orElseThrow(() -> {
                     logger.warn("ClassBatch not found with id: {} during removeStudents", request.getClassBatchId());
                     return new ResourceNotFoundException("Class not found with id: " + request.getClassBatchId());
                 });

        List<Student> students = new ArrayList<>();

        // Self Remove
        if (request.isSelfRemove()) {

            String studentId = request.getStudentId();

            if (studentId == null || studentId.isBlank()) {
                logger.warn("Self-remove attempted without studentId for classBatchId: {}", request.getClassBatchId());
                throw new IllegalArgumentException("StudentId required for self remove");
            }

            Student student = studentRepository.findByStudentId(studentId)
                    .orElseThrow(() -> {
                        logger.warn("Student not found with id: {} during self-remove", studentId);
                        return new StudentNotFoundException("Student not found with id: " + studentId);
                    });
            students.add(student);
            logger.debug("Self-remove: student {} resolved for classBatchId: {}", studentId, request.getClassBatchId());


        } else {

            // Staff Remove
        	 if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
                 logger.warn("Staff-remove attempted with empty studentIds for classBatchId: {}", request.getClassBatchId());
                 throw new IllegalArgumentException("StudentIds required for staff remove");
             }

            students = studentRepository.findByStudentIdIn(request.getStudentIds());

            if (students.isEmpty()) {
                logger.warn("No valid students found for provided studentIds during staff-remove in classBatchId: {}",
                        request.getClassBatchId());
                throw new StudentNotFoundException("No valid students found for the provided IDs");
            }

            logger.debug("Staff-remove: {} valid student(s) resolved for classBatchId: {}",
                    students.size(), request.getClassBatchId());
        }

        List<String> removedStudents = new ArrayList<>();

        for (Student student : students) {

            ClassStudent mapping = classStudentRepository
                    .findByClassBatchIdAndStudent_StudentId(
                            classBatch.getId(),
                            student.getStudentId()
                    )
                    .orElse(null);

            if (mapping == null) {
            	 logger.debug("Student {} not enrolled in classBatchId: {}, skipping",
                         student.getStudentId(), classBatch.getId());
                continue; // not enrolled
            }

            //  Hard Delete
            classStudentRepository.delete(mapping);

            //  (Recommended): Soft Delete / Status Update
            // mapping.setStatus(ClassStudentStatus.REMOVED);
            // classStudentRepository.save(mapping);

            removedStudents.add(student.getStudentId());
            logger.debug("Student {} removed from classBatchId: {}", student.getStudentId(), classBatch.getId());
        }

        if (removedStudents.isEmpty()) {
            logger.warn("No students removed from classBatchId: {} — none were enrolled", classBatch.getId());
            throw new ResourceNotFoundException("No students removed (not enrolled)");
        }

        logger.info("Removal complete for classBatchId: {} — {} student(s) removed",
                classBatch.getId(), removedStudents.size());


        return removedStudents;
    }

}
