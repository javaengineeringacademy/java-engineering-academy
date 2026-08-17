package academy.javaengineering.collections.map.linkedhashmap.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class LinkedHashMapTest {

    @Test
    void testAddAndSize() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        assertEquals(3, map.size());
    }

    @Test
    void testRemove() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.remove("Java");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("Java"));
    }

    @Test
    void testContains() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        assertTrue(map.containsKey("Java"));
        assertFalse(map.containsKey("Go"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(99));
    }

    @Test
    void testIteration() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        List<String> keys = new ArrayList<>(map.keySet());
        assertEquals(Arrays.asList("Java", "Python", "C++"), keys);
    }

    @Test
    void testEdgeCases() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("missing"));
    }

    @Test
    void testInsertionOrder() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Banana", 3);
        map.put("Apple", 5);
        map.put("Cherry", 2);
        List<String> keys = new ArrayList<>(map.keySet());
        assertEquals("Banana", keys.get(0));
        assertEquals("Apple", keys.get(1));
        assertEquals("Cherry", keys.get(2));
    }

    @Test
    void testGetAndPut() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Java", 1);
        assertEquals(1, map.get("Java"));
        map.put("Java", 10);
        assertEquals(10, map.get("Java"));
    }
}
