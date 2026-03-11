package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// This class handles the business logic for students
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // F2 - Get all active students from the repository
    public List<Student> getActiveStudents() {
        return studentRepository.findAll();
    }
}