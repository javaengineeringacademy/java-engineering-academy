import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

class ConcurrentHashMapDemoTest {

    @Test
    void testConcurrentPut() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
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
    void testConcurrentCompute() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
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
    void testComputeIfAbsent() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.computeIfAbsent("A", k -> 100);
        assertEquals(Integer.valueOf(100), map.get("A"));

        map.computeIfAbsent("A", k -> 200);
        assertEquals(Integer.valueOf(100), map.get("A"));
    }

    @Test
    void testMerge() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.merge("A", 5, Integer::sum);
        assertEquals(Integer.valueOf(6), map.get("A"));

        map.merge("B", 10, Integer::sum);
        assertEquals(Integer.valueOf(10), map.get("B"));
    }

    @Test
    void testConditionalReplace() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        assertTrue(map.replace("A", 1, 10));
        assertEquals(Integer.valueOf(10), map.get("A"));

        assertFalse(map.replace("A", 1, 20));
        assertEquals(Integer.valueOf(10), map.get("A"));
    }
}
