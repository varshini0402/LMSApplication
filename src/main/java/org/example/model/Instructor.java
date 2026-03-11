package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// This class represents an instructor in the LMS
@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    // Empty constructor needed for JPA
    public Instructor() {}

    // Constructor to create an instructor with id and name
    public Instructor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Get the instructor id
    public int getId() {
        return id;
    }

    // Get the instructor name
    public String getName() {
        return name;
    }

    // Check if two instructors are the same by comparing their id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instructor)) return false;
        Instructor that = (Instructor) o;
        return id == that.id;
    }

    // Generate hash code based on id
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}