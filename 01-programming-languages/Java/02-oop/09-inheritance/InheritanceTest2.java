import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Inheritance Tests")
class InheritanceTest {

    private Dog dog;
    private Cat cat;

    @BeforeEach
    void setUp() {
        dog = new Dog("Rex", 5, "German Shepherd");
        cat = new Cat("Whiskers", 3, true);
    }

    @Test
    @DisplayName("Subclass inherits parent fields")
    void inheritedFields() {
        assertEquals("Rex", dog.getName());
        assertEquals(5, dog.getAge());
        assertEquals("Whiskers", cat.getName());
    }

    @Test
    @DisplayName("Subclass has its own fields")
    void subclassFields() {
        assertEquals("German Shepherd", dog.getBreed());
        assertTrue(cat.isIndoor());
    }

    @Test
    @DisplayName("Subclass can call parent methods")
    void inheritedMethods() {
        assertNotNull(dog.describe());
        assertNotNull(cat.describe());
    }

    @Test
    @DisplayName("Subclass overrides describe()")
    void overriddenDescribe() {
        assertTrue(dog.describe().contains("German Shepherd"));
        assertTrue(cat.describe().contains("Indoor"));
    }

    @Test
    @DisplayName("Dog and Cat are instances of Animal")
    void instanceofCheck() {
        assertTrue(dog instanceof Animal);
        assertTrue(cat instanceof Animal);
        assertFalse(dog instanceof Cat);
    }

    @Test
    @DisplayName("Dog has specific methods")
    void dogSpecific() {
        dog.bark();
        dog.fetch("ball");
    }

    @Test
    @DisplayName("Cat has specific methods")
    void catSpecific() {
        cat.meow();
        cat.purr();
    }
}