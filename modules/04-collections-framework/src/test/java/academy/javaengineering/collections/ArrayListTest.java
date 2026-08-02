package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayListTest {

    private ArrayList<String> arrayList;
    private ArrayList<Integer> intList;

    @BeforeEach
    void setUp() {
        arrayList = new ArrayList<>();
        intList = new ArrayList<>();
    }

    @Test
    void testCreationWithDefaultCapacity() {
        ArrayList<String> list = new ArrayList<>();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    void testCreationWithInitialCapacity() {
        ArrayList<String> list = new ArrayList<>(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    void testCreationWithCollection() {
        List<String> source = Arrays.asList("A", "B", "C");
        ArrayList<String> list = new ArrayList<>(source);
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void testAddElement() {
        arrayList.add("Hello");
        assertEquals(1, arrayList.size());
        assertEquals("Hello", arrayList.get(0));
    }

    @Test
    void testAddElementAtIndex() {
        arrayList.add("A");
        arrayList.add("C");
        arrayList.add(1, "B");
        assertEquals(3, arrayList.size());
        assertEquals("A", arrayList.get(0));
        assertEquals("B", arrayList.get(1));
        assertEquals("C", arrayList.get(2));
    }

    @Test
    void testAddAll() {
        arrayList.add("A");
        List<String> more = Arrays.asList("B", "C", "D");
        arrayList.addAll(more);
        assertEquals(4, arrayList.size());
        assertEquals(Arrays.asList("A", "B", "C", "D"), arrayList);
    }

    @Test
    void testGetElement() {
        arrayList.add("First");
        arrayList.add("Second");
        assertEquals("First", arrayList.get(0));
        assertEquals("Second", arrayList.get(1));
    }

    @Test
    void testGetElementWithInvalidIndex() {
        arrayList.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(5));
    }

    @Test
    void testSetElement() {
        arrayList.add("A");
        arrayList.add("B");
        String old = arrayList.set(0, "X");
        assertEquals("A", old);
        assertEquals("X", arrayList.get(0));
        assertEquals("B", arrayList.get(1));
    }

    @Test
    void testRemoveElement() {
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        boolean removed = arrayList.remove("B");
        assertTrue(removed);
        assertEquals(2, arrayList.size());
        assertEquals("A", arrayList.get(0));
        assertEquals("C", arrayList.get(1));
    }

    @Test
    void testRemoveElementAtIndex() {
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        String removed = arrayList.remove(1);
        assertEquals("B", removed);
        assertEquals(2, arrayList.size());
    }

    @Test
    void testRemoveNonExistentElement() {
        arrayList.add("A");
        boolean removed = arrayList.remove("Z");
        assertFalse(removed);
        assertEquals(1, arrayList.size());
    }

    @Test
    void testContains() {
        arrayList.add("Apple");
        arrayList.add("Banana");
        assertTrue(arrayList.contains("Apple"));
        assertTrue(arrayList.contains("Banana"));
        assertFalse(arrayList.contains("Cherry"));
    }

    @Test
    void testIndexOf() {
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("A");
        assertEquals(0, arrayList.indexOf("A"));
        assertEquals(1, arrayList.indexOf("B"));
        assertEquals(-1, arrayList.indexOf("Z"));
    }

    @Test
    void testLastIndexOf() {
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("A");
        assertEquals(2, arrayList.lastIndexOf("A"));
        assertEquals(1, arrayList.lastIndexOf("B"));
    }

    @Test
    void testSize() {
        assertEquals(0, arrayList.size());
        arrayList.add("A");
        assertEquals(1, arrayList.size());
        arrayList.add("B");
        assertEquals(2, arrayList.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(arrayList.isEmpty());
        arrayList.add("A");
        assertFalse(arrayList.isEmpty());
    }

    @Test
    void testClear() {
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        arrayList.clear();
        assertEquals(0, arrayList.size());
        assertTrue(arrayList.isEmpty());
    }

    @Test
    void testSubList() {
        arrayList.addAll(Arrays.asList("A", "B", "C", "D", "E"));
        List<String> sub = arrayList.subList(1, 4);
        assertEquals(3, sub.size());
        assertEquals(Arrays.asList("B", "C", "D"), sub);
    }

    @Test
    void testAddNull() {
        arrayList.add(null);
        arrayList.add("A");
        assertNull(arrayList.get(0));
        assertEquals("A", arrayList.get(1));
    }

    @Test
    void testAddMultipleNulls() {
        arrayList.add(null);
        arrayList.add(null);
        arrayList.add("A");
        assertEquals(3, arrayList.size());
        assertNull(arrayList.get(0));
        assertNull(arrayList.get(1));
    }

    @Test
    void testEquals() {
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("A", "B"));
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("A", "B"));
        assertEquals(list1, list2);
    }

    @Test
    void testToString() {
        arrayList.add("A");
        arrayList.add("B");
        assertEquals("[A, B]", arrayList.toString());
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        arrayList.add("A");
        arrayList.add("B");
        @SuppressWarnings("unchecked")
        ArrayList<String> cloned = (ArrayList<String>) arrayList.clone();
        assertEquals(arrayList, cloned);
        cloned.set(0, "X");
        assertEquals("A", arrayList.get(0));
    }

    @Test
    void testAddAllToEmpty() {
        List<String> source = Arrays.asList("X", "Y", "Z");
        arrayList.addAll(source);
        assertEquals(3, arrayList.size());
        assertEquals(Arrays.asList("X", "Y", "Z"), arrayList);
    }

    @Test
    void testRemoveAll() {
        arrayList.addAll(Arrays.asList("A", "B", "C", "D"));
        List<String> toRemove = Arrays.asList("B", "D");
        arrayList.removeAll(toRemove);
        assertEquals(2, arrayList.size());
        assertEquals(Arrays.asList("A", "C"), arrayList);
    }

    @Test
    void testRetainAll() {
        arrayList.addAll(Arrays.asList("A", "B", "C", "D"));
        List<String> toKeep = Arrays.asList("B", "C", "E");
        arrayList.retainAll(toKeep);
        assertEquals(2, arrayList.size());
        assertEquals(Arrays.asList("B", "C"), arrayList);
    }

    @Test
    void testIntegerList() {
        intList.add(1);
        intList.add(2);
        intList.add(3);
        assertEquals(3, intList.size());
        assertEquals(Integer.valueOf(2), intList.get(1));
    }

    @Test
    void testRemoveFromEmptyList() {
        assertFalse(arrayList.remove("A"));
    }

    @Test
    void testGetFromEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayList.get(0));
    }

    @Test
    void testSetOnEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayList.set(0, "A"));
    }
}
