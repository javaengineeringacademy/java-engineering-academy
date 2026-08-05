import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Person Class Tests")
class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("Alice", 30, "alice@example.com");
    }

    @Test
    @DisplayName("Default constructor initializes defaults")
    void defaultConstructor() {
        Person p = new Person();
        assertEquals("Unknown", p.getName());
        assertEquals(0, p.getAge());
        assertEquals("", p.getEmail());
    }

    @Test
    @DisplayName("Parameterized constructor sets fields")
    void parameterizedConstructor() {
        assertEquals("Alice", person.getName());
        assertEquals(30, person.getAge());
        assertEquals("alice@example.com", person.getEmail());
    }

    @Test
    @DisplayName("Setters update fields with validation")
    void settersWithValidation() {
        person.setName("Bob");
        assertEquals("Bob", person.getName());

        person.setAge(25);
        assertEquals(25, person.getAge());
    }

    @Test
    @DisplayName("Setters reject invalid values")
    void settersRejectInvalid() {
        assertThrows(IllegalArgumentException.class, () -> person.setName(""));
        assertThrows(IllegalArgumentException.class, () -> person.setName(null));
        assertThrows(IllegalArgumentException.class, () -> person.setAge(-1));
        assertThrows(IllegalArgumentException.class, () -> person.setAge(200));
    }

    @Test
    @DisplayName("isAdult returns true for age >= 18")
    void isAdult() {
        assertTrue(person.isAdult());
        Person minor = new Person("Kid", 15, "kid@test.com");
        assertFalse(minor.isAdult());
    }

    @Test
    @DisplayName("equals and hashCode work correctly")
    void equality() {
        Person same = new Person("Alice", 30, "alice@example.com");
        Person different = new Person("Bob", 25, "bob@test.com");

        assertEquals(person, same);
        assertEquals(person.hashCode(), same.hashCode());
        assertNotEquals(person, different);
    }

    @Test
    @DisplayName("toString contains field values")
    void toStringContainsValues() {
        String str = person.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("30"));
    }
}