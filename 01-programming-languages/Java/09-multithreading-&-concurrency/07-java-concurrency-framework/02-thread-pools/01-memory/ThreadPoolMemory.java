package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates memory layout in thread pools.
 */
public class ThreadPoolMemory {

    public static void main(String[] args) throws Exception {
        demonstrateThreadStackMemory();
        demonstrateQueueMemory();
        demonstrateTaskMemoryCapture();
    }

    static void demonstrateThreadStackMemory() throws Exception {
        System.out.println("=== Thread Stack Memory ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Long> stackSize = executor.submit(() -> {
            // Each platform thread has ~1MB stack by default
            // Virtual threads use ~1KB heap-allocated stacks
            return Thread.currentThread().getId();
        });

        System.out.println("Worker thread ID: " + stackSize.get());

        executor.shutdown();
    }

    static void demonstrateQueueMemory() throws Exception {
        System.out.println("\n=== Queue Memory Overhead ===");

        // LinkedBlockingQueue: Node objects per task
        ThreadPoolExecutor linkedPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );

        // ArrayBlockingQueue: pre-allocated array
        ThreadPoolExecutor arrayPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(100)
        );

        for (int i = 0; i < 50; i++) {
            linkedPool.submit(() -> {});
            arrayPool.submit(() -> {});
        }

        System.out.println("Linked queue size: " + linkedPool.getQueue().size());
        System.out.println("Array queue size: " + arrayPool.getQueue().size());

        linkedPool.shutdown();
        arrayPool.shutdown();
    }

    static void demonstrateTaskMemoryCapture() throws Exception {
        System.out.println("\n=== Task Memory Capture ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        // Large captured object
        byte[] largeData = new byte[1024 * 1024]; // 1MB

        Future<?> future = executor.submit(() -> {
            // Lambda captures largeData reference — prevents GC
            System.out.println("Task running with captured data: " + largeData.length);
        });

        future.get();
        largeData = null; // release reference

        executor.shutdown();
    }
}
