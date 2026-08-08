import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.Test;

class CopyOnWriteArrayListTest {

    @Test
    void testCreation() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    void testCreationFromCollection() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
    }

    @Test
    void testAddElement() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Hello");
        assertEquals(1, list.size());
        assertEquals("Hello", list.get(0));
    }

    @Test
    void testAddElementAtIndex() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("C");
        list.add(1, "B");
        assertEquals(3, list.size());
        assertEquals("B", list.get(1));
    }

    @Test
    void testSetElement() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.set(0, "X");
        assertEquals("X", list.get(0));
    }

    @Test
    void testRemoveByObject() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        boolean removed = list.remove("B");
        assertTrue(removed);
        assertEquals(2, list.size());
    }

    @Test
    void testRemoveByIndex() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        String removed = list.remove(0);
        assertEquals("A", removed);
        assertEquals(1, list.size());
    }

    @Test
    void testContains() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Apple");
        list.add("Banana");
        assertTrue(list.contains("Apple"));
        assertFalse(list.contains("Cherry"));
    }

    @Test
    void testIteratorDoesNotThrowConcurrentModification() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        // This should NOT throw ConcurrentModificationException
        assertDoesNotThrow(() -> {
            for (String s : list) {
                list.add("D");
            }
        });
    }

    @Test
    void testIteratorSeesSnapshot() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");

        Iterator<String> it = list.iterator();
        list.add("C");

        // Iterator should see snapshot without "C"
        java.util.List<String> seen = new java.util.ArrayList<>();
        while (it.hasNext()) {
            seen.add(it.next());
        }
        assertEquals(2, seen.size());
        assertFalse(seen.contains("C"));
    }

    @Test
    void testAddAll() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.addAll(List.of("A", "B", "C"));
        assertEquals(3, list.size());
    }

    @Test
    void testClear() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    void testIsEmpty() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        assertTrue(list.isEmpty());
        list.add("A");
        assertFalse(list.isEmpty());
    }
}
