package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedListTest {

    private LinkedList<String> linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new LinkedList<>();
    }

    @Test
    void testCreation() {
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testAddFirst() {
        linkedList.addFirst("B");
        linkedList.addFirst("A");
        assertEquals(2, linkedList.size());
        assertEquals("A", linkedList.getFirst());
        assertEquals("B", linkedList.get(1));
    }

    @Test
    void testAddLast() {
        linkedList.addLast("A");
        linkedList.addLast("B");
        assertEquals(2, linkedList.size());
        assertEquals("A", linkedList.getFirst());
        assertEquals("B", linkedList.getLast());
    }

    @Test
    void testAdd() {
        linkedList.add("A");
        linkedList.add("B");
        assertEquals(2, linkedList.size());
        assertEquals("A", linkedList.get(0));
        assertEquals("B", linkedList.get(1));
    }

    @Test
    void testAddAtIndex() {
        linkedList.add("A");
        linkedList.add("C");
        linkedList.add(1, "B");
        assertEquals(3, linkedList.size());
        assertEquals("A", linkedList.get(0));
        assertEquals("B", linkedList.get(1));
        assertEquals("C", linkedList.get(2));
    }

    @Test
    void testGetFirstAndLast() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        assertEquals("A", linkedList.getFirst());
        assertEquals("C", linkedList.getLast());
    }

    @Test
    void testGetFirstOnEmpty() {
        assertThrows(java.util.NoSuchElementException.class, () -> linkedList.getFirst());
    }

    @Test
    void testGetLastOnEmpty() {
        assertThrows(java.util.NoSuchElementException.class, () -> linkedList.getLast());
    }

    @Test
    void testRemoveFirst() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        String removed = linkedList.removeFirst();
        assertEquals("A", removed);
        assertEquals(2, linkedList.size());
        assertEquals("B", linkedList.getFirst());
    }

    @Test
    void testRemoveLast() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        String removed = linkedList.removeLast();
        assertEquals("C", removed);
        assertEquals(2, linkedList.size());
        assertEquals("B", linkedList.getLast());
    }

    @Test
    void testRemoveFirstOnEmpty() {
        assertThrows(java.util.NoSuchElementException.class, () -> linkedList.removeFirst());
    }

    @Test
    void testRemoveLastOnEmpty() {
        assertThrows(java.util.NoSuchElementException.class, () -> linkedList.removeLast());
    }

    @Test
    void testRemove() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        boolean removed = linkedList.remove("B");
        assertTrue(removed);
        assertEquals(2, linkedList.size());
    }

    @Test
    void testRemoveNonExistent() {
        linkedList.add("A");
        boolean removed = linkedList.remove("Z");
        assertFalse(removed);
    }

    @Test
    void testContains() {
        linkedList.add("X");
        linkedList.add("Y");
        assertTrue(linkedList.contains("X"));
        assertFalse(linkedList.contains("Z"));
    }

    @Test
    void testSize() {
        assertEquals(0, linkedList.size());
        linkedList.add("A");
        assertEquals(1, linkedList.size());
        linkedList.removeFirst();
        assertEquals(0, linkedList.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(linkedList.isEmpty());
        linkedList.add("A");
        assertFalse(linkedList.isEmpty());
        linkedList.removeFirst();
        assertTrue(linkedList.isEmpty());
    }

    @Test
    void testClear() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.clear();
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.size());
    }

    @Test
    void testGet() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        assertEquals("B", linkedList.get(1));
    }

    @Test
    void testGetInvalidIndex() {
        linkedList.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(5));
    }

    @Test
    void testSet() {
        linkedList.add("A");
        linkedList.add("B");
        String old = linkedList.set(0, "X");
        assertEquals("A", old);
        assertEquals("X", linkedList.get(0));
    }

    @Test
    void testIndexOf() {
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("A");
        assertEquals(0, linkedList.indexOf("A"));
        assertEquals(2, linkedList.lastIndexOf("A"));
    }

    @Test
    void testPoll() {
        linkedList.add("A");
        linkedList.add("B");
        String polled = linkedList.poll();
        assertEquals("A", polled);
        assertEquals(1, linkedList.size());
    }

    @Test
    void testPollEmpty() {
        assertNull(linkedList.poll());
    }

    @Test
    void testPeek() {
        linkedList.add("A");
        linkedList.add("B");
        assertEquals("A", linkedList.peek());
        assertEquals(2, linkedList.size());
    }

    @Test
    void testPeekEmpty() {
        assertNull(linkedList.peek());
    }

    @Test
    void testOffer() {
        assertTrue(linkedList.offer("A"));
        assertTrue(linkedList.offer("B"));
        assertEquals(2, linkedList.size());
        assertEquals("A", linkedList.peek());
    }

    @Test
    void testPushAndPop() {
        linkedList.push("B");
        linkedList.push("A");
        assertEquals("A", linkedList.pop());
        assertEquals("B", linkedList.pop());
        assertTrue(linkedList.isEmpty());
    }

    @Test
    void testAddNull() {
        linkedList.add(null);
        linkedList.add("A");
        assertNull(linkedList.get(0));
        assertEquals("A", linkedList.get(1));
    }

    @Test
    void testToString() {
        linkedList.add("A");
        linkedList.add("B");
        assertEquals("[A, B]", linkedList.toString());
    }

    @Test
    void testEquals() {
        LinkedList<String> list1 = new LinkedList<>();
        list1.add("A");
        list1.add("B");
        LinkedList<String> list2 = new LinkedList<>();
        list2.add("A");
        list2.add("B");
        assertEquals(list1, list2);
    }

    @Test
    void testRemoveWhileIterating() {
        linkedList.addAll(java.util.Arrays.asList("A", "B", "C", "D"));
        var it = linkedList.iterator();
        while (it.hasNext()) {
            if (it.next().equals("B")) {
                it.remove();
            }
        }
        assertEquals(3, linkedList.size());
        assertFalse(linkedList.contains("B"));
    }

    @Test
    void testAddMultipleAndRemoveAll() {
        linkedList.addAll(java.util.Arrays.asList("A", "B", "C"));
        linkedList.clear();
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.size());
    }

    @Test
    void testPeekFirstAndLast() {
        linkedList.addAll(java.util.Arrays.asList("A", "B", "C"));
        assertEquals("A", linkedList.peekFirst());
        assertEquals("C", linkedList.peekLast());
    }

    @Test
    void testOfferFirstAndLast() {
        linkedList.offerLast("B");
        linkedList.offerFirst("A");
        assertEquals("A", linkedList.peekFirst());
        assertEquals("B", linkedList.peekLast());
    }
}
