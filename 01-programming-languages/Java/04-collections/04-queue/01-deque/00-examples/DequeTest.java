package academy.javaengineering.collections.queue.deque.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class DequeTest {

    @Test
    void testAddAndSize() {
        Deque<String> deque = new ArrayDeque<>();
        deque.add("Java");
        deque.add("Python");
        deque.add("C++");
        assertEquals(3, deque.size());
    }

    @Test
    void testRemove() {
        Deque<String> deque = new ArrayDeque<>(Arrays.asList("A", "B", "C"));
        assertEquals("A", deque.remove());
        assertEquals(2, deque.size());
    }

    @Test
    void testContains() {
        Deque<String> deque = new ArrayDeque<>(Arrays.asList("Java", "Python", "C++"));
        assertTrue(deque.contains("Java"));
        assertFalse(deque.contains("Go"));
    }

    @Test
    void testIteration() {
        Deque<Integer> deque = new ArrayDeque<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : deque) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        Deque<String> deque = new ArrayDeque<>();
        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
        assertNull(deque.peek());
        assertNull(deque.poll());
    }

    @Test
    void testPushPop() {
        Deque<String> deque = new ArrayDeque<>();
        deque.push("Bottom");
        deque.push("Middle");
        deque.push("Top");
        assertEquals("Top", deque.pop());
        assertEquals("Middle", deque.pop());
        assertEquals("Bottom", deque.pop());
    }

    @Test
    void testPeekFirstLast() {
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("First");
        deque.addLast("Last");
        deque.add("Middle");
        assertEquals("First", deque.peekFirst());
        assertEquals("Last", deque.peekLast());
    }

    @Test
    void testAddFirstLast() {
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("A");
        deque.addLast("B");
        deque.addFirst("C");
        assertEquals("C", deque.peekFirst());
        assertEquals("B", deque.peekLast());
    }

    @Test
    void testOffer() {
        Deque<Integer> deque = new ArrayDeque<>();
        assertTrue(deque.offer(1));
        assertTrue(deque.offer(2));
        assertTrue(deque.offer(3));
        assertEquals(3, deque.size());
    }

    @Test
    void testPoll() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.offer(1);
        deque.offer(2);
        deque.offer(3);
        assertEquals(1, deque.poll());
        assertEquals(2, deque.poll());
        assertEquals(3, deque.poll());
        assertNull(deque.poll());
    }
}
