package academy.javaengineering.concurrency.virtualthreads.examples;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class VirtualThreadExamples {
    public static void main(String[] args) throws Exception {
        // Example 1: Basic virtual thread
        System.out.println("=== Basic Virtual Thread ===");
        Thread vt = Thread.ofVirtual().start(() -> {
            System.out.println("Running on: " + Thread.currentThread().getName());
        });
        vt.join();

        // Example 2: Virtual thread executor (millions of tasks)
        System.out.println("\n=== Virtual Thread Executor ===");
        long start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                final int id = i;
                futures.add(executor.submit(() -> {
                    Thread.sleep(10);
                    return "Task-" + id;
                }));
            }
            for (Future<String> f : futures) f.get();
        }
        System.out.println("10,000 tasks with sleep: " + (System.currentTimeMillis() - start) + "ms");

        // Example 3: Blocking I/O without blocking carrier
        System.out.println("\n=== Blocking I/O ===");
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 5; i++) {
                final int id = i;
                executor.submit(() -> {
                    System.out.println("Start " + id + " on " + Thread.currentThread().getName());
                    try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    System.out.println("  End " + id);
                });
            }
        }

        // Example 4: Pinning detection
        System.out.println("\n=== Pinning Warning ===");
        System.out.println("synchronized blocks pin virtual threads to carrier.");
        System.out.println("Use ReentrantLock instead of synchronized for virtual threads.");
    }
}
