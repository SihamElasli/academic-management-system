package com.academic.teacher.dto;

import java.util.UUID;

public record TeacherResponse(
        UUID id,
        String teacherNumber,
        String firstName,
        String lastName,
        String email,
        String specialization
) {
}