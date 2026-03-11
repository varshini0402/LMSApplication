package org.example.repository;

import org.example.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface handles all database operations for enrollments
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}