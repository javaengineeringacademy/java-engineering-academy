package academy.javaengineering.concurrency.framework.pools;

import java.util.concurrent.*;

public class ThreadPoolExample {
    public static void main(String[] args) throws Exception {
        // Custom ThreadPoolExecutor
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // Submit tasks
        for (int i = 0; i < 8; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.printf("Task %d on %s%n", id, Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        // Monitor
        Thread.sleep(500);
        System.out.println("Pool size: " + pool.getPoolSize());
        System.out.println("Active count: " + pool.getActiveCount());
        System.out.println("Queue size: " + pool.getQueue().size());
        System.out.println("Completed: " + pool.getCompletedTaskCount());
        System.out.println("Largest pool size: " + pool.getLargestPoolSize());

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
