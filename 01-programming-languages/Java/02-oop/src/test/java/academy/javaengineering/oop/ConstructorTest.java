package academy.javaengineering.oop;

import academy.javaengineering.oop.constructors.ConstructorDemo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConstructorDemo Tests")
class ConstructorDemoTest {

    @Test
    @DisplayName("Default constructor initializes defaults")
    void defaultConstructor() {
        ConstructorDemo obj = new ConstructorDemo();
        assertEquals("Unknown", obj.getName());
        assertEquals(0, obj.getAge());
        assertFalse(obj.hasHobbies());
    }

    @Test
    @DisplayName("Two-param constructor sets name and age")
    void twoParamConstructor() {
        ConstructorDemo obj = new ConstructorDemo("Alice", 30);
        assertEquals("Alice", obj.getName());
        assertEquals(30, obj.getAge());
        assertFalse(obj.hasHobbies());
    }

    @Test
    @DisplayName("Full constructor sets all fields")
    void fullConstructor() {
        String[] hobbies = {"reading", "coding"};
        ConstructorDemo obj = new ConstructorDemo("Bob", 25, hobbies);
        assertEquals("Bob", obj.getName());
        assertEquals(25, obj.getAge());
        assertArrayEquals(hobbies, obj.getHobbies());
    }

    @Test
    @DisplayName("Copy constructor creates independent copy")
    void copyConstructor() {
        String[] hobbies = {"swimming"};
        ConstructorDemo original = new ConstructorDemo("Carol", 28, hobbies);
        ConstructorDemo copy = new ConstructorDemo(original);

        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getAge(), copy.getAge());
        assertNotSame(original, copy);
        assertNotSame(original.getHobbies(), copy.getHobbies());
    }

    @Test
    @DisplayName("Defensive copy prevents external mutation")
    void defensiveCopy() {
        String[] hobbies = {"gaming"};
        ConstructorDemo obj = new ConstructorDemo("Dave", 22, hobbies);
        hobbies[0] = "reading"; // Modify original array

        assertEquals("gaming", obj.getHobbies()[0],
                "Defensive copy should prevent external mutation");
    }

    @Test
    @DisplayName("Null hobbies defaults to empty array")
    void nullHobbies() {
        ConstructorDemo obj = new ConstructorDemo("Eve", 35, null);
        assertNotNull(obj.getHobbies());
        assertEquals(0, obj.getHobbies().length);
    }
}
