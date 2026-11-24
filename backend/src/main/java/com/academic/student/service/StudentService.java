package com.academic.student.service;

import com.academic.student.api.StudentApi;
import com.academic.student.dto.StudentRequest;
import com.academic.student.dto.StudentResponse;
import com.academic.student.entity.Student;
import com.academic.student.repository.StudentRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService implements StudentApi {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @Transactional
    public StudentResponse create(StudentRequest request) {

        if (repository.existsByStudentNumber(
                request.studentNumber())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce numéro étudiant existe déjà"
            );
        }

        Student student = new Student();

        updateFields(student, request);

        return toResponse(repository.save(student));
    }

    // FIND ALL
    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // FIND BY ID
    @Transactional(readOnly = true)
    public StudentResponse findById(UUID id) {

        return toResponse(getStudent(id));
    }

    // UPDATE
    @Transactional
    public StudentResponse update(
            UUID id,
            StudentRequest request) {

        Student student = getStudent(id);

        if (!student.getStudentNumber()
                .equals(request.studentNumber())
                && repository.existsByStudentNumber(
                request.studentNumber())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce numéro étudiant existe déjà"
            );
        }

        updateFields(student, request);

        return toResponse(repository.save(student));
    }

    // DELETE
    @Transactional
    public void delete(UUID id) {

        Student student = getStudent(id);

        try {

            repository.delete(student);
            repository.flush();

        } catch (DataIntegrityViolationException exception) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete student because related data exists",
                    exception
            );
        }
    }

    // PUBLIC CONTRACT
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID studentId) {

        return repository.existsById(studentId);
    }

    // FIND STUDENT ENTITY
    private Student getStudent(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"
                ));
    }

    // UPDATE ENTITY FIELDS
    private void updateFields(
            Student student,
            StudentRequest request) {

        student.setStudentNumber(request.studentNumber());
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
    }

    // MAP ENTITY TO DTO
    private StudentResponse toResponse(Student student) {

        return new StudentResponse(
                student.getId(),
                student.getStudentNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }
}