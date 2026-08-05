import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class QueueDemoTest {

    @Test
    void testOffer() {
        Queue<String> queue = new ArrayDeque<>();
        assertTrue(queue.offer("First"));
        assertTrue(queue.offer("Second"));
        assertEquals(2, queue.size());
    }

    @Test
    void testPeek() {
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("A");
        queue.offer("B");
        assertEquals("A", queue.peek());
        assertEquals(2, queue.size());
    }

    @Test
    void testPeekEmpty() {
        Queue<String> queue = new ArrayDeque<>();
        assertNull(queue.peek());
    }

    @Test
    void testPoll() {
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("A");
        queue.offer("B");
        assertEquals("A", queue.poll());
        assertEquals("B", queue.poll());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testPollEmpty() {
        Queue<String> queue = new ArrayDeque<>();
        assertNull(queue.poll());
    }

    @Test
    void testFIFOOrder() {
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");

        assertEquals("First", queue.poll());
        assertEquals("Second", queue.poll());
        assertEquals("Third", queue.poll());
    }

    @Test
    void testIsEmpty() {
        Queue<String> queue = new ArrayDeque<>();
        assertTrue(queue.isEmpty());
        queue.offer("A");
        assertFalse(queue.isEmpty());
        queue.poll();
        assertTrue(queue.isEmpty());
    }
}
