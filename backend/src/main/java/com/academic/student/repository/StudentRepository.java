
package com.academic.student.repository;

import com.academic.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    boolean existsByStudentNumber(String studentNumber);
}
