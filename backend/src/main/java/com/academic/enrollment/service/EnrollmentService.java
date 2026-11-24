package com.academic.enrollment.service;

import com.academic.enrollment.dto.EnrollmentRequest;
import com.academic.enrollment.dto.EnrollmentResponse;
import com.academic.enrollment.entity.Enrollment;
import com.academic.enrollment.entity.EnrollmentStatus;
import com.academic.enrollment.repository.EnrollmentRepository;

import com.academic.student.api.StudentApi;
import com.academic.course.api.CourseApi;
import com.academic.course.api.CourseInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentApi studentApi;
    private final CourseApi courseApi;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentApi studentApi,
            CourseApi courseApi) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentApi = studentApi;
        this.courseApi = courseApi;
    }

    // CREATE ENROLLMENT
    public EnrollmentResponse create(
            EnrollmentRequest request) {

        // 1. Validate student
        validateStudent(request.studentId());

        // 2. Get and lock the course
        CourseInfo course = courseApi.getCourseForEnrollment(
                request.courseId()
        );

        // 3. Validate course status
        if (!Boolean.TRUE.equals(course.active())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Course is not active"
            );
        }

        // 4. Check existing enrollment
        Optional<Enrollment> existingEnrollment =
                enrollmentRepository.findByStudentIdAndCourseId(
                        request.studentId(),
                        request.courseId()
                );

        if (existingEnrollment.isPresent()
                && existingEnrollment.get().getStatus()
                == EnrollmentStatus.ENROLLED) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Student is already enrolled in this course"
            );
        }

        // 5. Check course capacity
        long enrolledStudents =
                enrollmentRepository.countByCourseIdAndStatus(
                        request.courseId(),
                        EnrollmentStatus.ENROLLED
                );

        if (enrolledStudents >= course.capacity()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Course capacity reached"
            );
        }

        // 6. Create or reactivate enrollment
        Enrollment enrollment = existingEnrollment
                .orElseGet(Enrollment::new);

        enrollment.setStudentId(request.studentId());
        enrollment.setCourseId(request.courseId());
        enrollment.setEnrollmentDate(Instant.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        // 7. Save enrollment
        return toResponse(
                enrollmentRepository.save(enrollment)
        );
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findAll() {

        return enrollmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public EnrollmentResponse findById(UUID id) {

        return toResponse(getEnrollment(id));
    }

    // FIND BY STUDENT
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudent(
            UUID studentId) {

        validateStudent(studentId);

        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // FIND BY COURSE
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByCourse(
            UUID courseId) {

        if (!courseApi.existsById(courseId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Course not found"
            );
        }

        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // FIND BY STATUS
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStatus(
            EnrollmentStatus status) {

        return enrollmentRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // CANCEL ENROLLMENT
    public EnrollmentResponse cancel(UUID id) {

        Enrollment enrollment = getEnrollment(id);

        // Lock the course before changing enrollment status
        courseApi.getCourseForEnrollment(
                enrollment.getCourseId()
        );

        // Refresh state after acquiring the lock
        // See the concurrency adjustment below.

        if (enrollment.getStatus()
                == EnrollmentStatus.CANCELLED) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Enrollment is already cancelled"
            );
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);

        return toResponse(
                enrollmentRepository.save(enrollment)
        );
    }

    // VALIDATE STUDENT
    private void validateStudent(UUID studentId) {

        if (!studentApi.existsById(studentId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found"
            );
        }
    }

    // FIND ENROLLMENT
    private Enrollment getEnrollment(UUID id) {

        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enrollment not found"
                ));
    }

    // MAP ENTITY TO RESPONSE
    private EnrollmentResponse toResponse(
            Enrollment enrollment) {

        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus()
        );
    }
}