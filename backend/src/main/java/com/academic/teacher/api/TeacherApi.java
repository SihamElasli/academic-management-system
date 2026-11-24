package com.academic.teacher.api;

import java.util.UUID;

public interface TeacherApi {

    boolean existsById(UUID teacherId);
}