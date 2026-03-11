package org.example.repository;

import org.example.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface handles all database operations for courses
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}