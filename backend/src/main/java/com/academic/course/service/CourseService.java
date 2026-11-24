package com.academic.course.service;

import com.academic.course.dto.CourseRequest;
import com.academic.course.dto.CourseResponse;
import com.academic.course.entity.Course;
import com.academic.course.repository.CourseRepository;
import com.academic.teacher.api.TeacherApi;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.academic.course.api.CourseApi;
import com.academic.course.api.CourseInfo;
import org.springframework.transaction.annotation.Propagation;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourseService implements CourseApi {

    private final CourseRepository courseRepository;
    private final TeacherApi teacherApi;

    public CourseService(
            CourseRepository courseRepository,
            TeacherApi teacherApi) {

        this.courseRepository = courseRepository;
        this.teacherApi = teacherApi;
    }

    // CREATE
    public CourseResponse create(CourseRequest request) {

        if (courseRepository.existsByCourseCode(
                request.courseCode())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Course code already exists"
            );
        }

        validateTeacher(request.teacherId());

        Course course = new Course();

        updateFields(course, request);

        return toResponse(courseRepository.save(course));
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {

        return courseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public CourseResponse findById(UUID id) {

        return toResponse(getCourse(id));
    }

    // FIND BY TEACHER
    @Transactional(readOnly = true)
    public List<CourseResponse> findByTeacher(UUID teacherId) {

        validateTeacher(teacherId);

        return courseRepository.findByTeacherId(teacherId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // FIND ACTIVE COURSES
    @Transactional(readOnly = true)
    public List<CourseResponse> findActiveCourses() {

        return courseRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // UPDATE
    public CourseResponse update(
            UUID id, CourseRequest request) {

        Course course = courseRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"
                ));

        if (courseRepository.existsByCourseCodeAndIdNot(
                request.courseCode(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Course code already exists"
            );
        }

        validateTeacher(request.teacherId());

        updateFields(course, request);

        return toResponse(courseRepository.save(course));
    }

    public void delete(UUID id) {

        Course course = getCourse(id);

        try {

            courseRepository.delete(course);
            courseRepository.flush();

        } catch (DataIntegrityViolationException exception) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete course because related data exists",
                    exception
            );
        }
    }

    // Validate teacher through public contract
    private void validateTeacher(UUID teacherId) {

        if (!teacherApi.existsById(teacherId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Teacher not found"
            );
        }
    }

    // Find course or throw exception
    private Course getCourse(UUID id) {

        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"
                ));
    }

    // Map request to entity
    private void updateFields(

            Course course, CourseRequest request) {
        if (request.capacity() < course.getCapacity()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Course capacity cannot be reduced"
            );
        }
        course.setCourseCode(request.courseCode());
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCredits(request.credits());
        course.setCapacity(request.capacity());
        course.setTeacherId(request.teacherId());
        course.setActive(request.active());
    }

    // Map entity to response
    private CourseResponse toResponse(Course course) {

        return new CourseResponse(
                course.getId(),
                course.getCourseCode(),
                course.getTitle(),
                course.getDescription(),
                course.getCredits(),
                course.getCapacity(),
                course.getTeacherId(),
                course.getActive()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID courseId) {

        return courseRepository.existsById(courseId);
    }
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public CourseInfo getCourseForEnrollment(UUID courseId) {

        Course course = courseRepository
                .findByIdForUpdate(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found"
                ));

        return new CourseInfo(
                course.getId(),
                course.getCapacity(),
                course.getActive()
        );
    }

}