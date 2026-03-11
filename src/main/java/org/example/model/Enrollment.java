package org.example.model;

import jakarta.persistence.*;

// This class represents a student enrollment in a course
@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Many enrollments can have one student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Many enrollments can have one course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Empty constructor needed for JPA
    public Enrollment() {}

    // Constructor to create an enrollment with id, student and course
    public Enrollment(int id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
    }

    // Get the enrollment id
    public int getId() {
        return id;
    }

    // Get the student who is enrolled
    public Student getStudent() {
        return student;
    }

    // Get the course the student is enrolled in
    public Course getCourse() {
        return course;
    }
}