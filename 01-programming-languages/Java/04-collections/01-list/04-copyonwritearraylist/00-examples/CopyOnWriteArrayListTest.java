package list.copyonwritearraylist.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

class CopyOnWriteArrayListTest {

    @Test
    void testAddAndSize() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");
        assertEquals(3, list.size());
    }

    @Test
    void testRemove() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C"));
        list.remove("B");
        assertEquals(2, list.size());
        assertFalse(list.contains("B"));
    }

    @Test
    void testContains() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(list.contains("Java"));
        assertFalse(list.contains("Go"));
    }

    @Test
    void testIteration() {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : list) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testSet() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C"));
        list.set(1, "X");
        assertEquals("X", list.get(1));
    }

    @Test
    void testIteratorSnapshot() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C"));
        Iterator<String> iterator = list.iterator();
        list.add("D");
        List<String> iterResult = new ArrayList<>();
        while (iterator.hasNext()) iterResult.add(iterator.next());
        assertEquals(3, iterResult.size());
        assertEquals(4, list.size());
    }
}
