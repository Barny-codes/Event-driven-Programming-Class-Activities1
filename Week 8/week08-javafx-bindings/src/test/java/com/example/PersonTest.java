package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {
    @Test
    void nameAndAgePropertiesExposeObservableValues() {
        Person person = new Person();
        person.setName("Alice");
        person.setAge(21);

        assertEquals("Alice", person.getName());
        assertEquals("Alice", person.nameProperty().get());
        assertEquals(21, person.getAge());
        assertEquals(21, person.ageProperty().get());
    }

    @Test
    void settersNormalizeInvalidOrUnwantedValues() {
        Person person = new Person();

        person.setName("  Alice  ");
        person.setAge(-5);

        assertEquals("Alice", person.getName());
        assertEquals(0, person.getAge());

        person.setName(null);
        assertEquals("", person.getName());
    }
}
