import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Method Overriding Tests")
class OverrideTest {

    private MethodOverridingDog dog;

    @BeforeEach
    void setUp() {
        dog = new MethodOverridingDog("Rex", "Shepherd");
    }

    @Test
    @DisplayName("Overridden eat() calls Dog implementation")
    void overriddenEat() {
        dog.eat();
    }

    @Test
    @DisplayName("Overridden speak() returns dog sound")
    void overriddenSpeak() {
        assertEquals("Rex says: Woof!", dog.speak());
    }

    @Test
    @DisplayName("Overridden describe() includes breed")
    void overriddenDescribe() {
        assertTrue(dog.describe().contains("Shepherd"));
        assertTrue(dog.describe().contains("Rex"));
    }

    @Test
    @DisplayName("Polymorphic call uses overridden method")
    void polymorphicCall() {
        MethodOverridingAnimal animal = new MethodOverridingDog("Buddy", "Labrador");
        assertEquals("Buddy says: Woof!", animal.speak());
    }

    @Test
    @DisplayName("Dog-specific method not available on Animal reference")
    void dogSpecific() {
        MethodOverridingDog d = new MethodOverridingDog("Max", "Poodle");
        d.fetch();
    }

    @Test
    @DisplayName("super call works in subclass")
    void superCall() {
        MethodOverridingDog d = new MethodOverridingDog("Max", "Poodle");
        assertNotNull(d.describe());
    }
}
