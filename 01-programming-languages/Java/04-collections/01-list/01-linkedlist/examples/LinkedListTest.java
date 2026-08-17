package list.linkedlist.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class LinkedListTest {

    @Test
    void testAddAndSize() {
        LinkedList<String> list = new LinkedList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");
        assertEquals(3, list.size());
        list.addFirst("JavaScript");
        list.addLast("Go");
        assertEquals(5, list.size());
        assertEquals("JavaScript", list.getFirst());
        assertEquals("Go", list.getLast());
    }

    @Test
    void testRemove() {
        LinkedList<String> list = new LinkedList<>(Arrays.asList("A", "B", "C"));
        assertEquals("B", list.remove(1));
        assertEquals(2, list.size());
        list.addFirst("X");
        assertEquals("X", list.removeFirst());
        list.addLast("Y");
        assertEquals("Y", list.removeLast());
    }

    @Test
    void testContains() {
        LinkedList<String> list = new LinkedList<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(list.contains("Java"));
        assertFalse(list.contains("Go"));
    }

    @Test
    void testIteration() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : list) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testGetAndSet() {
        LinkedList<String> list = new LinkedList<>(Arrays.asList("A", "B", "C"));
        assertEquals("B", list.get(1));
        list.set(1, "X");
        assertEquals("X", list.get(1));
    }

    @Test
    void testIndexOf() {
        LinkedList<String> list = new LinkedList<>(Arrays.asList("A", "B", "C", "B"));
        assertEquals(1, list.indexOf("B"));
        assertEquals(3, list.lastIndexOf("B"));
        assertEquals(-1, list.indexOf("Z"));
    }

    @Test
    void testSubList() {
        LinkedList<String> list = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E"));
        List<String> sub = list.subList(1, 4);
        assertEquals(3, sub.size());
        assertEquals("B", sub.get(0));
    }

    @Test
    void testQueueDequeMethods() {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(10);
        queue.offer(20);
        assertEquals(10, queue.peek());
        assertEquals(10, queue.poll());
        assertEquals(1, queue.size());
    }

    @Test
    void testPushPop() {
        Deque<String> stack = new LinkedList<>();
        stack.push("A");
        stack.push("B");
        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());
    }
}
