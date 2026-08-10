package list.vector.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class VectorTest {

    @Test
    void testAddAndSize() {
        Vector<String> vector = new Vector<>();
        vector.add("Java");
        vector.add("Python");
        vector.add("C++");
        assertEquals(3, vector.size());
    }

    @Test
    void testRemove() {
        Vector<String> vector = new Vector<>(Arrays.asList("A", "B", "C"));
        vector.remove("B");
        assertEquals(2, vector.size());
        assertFalse(vector.contains("B"));
        vector.remove(0);
        assertEquals("C", vector.get(0));
    }

    @Test
    void testContains() {
        Vector<String> vector = new Vector<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(vector.contains("Java"));
        assertFalse(vector.contains("Go"));
    }

    @Test
    void testIteration() {
        Vector<Integer> vector = new Vector<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : vector) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        Vector<String> vector = new Vector<>();
        assertTrue(vector.isEmpty());
        assertEquals(0, vector.size());
    }

    @Test
    void testGetAndSet() {
        Vector<String> vector = new Vector<>(Arrays.asList("A", "B", "C"));
        assertEquals("B", vector.get(1));
        vector.set(1, "X");
        assertEquals("X", vector.get(1));
    }

    @Test
    void testCapacity() {
        Vector<Integer> vector = new Vector<>(5);
        assertEquals(5, vector.capacity());
        for (int i = 0; i < 20; i++) vector.add(i);
        assertTrue(vector.capacity() >= 20);
        assertEquals(20, vector.size());
    }

    @Test
    void testIndexOf() {
        Vector<String> vector = new Vector<>(Arrays.asList("A", "B", "C", "B"));
        assertEquals(1, vector.indexOf("B"));
        assertEquals(3, vector.lastIndexOf("B"));
        assertEquals(-1, vector.indexOf("Z"));
    }

    @Test
    void testEnumeration() {
        Vector<String> vector = new Vector<>(Arrays.asList("A", "B", "C"));
        Enumeration<String> enumeration = vector.elements();
        List<String> result = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            result.add(enumeration.nextElement());
        }
        assertEquals(Arrays.asList("A", "B", "C"), result);
    }
}
