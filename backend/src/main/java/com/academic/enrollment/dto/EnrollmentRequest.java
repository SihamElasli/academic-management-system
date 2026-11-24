package com.academic.enrollment.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnrollmentRequest(

        @NotNull(message = "Student ID is required")
        UUID studentId,

        @NotNull(message = "Course ID is required")
        UUID courseId

) {
}