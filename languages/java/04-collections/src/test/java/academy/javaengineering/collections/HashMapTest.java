package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashMapTest {

    private HashMap<String, Integer> hashMap;

    @BeforeEach
    void setUp() {
        hashMap = new HashMap<>();
    }

    @Test
    void testCreation() {
        HashMap<String, Integer> map = new HashMap<>();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testCreationWithCapacity() {
        HashMap<String, Integer> map = new HashMap<>(16);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testCreationWithLoadFactor() {
        HashMap<String, Integer> map = new HashMap<>(16, 0.75f);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testPut() {
        Integer old = hashMap.put("one", 1);
        assertNull(old);
        assertEquals(1, hashMap.size());
        assertEquals(Integer.valueOf(1), hashMap.get("one"));
    }

    @Test
    void testPutOverwrite() {
        hashMap.put("key", 1);
        Integer old = hashMap.put("key", 2);
        assertEquals(Integer.valueOf(1), old);
        assertEquals(Integer.valueOf(2), hashMap.get("key"));
        assertEquals(1, hashMap.size());
    }

    @Test
    void testPutAll() {
        Map<String, Integer> source = Map.of("a", 1, "b", 2);
        hashMap.putAll(source);
        assertEquals(2, hashMap.size());
        assertEquals(Integer.valueOf(1), hashMap.get("a"));
        assertEquals(Integer.valueOf(2), hashMap.get("b"));
    }

    @Test
    void testGet() {
        hashMap.put("key", 100);
        assertEquals(Integer.valueOf(100), hashMap.get("key"));
    }

    @Test
    void testGetNonExistent() {
        assertNull(hashMap.get("missing"));
    }

    @Test
    void testGetOrDefault() {
        hashMap.put("exists", 1);
        assertEquals(Integer.valueOf(1), hashMap.getOrDefault("exists", 0));
        assertEquals(Integer.valueOf(0), hashMap.getOrDefault("missing", 0));
    }

    @Test
    void testContainsKey() {
        hashMap.put("A", 1);
        assertTrue(hashMap.containsKey("A"));
        assertFalse(hashMap.containsKey("B"));
    }

    @Test
    void testContainsValue() {
        hashMap.put("A", 1);
        assertTrue(hashMap.containsValue(1));
        assertFalse(hashMap.containsValue(2));
    }

    @Test
    void testRemove() {
        hashMap.put("A", 1);
        Integer removed = hashMap.remove("A");
        assertEquals(Integer.valueOf(1), removed);
        assertEquals(0, hashMap.size());
    }

    @Test
    void testRemoveNonExistent() {
        assertNull(hashMap.remove("missing"));
    }

    @Test
    void testRemoveKeyValue() {
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        boolean removed = hashMap.remove("A", 1);
        assertTrue(removed);
        assertEquals(1, hashMap.size());
        assertFalse(hashMap.containsKey("A"));
    }

    @Test
    void testRemoveKeyValueMismatch() {
        hashMap.put("A", 1);
        boolean removed = hashMap.remove("A", 2);
        assertFalse(removed);
        assertEquals(1, hashMap.size());
        assertTrue(hashMap.containsKey("A"));
    }

    @Test
    void testSize() {
        assertEquals(0, hashMap.size());
        hashMap.put("A", 1);
        assertEquals(1, hashMap.size());
        hashMap.put("B", 2);
        assertEquals(2, hashMap.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(hashMap.isEmpty());
        hashMap.put("A", 1);
        assertFalse(hashMap.isEmpty());
    }

    @Test
    void testClear() {
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        hashMap.clear();
        assertEquals(0, hashMap.size());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    void testKeySet() {
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        assertEquals(2, hashMap.keySet().size());
        assertTrue(hashMap.keySet().contains("A"));
        assertTrue(hashMap.keySet().contains("B"));
    }

    @Test
    void testValues() {
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        assertEquals(2, hashMap.values().size());
        assertTrue(hashMap.values().contains(1));
        assertTrue(hashMap.values().contains(2));
    }

    @Test
    void testEntrySet() {
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        assertEquals(2, hashMap.entrySet().size());
    }

    @Test
    void testPutIfAbsent() {
        hashMap.put("A", 1);
        hashMap.putIfAbsent("A", 2);
        assertEquals(Integer.valueOf(1), hashMap.get("A"));
        hashMap.putIfAbsent("B", 3);
        assertEquals(Integer.valueOf(3), hashMap.get("B"));
    }

    @Test
    void testReplace() {
        hashMap.put("A", 1);
        Integer old = hashMap.replace("A", 10);
        assertEquals(Integer.valueOf(1), old);
        assertEquals(Integer.valueOf(10), hashMap.get("A"));
    }

    @Test
    void testReplaceKeyValue() {
        hashMap.put("A", 1);
        boolean replaced = hashMap.replace("A", 1, 10);
        assertTrue(replaced);
        assertEquals(Integer.valueOf(10), hashMap.get("A"));
    }

    @Test
    void testReplaceKeyValueMismatch() {
        hashMap.put("A", 1);
        boolean replaced = hashMap.replace("A", 2, 10);
        assertFalse(replaced);
        assertEquals(Integer.valueOf(1), hashMap.get("A"));
    }

    @Test
    void testCompute() {
        hashMap.put("A", 1);
        hashMap.compute("A", (k, v) -> v + 10);
        assertEquals(Integer.valueOf(11), hashMap.get("A"));
    }

    @Test
    void testComputeIfAbsent() {
        hashMap.computeIfAbsent("A", k -> 100);
        assertEquals(Integer.valueOf(100), hashMap.get("A"));
        hashMap.computeIfAbsent("A", k -> 200);
        assertEquals(Integer.valueOf(100), hashMap.get("A"));
    }

    @Test
    void testComputeIfPresent() {
        hashMap.put("A", 1);
        hashMap.computeIfPresent("A", (k, v) -> v + 1);
        assertEquals(Integer.valueOf(2), hashMap.get("A"));
        hashMap.computeIfPresent("B", (k, v) -> v + 1);
        assertNull(hashMap.get("B"));
    }

    @Test
    void testMerge() {
        hashMap.put("A", 1);
        hashMap.merge("A", 10, Integer::sum);
        assertEquals(Integer.valueOf(11), hashMap.get("A"));
        hashMap.merge("B", 5, Integer::sum);
        assertEquals(Integer.valueOf(5), hashMap.get("B"));
    }

    @Test
    void testPutNullKey() {
        hashMap.put(null, 1);
        assertEquals(Integer.valueOf(1), hashMap.get(null));
        assertEquals(1, hashMap.size());
    }

    @Test
    void testPutNullValue() {
        hashMap.put("A", null);
        assertNull(hashMap.get("A"));
        assertTrue(hashMap.containsKey("A"));
    }

    @Test
    void testContainsKeyAfterRemove() {
        hashMap.put("A", 1);
        hashMap.remove("A");
        assertFalse(hashMap.containsKey("A"));
    }

    @Test
    void testToString() {
        hashMap.put("A", 1);
        assertNotNull(hashMap.toString());
    }

    @Test
    void testEquals() {
        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);
        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("A", 1);
        map2.put("B", 2);
        assertEquals(map1, map2);
    }

    @Test
    void testClone() {
        hashMap.put("A", 1);
        @SuppressWarnings("unchecked")
        HashMap<String, Integer> cloned = (HashMap<String, Integer>) hashMap.clone();
        assertEquals(hashMap, cloned);
        cloned.put("B", 2);
        assertFalse(hashMap.containsKey("B"));
    }

    @Test
    void testForEach() {
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        int sum = 0;
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            sum += entry.getValue();
        }
        assertEquals(3, sum);
    }

    @Test
    void testLargeMap() {
        for (int i = 0; i < 1000; i++) {
            hashMap.put("key" + i, i);
        }
        assertEquals(1000, hashMap.size());
        assertEquals(Integer.valueOf(999), hashMap.get("key999"));
    }
}
