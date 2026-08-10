package academy.javaengineering.collections.map.concurrentexamples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;

class ConcurrentHashMapTest {

    @Test
    void testAddAndSize() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("C++", 3);
        assertEquals(3, map.size());
    }

    @Test
    void testRemove() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.remove("Java");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("Java"));
    }

    @Test
    void testContains() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        assertTrue(map.containsKey("Java"));
        assertFalse(map.containsKey("Go"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(99));
    }

    @Test
    void testIteration() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        int sum = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sum += entry.getValue();
        }
        assertEquals(3, sum);
    }

    @Test
    void testEdgeCases() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("missing"));
    }

    @Test
    void testGetAndPut() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        assertEquals(1, map.get("Java"));
        map.put("Java", 10);
        assertEquals(10, map.get("Java"));
    }

    @Test
    void testMerge() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.merge("Java", 10, Integer::sum);
        assertEquals(11, map.get("Java"));
    }

    @Test
    void testPutIfAbsent() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("Java", 1);
        map.putIfAbsent("Java", 99);
        assertEquals(1, map.get("Java"));
        map.putIfAbsent("Python", 2);
        assertEquals(2, map.get("Python"));
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        int numThreads = 4;
        int opsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(numThreads);
        for (int t = 0; t < numThreads; t++) {
            final int threadNum = t;
            new Thread(() -> {
                for (int i = 0; i < opsPerThread; i++) {
                    map.put("key-" + threadNum + "-" + i, i);
                }
                latch.countDown();
            }).start();
        }
        latch.await();
        assertEquals(numThreads * opsPerThread, map.size());
    }
}
