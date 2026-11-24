
package com.academic.student.service;

import com.academic.student.dto.StudentRequest;
import com.academic.student.dto.StudentResponse;
import com.academic.student.entity.Student;
import com.academic.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {

        if (repository.existsByStudentNumber(request.studentNumber())) {
            throw new IllegalArgumentException(
                    "Ce numéro étudiant existe déjà"
            );
        }

        Student student = new Student();
        student.setStudentNumber(request.studentNumber());
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());

        Student saved = repository.save(student);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(UUID id) {
        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new java.util.NoSuchElementException(
                                "Étudiant introuvable : " + id
                        )
                );

        return toResponse(student);
    }

    @Transactional
    public StudentResponse update(UUID id, StudentRequest request) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new java.util.NoSuchElementException(
                                "Étudiant introuvable : " + id
                        )
                );

        if (!student.getStudentNumber().equals(request.studentNumber())
                && repository.existsByStudentNumber(request.studentNumber())) {
            throw new IllegalArgumentException(
                    "Ce numéro étudiant existe déjà"
            );
        }

        student.setStudentNumber(request.studentNumber());
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());

        return toResponse(repository.save(student));
    }

    @Transactional
    public void delete(UUID id) {

        if (!repository.existsById(id)) {
            throw new java.util.NoSuchElementException(
                    "Étudiant introuvable : " + id
            );
        }

        repository.deleteById(id);
    }

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
