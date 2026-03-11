package org.example.repository;

import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface handles all database operations for students
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}