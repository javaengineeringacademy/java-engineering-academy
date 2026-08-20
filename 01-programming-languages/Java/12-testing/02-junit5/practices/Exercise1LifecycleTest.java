package academy.javaengineering.testing.junit5.practices;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Lifecycle and Assertions
 *
 * Tasks:
 * 1. Use @BeforeEach to initialize a list
 * 2. Test adding elements
 * 3. Test removing elements
 * 4. Test size assertions
 * 5. Test exception handling with assertThrows
 */
class Exercise1LifecycleTest {

    private List<String> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
    }

    @Test
    @DisplayName("should start with empty list")
    void shouldStartEmpty() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should add elements to list")
    void shouldAddElements() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should remove elements from list")
    void shouldRemoveElements() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should throw exception when removing from empty list")
    void shouldThrowOnRemoveFromEmpty() {
        // Arrange, Act, Assert
    }
}
