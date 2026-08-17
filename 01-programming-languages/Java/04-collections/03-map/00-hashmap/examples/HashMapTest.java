package academy.javaengineering.collections.map.hashmap.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class HashMapTest {

    @Test
    void testAddAndSize() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        assertEquals(3, map.size());
    }

    @Test
    void testRemove() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.remove("Java");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("Java"));
    }

    @Test
    void testContains() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        assertTrue(map.containsKey("Java"));
        assertFalse(map.containsKey("Go"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(99));
    }

    @Test
    void testIteration() {
        HashMap<String, Integer> map = new HashMap<>();
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
        HashMap<String, Integer> map = new HashMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("missing"));
    }

    @Test
    void testGetAndPut() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        assertEquals(1, map.get("Java"));
        map.put("Java", 10);
        assertEquals(10, map.get("Java"));
    }

    @Test
    void testPutIfAbsent() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.putIfAbsent("Java", 99);
        assertEquals(1, map.get("Java"));
        map.putIfAbsent("Python", 2);
        assertEquals(2, map.get("Python"));
    }

    @Test
    void testMerge() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.merge("Java", 10, Integer::sum);
        assertEquals(11, map.get("Java"));
    }
}
