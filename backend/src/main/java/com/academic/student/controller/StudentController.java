
package com.academic.student.controller;

import com.academic.student.dto.StudentRequest;
import com.academic.student.dto.StudentResponse;
import com.academic.student.service.StudentService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Ajouter un étudiant
    @PostMapping
    public ResponseEntity<StudentResponse> create(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse student = studentService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(student);
    }

    // Récupérer tous les étudiants
    @GetMapping
    public ResponseEntity<List<StudentResponse>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    // Récupérer un étudiant par ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(studentService.findById(id));
    }

    // Modifier un étudiant
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.ok(studentService.update(id, request));
    }

    // Supprimer un étudiant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
