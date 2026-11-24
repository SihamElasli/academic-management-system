package com.academic.course.api;

import java.util.UUID;

public interface CourseApi {

    boolean existsById(UUID courseId);

    CourseInfo getCourseForEnrollment(UUID courseId);

}