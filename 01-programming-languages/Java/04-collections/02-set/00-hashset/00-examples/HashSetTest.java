package set.hashset.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class HashSetTest {

    @Test
    void testAddAndSize() {
        HashSet<String> set = new HashSet<>();
        set.add("Java");
        set.add("Python");
        set.add("C++");
        assertEquals(3, set.size());
        set.add("Java");
        assertEquals(3, set.size());
    }

    @Test
    void testRemove() {
        HashSet<String> set = new HashSet<>(Arrays.asList("A", "B", "C"));
        set.remove("B");
        assertEquals(2, set.size());
        assertFalse(set.contains("B"));
    }

    @Test
    void testContains() {
        HashSet<String> set = new HashSet<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(set.contains("Java"));
        assertFalse(set.contains("Go"));
    }

    @Test
    void testIteration() {
        HashSet<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : set) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        HashSet<String> set = new HashSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testDuplicatesRejected() {
        HashSet<String> set = new HashSet<>();
        assertTrue(set.add("A"));
        assertFalse(set.add("A"));
        assertEquals(1, set.size());
    }

    @Test
    void testSetOperations() {
        HashSet<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3));
        HashSet<Integer> set2 = new HashSet<>(Arrays.asList(2, 3, 4));
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        assertEquals(4, union.size());
        assertTrue(union.contains(1));
        assertTrue(union.contains(4));
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        assertEquals(2, intersection.size());
        assertTrue(intersection.contains(2));
        assertTrue(intersection.contains(3));
    }

    @Test
    void testAddAll() {
        HashSet<String> set = new HashSet<>();
        set.addAll(Arrays.asList("A", "B", "C"));
        assertEquals(3, set.size());
        assertTrue(set.containsAll(Arrays.asList("A", "B", "C")));
    }
}
