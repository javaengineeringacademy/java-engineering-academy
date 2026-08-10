package list.arraylist.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class ArrayListTest {

    @Test
    void testAddAndSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");
        assertEquals(3, list.size());
        list.add(1, "JavaScript");
        assertEquals(4, list.size());
        assertEquals("JavaScript", list.get(1));
    }

    @Test
    void testRemove() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        list.remove("B");
        assertEquals(3, list.size());
        assertFalse(list.contains("B"));
        list.remove(0);
        assertEquals("C", list.get(0));
    }

    @Test
    void testContains() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(list.contains("Java"));
        assertFalse(list.contains("Go"));
        assertTrue(list.containsAll(Arrays.asList("Java", "Python")));
    }

    @Test
    void testIteration() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : list) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        ArrayList<String> list = new ArrayList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    void testGetAndSet() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
        assertEquals("B", list.get(1));
        list.set(1, "X");
        assertEquals("X", list.get(1));
    }

    @Test
    void testIndexOf() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "B"));
        assertEquals(1, list.indexOf("B"));
        assertEquals(3, list.lastIndexOf("B"));
        assertEquals(-1, list.indexOf("Z"));
    }

    @Test
    void testSubList() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        List<String> sub = list.subList(1, 4);
        assertEquals(3, sub.size());
        assertEquals("B", sub.get(0));
        assertEquals("D", sub.get(2));
    }
}
