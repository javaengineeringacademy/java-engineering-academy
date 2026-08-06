package academy.javaengineering.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing fundamentals - Why test, test types, TDD
 *
 * This file covers:
 * - Why testing is important
 * - Testing pyramid
 * - Unit vs Integration vs E2E tests
 * - Test-driven development (TDD)
 * - Writing your first test with JUnit 5
 */
class TestingBasicsTest {

    // =========================================================
    // 1. SIMPLE CLASS UNDER TEST
    // =========================================================

    static class User {
        private final String name;
        private final int age;

        User(String name, int age) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name required");
            }
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
            this.name = name;
            this.age = age;
        }

        String getName() { return name; }
        int getAge() { return age; }
        boolean isAdult() { return age >= 18; }

        String greet() {
            return "Hello, " + name + "!";
        }
    }

    // =========================================================
    // 2. UNIT TESTS - Test individual methods
    // =========================================================

    @Test
    @DisplayName("User constructor stores name correctly")
    void shouldStoreName() {
        User user = new User("Alice", 25);
        assertEquals("Alice", user.getName());
    }

    @Test
    @DisplayName("User constructor stores age correctly")
    void shouldStoreAge() {
        User user = new User("Alice", 25);
        assertEquals(25, user.getAge());
    }

    @Test
    @DisplayName("isAdult returns true for age >= 18")
    void shouldIdentifyAdults() {
        User adult = new User("Alice", 25);
        assertTrue(adult.isAdult());

        User boundary = new User("Bob", 18);
        assertTrue(boundary.isAdult());
    }

    @Test
    @DisplayName("isAdult returns false for age < 18")
    void shouldIdentifyMinors() {
        User minor = new User("Jane", 15);
        assertFalse(minor.isAdult());
    }

    @Test
    @DisplayName("greet returns personalized greeting")
    void shouldGreetUser() {
        User user = new User("Alice", 25);
        assertEquals("Hello, Alice!", user.greet());
    }

    // =========================================================
    // 3. EDGE CASES - Test boundaries
    // =========================================================

    @Test
    @DisplayName("Constructor rejects null name")
    void shouldRejectNullName() {
        assertThrows(IllegalArgumentException.class,
            () -> new User(null, 25));
    }

    @Test
    @DisplayName("Constructor rejects blank name")
    void shouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class,
            () -> new User("  ", 25));
    }

    @Test
    @DisplayName("Constructor rejects negative age")
    void shouldRejectNegativeAge() {
        assertThrows(IllegalArgumentException.class,
            () -> new User("Alice", -1));
    }

    @Test
    @DisplayName("Constructor rejects age > 150")
    void shouldRejectUnreasonableAge() {
        assertThrows(IllegalArgumentException.class,
            () -> new User("Alice", 151));
    }

    // =========================================================
    // 4. TESTING PYRAMID GUIDE (reference)
    // =========================================================
    //
    //        /\
    //       /  \      E2E Tests (Few, slow, expensive)
    //      /    \     - Test complete workflows
    //     /------\    - Simulate real user scenarios
    //    /        \   Integration Tests (Moderate number)
    //   /          \  - Test component interactions
    //  /------------\ - May use databases, APIs
    // /              \ Unit Tests (Many, fast, cheap)
    // /                \ - Test individual methods/classes
    //
    // Ideal ratio: 70% Unit, 20% Integration, 10% E2E
}
