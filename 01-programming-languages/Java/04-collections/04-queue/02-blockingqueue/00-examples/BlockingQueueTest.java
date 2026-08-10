package academy.javaengineering.collections.queue.blockingqueue.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

class BlockingQueueTest {

    @Test
    void testAddAndSize() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        queue.put("Java");
        queue.put("Python");
        queue.put("C++");
        assertEquals(3, queue.size());
    }

    @Test
    void testRemove() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        queue.put("A");
        queue.put("B");
        queue.put("C");
        assertEquals("A", queue.poll());
        assertEquals(2, queue.size());
    }

    @Test
    void testContains() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        queue.put("Java");
        queue.put("Python");
        assertTrue(queue.contains("Java"));
        assertFalse(queue.contains("Go"));
    }

    @Test
    void testIteration() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        queue.put(1);
        queue.put(2);
        queue.put(3);
        int sum = 0;
        for (int n : queue) sum += n;
        assertEquals(6, sum);
    }

    @Test
    void testEdgeCases() {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertNull(queue.poll());
    }

    @Test
    void testPutAndTake() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        queue.put("A");
        queue.put("B");
        queue.put("C");
        assertEquals("A", queue.take());
        assertEquals("B", queue.take());
        assertEquals("C", queue.take());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testOfferAndPoll() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);
        assertTrue(queue.offer(1));
        assertTrue(queue.offer(2));
        assertTrue(queue.offer(3));
        assertFalse(queue.offer(4));
        assertEquals(1, queue.poll());
        assertEquals(2, queue.poll());
        assertEquals(3, queue.poll());
        assertNull(queue.poll());
    }

    @Test
    void testCapacity() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);
        queue.put(1);
        queue.put(2);
        queue.put(3);
        assertEquals(3, queue.remainingCapacity());
        queue.poll();
        assertEquals(1, queue.remainingCapacity());
    }

    @Test
    void testPutBlocksWhenFull() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        queue.put("A");
        Thread t = new Thread(() -> {
            try {
                queue.put("B");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        t.start();
        Thread.sleep(100);
        t.interrupt();
        t.join(1000);
        assertEquals(1, queue.size());
    }
}
