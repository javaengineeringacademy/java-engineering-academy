package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkedHashMapDemoTest {

    private LinkedHashMap<String, Integer> linkedHashMap;

    @BeforeEach
    void setUp() {
        linkedHashMap = new LinkedHashMap<>();
    }

    @Nested
    @DisplayName("Insertion-Order Tests")
    class InsertionOrderTests {

        @Test
        @DisplayName("Should maintain insertion order")
        void testInsertionOrder() {
            linkedHashMap.put("Banana", 1);
            linkedHashMap.put("Apple", 2);
            linkedHashMap.put("Cherry", 3);

            List<String> keys = new ArrayList<>(linkedHashMap.keySet());
            assertEquals(List.of("Banana", "Apple", "Cherry"), keys);
        }

        @Test
        @DisplayName("Should maintain order after update")
        void testOrderAfterUpdate() {
            linkedHashMap.put("A", 1);
            linkedHashMap.put("B", 2);
            linkedHashMap.put("A", 10); // Update, not reorder

            List<String> keys = new ArrayList<>(linkedHashMap.keySet());
            assertEquals(List.of("A", "B"), keys);
            assertEquals(Integer.valueOf(10), linkedHashMap.get("A"));
        }
    }

    @Nested
    @DisplayName("Access-Order Tests")
    class AccessOrderTests {

        @Test
        @DisplayName("Should reorder on access when access-order enabled")
        void testAccessOrder() {
            LinkedHashMap<String, Integer> accessMap = new LinkedHashMap<>(16, 0.75f, true);
            accessMap.put("A", 1);
            accessMap.put("B", 2);
            accessMap.put("C", 3);

            accessMap.get("A"); // Moves A to end

            List<String> keys = new ArrayList<>(accessMap.keySet());
            assertEquals(List.of("B", "C", "A"), keys);
        }

        @Test
        @DisplayName("Should evict eldest in access-order LRU cache")
        void testLRUEviction() {
            LinkedHashMap<String, String> lruCache = new LinkedHashMap<>(3, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > 3;
                }
            };

            lruCache.put("key1", "v1");
            lruCache.put("key2", "v2");
            lruCache.put("key3", "v3");
            lruCache.put("key4", "v4"); // Evicts key1

            assertFalse(lruCache.containsKey("key1"));
            assertTrue(lruCache.containsKey("key4"));
        }
    }

    @Nested
    @DisplayName("Basic Operations Tests")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should put and get values")
        void testPutAndGet() {
            linkedHashMap.put("X", 10);
            assertEquals(Integer.valueOf(10), linkedHashMap.get("X"));
        }

        @Test
        @DisplayName("Should return null for missing key")
        void testGetMissing() {
            assertNull(linkedHashMap.get("missing"));
        }

        @Test
        @DisplayName("Should put if absent")
        void testPutIfAbsent() {
            linkedHashMap.put("A", 1);
            linkedHashMap.putIfAbsent("A", 2);
            assertEquals(Integer.valueOf(1), linkedHashMap.get("A"));

            linkedHashMap.putIfAbsent("B", 3);
            assertEquals(Integer.valueOf(3), linkedHashMap.get("B"));
        }

        @Test
        @DisplayName("Should remove entry")
        void testRemove() {
            linkedHashMap.put("A", 1);
            linkedHashMap.put("B", 2);
            Integer removed = linkedHashMap.remove("A");
            assertEquals(Integer.valueOf(1), removed);
            assertEquals(1, linkedHashMap.size());
            assertFalse(linkedHashMap.containsKey("A"));
        }
    }
}
