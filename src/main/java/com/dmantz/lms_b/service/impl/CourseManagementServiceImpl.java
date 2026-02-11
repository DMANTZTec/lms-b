package com.dmantz.lms_b.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dmantz.lms_b.dto.request.ChapterRequest;
import com.dmantz.lms_b.dto.request.CourseRequest;
import com.dmantz.lms_b.dto.request.SubjectRequest;
import com.dmantz.lms_b.dto.response.ChapterResponse;
import com.dmantz.lms_b.dto.response.CourseResponse;
import com.dmantz.lms_b.dto.response.SubjectResponse;
import com.dmantz.lms_b.entity.Chapter;
import com.dmantz.lms_b.entity.Course;
import com.dmantz.lms_b.entity.Provider;
import com.dmantz.lms_b.entity.Staff;
import com.dmantz.lms_b.entity.Subject;
import com.dmantz.lms_b.exceptions.DuplicateValuesException;
import com.dmantz.lms_b.exceptions.ResourceNotFoundException;
import com.dmantz.lms_b.mapper.ChapterMapper;
import com.dmantz.lms_b.mapper.CourseMapper;
import com.dmantz.lms_b.mapper.SubjectMapper;
import com.dmantz.lms_b.repository.ChapterRepository;
import com.dmantz.lms_b.repository.CourseRepository;
import com.dmantz.lms_b.repository.ProviderRepository;
import com.dmantz.lms_b.repository.StaffRepository;
import com.dmantz.lms_b.repository.SubjectRepository;
import com.dmantz.lms_b.service.CourseManagementService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseManagementServiceImpl implements CourseManagementService {

	private final SubjectRepository subjectRepository;
	private final StaffRepository staffRepository;
	private final SubjectMapper subjectMapper;

	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
	private final ProviderRepository providerRepository;

	private final ChapterRepository chapterRepository;
	private final ChapterMapper chapterMapper;

	public CourseManagementServiceImpl(SubjectRepository subjectRepository, StaffRepository staffRepository,
			SubjectMapper subjectMapper, CourseRepository courseRepository, CourseMapper courseMapper,
			ProviderRepository providerRepository, ChapterRepository chapterRepository, ChapterMapper chapterMapper) {
		super();
		this.subjectRepository = subjectRepository;
		this.staffRepository = staffRepository;
		this.subjectMapper = subjectMapper;
		this.courseRepository = courseRepository;
		this.courseMapper = courseMapper;
		this.providerRepository = providerRepository;
		this.chapterRepository = chapterRepository;
		this.chapterMapper = chapterMapper;
	}

	// ------------------ CREATE SUBJECT ------------------
	@Override
	public SubjectResponse createSubject(SubjectRequest requestDto, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Check duplicate short code
		subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd()).ifPresent(existing -> {
			throw new DuplicateValuesException("Subject already exists with code: " + requestDto.getSubjectShortCd());
		});

		Subject subject = subjectMapper.toEntity(requestDto);
		subject.setCreatedBy(staffId);
		subject.setCreatedDt(LocalDateTime.now());

		Subject savedSubject = subjectRepository.save(subject);
		return subjectMapper.toDto(savedSubject);
	}

	// ------------------ VIEW ALL SUBJECTS ------------------
	@Override
	public List<SubjectResponse> viewAllSubjects() {
		return subjectRepository.findAll().stream().map(subjectMapper::toDto).collect(Collectors.toList());
	}

	// ------------------ UPDATE SUBJECT ------------------
	@Override
	public SubjectResponse updateSubject(Long subjectId, SubjectRequest requestDto, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch subject
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

		// Check duplicate short code (excluding same subject)
		subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd()).ifPresent(existing -> {
			if (!existing.getId().equals(subjectId)) {
				throw new DuplicateValuesException(
						"Another subject already exists with code: " + requestDto.getSubjectShortCd());
			}
		});

		// Update fields using MapStruct
		subjectMapper.updateSubjectFromRequest(requestDto, subject);

		subject.setUpdatedBy(staffId);
		subject.setUpdatedDt(LocalDateTime.now());

		Subject updatedSubject = subjectRepository.save(subject);
		return subjectMapper.toDto(updatedSubject);
	}

	// -------------------------DELETE SUBJECT-----------------------------
	@Override
	public void deleteSubject(Long subjectId, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff not found with id: " + staffId);
		}

		// Fetch subject
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
		subjectRepository.delete(subject);
	}

//  ----------------------------CREATE COURSE--------------------
	@Override
	public CourseResponse createCourse(CourseRequest requestDto, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch Subject
		Subject subject = subjectRepository.findById(requestDto.getSubjectId()).orElseThrow(
				() -> new ResourceNotFoundException("Subject not found with ID: " + requestDto.getSubjectId()));

		// Fetch Provider
		Provider provider = providerRepository.findById(requestDto.getProviderId()).orElseThrow(
				() -> new ResourceNotFoundException("Provider not found with ID: " + requestDto.getProviderId()));

		// Check duplicate
		boolean exists = courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
				requestDto.getCourseTitle(), subject.getId(), provider.getId(), requestDto.getLanguage());

		if (exists) {
			throw new DuplicateValuesException("Course already exists for this subject, provider, and language");
		}

		// Generate courseId
		String courseId = generateCourseId(subject);

		// Map request DTO → entity
		Course course = courseMapper.toEntity(requestDto);
		course.setCourseId(courseId);
		course.setSubject(subject);
		course.setProvider(provider);

		// Set audit fields
		course.setCreatedBy(staffId);
		course.setCreatedDt(LocalDateTime.now());

		// Save
		Course savedCourse = courseRepository.save(course);

		// Return response
		return courseMapper.toDto(savedCourse);
	}

	private String generateCourseId(Subject subject) {

		String subjectShortCd = subject.getSubjectShortCd();

		if (subjectShortCd == null || subjectShortCd.isBlank()) {
			throw new IllegalStateException("Subject short code is required");
		}

		long count = courseRepository.countBySubject_SubjectShortCd(subjectShortCd) + 1;

		return String.format("%s%03d", subjectShortCd.toUpperCase(), count);
	}

	// ------------------ VIEW ALL COURSES ------------------
	@Override
	public List<CourseResponse> viewAllCourses() {
		return courseRepository.findAll().stream().map(courseMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public CourseResponse updateCourse(Long courseId, CourseRequest request, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch course
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

		// Fetch Subject
		Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(
				() -> new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId()));

		// Fetch Provider
		Provider provider = providerRepository.findById(request.getProviderId()).orElseThrow(
				() -> new ResourceNotFoundException("Provider not found with id: " + request.getProviderId()));

		// Check duplicate course
		boolean exists = courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
				request.getCourseTitle(), subject.getId(), provider.getId(), request.getLanguage());

		if (exists && !(course.getCourseTitle().equals(request.getCourseTitle())
				&& course.getSubject().getId().equals(subject.getId())
				&& course.getProvider().getId().equals(provider.getId())
				&& course.getLanguage().equals(request.getLanguage()))) {

			throw new DuplicateValuesException(
					"Another course already exists with same title, subject, provider and language");
		}

		// Update fields using MapStruct
		courseMapper.updateCourseFromRequest(request, course);

		// Update relations
		course.setSubject(subject);
		course.setProvider(provider);

		// Audit fields
		course.setUpdatedBy(staffId);
		course.setUpdatedDt(LocalDateTime.now());

		Course updatedCourse = courseRepository.save(course);
		return courseMapper.toDto(updatedCourse);
	}

	@Override
	public void deleteCourse(Long courseId, Long staffId) {

		// Validate staff
		if (!staffRepository.existsById(staffId)) {
			throw new ResourceNotFoundException("Staff not found with id: " + staffId);
		}

		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

		courseRepository.delete(course);
	}

	@Override
	public List<CourseResponse> viewCoursesBySubject(Long subjectId) {
		// Validate subject exists
		if (!subjectRepository.existsById(subjectId)) {
			throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
		}

		return courseRepository.findBySubject_Id(subjectId).stream().map(courseMapper::toDto)
				.collect(Collectors.toList());
	}

	// ================= CREATE CHAPTER=================
	@Override
	public ChapterResponse createChapter(Long staffId, ChapterRequest request) {

		Staff staff = staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Course course = courseRepository.findByCourseId(request.getCourseId())
				.orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

		chapterRepository.findByCourse_CourseIdAndChapterNmIgnoreCase(request.getCourseId(), request.getChapterNm())
				.ifPresent(ch -> {
					throw new DuplicateValuesException("Chapter name already exists in this course");
				});

		Long nextChapterNum = chapterRepository.findTopByCourse_CourseIdOrderByChapterNumDesc(request.getCourseId())
				.map(ch -> ch.getChapterNum() + 1).orElse(1L);
		Chapter chapter = chapterMapper.toEntity(request);
		chapter.setCourse(course); // ✅ managed entity
		chapter.setChapterNum(nextChapterNum);
		chapter.setCreatedBy(staff.getId());
		chapter.setCreatedDt(LocalDateTime.now());

		Chapter savedChapter = chapterRepository.save(chapter);

		return chapterMapper.toResponse(savedChapter);
	}

	// ================= GET CHAPTER BY ID =================
	@Override
	public ChapterResponse getChapterById(Long chapterId) {
		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		return chapterMapper.toResponse(chapter);
	}

	// ================= GET ALL CHAPTERS =================
	@Override
	public List<ChapterResponse> getAllChapters() {
		return chapterRepository.findAll().stream().map(chapterMapper::toResponse).toList();
	}

	// ================= UPDATE CHAPTER =================
	@Override
	public ChapterResponse updateChapter(Long chapterId, ChapterRequest request, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + chapterId));

		Course course = null;
		if (request.getCourseId() != null) {
			course = courseRepository.findByCourseId(request.getCourseId()).orElseThrow(
					() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
		}

		String courseIdToCheck = (course != null) ? course.getCourseId() : chapter.getCourse().getCourseId();

		chapterRepository.findByCourse_CourseIdAndChapterNmIgnoreCase(courseIdToCheck, request.getChapterNm())
				.filter(ch -> !ch.getId().equals(chapterId)).ifPresent(ch -> {
					throw new DuplicateValuesException("Chapter name already exists in this course");
				});

		chapterMapper.updateEntityFromRequest(request, chapter);
		chapter.setUpdatedBy(staffId);
		chapter.setUpdatedDt(LocalDateTime.now());
		Chapter updatedChapter = chapterRepository.save(chapter);

		return chapterMapper.toResponse(updatedChapter);
	}

	// ================= DELETE CHAPTER=================
	@Override
	public void deleteChapter(Long chapterId, Long staffId) {

		staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id: " + chapterId));

		chapterRepository.delete(chapter);
	}

}
