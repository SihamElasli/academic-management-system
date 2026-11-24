package com.academic.course.repository;

import com.academic.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository
        extends JpaRepository<Course, UUID> {

    boolean existsByCourseCode(String courseCode);

    boolean existsByCourseCodeAndIdNot(
            String courseCode, UUID id);

    List<Course> findByTeacherId(UUID teacherId);

    List<Course> findByActiveTrue();
}