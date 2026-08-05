import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class HashMapTest {

    @Test
    void testCreation() {
        HashMap<String, Integer> map = new HashMap<>();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testPut() {
        HashMap<String, Integer> map = new HashMap<>();
        Integer old = map.put("one", 1);
        assertNull(old);
        assertEquals(1, map.size());
        assertEquals(Integer.valueOf(1), map.get("one"));
    }

    @Test
    void testPutOverwrite() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("key", 1);
        Integer old = map.put("key", 2);
        assertEquals(Integer.valueOf(1), old);
        assertEquals(Integer.valueOf(2), map.get("key"));
        assertEquals(1, map.size());
    }

    @Test
    void testGet() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("key", 100);
        assertEquals(Integer.valueOf(100), map.get("key"));
    }

    @Test
    void testGetNonExistent() {
        HashMap<String, Integer> map = new HashMap<>();
        assertNull(map.get("missing"));
    }

    @Test
    void testContainsKey() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        assertTrue(map.containsKey("A"));
        assertFalse(map.containsKey("B"));
    }

    @Test
    void testContainsValue() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(2));
    }

    @Test
    void testRemove() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        Integer removed = map.remove("A");
        assertEquals(Integer.valueOf(1), removed);
        assertEquals(0, map.size());
    }

    @Test
    void testSize() {
        HashMap<String, Integer> map = new HashMap<>();
        assertEquals(0, map.size());
        map.put("A", 1);
        assertEquals(1, map.size());
    }

    @Test
    void testIsEmpty() {
        HashMap<String, Integer> map = new HashMap<>();
        assertTrue(map.isEmpty());
        map.put("A", 1);
        assertFalse(map.isEmpty());
    }

    @Test
    void testClear() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    void testPutIfAbsent() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.putIfAbsent("A", 2);
        assertEquals(Integer.valueOf(1), map.get("A"));
        map.putIfAbsent("B", 3);
        assertEquals(Integer.valueOf(3), map.get("B"));
    }

    @Test
    void testReplace() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        Integer old = map.replace("A", 10);
        assertEquals(Integer.valueOf(1), old);
        assertEquals(Integer.valueOf(10), map.get("A"));
    }

    @Test
    void testCompute() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.compute("A", (k, v) -> v + 10);
        assertEquals(Integer.valueOf(11), map.get("A"));
    }

    @Test
    void testMerge() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.merge("A", 10, Integer::sum);
        assertEquals(Integer.valueOf(11), map.get("A"));
        map.merge("B", 5, Integer::sum);
        assertEquals(Integer.valueOf(5), map.get("B"));
    }
}
