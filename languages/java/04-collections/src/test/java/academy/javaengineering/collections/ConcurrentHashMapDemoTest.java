package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConcurrentHashMapDemoTest {

    private ConcurrentHashMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new ConcurrentHashMap<>();
    }

    @Nested
    @DisplayName("Thread-Safe Operations Tests")
    class ThreadSafeTests {

        @Test
        @DisplayName("Should perform concurrent put operations")
        void testConcurrentPut() throws InterruptedException {
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int val = i;
                threads[i] = new Thread(() -> map.put("key" + val, val));
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            assertEquals(threadCount, map.size());
        }

        @Test
        @DisplayName("Should perform concurrent compute operations")
        void testConcurrentCompute() throws InterruptedException {
            map.put("counter", 0);
            int threadCount = 100;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(() -> map.compute("counter", (k, v) -> v + 1));
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            assertEquals(Integer.valueOf(threadCount), map.get("counter"));
        }

        @Test
        @DisplayName("Should perform concurrent merge operations")
        void testConcurrentMerge() throws InterruptedException {
            map.put("total", 0);
            int threadCount = 50;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(() -> map.merge("total", 1, Integer::sum));
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            assertEquals(Integer.valueOf(threadCount), map.get("total"));
        }
    }

    @Nested
    @DisplayName("Atomic Operations Tests")
    class AtomicOperationsTests {

        @Test
        @DisplayName("Should compute if absent atomically")
        void testComputeIfAbsent() {
            map.computeIfAbsent("A", k -> 100);
            assertEquals(Integer.valueOf(100), map.get("A"));

            map.computeIfAbsent("A", k -> 200);
            assertEquals(Integer.valueOf(100), map.get("A"));
        }

        @Test
        @DisplayName("Should compute if present atomically")
        void testComputeIfPresent() {
            map.put("A", 1);
            map.computeIfPresent("A", (k, v) -> v + 10);
            assertEquals(Integer.valueOf(11), map.get("A"));

            map.computeIfPresent("B", (k, v) -> v + 10);
            assertNull(map.get("B"));
        }

        @Test
        @DisplayName("Should merge values atomically")
        void testMerge() {
            map.put("A", 1);
            map.merge("A", 5, Integer::sum);
            assertEquals(Integer.valueOf(6), map.get("A"));

            map.merge("B", 10, Integer::sum);
            assertEquals(Integer.valueOf(10), map.get("B"));
        }

        @Test
        @DisplayName("Should replace conditionally")
        void testConditionalReplace() {
            map.put("A", 1);
            assertTrue(map.replace("A", 1, 10));
            assertEquals(Integer.valueOf(10), map.get("A"));

            assertFalse(map.replace("A", 1, 20));
            assertEquals(Integer.valueOf(10), map.get("A"));
        }
    }
}
