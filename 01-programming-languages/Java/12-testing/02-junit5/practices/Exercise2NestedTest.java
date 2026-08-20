package academy.javaengineering.testing.junit5.practices;

import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: Nested Tests and Display Names
 *
 * Tasks:
 * 1. Create nested test classes for different scenarios
 * 2. Use @DisplayName for readable test output
 * 3. Test HashMap operations in nested groups
 * 4. Use @BeforeAll and @AfterAll appropriately
 */
class Exercise2NestedTest {

    @Nested
    @DisplayName("When map is empty")
    class EmptyMapTests {
        @Test
        @DisplayName("should have size zero")
        void shouldHaveSizeZero() {
            // Arrange, Act, Assert
        }

        @Test
        @DisplayName("should return null for get")
        void shouldReturnNull() {
            // Arrange, Act, Assert
        }
    }

    @Nested
    @DisplayName("When adding entries")
    class AddingEntriesTests {
        @Test
        @DisplayName("should store key-value pair")
        void shouldStoreEntry() {
            // Arrange, Act, Assert
        }

        @Test
        @DisplayName("should overwrite existing key")
        void shouldOverwriteKey() {
            // Arrange, Act, Assert
        }
    }
}
