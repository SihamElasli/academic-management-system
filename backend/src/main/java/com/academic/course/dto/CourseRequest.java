package com.academic.course.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record CourseRequest(

        @NotBlank
        @Size(max = 50)
        String courseCode,

        @NotBlank
        @Size(max = 150)
        String title,

        String description,

        @NotNull
        @Positive
        Integer credits,

        @NotNull
        @Positive
        Integer capacity,

        @NotNull
        UUID teacherId,

        @NotNull
        Boolean active

) {
}