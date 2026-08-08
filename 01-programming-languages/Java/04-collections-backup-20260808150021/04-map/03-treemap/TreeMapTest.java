import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class TreeMapTest {

    @Test
    void testCreation() {
        TreeMap<String, Integer> map = new TreeMap<>();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testPut() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Banana", 2);
        map.put("Apple", 1);
        map.put("Cherry", 3);
        assertEquals(3, map.size());
        assertEquals(Integer.valueOf(1), map.get("Apple"));
    }

    @Test
    void testFirstKey() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("C", 3);
        map.put("A", 1);
        map.put("B", 2);
        assertEquals("A", map.firstKey());
    }

    @Test
    void testLastKey() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("C", 3);
        map.put("A", 1);
        map.put("B", 2);
        assertEquals("C", map.lastKey());
    }

    @Test
    void testHeadMap() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        Map<String, Integer> head = map.headMap("C");
        assertEquals(2, head.size());
        assertTrue(head.containsKey("A"));
        assertTrue(head.containsKey("B"));
    }

    @Test
    void testTailMap() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        Map<String, Integer> tail = map.tailMap("B");
        assertEquals(3, tail.size());
        assertTrue(tail.containsKey("B"));
        assertTrue(tail.containsKey("C"));
        assertTrue(tail.containsKey("D"));
    }

    @Test
    void testHigherKey() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("A", 1);
        map.put("C", 3);
        map.put("E", 5);
        assertEquals("C", map.higherKey("A"));
        assertEquals("E", map.higherKey("C"));
        assertNull(map.higherKey("E"));
    }

    @Test
    void testLowerKey() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("A", 1);
        map.put("C", 3);
        map.put("E", 5);
        assertEquals("C", map.lowerKey("E"));
        assertEquals("A", map.lowerKey("C"));
        assertNull(map.lowerKey("A"));
    }

    @Test
    void testNaturalOrdering() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Z", 26);
        map.put("A", 1);
        map.put("M", 13);
        Object[] keys = map.keySet().toArray();
        assertEquals("A", keys[0]);
        assertEquals("M", keys[1]);
        assertEquals("Z", keys[2]);
    }
}
