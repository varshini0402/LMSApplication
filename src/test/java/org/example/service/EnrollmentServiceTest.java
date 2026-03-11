package org.example.service;

import org.example.model.*;
import org.example.repository.EnrollmentRepository;
import org.example.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// This class tests the EnrollmentService using mock objects
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEnrollmentsByStudent() {
        // Arrange
        Instructor i1 = new Instructor(1, "Albus Dumbledore");
        Student s1 = new Student(1, "Harry Potter");
        Course c1 = new Course(1, "Defence Against the Dark Arts", i1);
        Course c2 = new Course(2, "Potions", i1);
        List<Enrollment> mockEnrollments = Arrays.asList(
                new Enrollment(1, s1, c1),
                new Enrollment(2, s1, c2)
        );
        when(enrollmentRepository.findAll()).thenReturn(mockEnrollments);

        // Act
        List<Enrollment> result = enrollmentService.getEnrollmentsByStudent(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify interaction
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentsByStudentNotFound() {
        // Arrange
        when(enrollmentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Enrollment> result = enrollmentService.getEnrollmentsByStudent(99);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());

        // Verify interaction
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetMostActiveInstructor() {
        // Arrange
        Instructor i1 = new Instructor(1, "Albus Dumbledore");
        Instructor i2 = new Instructor(2, "Severus Snape");
        Student s1 = new Student(1, "Harry Potter");
        Student s2 = new Student(2, "Hermione Granger");
        Course c1 = new Course(1, "Defence Against the Dark Arts", i1);
        Course c2 = new Course(2, "Potions", i2);
        List<Enrollment> mockEnrollments = Arrays.asList(
                new Enrollment(1, s1, c1),
                new Enrollment(2, s2, c1),
                new Enrollment(3, s1, c2)
        );
        when(enrollmentRepository.findAll()).thenReturn(mockEnrollments);

        // Act
        Instructor result = enrollmentService.getMostActiveInstructor();

        // Assert
        assertNotNull(result);
        assertEquals("Albus Dumbledore", result.getName());

        // Verify interaction
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetInstructorsWithNoEnrollments() {
        // Arrange
        Instructor i1 = new Instructor(1, "Albus Dumbledore");
        Instructor i2 = new Instructor(2, "Severus Snape");
        Instructor i3 = new Instructor(3, "Minerva McGonagall");
        Student s1 = new Student(1, "Harry Potter");
        Course c1 = new Course(1, "Defence Against the Dark Arts", i1);
        Course c2 = new Course(2, "Potions", i2);
        List<Enrollment> mockEnrollments = Arrays.asList(
                new Enrollment(1, s1, c1),
                new Enrollment(2, s1, c2)
        );
        when(enrollmentRepository.findAll()).thenReturn(mockEnrollments);

        // Act
        List<Instructor> allInstructors = Arrays.asList(i1, i2, i3);
        List<Instructor> result = enrollmentService.getInstructorsWithNoEnrollments(allInstructors);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Minerva McGonagall", result.get(0).getName());

        // Verify interaction
        verify(enrollmentRepository, times(1)).findAll();
    }
}