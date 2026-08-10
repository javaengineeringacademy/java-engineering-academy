package academy.javaengineering.collections.queue.priorityqueue.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class PriorityQueueTest {

    @Test
    void testAddAndSize() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        assertEquals(3, pq.size());
    }

    @Test
    void testRemove() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        assertEquals(1, pq.poll());
        assertEquals(2, pq.size());
    }

    @Test
    void testContains() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(5, 1, 3, 2, 4));
        assertTrue(pq.contains(3));
        assertFalse(pq.contains(99));
    }

    @Test
    void testIteration() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(5, 1, 3, 2, 4));
        List<Integer> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void testEdgeCases() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());
        assertNull(pq.peek());
        assertNull(pq.poll());
    }

    @Test
    void testPeekAndPoll() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        assertEquals(1, pq.peek());
        assertEquals(1, pq.poll());
        assertEquals(3, pq.peek());
    }

    @Test
    void testOffer() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertTrue(pq.offer(5));
        assertTrue(pq.offer(1));
        assertTrue(pq.offer(3));
        assertEquals(3, pq.size());
    }

    @Test
    void testCustomComparator() {
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(String::length));
        pq.add("Java");
        pq.add("Hi");
        pq.add("Python");
        assertEquals("Hi", pq.poll());
        assertEquals("Java", pq.poll());
        assertEquals("Python", pq.poll());
    }

    @Test
    void testPriorityOrder() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        pq.add(2);
        pq.add(4);
        assertEquals(1, pq.poll());
        assertEquals(2, pq.poll());
        assertEquals(3, pq.poll());
        assertEquals(4, pq.poll());
        assertEquals(5, pq.poll());
    }
}
