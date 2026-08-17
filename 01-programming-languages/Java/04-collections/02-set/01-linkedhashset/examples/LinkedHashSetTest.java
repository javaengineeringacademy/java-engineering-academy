package set.linkedhashset.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class LinkedHashSetTest {

    @Test
    void testAddAndSize() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("Java");
        set.add("Python");
        set.add("C++");
        assertEquals(3, set.size());
        set.add("Java");
        assertEquals(3, set.size());
    }

    @Test
    void testRemove() {
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));
        set.remove("B");
        assertEquals(2, set.size());
        assertFalse(set.contains("B"));
    }

    @Test
    void testContains() {
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(set.contains("Java"));
        assertFalse(set.contains("Go"));
    }

    @Test
    void testIteration() {
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));
        Iterator<String> iterator = set.iterator();
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
    }

    @Test
    void testEdgeCases() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testInsertionOrder() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("Banana");
        set.add("Apple");
        set.add("Cherry");
        List<String> ordered = new ArrayList<>(set);
        assertEquals(Arrays.asList("Banana", "Apple", "Cherry"), ordered);
    }

    @Test
    void testDuplicatesRejected() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        assertTrue(set.add("A"));
        assertFalse(set.add("A"));
        assertEquals(1, set.size());
    }

    @Test
    void testSetOperations() {
        LinkedHashSet<Integer> set1 = new LinkedHashSet<>(Arrays.asList(1, 2, 3));
        LinkedHashSet<Integer> set2 = new LinkedHashSet<>(Arrays.asList(2, 3, 4));
        Set<Integer> union = new LinkedHashSet<>(set1);
        union.addAll(set2);
        assertEquals(4, union.size());
        Set<Integer> intersection = new LinkedHashSet<>(set1);
        intersection.retainAll(set2);
        assertEquals(2, intersection.size());
    }
}
