import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class TreeSetTest {

    @Test
    void testCreation() {
        TreeSet<String> set = new TreeSet<>();
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void testAdd() {
        TreeSet<String> set = new TreeSet<>();
        set.add("B");
        set.add("A");
        set.add("C");
        assertEquals(3, set.size());
        assertEquals("A", set.first());
        assertEquals("C", set.last());
    }

    @Test
    void testAddDuplicate() {
        TreeSet<String> set = new TreeSet<>();
        set.add("A");
        boolean added = set.add("A");
        assertFalse(added);
        assertEquals(1, set.size());
    }

    @Test
    void testContains() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "B"));
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
        assertFalse(set.contains("C"));
    }

    @Test
    void testRemove() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "B", "C"));
        boolean removed = set.remove("B");
        assertTrue(removed);
        assertEquals(2, set.size());
    }

    @Test
    void testFirst() {
        TreeSet<String> set = new TreeSet<>(Set.of("C", "A", "B"));
        assertEquals("A", set.first());
    }

    @Test
    void testLast() {
        TreeSet<String> set = new TreeSet<>(Set.of("C", "A", "B"));
        assertEquals("C", set.last());
    }

    @Test
    void testHigher() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "C", "E"));
        assertEquals("C", set.higher("A"));
        assertEquals("E", set.higher("C"));
        assertEquals(null, set.higher("E"));
    }

    @Test
    void testLower() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "C", "E"));
        assertEquals("A", set.lower("C"));
        assertEquals("C", set.lower("E"));
        assertEquals(null, set.lower("A"));
    }

    @Test
    void testCeiling() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "C", "E"));
        assertEquals("C", set.ceiling("B"));
        assertEquals("A", set.ceiling("A"));
    }

    @Test
    void testFloor() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "C", "E"));
        assertEquals("A", set.floor("B"));
        assertEquals("C", set.floor("C"));
    }

    @Test
    void testHeadSet() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "B", "C", "D"));
        Set<String> head = set.headSet("C");
        assertEquals(2, head.size());
        assertTrue(head.contains("A"));
        assertTrue(head.contains("B"));
    }

    @Test
    void testTailSet() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "B", "C", "D"));
        Set<String> tail = set.tailSet("B");
        assertEquals(3, tail.size());
        assertTrue(tail.contains("B"));
        assertTrue(tail.contains("C"));
        assertTrue(tail.contains("D"));
    }

    @Test
    void testDescendingSet() {
        TreeSet<String> set = new TreeSet<>(Set.of("A", "B", "C"));
        TreeSet<String> desc = (TreeSet<String>) set.descendingSet();
        assertEquals("C", desc.first());
        assertEquals("A", desc.last());
    }
}
