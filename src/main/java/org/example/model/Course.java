package org.example.model;

import jakarta.persistence.*;

// This class represents a course in the LMS
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    // Many courses can have one instructor
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // Empty constructor needed for JPA
    public Course() {}

    // Constructor to create a course with id, name and instructor
    public Course(int id, String name, Instructor instructor) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
    }

    // Get the course id
    public int getId() {
        return id;
    }

    // Get the course name
    public String getName() {
        return name;
    }

    // Get the instructor who teaches this course
    public Instructor getInstructor() {
        return instructor;
    }
}