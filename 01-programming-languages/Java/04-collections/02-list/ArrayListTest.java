import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ArrayListTest {

    @Test
    void testCreationWithDefaultCapacity() {
        ArrayList<String> list = new ArrayList<>();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    void testAddElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        assertEquals(1, list.size());
        assertEquals("Hello", list.get(0));
    }

    @Test
    void testAddElementAtIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("C");
        list.add(1, "B");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void testGetElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("First");
        list.add("Second");
        assertEquals("First", list.get(0));
        assertEquals("Second", list.get(1));
    }

    @Test
    void testGetElementWithInvalidIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    }

    @Test
    void testSetElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        String old = list.set(0, "X");
        assertEquals("A", old);
        assertEquals("X", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    void testRemoveElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        boolean removed = list.remove("B");
        assertTrue(removed);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    void testRemoveElementAtIndex() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(2, list.size());
    }

    @Test
    void testContains() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        assertTrue(list.contains("Apple"));
        assertTrue(list.contains("Banana"));
        assertFalse(list.contains("Cherry"));
    }

    @Test
    void testIndexOf() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("A");
        assertEquals(0, list.indexOf("A"));
        assertEquals(1, list.indexOf("B"));
        assertEquals(-1, list.indexOf("Z"));
    }

    @Test
    void testSize() {
        ArrayList<String> list = new ArrayList<>();
        assertEquals(0, list.size());
        list.add("A");
        assertEquals(1, list.size());
        list.add("B");
        assertEquals(2, list.size());
    }

    @Test
    void testIsEmpty() {
        ArrayList<String> list = new ArrayList<>();
        assertTrue(list.isEmpty());
        list.add("A");
        assertFalse(list.isEmpty());
    }

    @Test
    void testClear() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void testSubList() {
        ArrayList<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        List<String> sub = list.subList(1, 4);
        assertEquals(3, sub.size());
        assertEquals(List.of("B", "C", "D"), sub);
    }
}
