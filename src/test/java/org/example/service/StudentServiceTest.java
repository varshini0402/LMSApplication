package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    // Mock the repository so we don't need a real database
    @Mock
    private StudentRepository studentRepository;

    // Inject the mock into the service we are testing
    @InjectMocks
    private StudentService studentService;

    // Set up the mocks before each test
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetActiveStudents() {
        // Arrange - set up sample students
        Student s1 = new Student(1, "Harry Potter");
        Student s2 = new Student(2, "Hermione Granger");
        when(studentRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        // Act - call the method we are testing
        List<Student> result = studentService.getActiveStudents();

        // Assert - verify the results are correct
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Harry Potter", result.get(0).getName());

        // Verify the repository was called exactly once
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetActiveStudentsEmptyList() {
        // Arrange - set up empty list
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // Act - call the method we are testing
        List<Student> result = studentService.getActiveStudents();

        // Assert - verify the result is empty
        assertNotNull(result);
        assertEquals(0, result.size());

        // Verify the repository was called exactly once
        verify(studentRepository, times(1)).findAll();
    }
}