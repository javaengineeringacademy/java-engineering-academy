package academy.javaengineering.oop.inheritance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for inheritance hierarchy.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class InheritanceTest {

    @Test
    void shouldCreateAnimal() {
        Animal animal = new Animal("Generic");
        assertEquals("Generic", animal.getName());
        assertEquals(100, animal.getEnergy());
    }

    @Test
    void shouldInheritEatMethod() {
        Dog dog = new Dog("Buddy", "Labrador");
        dog.eat();
        assertEquals(110, dog.getEnergy());
    }

    @Test
    void shouldOverrideSoundMethod() {
        Dog dog = new Dog("Rex", "German Shepherd");
        // Should not throw - sound is overridden
        assertDoesNotThrow(dog::sound);
    }

    @Test
    void shouldSupportMultilevelInheritance() {
        Puppy puppy = new Puppy("Max", "Golden Retriever", 3);
        
        // Inherited from Animal
        assertEquals("Max", puppy.getName());
        
        // From Dog
        assertEquals("Golden Retriever", puppy.getBreed());
        
        // Puppy-specific
        assertEquals(3, puppy.getMonthsOld());
    }

    @Test
    void shouldSupportInstanceofChecks() {
        Puppy puppy = new Puppy("Max", "Lab", 2);
        
        assertTrue(puppy instanceof Puppy);
        assertTrue(puppy instanceof Dog);
        assertTrue(puppy instanceof Animal);
        assertTrue(puppy instanceof Object);
    }

    @Test
    void shouldCallParentConstructor() {
        // No exception means constructor chain worked
        assertDoesNotThrow(() -> new Puppy("Test", "Breed", 1));
    }
}