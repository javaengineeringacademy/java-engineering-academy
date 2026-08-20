package academy.javaengineering.testing.junit5.solutions;

import org.junit.jupiter.api.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Exercise2NestedTestSolution {

    private Map<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new HashMap<>();
    }

    @Nested
    @DisplayName("When map is empty")
    class EmptyMapTests {
        @Test
        @DisplayName("should have size zero")
        void shouldHaveSizeZero() {
            assertEquals(0, map.size());
        }

        @Test
        @DisplayName("should return null for get")
        void shouldReturnNull() {
            assertNull(map.get("key"));
        }
    }

    @Nested
    @DisplayName("When adding entries")
    class AddingEntriesTests {
        @BeforeEach
        void addEntries() {
            map.put("one", 1);
            map.put("two", 2);
        }

        @Test
        @DisplayName("should store key-value pair")
        void shouldStoreEntry() {
            assertEquals(1, map.get("one"));
            assertEquals(2, map.get("two"));
            assertEquals(2, map.size());
        }

        @Test
        @DisplayName("should overwrite existing key")
        void shouldOverwriteKey() {
            map.put("one", 100);
            assertEquals(100, map.get("one"));
            assertEquals(2, map.size());
        }
    }
}
