package academy.javaengineering.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for VirtualThreadExamples class.
 */
class VirtualThreadTest {

    @Test
    @DisplayName("Test virtual thread creation")
    void testVirtualThreadCreation() throws InterruptedException {
        final boolean[] isVirtual = {false};
        final String[] threadName = {""};

        Thread virtualThread = Thread.ofVirtual()
                .name("test-virtual-thread")
                .start(() -> {
                    isVirtual[0] = Thread.currentThread().isVirtual();
                    threadName[0] = Thread.currentThread().getName();
                });

        virtualThread.join();

        assertTrue(isVirtual[0]);
        assertEquals("test-virtual-thread", threadName[0]);
    }

    @Test
    @DisplayName("Test virtual thread executor")
    void testVirtualThreadExecutor() throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        final int[] counter = {0};

        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                synchronized (counter) {
                    counter[0]++;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(100, counter[0]);
    }

    @Test
    @DisplayName("Test virtual threads are lightweight")
    void testVirtualThreadsLightweight() throws InterruptedException {
        final int[] counter = {0};
        Thread[] threads = new Thread[10000];

        for (int i = 0; i < 10000; i++) {
            threads[i] = Thread.ofVirtual().start(() -> {
                synchronized (counter) {
                    counter[0]++;
                }
            });
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(10000, counter[0]);
    }

    @Test
    @DisplayName("Test virtual thread with blocking operation")
    void testVirtualThreadBlocking() throws InterruptedException {
        final boolean[] completed = {false};

        Thread virtualThread = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(100);
                completed[0] = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        virtualThread.join();
        assertTrue(completed[0]);
    }
}
