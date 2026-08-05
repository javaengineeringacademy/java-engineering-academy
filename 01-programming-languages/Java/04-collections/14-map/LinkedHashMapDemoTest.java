import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class LinkedHashMapDemoTest {

    @Test
    void testInsertionOrder() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("Banana", 1);
        map.put("Apple", 2);
        map.put("Cherry", 3);

        List<String> keys = new ArrayList<>(map.keySet());
        assertEquals(List.of("Banana", "Apple", "Cherry"), keys);
    }

    @Test
    void testOrderAfterUpdate() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("A", 10);

        List<String> keys = new ArrayList<>(map.keySet());
        assertEquals(List.of("A", "B"), keys);
        assertEquals(Integer.valueOf(10), map.get("A"));
    }

    @Test
    void testAccessOrder() {
        LinkedHashMap<String, Integer> accessMap = new LinkedHashMap<>(16, 0.75f, true);
        accessMap.put("A", 1);
        accessMap.put("B", 2);
        accessMap.put("C", 3);

        accessMap.get("A");

        List<String> keys = new ArrayList<>(accessMap.keySet());
        assertEquals(List.of("B", "C", "A"), keys);
    }

    @Test
    void testPutAndGet() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("X", 10);
        assertEquals(Integer.valueOf(10), map.get("X"));
    }

    @Test
    void testGetMissing() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        assertNull(map.get("missing"));
    }

    @Test
    void testPutIfAbsent() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.putIfAbsent("A", 2);
        assertEquals(Integer.valueOf(1), map.get("A"));
        map.putIfAbsent("B", 3);
        assertEquals(Integer.valueOf(3), map.get("B"));
    }

    @Test
    void testRemove() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        Integer removed = map.remove("A");
        assertEquals(Integer.valueOf(1), removed);
        assertFalse(map.containsKey("A"));
    }
}
