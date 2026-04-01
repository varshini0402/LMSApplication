package org.example.service;

import org.example.model.Enrollment;
import org.example.model.Instructor;
import org.example.repository.EnrollmentRepository;
import org.example.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

// This class handles the business logic for enrollments
@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    // F1 - Get all enrollments for a specific student using their id
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollmentRepository.findAll().stream()
                .filter(e -> e.getStudent().getId() == studentId)
                .collect(Collectors.toList());
    }

    // F3 - Find the instructor with the most student enrollments
    public Instructor getMostActiveInstructor() {
        return enrollmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(e -> e.getCourse().getInstructor(),
                        Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(null);
    }

    // Get all instructors from the database
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    // F4 - Get instructors who have no students enrolled in their courses
    public List<Instructor> getInstructorsWithNoEnrollments(List<Instructor> allInstructors) {
        List<Instructor> activeInstructors = enrollmentRepository.findAll().stream()
                .map(e -> e.getCourse().getInstructor())
                .collect(Collectors.toList());
        return allInstructors.stream()
                .filter(i -> !activeInstructors.contains(i))
                .collect(Collectors.toList());
    }

    // F5 - Get all enrollments showing student name and course name
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}