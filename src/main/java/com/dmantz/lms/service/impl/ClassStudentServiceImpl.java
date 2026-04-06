package com.dmantz.lms.service.impl;

import com.dmantz.lms.dto.request.EnrollStudentRequest;
import com.dmantz.lms.dto.response.EnrollStudentResponse;
import com.dmantz.lms.entity.ClassBatch;
import com.dmantz.lms.entity.ClassStudent;
import com.dmantz.lms.entity.ClassStudentStatus;
import com.dmantz.lms.entity.Student;
import com.dmantz.lms.mapper.ClassStudentMapper;
import com.dmantz.lms.repository.ClassBatchRepository;
import com.dmantz.lms.repository.ClassStudentRepository;
import com.dmantz.lms.repository.StudentRepository;
import com.dmantz.lms.service.ClassStudentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassStudentServiceImpl implements ClassStudentService {

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
        ClassBatch classBatch = classBatchRepository.findById(request.getClassBatchId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        List<Student> students = new ArrayList<>();

        // Self Enrollment
        if (request.isSelfEnroll()) {

            String studentId = request.getStudentId();

            if (studentId == null || studentId.isBlank()) {
                throw new RuntimeException("StudentId required for self enroll");
            }

            Student student = studentRepository.findByStudentId(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            students.add(student);

        } else {

            //  Staff Enrollment
            if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
                throw new RuntimeException("StudentIds required for staff enroll");
            }

            students = studentRepository.findByStudentIdIn(request.getStudentIds());

            if (students.isEmpty()) {
                throw new RuntimeException("No valid students found");
            }
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
            throw new RuntimeException("All students already enrolled");
        }

        List<ClassStudent> saved = classStudentRepository.saveAll(mappings);

        return saved.stream()
                .map(mapper::toDto)
                .toList();
    }
}
