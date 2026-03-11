package org.example.repository;

import org.example.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface handles all database operations for instructors
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
}