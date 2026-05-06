package com.dmantz.lms.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dmantz.lms.dto.request.*;
import com.dmantz.lms.dto.response.*;
import com.dmantz.lms.entity.*;
import com.dmantz.lms.mapper.*;
import com.dmantz.lms.repository.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dmantz.lms.exceptions.DuplicateValuesException;
import com.dmantz.lms.exceptions.ResourceNotFoundException;
import com.dmantz.lms.service.CourseManagementService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseManagementServiceImpl implements CourseManagementService {

	private static final Logger logger = LogManager.getLogger(CourseManagementServiceImpl.class);

	private final SubjectRepository subjectRepository;
	private final StaffRepository staffRepository;
	private final SubjectMapper subjectMapper;

	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
	private final ProviderRepository providerRepository;

	private final ChapterRepository chapterRepository;
	private final ChapterMapper chapterMapper;

	private final TopicRepository topicRepository;
	private final TopicMapper topicMapper;

	private final TopicReferenceRepository topicReferenceRepository;
	private final TopicReferenceMapper topicReferenceMapper;

	private final ProgramRepository programRepository;
	private final ProgramCourseRepository programCourseRepository;
	private final ProgramCourseMapper programcourseMapper;
	private final ProgramMapper programMapper;

	public CourseManagementServiceImpl(SubjectRepository subjectRepository, StaffRepository staffRepository,
			SubjectMapper subjectMapper, CourseRepository courseRepository, CourseMapper courseMapper,
			ProviderRepository providerRepository, ChapterRepository chapterRepository, ChapterMapper chapterMapper,
			TopicRepository topicRepository, TopicMapper topicMapper, TopicReferenceRepository topicReferenceRepository,
			TopicReferenceMapper topicReferenceMapper, ProgramRepository programRepository,
			ProgramCourseRepository programCourseRepository, ProgramCourseMapper programcourseMapper,
			ProgramMapper programMapper) {
		super();
		this.subjectRepository = subjectRepository;
		this.staffRepository = staffRepository;
		this.subjectMapper = subjectMapper;
		this.courseRepository = courseRepository;
		this.courseMapper = courseMapper;
		this.providerRepository = providerRepository;
		this.chapterRepository = chapterRepository;
		this.chapterMapper = chapterMapper;
		this.topicRepository = topicRepository;
		this.topicMapper = topicMapper;
		this.topicReferenceRepository = topicReferenceRepository;
		this.topicReferenceMapper = topicReferenceMapper;
		this.programRepository = programRepository;
		this.programCourseRepository = programCourseRepository;
		this.programcourseMapper = programcourseMapper;
		this.programMapper = programMapper;
	}

	// ------------------ CREATE SUBJECT ------------------
	@Override
	public SubjectResponse createSubject(SubjectRequest requestDto, String staffId) {

		logger.info("Creating subject with shortCode: {} by staffId: {}", requestDto.getSubjectShortCd(), staffId);

		// Validate staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.warn("Staff not found with id: {} during createSubject", staffId);
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Check duplicate short code
		subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd()).ifPresent(existing -> {
			logger.warn("Duplicate subject shortCode detected: {}", requestDto.getSubjectShortCd());
			throw new DuplicateValuesException("Subject already exists with code: " + requestDto.getSubjectShortCd());
		});

		Subject subject = subjectMapper.toEntity(requestDto);

		Subject savedSubject = subjectRepository.save(subject);

		logger.info("Subject created successfully with id: {}, shortCode: {}", savedSubject.getId(),
				savedSubject.getSubjectShortCd());
		return subjectMapper.toDto(savedSubject);
	}

	// ------------------ VIEW ALL SUBJECTS ------------------
	@Override
	public List<SubjectResponse> viewAllSubjects() {
		logger.info("Fetching all subjects");
		List<SubjectResponse> subjects = subjectRepository.findAll().stream().map(subjectMapper::toDto)
				.collect(Collectors.toList());
		logger.debug("Total subjects found: {}", subjects.size());
		return subjects;
	}

	// ------------------ UPDATE SUBJECT ------------------
	@Override
	public SubjectResponse updateSubject(Long subjectId, SubjectRequest requestDto, String staffId) {

		logger.info("Updating subject with id: {} by staffId: {}", subjectId, staffId);

		// Validate staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.warn("Staff not found with id: {} during updateSubject", staffId);
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch subject
		Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
			logger.warn("Subject not found with id: {}", subjectId);
			return new ResourceNotFoundException("Subject not found with id: " + subjectId);
		});

		// Check duplicate short code (excluding same subject)
		subjectRepository.findBySubjectShortCd(requestDto.getSubjectShortCd()).ifPresent(existing -> {
			if (!existing.getId().equals(subjectId)) {
				logger.warn("Duplicate subject shortCode {} found for a different subject",
						requestDto.getSubjectShortCd());
				throw new DuplicateValuesException(
						"Another subject already exists with code: " + requestDto.getSubjectShortCd());
			}
		});

		// Update fields using MapStruct
		subjectMapper.updateSubjectFromRequest(requestDto, subject);
		Subject updatedSubject = subjectRepository.save(subject);
		logger.info("Subject updated successfully with id: {}", updatedSubject.getId());
		return subjectMapper.toDto(updatedSubject);
	}

	// -------------------------DELETE SUBJECT-----------------------------
	@Override
	public void deleteSubject(Long subjectId, String staffId) {

		logger.info("Deleting subject with id: {} by staffId: {}", subjectId, staffId);
		// Validate staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.warn("Staff not found with id: {} during deleteSubject", staffId);
			throw new ResourceNotFoundException("Staff not found with id: " + staffId);
		}

		// Fetch subject
		Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> {
			logger.warn("Subject not found with id: {} for deletion", subjectId);
			return new ResourceNotFoundException("Subject not found with id: " + subjectId);
		});

		subjectRepository.delete(subject);
		logger.info("Subject deleted successfully with id: {}", subjectId);
	}

//  ----------------------------CREATE COURSE--------------------
	@Override
	public CourseResponse createCourse(CourseRequest requestDto, String staffId) {

		logger.info("Creating course: {} by staffId: {}", requestDto.getCourseTitle(), staffId);

		// Validate staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.warn("Staff not found with id: {} during createCourse", staffId);
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch Subject
		Subject subject = subjectRepository.findById(requestDto.getSubjectId()).orElseThrow(() -> {
			logger.warn("Subject not found with id: {}", requestDto.getSubjectId());
			return new ResourceNotFoundException("Subject not found with ID: " + requestDto.getSubjectId());
		});

		// Fetch Provider
		Provider provider = providerRepository.findById(requestDto.getProviderId()).orElseThrow(() -> {
			logger.warn("Provider not found with id: {}", requestDto.getProviderId());
			return new ResourceNotFoundException("Provider not found with ID: " + requestDto.getProviderId());
		});

		// Check duplicate
		boolean exists = courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
				requestDto.getCourseTitle(), subject.getId(), provider.getId(), requestDto.getLanguage());

		if (exists) {
			logger.warn("Duplicate course detected: title={}, subjectId={}, providerId={}, language={}",
					requestDto.getCourseTitle(), subject.getId(), provider.getId(), requestDto.getLanguage());
			throw new DuplicateValuesException("Course already exists for this subject, provider, and language");
		}

		// Generate courseId
		String courseId = generateCourseId(subject);
		logger.debug("Generated courseId: {} for subject: {}", courseId, subject.getSubjectShortCd());
		// Map request DTO → entity
		Course course = courseMapper.toEntity(requestDto);
		course.setCourseId(courseId);
		course.setSubject(subject);
		course.setProvider(provider);

		// Save
		Course savedCourse = courseRepository.save(course);
		logger.info("Course created successfully with courseId: {}", savedCourse.getCourseId());
		// Return response
		return courseMapper.toDto(savedCourse);
	}

	private String generateCourseId(Subject subject) {
		logger.debug("Generating courseId for subject shortCode: {}", subject.getSubjectShortCd());
		String subjectShortCd = subject.getSubjectShortCd();

		if (subjectShortCd == null || subjectShortCd.isBlank()) {
			logger.error("Subject short code is null or blank for subject id: {}", subject.getId());
			throw new IllegalStateException("Subject short code is required");
		}

		Optional<Course> lastCourse = courseRepository.findTopBySubject_SubjectShortCdOrderByIdDesc(subjectShortCd);

		int nextNumber = 1;

		if (lastCourse.isPresent() && lastCourse.get().getCourseId() != null) {
			String lastId = lastCourse.get().getCourseId();
			try {
				nextNumber = Integer.parseInt(lastId.substring(subjectShortCd.length())) + 1;
			} catch (NumberFormatException e) {
				logger.warn("Could not parse courseId suffix from {}, resetting counter to 1", lastId);
				nextNumber = 1;
			}
		}

		String generatedId;

		do {
			generatedId = String.format("%s%03d", subjectShortCd.toUpperCase(), nextNumber++);
		} while (courseRepository.existsByCourseId(generatedId));
		logger.debug("Final generated courseId: {}", generatedId);
		return generatedId;
	}

	// ------------------ VIEW ALL COURSES ------------------
	@Override
	public List<CourseResponse> viewAllCourses() {
		logger.info("Fetching all courses");
		List<CourseResponse> courses = courseRepository.findAll().stream().map(courseMapper::toDto)
				.collect(Collectors.toList());
		logger.debug("Total courses found: {}", courses.size());
		return courses;
	}

	@Override
	public CourseResponse updateCourse(Long courseId, CourseRequest request, String staffId) {

		logger.info("Updating course with id: {} by staffId: {}", courseId, staffId);
		// Validate staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.warn("Staff not found with id: {} during updateCourse", staffId);
			throw new ResourceNotFoundException("Staff with ID " + staffId + " does not exist");
		}

		// Fetch course
		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			logger.warn("Course not found with id: {}", courseId);
			return new ResourceNotFoundException("Course not found with id: " + courseId);
		});

		Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> {
			logger.warn("Subject not found with id: {}", request.getSubjectId());
			return new ResourceNotFoundException("Subject not found with id: " + request.getSubjectId());
		});

		Provider provider = providerRepository.findById(request.getProviderId()).orElseThrow(() -> {
			logger.warn("Provider not found with id: {}", request.getProviderId());
			return new ResourceNotFoundException("Provider not found with id: " + request.getProviderId());
		});

		// Check duplicate course
		boolean exists = courseRepository.existsByCourseTitleAndSubject_IdAndProvider_IdAndLanguage(
				request.getCourseTitle(), subject.getId(), provider.getId(), request.getLanguage());

		if (exists && !(course.getCourseTitle().equals(request.getCourseTitle())
				&& course.getSubject().getId().equals(subject.getId())
				&& course.getProvider().getId().equals(provider.getId())
				&& course.getLanguage().equals(request.getLanguage()))) {
			logger.warn("Duplicate course conflict on update for courseId: {}", courseId);
			throw new DuplicateValuesException(
					"Another course already exists with same title, subject, provider and language");
		}

		// Update fields using MapStruct
		courseMapper.updateCourseFromRequest(request, course);

		// Update relations
		course.setSubject(subject);
		course.setProvider(provider);

		Course updatedCourse = courseRepository.save(course);
		logger.info("Course updated successfully with id: {}", updatedCourse.getId());
		return courseMapper.toDto(updatedCourse);
	}

	@Override
	public void deleteCourse(Long courseId, String staffId) {
		logger.info("Deleting course with id: {} by staffId: {}", courseId, staffId);
		// Validate staff
		if (!staffRepository.existsByStaffId(staffId)) {
			logger.warn("Staff not found with id: {} during deleteCourse", staffId);
			throw new ResourceNotFoundException("Staff not found with id: " + staffId);
		}

		Course course = courseRepository.findById(courseId).orElseThrow(() -> {
			logger.warn("Course not found with id: {} for deletion", courseId);
			return new ResourceNotFoundException("Course not found with id: " + courseId);
		});

		courseRepository.delete(course);
		logger.info("Course deleted successfully with id: {}", courseId);
	}

	@Override
	public List<CourseResponse> viewCoursesBySubject(Long subjectId) {
		logger.info("Fetching courses for subjectId: {}", subjectId);
		// Validate subject exists
		if (!subjectRepository.existsById(subjectId)) {
			logger.warn("Subject not found with id: {}", subjectId);
			throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
		}

		List<CourseResponse> courses = courseRepository.findBySubject_Id(subjectId).stream().map(courseMapper::toDto)
				.collect(Collectors.toList());

		logger.debug("Found {} courses for subjectId: {}", courses.size(), subjectId);
		return courses;
	}

	// ================= CREATE CHAPTER=================
	@Override
	public ChapterResponse createChapter(String staffId, ChapterRequest request) {
		logger.info("Creating chapter: {} for courseId: {} by staffId: {}", request.getChapterNm(),
				request.getCourseId(), staffId);

		staffRepository.findByStaffId(staffId).orElseThrow(() -> {
			logger.warn("Staff not found with id: {} during createChapter", staffId);
			return new ResourceNotFoundException("Staff not found with id: " + staffId);
		});

		Course course = courseRepository.findByCourseId(request.getCourseId()).orElseThrow(() -> {
			logger.warn("Course not found with id: {}", request.getCourseId());
			return new ResourceNotFoundException("Course not found with id: " + request.getCourseId());
		});

		chapterRepository.findByCourse_CourseIdAndChapterNmIgnoreCase(request.getCourseId(), request.getChapterNm())
				.ifPresent(ch -> {
					logger.warn("Duplicate chapter name {} in courseId: {}", request.getChapterNm(),
							request.getCourseId());
					throw new DuplicateValuesException("Chapter name already exists in this course");
				});

		Long nextChapterNum = chapterRepository.findTopByCourse_CourseIdOrderByChapterNumDesc(request.getCourseId())
				.map(ch -> ch.getChapterNum() + 1).orElse(1L);

		logger.debug("Next chapter number for courseId {}: {}", request.getCourseId(), nextChapterNum);

		Chapter chapter = chapterMapper.toEntity(request);
		chapter.setCourse(course);
		chapter.setChapterNum(nextChapterNum);

		Chapter savedChapter = chapterRepository.save(chapter);
		logger.info("Chapter created with id: {}, num: {}", savedChapter.getId(), savedChapter.getChapterNum());

		return chapterMapper.toResponse(savedChapter);
	}

	// ================= GET CHAPTER BY ID =================
	@Override
	public ChapterResponse getChapterById(Long chapterId) {
		logger.info("Fetching chapter with id: {}", chapterId);

		Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {}", chapterId);
			return new ResourceNotFoundException("Chapter not found");
		});

		return chapterMapper.toResponse(chapter);
	}

	// ================= GET ALL CHAPTERS =================
	@Override
	public List<ChapterResponse> getAllChapters() {
		logger.info("Fetching all chapters");
		List<ChapterResponse> chapters = chapterRepository.findAll().stream().map(chapterMapper::toResponse).toList();
		logger.debug("Total chapters found: {}", chapters.size());
		return chapters;
	}

	// ================= UPDATE CHAPTER =================
	@Override
	public ChapterResponse updateChapter(Long chapterId, ChapterRequest request, String staffId) {

		logger.info("Updating chapter with id: {} by staffId: {}", chapterId, staffId);

		staffRepository.findByStaffId(staffId).orElseThrow(() -> {
			logger.warn("Staff not found with id: {} during updateChapter", staffId);
			return new ResourceNotFoundException("Staff not found");
		});

		Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {}", chapterId);
			return new ResourceNotFoundException("Chapter not found");
		});

		if (request.getChapterNm() != null) {
			chapterRepository.findByCourse_CourseIdAndChapterNmIgnoreCase(chapter.getCourse().getCourseId(),
					request.getChapterNm()).filter(ch -> !ch.getId().equals(chapterId)).ifPresent(ch -> {
						logger.warn("Duplicate chapter name {} found in courseId: {} on update", request.getChapterNm(),
								chapter.getCourse().getCourseId());
						throw new DuplicateValuesException("Chapter name already exists in this course");
					});
		}

		chapterMapper.updateEntityFromRequest(request, chapter);

		Chapter updated = chapterRepository.saveAndFlush(chapter);

		logger.info("Chapter updated successfully with id: {}", updated.getId());

		return chapterMapper.toResponse(updated);
	}

	// ================= DELETE CHAPTER=================
	@Override
	public void deleteChapter(Long chapterId, String staffId) {

		logger.info("Deleting chapter with id: {} by staffId: {}", chapterId, staffId);

		staffRepository.findByStaffId(staffId).orElseThrow(() -> {
			logger.warn("Staff not found with id: {} during deleteChapter", staffId);
			return new ResourceNotFoundException("Staff not found with id: " + staffId);
		});

		Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {} for deletion", chapterId);
			return new ResourceNotFoundException("Chapter not found with id: " + chapterId);
		});

		chapterRepository.delete(chapter);
		logger.info("Chapter deleted successfully with id: {}", chapterId);
	}

	// ================== CREATE A TOPIC ===================

	@Override
	public TopicResponseDto createTopic(TopicRequestDto request) {

		logger.info("Creating topic: {} in chapterId: {} by staffId: {}", request.getTopicName(),
				request.getChapterId(), request.getStaffId());

		Chapter chapter = chapterRepository.findById(request.getChapterId()).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {}", request.getChapterId());
			return new ResourceNotFoundException("Chapter not found with id: " + request.getChapterId());
		});

		Staff staff = staffRepository.findByStaffId(request.getStaffId()).orElseThrow(() -> {
			logger.warn("Staff not found with id: {}", request.getStaffId());
			return new ResourceNotFoundException("Staff not found with id: " + request.getStaffId());
		});

		boolean exists = topicRepository.existsByChapter_IdAndTopicNm(request.getChapterId(), request.getTopicName());

		if (exists) {
			logger.warn("Duplicate topic {} already exists in chapterId: {}", request.getTopicName(),
					request.getChapterId());
			throw new DuplicateValuesException("Topic already exists in this chapter");
		}

		Long maxTopicNum = topicRepository.findMaxTopicNumByChapterId(request.getChapterId());
		Long nextTopicNum = (maxTopicNum == null) ? 1L : maxTopicNum + 1;

		logger.debug("Next topic number for chapterId {}: {}", request.getChapterId(), nextTopicNum);

		Topic topic = topicMapper.toEntity(request);

		topic.setChapter(chapter);
		topic.setTopicNum(nextTopicNum);
		topic.setCreatedBy(staff.getId());
		topic.setUpdatedBy(staff.getId());

		Topic savedTopic = topicRepository.save(topic);

		logger.info("Topic created successfully with id: {}, topicNum: {}", savedTopic.getId(),
				savedTopic.getTopicNum());

		return topicMapper.toResponseDto(savedTopic);
	}
	// ================= Get all Topics by ChapterId ============================

	@Override
	public List<TopicResponseDto> getTopicsByChapterId(Long chapterId) {
		logger.info("Fetching topics for chapterId: {}", chapterId);
		chapterRepository.findById(chapterId).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {}", chapterId);
			return new ResourceNotFoundException("Chapter not found with id: " + chapterId);
		});

		List<Topic> topics = topicRepository.findByChapterIdOrderByTopicNumAsc(chapterId);
		logger.debug("Found {} topics for chapterId: {}", topics.size(), chapterId);
		return topics.stream().map(topicMapper::toResponseDto).collect(Collectors.toList());
	}

	// ====================== Get Topic by Id and Chapter Id
	// =========================

	@Override
	public TopicResponseDto getTopicByIdAndChapterId(Long topicId, Long chapterId) {

		logger.info("Fetching topic with id: {} in chapterId: {}", topicId, chapterId);

		chapterRepository.findById(chapterId).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {}", chapterId);
			return new ResourceNotFoundException("Chapter not found with id: " + chapterId);
		});

		Topic topic = topicRepository.findByIdAndChapterId(topicId, chapterId).orElseThrow(() -> {
			logger.warn("Topic id: {} not found in chapterId: {}", topicId, chapterId);
			return new ResourceNotFoundException("Topic not found in this chapter");
		});

		return topicMapper.toResponseDto(topic);
	}

	// ======================== Update Topic ==================================

	@Override
	public TopicResponseDto updateTopic(Long id, TopicRequestDto requestDto) {

		logger.info("Updating topic with id: {}", id);

		Topic topic = topicRepository.findById(id).orElseThrow(() -> {
			logger.warn("Topic not found with id: {}", id);
			return new ResourceNotFoundException("Topic not found");
		});

		Chapter chapter = chapterRepository.findById(requestDto.getChapterId()).orElseThrow(() -> {
			logger.warn("Chapter not found with id: {}", requestDto.getChapterId());
			return new ResourceNotFoundException("Chapter not found");
		});

		staffRepository.findByStaffId(requestDto.getStaffId()).orElseThrow(() -> {
			logger.warn("Staff not found with id: {}", requestDto.getStaffId());
			return new ResourceNotFoundException("Staff not found");
		});

		topic.setTopicNm(requestDto.getTopicName());
		topic.setDescription(requestDto.getDescription());
		topic.setExpectedTimeMin(requestDto.getExpectedTimeMin());
		topic.setChapter(chapter);

		Topic updatedTopic = topicRepository.save(topic);
		logger.info("Topic updated successfully with id: {}", updatedTopic.getId());
		return topicMapper.toResponseDto(updatedTopic);
	}

	// ============================= Delete Topic   ===================================

	@Override
	public void deleteTopic(Long id) {

		logger.info("Deleting topic with id: {}", id);

		Topic topic = topicRepository.findById(id).orElseThrow(() -> {
			logger.warn("Topic not found with id: {} for deletion", id);
			return new ResourceNotFoundException("Topic not found with id: " + id);
		});

		topicRepository.delete(topic);
		logger.info("Topic deleted successfully with id: {}", id);
	}
//      =====================  Move chapter ========================================

	@Override
	public void moveChapter(Long chapterId, int targetPosition) {

		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		Long courseId = chapter.getCourse().getId();

		List<Chapter> chapters = chapterRepository.findByCourseIdOrderByChapterNumAsc(courseId);

		int size = chapters.size();

		if (targetPosition < 1 || targetPosition > size) {
			throw new RuntimeException("Invalid target position");
		}

		// Remove old position
		chapters.removeIf(ch -> ch.getId().equals(chapterId));

		// Insert at new position
		chapters.add(targetPosition - 1, chapter);

		// 🔥 STEP 1 — Shift all values temporarily
		for (Chapter ch : chapters) {
			ch.setChapterNum(ch.getChapterNum() + 1000);
		}

		chapterRepository.saveAll(chapters);
		chapterRepository.flush();

		// 🔥 STEP 2 — Reassign correct order
		for (int i = 0; i < chapters.size(); i++) {
			chapters.get(i).setChapterNum((long) (i + 1));
		}

		chapterRepository.saveAll(chapters);
	}
//  =====================  Move topic ========================================

	@Override
	public void moveTopic(Long topicId, int targetPosition) {

		// 1️⃣ Fetch topic
		Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));

		Long chapterId = topic.getChapter().getId();

		// 2️⃣ Fetch all topics ordered
		List<Topic> topics = topicRepository.findByChapterIdOrderByTopicNumAsc(chapterId);

		int size = topics.size();

		if (targetPosition < 1 || targetPosition > size) {
			throw new RuntimeException("Invalid target position");
		}

		// 3️⃣ Remove current topic
		topics.removeIf(t -> t.getId().equals(topicId));

		// 4️⃣ Insert at new position
		topics.add(targetPosition - 1, topic);

		// 🔥 STEP 1: Temporary shift to avoid UNIQUE conflict
		for (Topic t : topics) {
			t.setTopicNum(t.getTopicNum() + 1000);
		}

		topicRepository.saveAll(topics);
		topicRepository.flush();

		// 🔥 STEP 2: Reassign correct order
		for (int i = 0; i < topics.size(); i++) {
			topics.get(i).setTopicNum((long) (i + 1));
		}

		topicRepository.saveAll(topics);
	}

// =============================== Add Topic References ======================================
	@Override
	public TopicReferenceResponseDto addUrlReference(Long topicId, TopicReferenceRequestDto dto) {

		return saveReference(topicId, dto, "URL");
	}

	@Override
	public TopicReferenceResponseDto addVideoReference(Long topicId, TopicReferenceRequestDto dto) {

		return saveReference(topicId, dto, "VIDEO");
	}

	@Override
	public TopicReferenceResponseDto addDocumentReference(Long topicId, TopicReferenceRequestDto dto) {

		return saveReference(topicId, dto, "DOCUMENT");
	}

	private TopicReferenceResponseDto saveReference(Long topicId, TopicReferenceRequestDto dto, String type) {

		Topic topic = topicRepository.findById(topicId)
				.orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

		TopicReference entity = topicReferenceMapper.toEntity(dto);
		entity.setRefType(type);
		entity.setTopic(topic);

		TopicReference saved = topicReferenceRepository.save(entity);

		return topicReferenceMapper.toDto(saved);
	}

//	  create program
	@Override
	public ProgramResponse createProgram(ProgramRequest request) {

		Provider provider = providerRepository.findById(request.getProviderId())
				.orElseThrow(() -> new RuntimeException("Provider not found"));
		if (programRepository.existsByProgramTitleAndProvider_Id(request.getProgramTitle(), request.getProviderId())) {
			throw new DuplicateValuesException("Program already exists for this provider");
		}

		Program program = programMapper.toEntity(request);

		program.setProvider(provider);

		program.setProgramId(generateProgramId());

		program.setStatus(ProgramStatus.ACTIVE);

		Program savedProgram = programRepository.save(program);

		return programMapper.toResponse(savedProgram);
	}

	private String generateProgramId() {

		Optional<Program> lastProgram = programRepository.findTopByOrderByIdDesc();

		int nextNumber = 1;

		if (lastProgram.isPresent() && lastProgram.get().getProgramId() != null) {
			String lastId = lastProgram.get().getProgramId();
			nextNumber = Integer.parseInt(lastId.substring(3)) + 1;
		}

		String generatedId;

		do {
			generatedId = String.format("PRG%03d", nextNumber++);
		} while (programRepository.existsByProgramId(generatedId));

		return generatedId;
	}

	@Override
	public ProgramResponse getProgramById(Long id) {
		Program program = programRepository.findByIdWithCourses(id)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + id));
		return programMapper.toResponse(program);
	}

	@Override
	public List<ProgramResponse> getAllPrograms() {
		return programRepository.findAllWithCourses().stream().map(programMapper::toResponse).toList();
	}

	// ================= UPDATE =================
	@Override
	public ProgramResponse updateProgram(Long programId, ProgramRequest request) {

		// Validate Program
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + programId));

		// Validate Provider
		Provider provider = providerRepository.findById(request.getProviderId()).orElseThrow(
				() -> new ResourceNotFoundException("Provider not found with id: " + request.getProviderId()));

		// Duplicate check
		if (programRepository.existsByProgramTitleAndProvider_IdAndIdNot(request.getProgramTitle(),
				request.getProviderId(), programId)) {
			throw new DuplicateValuesException("Program already exists for this provider");
		}

		// Update entity (excluding programId)
		programMapper.updateEntityFromRequest(request, program);
		program.setProvider(provider);

		Program updatedProgram = programRepository.save(program);

		return programMapper.toResponse(updatedProgram);
	}

	// ================= DELETE =================
	@Override
	public void deleteProgram(Long programId) {
		Program program = programRepository.findById(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + programId));

		programRepository.delete(program);
	}

//	========================= Add course to program =============================
	@Override
	public List<ProgramCourseResponse> addCoursesToProgram(ProgramCourseRequest request) {

		String programId = request.getProgramId();
		List<String> courseIds = request.getCourseIds();

		Program program = programRepository.findByProgramId(programId)
				.orElseThrow(() -> new ResourceNotFoundException("Program not found with ID: " + programId));

		List<ProgramCourse> savedList = new ArrayList<>();

		for (String courseId : courseIds) {

			Course course = courseRepository.findByCourseId(courseId)
					.orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

			boolean exists = programCourseRepository.existsByProgram_ProgramIdAndCourse_CourseId(programId, courseId);

			if (exists) {
				throw new DuplicateValuesException("Course " + courseId + " is already mapped to Program " + programId);
			}

			ProgramCourse programCourse = new ProgramCourse();
			programCourse.setProgram(program);
			programCourse.setCourse(course);

			savedList.add(programCourseRepository.save(programCourse));
		}

		return programcourseMapper.toResponseList(savedList);
	}

	// ========================= delete course from program
	@Override
	public void deleteProgramCourse(Long programCourseId) {

		ProgramCourse programCourse = programCourseRepository.findById(programCourseId).orElseThrow(
				() -> new ResourceNotFoundException("ProgramCourse not found with id: " + programCourseId));

		programCourseRepository.delete(programCourse);
	}

}
