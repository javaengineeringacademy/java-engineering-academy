package academy.javaengineering.concurrency.virtualthreads.memory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

/**
 * Demonstrates virtual thread memory characteristics:
 * - Stack memory comparison
 * - Scalability comparison
 * - Memory monitoring
 */
public class VirtualThreadsMemory {

    public static void main(String[] args) throws Exception {
        stackMemoryComparison();
        scalabilityComparison();
        memoryMonitoring();
    }

    private static void stackMemoryComparison() {
        System.out.println("=== Stack Memory Comparison ===\n");

        Runtime runtime = Runtime.getRuntime();

        // Platform threads
        long before = runtime.totalMemory() - runtime.freeMemory();
        try (ExecutorService executor = Executors.newFixedThreadPool(1000)) {
            for (int i = 0; i < 1000; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        long after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("  1000 platform threads: ~" + ((after - before) / 1024 / 1024) + "MB");

        // Virtual threads
        before = runtime.totalMemory() - runtime.freeMemory();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("  1000 virtual threads: ~" + ((after - before) / 1024) + "KB");
        System.out.println("  Virtual threads use ~1000x less stack memory\n");
    }

    private static void scalabilityComparison() throws Exception {
        System.out.println("=== Scalability Comparison ===\n");

        int taskCount = 10000;
        long sleepMs = 10;

        // Platform threads (limited pool)
        Instant start1 = Instant.now();
        try (ExecutorService executor = Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Duration platformTime = Duration.between(start1, Instant.now());

        // Virtual threads
        Instant start2 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Duration virtualTime = Duration.between(start2, Instant.now());

        System.out.println("  " + taskCount + " tasks, each sleep(" + sleepMs + "ms):");
        System.out.println("  Platform threads (pool=200): " + platformTime.toMillis() + "ms");
        System.out.println("  Virtual threads: " + virtualTime.toMillis() + "ms");
        System.out.println("  Speedup: ~" + (platformTime.toMillis() / Math.max(virtualTime.toMillis(), 1)) + "x\n");
    }

    private static void memoryMonitoring() {
        System.out.println("=== Memory Monitoring ===\n");

        Runtime runtime = Runtime.getRuntime();
        System.out.println("  JVM Max Memory: " + (runtime.maxMemory() / 1024 / 1024) + "MB");
        System.out.println("  JVM Total Memory: " + (runtime.totalMemory() / 1024 / 1024) + "MB");
        System.out.println("  JVM Free Memory: " + (runtime.freeMemory() / 1024 / 1024) + "MB");
        System.out.println("\n  Virtual thread memory is heap-managed — GC handles cleanup");
        System.out.println("  No need to manually manage thread stack memory\n");
    }
}
