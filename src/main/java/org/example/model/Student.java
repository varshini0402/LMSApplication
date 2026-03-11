package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// This class represents a student in the LMS
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    // Empty constructor needed for JPA
    public Student() {}

    // Constructor to create a student with id and name
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Get the student id
    public int getId() {
        return id;
    }

    // Get the student name
    public String getName() {
        return name;
    }
}