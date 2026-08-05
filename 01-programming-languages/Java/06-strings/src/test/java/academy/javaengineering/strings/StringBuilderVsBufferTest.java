package academy.javaengineering.strings;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class StringBuilderVsBufferTest {

    @Test
    void testStringBuilderAppend() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        assertEquals("Hello World", sb.toString());
    }

    @Test
    void testStringBuilderInsert() {
        StringBuilder sb = new StringBuilder("Hello World");
        sb.insert(5, ",");
        assertEquals("Hello, World", sb.toString());
    }

    @Test
    void testStringBuilderDelete() {
        StringBuilder sb = new StringBuilder("Hello World");
        sb.delete(5, 11);
        assertEquals("Hello", sb.toString());
    }

    @Test
    void testStringBuilderReverse() {
        StringBuilder sb = new StringBuilder("Hello");
        assertEquals("olleH", sb.reverse().toString());
    }

    @Test
    void testStringBufferAppend() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("Hello");
        sbf.append(" ");
        sbf.append("World");
        assertEquals("Hello World", sbf.toString());
    }

    @Test
    void testStringBufferInsert() {
        StringBuffer sbf = new StringBuffer("Hello World");
        sbf.insert(5, ",");
        assertEquals("Hello, World", sbf.toString());
    }

    @Test
    void testStringBufferDelete() {
        StringBuffer sbf = new StringBuffer("Hello World");
        sbf.delete(5, 11);
        assertEquals("Hello", sbf.toString());
    }

    @Test
    void testStringBufferReverse() {
        StringBuffer sbf = new StringBuffer("Hello");
        assertEquals("olleH", sbf.reverse().toString());
    }

    @Test
    void testThreadSafetyStringBuilder() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        int threadCount = 5;
        int appendCount = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < appendCount; j++) {
                    sb.append("A");
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        int expectedLength = threadCount * appendCount;
        assertNotEquals(expectedLength, sb.length(), "StringBuilder should not be thread-safe");
    }

    @Test
    void testThreadSafetyStringBuffer() throws InterruptedException {
        StringBuffer sbf = new StringBuffer();
        int threadCount = 5;
        int appendCount = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < appendCount; j++) {
                    sbf.append("A");
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        int expectedLength = threadCount * appendCount;
        assertEquals(expectedLength, sbf.length(), "StringBuffer should be thread-safe");
    }

    @Test
    void testStringBuilderCapacityManagement() {
        StringBuilder sb = new StringBuilder(100);
        assertEquals(100, sb.capacity());
        assertEquals(0, sb.length());

        sb.append("Hello");
        assertEquals(100, sb.capacity());
        assertEquals(5, sb.length());

        sb.ensureCapacity(200);
        assertTrue(sb.capacity() >= 200);

        sb.trimToSize();
        assertTrue(sb.capacity() >= sb.length());
    }

    @Test
    void testStringBufferCapacityManagement() {
        StringBuffer sbf = new StringBuffer(100);
        assertEquals(100, sbf.capacity());
        assertEquals(0, sbf.length());

        sbf.append("Hello");
        assertEquals(100, sbf.capacity());
        assertEquals(5, sbf.length());

        sbf.ensureCapacity(200);
        assertTrue(sbf.capacity() >= 200);

        sbf.trimToSize();
        assertTrue(sbf.capacity() >= sbf.length());
    }

    @Test
    void testPerformanceComparison() {
        int iterations = 10000;

        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        long builderTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbf.append("a");
        }
        long bufferTime = System.currentTimeMillis() - start;

        assertTrue(builderTime <= bufferTime + 50, "StringBuilder should be as fast or faster than StringBuffer");
    }
}
