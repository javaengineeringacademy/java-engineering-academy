package academy.javaengineering.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ExecutorExamples class.
 */
class ExecutorTest {

    @Test
    @DisplayName("Test fixed thread pool executes all tasks")
    void testFixedThreadPool() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final int[] counter = {0};

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                synchronized (counter) {
                    counter[0]++;
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        assertEquals(10, counter[0]);
    }

    @Test
    @DisplayName("Test single thread executor runs tasks sequentially")
    void testSingleThreadExecutor() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final StringBuilder sequence = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            final int num = i;
            executor.submit(() -> {
                synchronized (sequence) {
                    sequence.append(num);
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        // In single thread, tasks should execute in order
        assertEquals("01234", sequence.toString());
    }

    @Test
    @DisplayName("Test Callable returns result via Future")
    void testCallableReturnsResult() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            return "Test Result";
        };

        Future<String> future = executor.submit(callable);
        String result = future.get(5, TimeUnit.SECONDS);

        assertEquals("Test Result", result);
        assertTrue(future.isDone());

        executor.shutdown();
    }

    @Test
    @DisplayName("Test ExecutorService shutdown")
    void testExecutorShutdown() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        assertFalse(executor.isShutdown());
        assertFalse(executor.isTerminated());

        executor.shutdown();
        assertTrue(executor.isShutdown());

        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        assertTrue(executor.isTerminated());
    }
}
