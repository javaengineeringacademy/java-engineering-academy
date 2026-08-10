package set.treeset.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class TreeSetTest {

    @Test
    void testAddAndSize() {
        TreeSet<String> set = new TreeSet<>();
        set.add("Java");
        set.add("Python");
        set.add("C++");
        assertEquals(3, set.size());
        set.add("Java");
        assertEquals(3, set.size());
    }

    @Test
    void testRemove() {
        TreeSet<String> set = new TreeSet<>(Arrays.asList("A", "B", "C"));
        set.remove("B");
        assertEquals(2, set.size());
        assertFalse(set.contains("B"));
    }

    @Test
    void testContains() {
        TreeSet<String> set = new TreeSet<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(set.contains("Java"));
        assertFalse(set.contains("Go"));
    }

    @Test
    void testIteration() {
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(5, 1, 3, 2, 4));
        int prev = Integer.MIN_VALUE;
        for (int n : set) {
            assertTrue(n > prev);
            prev = n;
        }
    }

    @Test
    void testEdgeCases() {
        TreeSet<String> set = new TreeSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testSortedOrder() {
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(5, 1, 3, 2, 4));
        assertEquals(1, set.first());
        assertEquals(5, set.last());
    }

    @Test
    void testNavigableSetMethods() {
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(10, 20, 30, 40, 50));
        assertEquals(20, set.lower(25));
        assertEquals(30, set.higher(25));
        assertEquals(25, set.ceiling(25));
        assertEquals(20, set.floor(25));
    }

    @Test
    void testHeadSetTailSet() {
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(10, 20, 30, 40, 50));
        assertEquals(2, set.headSet(30).size());
        assertEquals(3, set.tailSet(30).size());
    }
}
