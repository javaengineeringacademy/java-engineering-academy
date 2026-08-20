package academy.javaengineering.testing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 examples - annotations, assertions, lifecycle, parameterized tests
 *
 * This file covers:
 * - @Test, @BeforeEach, @AfterEach, @BeforeAll, @AfterAll
 * - Assertions: assertEquals, assertTrue, assertThrows, assertAll
 * - @DisplayName for readable test names
 * - @Nested for grouping related tests
 * - @ParameterizedTest with @ValueSource, @CsvSource
 * - Exception testing
 */
class Junit5ExamplesTest {

    private List<String> names;

    // =========================================================
    // 1. LIFECYCLE CALLBACKS
    // =========================================================

    @BeforeAll
    static void beforeAll() {
        // Runs once before all tests in this class
        // Use for expensive setup (database, file system)
        System.out.println("Setting up test class...");
    }

    @AfterAll
    static void afterAll() {
        // Runs once after all tests in this class
        // Use for cleanup (close connections, delete temp files)
        System.out.println("Tearing down test class...");
    }

    @BeforeEach
    void setUp() {
        // Runs before each test method
        names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
    }

    @AfterEach
    void tearDown() {
        // Runs after each test method
        // Use to clean up resources between tests
        names = null;
    }

    // =========================================================
    // 2. BASIC ASSERTIONS
    // =========================================================

    @Test
    @DisplayName("List should contain 3 elements after setup")
    void shouldHaveThreeElements() {
        assertEquals(3, names.size());
    }

    @Test
    @DisplayName("List should contain Alice, Bob, Charlie")
    void shouldContainInitialNames() {
        assertAll(
            () -> assertTrue(names.contains("Alice"), "Should contain Alice"),
            () -> assertTrue(names.contains("Bob"), "Should contain Bob"),
            () -> assertTrue(names.contains("Charlie"), "Should contain Charlie")
        );
    }

    // =========================================================
    // 3. ADDING AND REMOVING
    // =========================================================

    @Test
    @DisplayName("Adding a name increases list size")
    void shouldAddName() {
        names.add("David");

        assertEquals(4, names.size());
        assertTrue(names.contains("David"));
    }

    @Test
    @DisplayName("Removing a name decreases list size")
    void shouldRemoveName() {
        names.remove("Bob");

        assertEquals(2, names.size());
        assertFalse(names.contains("Bob"));
    }

    // =========================================================
    // 4. EXCEPTION TESTING
    // =========================================================

    @Test
    @DisplayName("Accessing index beyond size throws exception")
    void shouldThrowOnOutOfBoundsAccess() {
        IndexOutOfBoundsException exception = assertThrows(
            IndexOutOfBoundsException.class,
            () -> names.get(10)
        );
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Removing non-existent element does not change size")
    void shouldNotChangeSizeWhenRemovingNonExistent() {
        boolean removed = names.remove("Zara");

        assertFalse(removed);
        assertEquals(3, names.size());
    }

    // =========================================================
    // 5. NESTED TESTS - Group related tests
    // =========================================================

    @Nested
    @DisplayName("When list is empty")
    class EmptyListTests {

        @BeforeEach
        void setUpEmpty() {
            names.clear();
        }

        @Test
        @DisplayName("Size should be 0")
        void shouldHaveSizeZero() {
            assertEquals(0, names.size());
        }

        @Test
        @DisplayName("Contains should return false")
        void shouldNotContainAnything() {
            assertFalse(names.contains("Alice"));
        }

        @Test
        @DisplayName("Adding first element should work")
        void shouldAddFirstElement() {
            names.add("First");
            assertEquals(1, names.size());
            assertEquals("First", names.get(0));
        }
    }

    // =========================================================
    // 6. PARAMETERIZED TESTS
    // =========================================================

    @ParameterizedTest
    @DisplayName("All listed names should be recognized as valid")
    @ValueSource(strings = {"Alice", "Bob", "Charlie", "David"})
    void shouldRecognizeValidNames(String name) {
        names.add(name);
        assertTrue(names.contains(name));
    }

    @ParameterizedTest
    @DisplayName("Age boundary checks")
    @CsvSource({
        "18, true",
        "17, false",
        "25, true",
        "0, false",
        "150, true",
        "151, false"
    })
    void shouldValidateAgeBoundaries(int age, boolean expectedAdult) {
        if (age >= 0 && age <= 150) {
            User user = new User("Test", age);
            assertEquals(expectedAdult, user.isAdult());
        } else {
            assertThrows(IllegalArgumentException.class,
                () -> new User("Test", age));
        }
    }

    // =========================================================
    // 7. ASSERTALL - Multiple assertions without stopping
    // =========================================================

    @Test
    @DisplayName("Validate all list invariants at once")
    void shouldValidateAllInvariants() {
        names.add("David");

        assertAll("list state",
            () -> assertEquals(4, names.size()),
            () -> assertTrue(names.contains("Alice")),
            () -> assertTrue(names.contains("Bob")),
            () -> assertTrue(names.contains("Charlie")),
            () -> assertTrue(names.contains("David")),
            () -> assertEquals("Alice", names.get(0))
        );
    }

    // Inner class used by parameterized test
    static class User {
        private final String name;
        private final int age;

        User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        boolean isAdult() { return age >= 18; }
    }
}
