package com.academic.teacher.service;

import com.academic.teacher.dto.TeacherRequest;
import com.academic.teacher.dto.TeacherResponse;
import com.academic.teacher.entity.Teacher;
import com.academic.teacher.repository.TeacherRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.academic.teacher.api.TeacherApi;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
@Service
@Transactional
public class TeacherService implements TeacherApi{

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    // CREATE
    public TeacherResponse create(TeacherRequest request) {

        if (teacherRepository.existsByTeacherNumber(
                request.teacherNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Teacher number already exists"
            );
        }

        if (teacherRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        Teacher teacher = new Teacher();

        updateFields(teacher, request);

        return toResponse(teacherRepository.save(teacher));
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<TeacherResponse> findAll() {

        return teacherRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public TeacherResponse findById(UUID id) {

        return toResponse(getTeacher(id));
    }

    // UPDATE
    public TeacherResponse update(UUID id, TeacherRequest request) {

        Teacher teacher = getTeacher(id);

        if (teacherRepository.existsByTeacherNumberAndIdNot(
                request.teacherNumber(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Teacher number already exists"
            );
        }

        if (teacherRepository.existsByEmailAndIdNot(
                request.email(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        updateFields(teacher, request);

        return toResponse(teacherRepository.save(teacher));
    }

    // DELETE
    public void delete(UUID id) {

        Teacher teacher = getTeacher(id);

        try {

            teacherRepository.delete(teacher);
            teacherRepository.flush();

        } catch (DataIntegrityViolationException exception) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete teacher because related data exists",
                    exception
            );
        }
    }

    // Find teacher or throw exception
    private Teacher getTeacher(UUID id) {

        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Teacher not found"
                ));
    }

    // Map request to entity
    private void updateFields(
            Teacher teacher, TeacherRequest request) {

        teacher.setTeacherNumber(request.teacherNumber());
        teacher.setFirstName(request.firstName());
        teacher.setLastName(request.lastName());
        teacher.setEmail(request.email());
        teacher.setSpecialization(request.specialization());
    }

    // Map entity to response
    private TeacherResponse toResponse(Teacher teacher) {

        return new TeacherResponse(
                teacher.getId(),
                teacher.getTeacherNumber(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getSpecialization()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID teacherId) {

        return teacherRepository.existsById(teacherId);
    }
}