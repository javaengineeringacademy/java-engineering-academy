import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class HashSetTest {

    @Test
    void testCreation() {
        HashSet<String> set = new HashSet<>();
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    void testAdd() {
        HashSet<String> set = new HashSet<>();
        boolean added = set.add("A");
        assertTrue(added);
        assertEquals(1, set.size());
    }

    @Test
    void testAddDuplicate() {
        HashSet<String> set = new HashSet<>();
        set.add("A");
        boolean added = set.add("A");
        assertFalse(added);
        assertEquals(1, set.size());
    }

    @Test
    void testContains() {
        HashSet<String> set = new HashSet<>(Set.of("A", "B"));
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
        assertFalse(set.contains("C"));
    }

    @Test
    void testRemove() {
        HashSet<String> set = new HashSet<>(Set.of("A", "B"));
        boolean removed = set.remove("A");
        assertTrue(removed);
        assertEquals(1, set.size());
        assertFalse(set.contains("A"));
    }

    @Test
    void testSize() {
        HashSet<String> set = new HashSet<>();
        assertEquals(0, set.size());
        set.add("A");
        assertEquals(1, set.size());
    }

    @Test
    void testIsEmpty() {
        HashSet<String> set = new HashSet<>();
        assertTrue(set.isEmpty());
        set.add("A");
        assertFalse(set.isEmpty());
    }

    @Test
    void testClear() {
        HashSet<String> set = new HashSet<>(Set.of("A", "B", "C"));
        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    void testUnion() {
        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Set.of("B", "C", "D"));
        set1.addAll(set2);
        assertEquals(4, set1.size());
        assertEquals(Set.of("A", "B", "C", "D"), set1);
    }

    @Test
    void testIntersection() {
        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Set.of("B", "C", "D"));
        set1.retainAll(set2);
        assertEquals(2, set1.size());
        assertEquals(Set.of("B", "C"), set1);
    }

    @Test
    void testDifference() {
        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Set.of("B", "C", "D"));
        set1.removeAll(set2);
        assertEquals(1, set1.size());
        assertEquals(Set.of("A"), set1);
    }
}
