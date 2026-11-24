package com.academic.course.api;

import java.util.UUID;

public record CourseInfo(

        UUID id,
        Integer capacity,
        Boolean active

) {
}