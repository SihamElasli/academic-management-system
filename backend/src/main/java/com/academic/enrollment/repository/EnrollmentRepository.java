package com.academic.enrollment.repository;

import com.academic.enrollment.entity.Enrollment;
import com.academic.enrollment.entity.EnrollmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, UUID> {

    Optional<Enrollment> findByStudentIdAndCourseId(
            UUID studentId,
            UUID courseId
    );

    List<Enrollment> findByStudentId(UUID studentId);

    List<Enrollment> findByCourseId(UUID courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    long countByCourseIdAndStatus(
            UUID courseId,
            EnrollmentStatus status
    );
}