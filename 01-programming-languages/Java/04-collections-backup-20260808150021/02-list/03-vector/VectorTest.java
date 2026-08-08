import static org.junit.jupiter.api.Assertions.*;

import java.util.Vector;

import org.junit.jupiter.api.Test;

class VectorTest {

    @Test
    void testCreationWithDefaultCapacity() {
        Vector<String> vector = new Vector<>();
        assertNotNull(vector);
        assertEquals(0, vector.size());
        assertEquals(10, vector.capacity());
    }

    @Test
    void testCreationWithCustomCapacity() {
        Vector<Integer> vector = new Vector<>(50);
        assertEquals(0, vector.size());
        assertEquals(50, vector.capacity());
    }

    @Test
    void testAddElement() {
        Vector<String> vector = new Vector<>();
        vector.add("Hello");
        assertEquals(1, vector.size());
        assertEquals("Hello", vector.get(0));
    }

    @Test
    void testAddElementAtIndex() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("C");
        vector.add(1, "B");
        assertEquals(3, vector.size());
        assertEquals("B", vector.get(1));
    }

    @Test
    void testFirstAndLastElement() {
        Vector<String> vector = new Vector<>();
        vector.add("First");
        vector.add("Middle");
        vector.add("Last");
        assertEquals("First", vector.firstElement());
        assertEquals("Last", vector.lastElement());
    }

    @Test
    void testElementAt() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        vector.add("C");
        assertEquals("B", vector.elementAt(1));
    }

    @Test
    void testRemoveByObject() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        vector.add("C");
        boolean removed = vector.remove("B");
        assertTrue(removed);
        assertEquals(2, vector.size());
    }

    @Test
    void testRemoveByIndex() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        vector.add("C");
        String removed = vector.remove(1);
        assertEquals("B", removed);
        assertEquals(2, vector.size());
    }

    @Test
    void testContains() {
        Vector<String> vector = new Vector<>();
        vector.add("Apple");
        vector.add("Banana");
        assertTrue(vector.contains("Apple"));
        assertFalse(vector.contains("Cherry"));
    }

    @Test
    void testIndexOf() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        vector.add("A");
        assertEquals(0, vector.indexOf("A"));
        assertEquals(1, vector.indexOf("B"));
        assertEquals(-1, vector.indexOf("Z"));
    }

    @Test
    void testSize() {
        Vector<Integer> vector = new Vector<>();
        assertEquals(0, vector.size());
        vector.add(1);
        assertEquals(1, vector.size());
    }

    @Test
    void testIsEmpty() {
        Vector<String> vector = new Vector<>();
        assertTrue(vector.isEmpty());
        vector.add("A");
        assertFalse(vector.isEmpty());
    }

    @Test
    void testClear() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        vector.clear();
        assertEquals(0, vector.size());
        assertTrue(vector.isEmpty());
    }

    @Test
    void testCapacityGrowth() {
        Vector<Integer> vector = new Vector<>(2);
        vector.add(1);
        vector.add(2);
        vector.add(3);
        assertTrue(vector.capacity() > 2);
        assertEquals(3, vector.size());
    }

    @Test
    void testTrimToSize() {
        Vector<Integer> vector = new Vector<>(100);
        vector.add(1);
        vector.add(2);
        vector.trimToSize();
        assertEquals(2, vector.capacity());
    }

    @Test
    void testElementsEnumeration() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        java.util.Enumeration<String> enumElements = vector.elements();
        assertTrue(enumElements.hasMoreElements());
        assertEquals("A", enumElements.nextElement());
        assertEquals("B", enumElements.nextElement());
        assertFalse(enumElements.hasMoreElements());
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        Vector<Integer> vector = new Vector<>();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    vector.add(j);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        assertEquals(1000, vector.size());
    }
}
