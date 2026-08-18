package academy.javaengineering.concurrency.virtualthreads.solutions;

import java.util.concurrent.*;

public class VirtualThreadSolutions {
    public static void main(String[] args) throws Exception {
        // Solution 1: 100,000 virtual threads
        System.out.println("=== 100,000 Virtual Threads ===");
        long start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100_000; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");

        // Solution 2: Platform vs Virtual comparison
        System.out.println("\n=== Platform vs Virtual ===");
        int taskCount = 1000;

        // Platform threads
        start = System.currentTimeMillis();
        try (var executor = Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        }
        System.out.println("Platform (200 threads): " + (System.currentTimeMillis() - start) + "ms");

        // Virtual threads
        start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        }
        System.out.println("Virtual: " + (System.currentTimeMillis() - start) + "ms");
    }
}
