package org.example.controller;

import org.example.model.*;
import org.example.repository.InstructorRepository;
import org.example.service.EnrollmentService;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// This class handles all the API endpoints for the LMS system
@RestController
@RequestMapping("/api")
public class LMSController {

    // Autowire the services so Spring can inject them automatically
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorRepository instructorRepository;

    // F1 - Get all enrollments for a specific student using their id
    @GetMapping("/enrollments/{studentId}")
    public List<Enrollment> getStudentEnrollments(@PathVariable int studentId) {
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    // F2 - Get all active students currently enrolled in courses
    @GetMapping("/students/active")
    public List<Student> getActiveStudents() {
        return studentService.getActiveStudents();
    }

    // F3 - Find the instructor with the most student enrollments
    @GetMapping("/instructors/most-active")
    public Instructor getMostActiveInstructor() {
        return enrollmentService.getMostActiveInstructor();
    }

    // F4 - Get instructors who have no students enrolled in their courses
    @GetMapping("/instructors/no-enrollments")
    public List<Instructor> getInstructorsWithNoEnrollments() {
        List<Instructor> all = instructorRepository.findAll();
        return enrollmentService.getInstructorsWithNoEnrollments(all);
    }
}