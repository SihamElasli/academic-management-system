package com.academic.teacher.controller;

import com.academic.teacher.dto.TeacherRequest;
import com.academic.teacher.dto.TeacherResponse;
import com.academic.teacher.service.TeacherService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<TeacherResponse> create(
            @Valid @RequestBody TeacherRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherService.create(request));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<TeacherResponse>> findAll() {

        return ResponseEntity.ok(teacherService.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(teacherService.findById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TeacherRequest request) {

        return ResponseEntity.ok(
                teacherService.update(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        teacherService.delete(id);

        return ResponseEntity.noContent().build();
    }
}