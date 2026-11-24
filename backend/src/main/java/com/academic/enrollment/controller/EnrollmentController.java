package com.academic.enrollment.controller;

import com.academic.enrollment.dto.EnrollmentRequest;
import com.academic.enrollment.dto.EnrollmentResponse;
import com.academic.enrollment.entity.EnrollmentStatus;
import com.academic.enrollment.service.EnrollmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(
            EnrollmentService enrollmentService) {

        this.enrollmentService = enrollmentService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<EnrollmentResponse> create(
            @Valid @RequestBody EnrollmentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollmentService.create(request));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> findAll() {

        return ResponseEntity.ok(
                enrollmentService.findAll()
        );
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> findById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                enrollmentService.findById(id)
        );
    }

    // FIND BY STUDENT
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> findByStudent(
            @PathVariable UUID studentId) {

        return ResponseEntity.ok(
                enrollmentService.findByStudent(studentId)
        );
    }

    // FIND BY COURSE
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> findByCourse(
            @PathVariable UUID courseId) {

        return ResponseEntity.ok(
                enrollmentService.findByCourse(courseId)
        );
    }

    // FIND BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentResponse>> findByStatus(
            @PathVariable EnrollmentStatus status) {

        return ResponseEntity.ok(
                enrollmentService.findByStatus(status)
        );
    }

    // CANCEL
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EnrollmentResponse> cancel(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                enrollmentService.cancel(id)
        );
    }
}