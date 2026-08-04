package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class QueueDemoTest {

    private Queue<String> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayDeque<>();
    }

    @Nested
    @DisplayName("ArrayDeque Queue Tests")
    class ArrayDequeTests {

        @Test
        @DisplayName("Should add elements with offer")
        void testOffer() {
            assertTrue(queue.offer("First"));
            assertTrue(queue.offer("Second"));
            assertEquals(2, queue.size());
        }

        @Test
        @DisplayName("Should retrieve head with peek")
        void testPeek() {
            queue.offer("A");
            queue.offer("B");
            assertEquals("A", queue.peek());
            assertEquals(2, queue.size());
        }

        @Test
        @DisplayName("Should return null when peek is empty")
        void testPeekEmpty() {
            assertNull(queue.peek());
        }

        @Test
        @DisplayName("Should remove head with poll")
        void testPoll() {
            queue.offer("A");
            queue.offer("B");
            assertEquals("A", queue.poll());
            assertEquals("B", queue.poll());
            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Should return null when poll is empty")
        void testPollEmpty() {
            assertNull(queue.poll());
        }

        @Test
        @DisplayName("Should process elements in FIFO order")
        void testFIFOOrder() {
            queue.offer("First");
            queue.offer("Second");
            queue.offer("Third");

            assertEquals("First", queue.poll());
            assertEquals("Second", queue.poll());
            assertEquals("Third", queue.poll());
        }

        @Test
        @DisplayName("Should check isEmpty correctly")
        void testIsEmpty() {
            assertTrue(queue.isEmpty());
            queue.offer("A");
            assertFalse(queue.isEmpty());
            queue.poll();
            assertTrue(queue.isEmpty());
        }
    }

    @Nested
    @DisplayName("LinkedList Queue Tests")
    class LinkedListQueueTests {

        @BeforeEach
        void setUp() {
            queue = new LinkedList<>();
        }

        @Test
        @DisplayName("Should add with offer and remove with poll")
        void testOfferAndPoll() {
            queue.offer("X");
            queue.offer("Y");
            assertEquals("X", queue.poll());
            assertEquals("Y", queue.poll());
        }

        @Test
        @DisplayName("Should add and remove with add/remove")
        void testAddAndRemove() {
            queue.add("A");
            queue.add("B");
            assertEquals("A", queue.remove());
            assertEquals("B", queue.remove());
        }
    }
}
