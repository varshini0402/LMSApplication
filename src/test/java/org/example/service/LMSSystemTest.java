package org.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This class tests the whole system end to end through the API endpoints
@SpringBootTest
@AutoConfigureMockMvc
public class LMSSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testF1GetStudentEnrollments() throws Exception {
        // Act and Assert - test the F1 endpoint returns correct data
        mockMvc.perform(get("/api/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student.name").value("Harry Potter"));
    }

    @Test
    void testF2GetActiveStudents() throws Exception {
        // Act and Assert - test the F2 endpoint returns list of students
        mockMvc.perform(get("/api/students/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"));
    }

    @Test
    void testF3GetMostActiveInstructor() throws Exception {
        // Act and Assert - test the F3 endpoint returns most active instructor
        mockMvc.perform(get("/api/instructors/most-active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Albus Dumbledore"));
    }

    @Test
    void testF4GetInstructorsWithNoEnrollments() throws Exception {
        // Act and Assert - test the F4 endpoint returns instructors with no enrollments
        mockMvc.perform(get("/api/instructors/no-enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Minerva McGonagall"));
    }
}