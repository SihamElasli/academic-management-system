package com.academic.student.api;

import java.util.UUID;

public interface StudentApi {

    boolean existsById(UUID studentId);

}