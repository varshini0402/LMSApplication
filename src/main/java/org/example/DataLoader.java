package org.example;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// This class loads sample data into the database when the app starts
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public void run(String... args) throws Exception {

        // Create sample instructors
        Instructor i1 = instructorRepository.save(new Instructor(0, "Albus Dumbledore"));
        Instructor i2 = instructorRepository.save(new Instructor(0, "Severus Snape"));
        Instructor i3 = instructorRepository.save(new Instructor(0, "Minerva McGonagall"));

        // Create sample students
        Student s1 = studentRepository.save(new Student(0, "Harry Potter"));
        Student s2 = studentRepository.save(new Student(0, "Hermione Granger"));
        Student s3 = studentRepository.save(new Student(0, "Ron Weasley"));
        Student s4 = studentRepository.save(new Student(0, "Draco Malfoy"));
        Student s5 = studentRepository.save(new Student(0, "Neville Longbottom"));

        // Create sample courses
        Course c1 = courseRepository.save(new Course(0, "Defence Against the Dark Arts", i1));
        Course c2 = courseRepository.save(new Course(0, "Potions", i2));
        Course c3 = courseRepository.save(new Course(0, "Transfiguration", i2));

        // Create sample enrollments
        enrollmentRepository.save(new Enrollment(0, s1, c1));
        enrollmentRepository.save(new Enrollment(0, s2, c1));
        enrollmentRepository.save(new Enrollment(0, s1, c2));
        enrollmentRepository.save(new Enrollment(0, s3, c3));
        enrollmentRepository.save(new Enrollment(0, s4, c2));
        enrollmentRepository.save(new Enrollment(0, s5, c1));
    }
}