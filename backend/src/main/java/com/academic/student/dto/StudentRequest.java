
package com.academic.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentRequest(

        @NotBlank(message = "Le numéro étudiant est obligatoire")
        @Size(max = 50)
        String studentNumber,

        @NotBlank(message = "Le prénom est obligatoire")
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        String lastName,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format email invalide")
        String email

) {}
