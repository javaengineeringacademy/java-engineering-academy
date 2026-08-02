package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TreeMapTest {

    private TreeMap<String, Integer> treeMap;

    @BeforeEach
    void setUp() {
        treeMap = new TreeMap<>();
    }

    @Test
    void testCreation() {
        TreeMap<String, Integer> map = new TreeMap<>();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testCreationWithComparator() {
        TreeMap<String, Integer> map = new TreeMap<>(Comparator.reverseOrder());
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    void testCreationWithSortedKeys() {
        TreeMap<String, Integer> map = new TreeMap<>(Map.of("C", 3, "A", 1, "B", 2));
        assertEquals(3, map.size());
        assertEquals("A", map.firstKey());
        assertEquals("C", map.lastKey());
    }

    @Test
    void testPut() {
        treeMap.put("Banana", 2);
        treeMap.put("Apple", 1);
        treeMap.put("Cherry", 3);
        assertEquals(3, treeMap.size());
        assertEquals(Integer.valueOf(1), treeMap.get("Apple"));
    }

    @Test
    void testPutOverwrite() {
        treeMap.put("A", 1);
        Integer old = treeMap.put("A", 2);
        assertEquals(Integer.valueOf(1), old);
        assertEquals(Integer.valueOf(2), treeMap.get("A"));
    }

    @Test
    void testGet() {
        treeMap.put("A", 10);
        assertEquals(Integer.valueOf(10), treeMap.get("A"));
    }

    @Test
    void testGetNonExistent() {
        assertNull(treeMap.get("missing"));
    }

    @Test
    void testContainsKey() {
        treeMap.put("A", 1);
        assertTrue(treeMap.containsKey("A"));
        assertFalse(treeMap.containsKey("B"));
    }

    @Test
    void testContainsValue() {
        treeMap.put("A", 1);
        assertTrue(treeMap.containsValue(1));
        assertFalse(treeMap.containsValue(2));
    }

    @Test
    void testRemove() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        Integer removed = treeMap.remove("A");
        assertEquals(Integer.valueOf(1), removed);
        assertEquals(1, treeMap.size());
    }

    @Test
    void testRemoveNonExistent() {
        assertNull(treeMap.remove("missing"));
    }

    @Test
    void testSize() {
        assertEquals(0, treeMap.size());
        treeMap.put("A", 1);
        assertEquals(1, treeMap.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(treeMap.isEmpty());
        treeMap.put("A", 1);
        assertFalse(treeMap.isEmpty());
    }

    @Test
    void testClear() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.clear();
        assertEquals(0, treeMap.size());
        assertTrue(treeMap.isEmpty());
    }

    @Test
    void testFirstKey() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        assertEquals("A", treeMap.firstKey());
    }

    @Test
    void testLastKey() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        assertEquals("C", treeMap.lastKey());
    }

    @Test
    void testFirstEntry() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        Map.Entry<String, Integer> first = treeMap.firstEntry();
        assertEquals("A", first.getKey());
        assertEquals(Integer.valueOf(1), first.getValue());
    }

    @Test
    void testLastEntry() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        Map.Entry<String, Integer> last = treeMap.lastEntry();
        assertEquals("C", last.getKey());
        assertEquals(Integer.valueOf(3), last.getValue());
    }

    @Test
    void testPollFirstEntry() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        Map.Entry<String, Integer> polled = treeMap.pollFirstEntry();
        assertEquals("A", polled.getKey());
        assertEquals(2, treeMap.size());
    }

    @Test
    void testPollLastEntry() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        Map.Entry<String, Integer> polled = treeMap.pollLastEntry();
        assertEquals("C", polled.getKey());
        assertEquals(1, treeMap.size());
    }

    @Test
    void testHeadMap() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.put("C", 3);
        treeMap.put("D", 4);
        Map<String, Integer> head = treeMap.headMap("C");
        assertEquals(2, head.size());
        assertTrue(head.containsKey("A"));
        assertTrue(head.containsKey("B"));
        assertFalse(head.containsKey("C"));
    }

    @Test
    void testTailMap() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.put("C", 3);
        treeMap.put("D", 4);
        Map<String, Integer> tail = treeMap.tailMap("B");
        assertEquals(3, tail.size());
        assertTrue(tail.containsKey("B"));
        assertTrue(tail.containsKey("C"));
        assertTrue(tail.containsKey("D"));
        assertFalse(tail.containsKey("A"));
    }

    @Test
    void testSubMap() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.put("C", 3);
        treeMap.put("D", 4);
        Map<String, Integer> sub = treeMap.subMap("B", "D");
        assertEquals(2, sub.size());
        assertTrue(sub.containsKey("B"));
        assertTrue(sub.containsKey("C"));
        assertFalse(sub.containsKey("D"));
    }

    @Test
    void testKeySet() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        Object[] keys = treeMap.keySet().toArray();
        assertEquals("A", keys[0]);
        assertEquals("B", keys[1]);
        assertEquals("C", keys[2]);
    }

    @Test
    void testValues() {
        treeMap.put("C", 3);
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        assertEquals(3, treeMap.values().size());
        assertTrue(treeMap.values().contains(1));
        assertTrue(treeMap.values().contains(2));
        assertTrue(treeMap.values().contains(3));
    }

    @Test
    void testEntrySet() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        assertEquals(2, treeMap.entrySet().size());
    }

    @Test
    void testHigherKey() {
        treeMap.put("A", 1);
        treeMap.put("C", 3);
        treeMap.put("E", 5);
        assertEquals("C", treeMap.higherKey("A"));
        assertEquals("E", treeMap.higherKey("C"));
        assertNull(treeMap.higherKey("E"));
    }

    @Test
    void testLowerKey() {
        treeMap.put("A", 1);
        treeMap.put("C", 3);
        treeMap.put("E", 5);
        assertEquals("C", treeMap.lowerKey("E"));
        assertEquals("A", treeMap.lowerKey("C"));
        assertNull(treeMap.lowerKey("A"));
    }

    @Test
    void testHigherEntry() {
        treeMap.put("A", 1);
        treeMap.put("C", 3);
        Map.Entry<String, Integer> higher = treeMap.higherEntry("A");
        assertEquals("C", higher.getKey());
    }

    @Test
    void testLowerEntry() {
        treeMap.put("A", 1);
        treeMap.put("C", 3);
        Map.Entry<String, Integer> lower = treeMap.lowerEntry("C");
        assertEquals("A", lower.getKey());
    }

    @Test
    void testCeilingKey() {
        treeMap.put("A", 1);
        treeMap.put("C", 3);
        assertEquals("C", treeMap.ceilingKey("B"));
        assertEquals("A", treeMap.ceilingKey("A"));
    }

    @Test
    void testFloorKey() {
        treeMap.put("A", 1);
        treeMap.put("C", 3);
        assertEquals("A", treeMap.floorKey("B"));
        assertEquals("C", treeMap.floorKey("C"));
    }

    @Test
    void testDescendingKeySet() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.put("C", 3);
        Object[] keys = treeMap.descendingKeySet().toArray();
        assertEquals("C", keys[0]);
        assertEquals("B", keys[1]);
        assertEquals("A", keys[2]);
    }

    @Test
    void testDescendingMap() {
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.put("C", 3);
        TreeMap<String, Integer> desc = treeMap.descendingMap();
        assertEquals("C", desc.firstKey());
        assertEquals("A", desc.lastKey());
    }

    @Test
    void testPollFirstEntryEmpty() {
        assertNull(treeMap.pollFirstEntry());
    }

    @Test
    void testPollLastEntryEmpty() {
        assertNull(treeMap.pollLastEntry());
    }

    @Test
    void testPutIfAbsent() {
        treeMap.put("A", 1);
        treeMap.putIfAbsent("A", 2);
        assertEquals(Integer.valueOf(1), treeMap.get("A"));
        treeMap.putIfAbsent("B", 3);
        assertEquals(Integer.valueOf(3), treeMap.get("B"));
    }

    @Test
    void testReplace() {
        treeMap.put("A", 1);
        Integer old = treeMap.replace("A", 10);
        assertEquals(Integer.valueOf(1), old);
        assertEquals(Integer.valueOf(10), treeMap.get("A"));
    }

    @Test
    void testToString() {
        treeMap.put("A", 1);
        assertNotNull(treeMap.toString());
    }

    @Test
    void testEquals() {
        TreeMap<String, Integer> map1 = new TreeMap<>();
        map1.put("A", 1);
        TreeMap<String, Integer> map2 = new TreeMap<>();
        map2.put("A", 1);
        assertEquals(map1, map2);
    }

    @Test
    void testNaturalOrdering() {
        treeMap.put("Z", 26);
        treeMap.put("A", 1);
        treeMap.put("M", 13);
        Object[] keys = treeMap.keySet().toArray();
        assertEquals("A", keys[0]);
        assertEquals("M", keys[1]);
        assertEquals("Z", keys[2]);
    }

    @Test
    void testReverseOrdering() {
        TreeMap<String, Integer> reverseMap = new TreeMap<>(Comparator.reverseOrder());
        reverseMap.put("Z", 26);
        reverseMap.put("A", 1);
        reverseMap.put("M", 13);
        Object[] keys = reverseMap.keySet().toArray();
        assertEquals("Z", keys[0]);
        assertEquals("M", keys[1]);
        assertEquals("A", keys[2]);
    }
}
