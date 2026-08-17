package academy.javaengineering.collections.map.hashtable.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class HashtableTest {

    @Test
    void testAddAndSize() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Java", 1);
        table.put("Python", 2);
        table.put("C++", 3);
        assertEquals(3, table.size());
    }

    @Test
    void testRemove() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Java", 1);
        table.put("Python", 2);
        table.remove("Java");
        assertEquals(1, table.size());
        assertFalse(table.containsKey("Java"));
    }

    @Test
    void testContains() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Java", 1);
        table.put("Python", 2);
        assertTrue(table.containsKey("Java"));
        assertFalse(table.containsKey("Go"));
        assertTrue(table.containsValue(1));
        assertFalse(table.containsValue(99));
    }

    @Test
    void testIteration() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Java", 1);
        table.put("Python", 2);
        int sum = 0;
        for (Map.Entry<String, Integer> entry : table.entrySet()) {
            sum += entry.getValue();
        }
        assertEquals(3, sum);
    }

    @Test
    void testEdgeCases() {
        Hashtable<String, Integer> table = new Hashtable<>();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("missing"));
    }

    @Test
    void testGetAndPut() {
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("Java", 1);
        assertEquals(1, table.get("Java"));
        table.put("Java", 10);
        assertEquals(10, table.get("Java"));
    }

    @Test
    void testNullKeyThrows() {
        Hashtable<String, Integer> table = new Hashtable<>();
        assertThrows(NullPointerException.class, () -> table.put(null, 1));
    }

    @Test
    void testNullValueThrows() {
        Hashtable<String, Integer> table = new Hashtable<>();
        assertThrows(NullPointerException.class, () -> table.put("Java", null));
    }
}
