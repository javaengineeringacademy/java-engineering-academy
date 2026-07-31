package academy.javaengineering.oop.encapsulation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for encapsulated Person class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class PersonTest {

    @Test
    void shouldCreatePersonWithValidData() {
        Person person = new Person("Alice", 30);
        assertEquals("Alice", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    void shouldRejectInvalidAge() {
        Person person = new Person("Bob", 25);
        assertFalse(person.setAge(-5));
        assertEquals(25, person.getAge()); // Unchanged
    }

    @Test
    void shouldAcceptValidAge() {
        Person person = new Person("Charlie", 25);
        assertTrue(person.setAge(35));
        assertEquals(35, person.getAge());
    }

    @Test
    void shouldReturnCorrectAgeCategory() {
        Person child = new Person("Child", 10);
        Person teen = new Person("Teen", 15);
        Person adult = new Person("Adult", 30);
        Person senior = new Person("Senior", 70);

        assertEquals("Child", child.getAgeCategory());
        assertEquals("Teenager", teen.getAgeCategory());
        assertEquals("Adult", adult.getAgeCategory());
        assertEquals("Senior", senior.getAgeCategory());
    }

    @Test
    void shouldThrowOnInvalidConstructorAge() {
        assertThrows(IllegalArgumentException.class, () -> new Person("Invalid", -1));
        assertThrows(IllegalArgumentException.class, () -> new Person("Invalid", 200));
    }

    @Test
    void shouldHaveReadOnlyTimestamp() {
        Person person = new Person("Dave", 40);
        long timestamp = person.getCreatedTimestamp();
        assertTrue(timestamp > 0);
        assertEquals(timestamp, person.getCreatedTimestamp()); // Same value
    }

    @Test
    void shouldHaveWriteOnlySSN() {
        Person person = new Person("Eve", 28);
        person.setSSN("123-45-6789");
        // SSN is write-only - no getter, can't read it back
        // This is intentional for security
    }
}