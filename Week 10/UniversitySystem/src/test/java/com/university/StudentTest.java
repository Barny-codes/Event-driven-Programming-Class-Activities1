package com.university;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {
    @Test
    void studentPropertiesExposeDatabaseValues() {
        Student student = new Student(
            7, "S101", "Alice", "Mwangi", "alice@university.edu");

        assertEquals(7, student.getId());
        assertEquals("S101", student.getRegNumber());
        assertEquals("Alice", student.firstNameProperty().get());
        assertEquals("Mwangi", student.lastNameProperty().get());
        assertEquals("alice@university.edu", student.getEmail());
    }

    @Test
    void studentHandlesMissingEmailForBackwardCompatibility() {
        Student student = new Student(8, "S102", "Bob", "Otieno");

        assertEquals("", student.getEmail());
        assertEquals(8, student.getId());
    }
}
