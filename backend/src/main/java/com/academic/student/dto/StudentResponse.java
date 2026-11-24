
package com.academic.student.dto;

import java.util.UUID;

public record StudentResponse(
        UUID id,
        String studentNumber,
        String firstName,
        String lastName,
        String email
) {}
