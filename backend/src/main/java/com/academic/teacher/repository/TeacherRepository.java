package com.academic.teacher.repository;

import com.academic.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    boolean existsByTeacherNumber(String teacherNumber);

    boolean existsByEmail(String email);

    boolean existsByTeacherNumberAndIdNot(String teacherNumber, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);
}