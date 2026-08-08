import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class LinkedHashSetDemoTest {

    @Test
    void testInsertionOrder() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("Charlie");
        set.add("Alice");
        set.add("Bob");

        List<String> iterationOrder = new ArrayList<>(set);
        assertEquals(List.of("Charlie", "Alice", "Bob"), iterationOrder);
    }

    @Test
    void testAddNoDuplicates() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        assertTrue(set.add("A"));
        assertTrue(set.add("B"));
        assertFalse(set.add("A"));
        assertEquals(2, set.size());
    }

    @Test
    void testRemove() {
        LinkedHashSet<String> set = new LinkedHashSet<>(Set.of("A", "B"));
        assertTrue(set.remove("A"));
        assertFalse(set.contains("A"));
        assertEquals(1, set.size());
    }

    @Test
    void testContains() {
        LinkedHashSet<String> set = new LinkedHashSet<>(Set.of("X"));
        assertTrue(set.contains("X"));
        assertFalse(set.contains("Y"));
    }

    @Test
    void testClear() {
        LinkedHashSet<String> set = new LinkedHashSet<>(Set.of("A", "B"));
        set.clear();
        assertTrue(set.isEmpty());
    }

    @Test
    void testIterationOrder() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("First");
        set.add("Second");
        set.add("Third");

        Iterator<String> it = set.iterator();
        assertEquals("First", it.next());
        assertEquals("Second", it.next());
        assertEquals("Third", it.next());
    }
}
