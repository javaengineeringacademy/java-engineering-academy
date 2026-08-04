package academy.javaengineering.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SynchronizationExamples class.
 */
class SynchronizationTest {

    @Test
    @DisplayName("Test synchronized method counter")
    void testSynchronizedMethodCounter() throws InterruptedException {
        SynchronizationExamples example = new SynchronizationExamples();
        example.demonstrateSynchronizedMethod();
        // Counter should be exactly 10000
        assertEquals(10000, example.getCounter());
    }

    @Test
    @DisplayName("Test synchronized block counter")
    void testSynchronizedBlockCounter() throws InterruptedException {
        SynchronizationExamples example = new SynchronizationExamples();
        example.demonstrateSynchronizedBlock();
        // Counter should be exactly 10000
        assertEquals(10000, example.getCounter());
    }

    @Test
    @DisplayName("Test concurrent increment without synchronization")
    void testConcurrentIncrementWithoutSync() throws InterruptedException {
        final int[] counter = {0};
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter[0]++;
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Without synchronization, counter may be less than 10000
        // (Race condition is not guaranteed to manifest every time)
        assertTrue(counter[0] <= 10000);
    }

    @Test
    @DisplayName("Test synchronized method prevents race conditions")
    void testSynchronizedPreventsRaceCondition() throws InterruptedException {
        final int[] unsafeCounter = {0};
        SynchronizationExamples example = new SynchronizationExamples();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    example.incrementCounter();
                    unsafeCounter[0]++;
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Synchronized counter should be correct, unsafe may not be
        assertEquals(10000, example.getCounter());
    }
}
