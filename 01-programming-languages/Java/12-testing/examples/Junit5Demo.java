package academy.javaengineering.testing.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5 Basics - Annotations, Assertions, Lifecycle
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Junit5Demo {

    // ============================================
    // Lifecycle Annotations
    // ============================================

    @BeforeAll
    static void beforeAll() {
        System.out.println("@BeforeAll - Runs once before all tests");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("@BeforeEach - Runs before each test");
    }

    @AfterEach
    void afterEach() {
        System.out.println("@AfterEach - Runs after each test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("@AfterAll - Runs once after all tests");
    }

    // ============================================
    // Basic Test with Assertions
    // ============================================

    @Test
    @Order(1)
    @DisplayName("Basic addition test")
    void testAddition() {
        // Arrange
        int a = 5;
        int b = 3;

        // Act
        int result = a + b;

        // Assert
        assertEquals(8, result, "5 + 3 should equal 8");
        assertNotEquals(9, result);
        assertTrue(result > 0);
    }

    // ============================================
    // Exception Testing
    // ============================================

    @Test
    @Order(2)
    @DisplayName("Exception should be thrown on division by zero")
    void testExceptionThrown() {
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> 10 / 0
        );
        assertEquals("/ by zero", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("No exception should be thrown for valid input")
    void testNoException() {
        assertDoesNotThrow(() -> {
            int result = 10 / 2;
            assertEquals(5, result);
        });
    }

    // ============================================
    // Assertions with Messages
    // ============================================

    @Test
    @Order(4)
    @DisplayName("String operations with descriptive assertions")
    void testStringOperations() {
        String str = "Hello, JUnit 5!";

        // assertEquals with message
        assertEquals(15, str.length(), "String length should be 15");

        // assertTrue/assertFalse
        assertTrue(str.startsWith("Hello"), "Should start with 'Hello'");
        assertFalse(str.isEmpty(), "Should not be empty");

        // assertNull/assertNotNull
        assertNotNull(str, "String should not be null");
        assertNull(null, "Null should be null");

        // assertSame - reference equality
        String sameStr = str;
        assertSame(str, sameStr, "Should be same reference");

        // assertNotSame - different references
        String newStr = new String("Hello, JUnit 5!");
        assertNotSame(str, newStr, "Should be different references");
    }

    // ============================================
    // Grouped Assertions
    // ============================================

    @Test
    @Order(5)
    @DisplayName("Grouped assertions for Person object")
    void testGroupedAssertions() {
        String name = "John";
        int age = 30;
        String email = "john@example.com";

        assertAll("Person properties",
            () -> assertEquals("John", name, "Name should be John"),
            () -> assertEquals(30, age, "Age should be 30"),
            () -> assertEquals("john@example.com", email, "Email should match"),
            () -> assertNotNull(name, "Name should not be null"),
            () -> assertTrue(age > 0 && age < 150, "Age should be valid")
        );
    }

    // ============================================
    // Exception Assertions
    // ============================================

    @Test
    @Order(6)
    @DisplayName("Test various exceptions")
    void testExceptions() {
        // NullPointerException
        assertThrows(NullPointerException.class, () -> {
            String nullStr = null;
            nullStr.length();
        });

        // IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> {
            List<String> list = new ArrayList<>();
            list.get(0);
        });

        // IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            Integer.parseInt("not a number");
        });
    }

    // ============================================
    // Disabled Test
    // ============================================

    @Test
    @Disabled("Not yet implemented")
    @Order(7)
    @DisplayName("This test is disabled")
    void disabledTest() {
        fail("This should not run");
    }

    // ============================================
    // Nested Tests
    // ============================================

    @Nested
    @DisplayName("When testing string operations")
    class StringOperations {

        @Test
        @DisplayName("concatenation should combine strings")
        void testConcatenation() {
            String result = "Hello" + " " + "World";
            assertEquals("Hello World", result);
        }

        @Test
        @DisplayName("substring should extract portion")
        void testSubstring() {
            String str = "Hello World";
            assertEquals("World", str.substring(6));
        }

        @Nested
        @DisplayName("When input is null")
        class NullInput {

            @Test
            @DisplayName("should handle null safely")
            void testNullHandling() {
                String str = null;
                assertNull(str);
            }
        }
    }
}
