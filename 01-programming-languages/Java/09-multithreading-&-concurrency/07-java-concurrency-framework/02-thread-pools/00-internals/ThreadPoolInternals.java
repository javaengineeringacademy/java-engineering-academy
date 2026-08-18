package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates internal mechanics of thread pools.
 */
public class ThreadPoolInternals {

    public static void main(String[] args) throws Exception {
        demonstrateWorkQueueTypes();
        demonstratePoolSizing();
        demonstrateDynamicResize();
    }

    static void demonstrateWorkQueueTypes() throws Exception {
        System.out.println("=== Work Queue Types ===");

        // SynchronousQueue — zero capacity, direct handoff
        ThreadPoolExecutor syncPool = new ThreadPoolExecutor(
            2, 2, 0L, TimeUnit.MILLISECONDS,
            new SynchronousQueue<>()
        );
        syncPool.submit(() -> System.out.println("SynchronousQueue task"));
        syncPool.shutdown();
        syncPool.awaitTermination(1, TimeUnit.SECONDS);

        // ArrayBlockingQueue — bounded
        ThreadPoolExecutor arrayPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10)
        );
        for (int i = 0; i < 3; i++) {
            arrayPool.submit(() -> System.out.println("ArrayBlockingQueue task"));
        }
        arrayPool.shutdown();
        arrayPool.awaitTermination(1, TimeUnit.SECONDS);
    }

    static void demonstratePoolSizing() throws Exception {
        System.out.println("\n=== Pool Sizing ===");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(20)
        );

        System.out.println("Core: " + executor.getCorePoolSize());
        System.out.println("Max: " + executor.getMaximumPoolSize());
        System.out.println("Queue remaining: " + executor.getQueue().remainingCapacity());

        executor.shutdown();
    }

    static void demonstrateDynamicResize() throws Exception {
        System.out.println("\n=== Dynamic Resize ===");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 4, 5L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10)
        );

        System.out.println("Initial pool size: " + executor.getPoolSize());

        // Submit tasks to grow pool
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                latch.countDown();
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        latch.await();
        System.out.println("After load, pool size: " + executor.getPoolSize());

        // Wait for keepAliveTime
        Thread.sleep(6000);
        System.out.println("After idle timeout, pool size: " + executor.getPoolSize());

        executor.shutdown();
    }
}
