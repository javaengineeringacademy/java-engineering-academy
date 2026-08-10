package academy.javaengineering.collections.map.treemap.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class TreeMapTest {

    @Test
    void testAddAndSize() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        assertEquals(3, map.size());
    }

    @Test
    void testRemove() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.remove("Java");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("Java"));
    }

    @Test
    void testContains() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        assertTrue(map.containsKey("Java"));
        assertFalse(map.containsKey("Go"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(99));
    }

    @Test
    void testIteration() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Banana", 3);
        map.put("Apple", 5);
        map.put("Cherry", 2);
        List<String> keys = new ArrayList<>(map.keySet());
        assertEquals("Apple", keys.get(0));
        assertEquals("Banana", keys.get(1));
        assertEquals("Cherry", keys.get(2));
    }

    @Test
    void testEdgeCases() {
        TreeMap<String, Integer> map = new TreeMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("missing"));
    }

    @Test
    void testSortedOrder() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Banana", 3);
        map.put("Apple", 5);
        map.put("Cherry", 2);
        assertEquals("Apple", map.firstKey());
        assertEquals("Cherry", map.lastKey());
    }

    @Test
    void testHeadMapTailMap() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Banana", 3);
        map.put("Apple", 5);
        map.put("Cherry", 2);
        assertEquals(1, map.headMap("Cherry").size());
        assertEquals(2, map.tailMap("Banana").size());
    }

    @Test
    void testGetAndPut() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Java", 1);
        assertEquals(1, map.get("Java"));
        map.put("Java", 10);
        assertEquals(10, map.get("Java"));
    }
}
