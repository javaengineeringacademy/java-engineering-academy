package academy.javaengineering.collections.map.weakhashmap.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class WeakHashMapTest {

    @Test
    void testAddAndSize() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        assertEquals(3, map.size());
    }

    @Test
    void testRemove() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.remove("Java");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("Java"));
    }

    @Test
    void testContains() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        assertTrue(map.containsKey("Java"));
        assertFalse(map.containsKey("Go"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(99));
    }

    @Test
    void testIteration() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        int sum = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sum += entry.getValue();
        }
        assertEquals(3, sum);
    }

    @Test
    void testEdgeCases() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("missing"));
    }

    @Test
    void testGetAndPut() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        map.put("Java", 1);
        assertEquals(1, map.get("Java"));
        map.put("Java", 10);
        assertEquals(10, map.get("Java"));
    }

    @Test
    void testNullKey() {
        WeakHashMap<String, Integer> map = new WeakHashMap<>();
        map.put(null, 1);
        assertEquals(1, map.get(null));
    }
}
