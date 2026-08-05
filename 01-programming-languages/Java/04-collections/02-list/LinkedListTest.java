import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class LinkedListTest {

    @Test
    void testCreation() {
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testAddFirst() {
        LinkedList<String> list = new LinkedList<>();
        list.addFirst("B");
        list.addFirst("A");
        assertEquals(2, list.size());
        assertEquals("A", list.getFirst());
    }

    @Test
    void testAddLast() {
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        assertEquals(2, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("B", list.getLast());
    }

    @Test
    void testGetFirstAndLast() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B", "C"));
        assertEquals("A", list.getFirst());
        assertEquals("C", list.getLast());
    }

    @Test
    void testRemoveFirst() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B", "C"));
        String removed = list.removeFirst();
        assertEquals("A", removed);
        assertEquals(2, list.size());
        assertEquals("B", list.getFirst());
    }

    @Test
    void testRemoveLast() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B", "C"));
        String removed = list.removeLast();
        assertEquals("C", removed);
        assertEquals(2, list.size());
    }

    @Test
    void testRemove() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B", "C"));
        boolean removed = list.remove("B");
        assertTrue(removed);
        assertEquals(2, list.size());
    }

    @Test
    void testContains() {
        LinkedList<String> list = new LinkedList<>(List.of("X", "Y"));
        assertTrue(list.contains("X"));
        assertFalse(list.contains("Z"));
    }

    @Test
    void testSize() {
        LinkedList<String> list = new LinkedList<>();
        assertEquals(0, list.size());
        list.add("A");
        assertEquals(1, list.size());
    }

    @Test
    void testIsEmpty() {
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty());
        list.add("A");
        assertFalse(list.isEmpty());
    }

    @Test
    void testClear() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B"));
        list.clear();
        assertTrue(list.isEmpty());
    }

    @Test
    void testPoll() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B"));
        String polled = list.poll();
        assertEquals("A", polled);
        assertEquals(1, list.size());
    }

    @Test
    void testPollEmpty() {
        LinkedList<String> list = new LinkedList<>();
        assertNull(list.poll());
    }

    @Test
    void testPeek() {
        LinkedList<String> list = new LinkedList<>(List.of("A", "B"));
        assertEquals("A", list.peek());
        assertEquals(2, list.size());
    }

    @Test
    void testPeekEmpty() {
        LinkedList<String> list = new LinkedList<>();
        assertNull(list.peek());
    }

    @Test
    void testPushAndPop() {
        LinkedList<String> list = new LinkedList<>();
        list.push("B");
        list.push("A");
        assertEquals("A", list.pop());
        assertEquals("B", list.pop());
        assertTrue(list.isEmpty());
    }
}
