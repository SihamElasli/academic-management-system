package com.academic.course.dto;

import java.util.UUID;

public record CourseResponse(

        UUID id,
        String courseCode,
        String title,
        String description,
        Integer credits,
        Integer capacity,
        UUID teacherId,
        Boolean active

) {
}