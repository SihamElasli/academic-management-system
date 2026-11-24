package com.academic.enrollment.dto;

import com.academic.enrollment.entity.EnrollmentStatus;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentResponse(

        UUID id,
        UUID studentId,
        UUID courseId,
        Instant enrollmentDate,
        EnrollmentStatus status

) {
}