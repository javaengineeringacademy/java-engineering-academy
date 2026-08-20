package academy.javaengineering.testing.junit5.solutions;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Exercise1LifecycleTestSolution {

    private List<String> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
    }

    @Test
    @DisplayName("should start with empty list")
    void shouldStartEmpty() {
        assertTrue(items.isEmpty());
        assertEquals(0, items.size());
    }

    @Test
    @DisplayName("should add elements to list")
    void shouldAddElements() {
        items.add("first");
        items.add("second");

        assertEquals(2, items.size());
        assertTrue(items.contains("first"));
        assertEquals("first", items.get(0));
    }

    @Test
    @DisplayName("should remove elements from list")
    void shouldRemoveElements() {
        items.add("first");
        items.add("second");
        items.remove("first");

        assertEquals(1, items.size());
        assertFalse(items.contains("first"));
        assertTrue(items.contains("second"));
    }

    @Test
    @DisplayName("should throw exception when removing from empty list")
    void shouldThrowOnRemoveFromEmpty() {
        assertThrows(IllegalArgumentException.class,
            () -> items.remove("nonexistent"));
    }
}
