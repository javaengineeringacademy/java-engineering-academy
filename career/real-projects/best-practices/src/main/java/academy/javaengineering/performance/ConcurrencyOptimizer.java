package academy.javaengineering.performance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates concurrency performance patterns.
 */
public class ConcurrencyOptimizer {

    private final AtomicLong counter = new AtomicLong(0);
    private final ExecutorService executor;

    public ConcurrencyOptimizer(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    public void processInParallel(int taskCount, Runnable task) throws InterruptedException {
        counter.set(0);
        
        for (int i = 0; i < taskCount; i++) {
            executor.submit(() -> {
                task.run();
                counter.incrementAndGet();
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    public long getCompletedTasks() {
        return counter.get();
    }

    public void shutdown() {
        executor.shutdown();
    }
}
