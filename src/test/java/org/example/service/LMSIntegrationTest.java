package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This class tests the services working together using the real Spring context
@SpringBootTest
@Transactional
public class LMSIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void testGetActiveStudents() {
        // Integration Testing - only implement the Act and Assert

        // Act
        List<Student> students = studentService.getActiveStudents();

        // Assert
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(5, students.size());
    }

    @Test
    void testGetEnrollmentsByStudent() {
        // Integration Testing - only implement the Act and Assert

        // Act
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(1);

        // Assert
        assertNotNull(enrollments);
        assertFalse(enrollments.isEmpty());
        assertEquals(2, enrollments.size());
    }

    @Test
    void testGetMostActiveInstructor() {
        // Integration Testing - only implement the Act and Assert

        // Act
        Instructor instructor = enrollmentService.getMostActiveInstructor();

        // Assert
        assertNotNull(instructor);
        assertEquals("Albus Dumbledore", instructor.getName());
    }

    @Test
    void testGetInstructorsWithNoEnrollments() {
        // Integration Testing - only implement the Act and Assert

        // Act
        List<Instructor> allInstructors = enrollmentService.getAllInstructors();
        List<Instructor> result = enrollmentService.getInstructorsWithNoEnrollments(allInstructors);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Minerva McGonagall", result.get(0).getName());
    }
}