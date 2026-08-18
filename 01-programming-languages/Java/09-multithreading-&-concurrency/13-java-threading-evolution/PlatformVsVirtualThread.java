package academy.javaengineering.concurrency.evolution;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Platform vs Virtual Thread Comparison
 * 
 * Demonstrates the key differences between platform threads
 * and virtual threads with practical examples.
 */
public class PlatformVsVirtualThread {

    private static final AtomicInteger platformCompleted = new AtomicInteger(0);
    private static final AtomicInteger virtualCompleted = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("=== PLATFORM vs VIRTUAL THREAD COMPARISON ===\n");

        compareMemoryUsage();
        compareIOPerformance();
        compareCPUBoundWork();
        compareThreadCreation();
        showDecisionGuide();
    }

    /**
     * Compares memory usage between platform and virtual threads
     */
    private static void compareMemoryUsage() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         MEMORY USAGE COMPARISON                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        Runtime runtime = Runtime.getRuntime();

        // Platform threads
        System.out.println("--- Platform Threads ---");
        long beforePlatform = runtime.totalMemory() - runtime.freeMemory();

        int platformCount = 1000;
        Thread[] platformThreads = new Thread[platformCount];

        for (int i = 0; i < platformCount; i++) {
            platformThreads[i] = new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            platformThreads[i].start();
        }

        long afterPlatform = runtime.totalMemory() - runtime.freeMemory();
        long platformMemory = (afterPlatform - beforePlatform) / 1024;

        System.out.println("  Created " + platformCount + " platform threads");
        System.out.println("  Memory used: ~" + platformMemory + " KB");
        System.out.println("  Per thread: ~" + (platformMemory / platformCount) + " KB");

        // Cleanup
        for (Thread t : platformThreads) {
            t.interrupt();
        }

        // Virtual threads
        System.out.println("\n--- Virtual Threads ---");
        runtime.gc();
        long beforeVirtual = runtime.totalMemory() - runtime.freeMemory();

        int virtualCount = 10000;
        AtomicInteger virtualCountActual = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, virtualCount).forEach(i -> {
                executor.submit(() -> {
                    virtualCountActual.incrementAndGet();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long afterVirtual = runtime.totalMemory() - runtime.freeMemory();
        long virtualMemory = (afterVirtual - beforeVirtual) / 1024;

        System.out.println("  Created " + virtualCount + " virtual threads");
        System.out.println("  Memory used: ~" + virtualMemory + " KB");
        System.out.println("  Per thread: ~" + (virtualMemory / virtualCount) + " KB");

        System.out.println("\n--- Memory Comparison ---");
        System.out.println("  Platform threads: ~" + (platformMemory / platformCount) + " KB per thread");
        System.out.println("  Virtual threads: ~" + Math.max(1, virtualMemory / virtualCount) + " KB per thread");
        System.out.println("  Ratio: Virtual threads use ~1000x less memory!");
    }

    /**
     * Compares I/O performance
     */
    private static void compareIOPerformance() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         I/O PERFORMANCE COMPARISON               ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        int taskCount = 100;
        long sleepMs = 50;

        // Platform threads with limited pool
        System.out.println("--- Platform Threads (Fixed Pool: 10) ---");
        long startPlatform = System.nanoTime();

        try (var executor = Executors.newFixedThreadPool(10)) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(sleepMs);
                        platformCompleted.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endPlatform = System.nanoTime();
        long platformTime = (endPlatform - startPlatform) / 1_000_000;

        System.out.println("  Completed " + platformCompleted.get() + " tasks");
        System.out.println("  Time: " + platformTime + "ms");
        System.out.println("  Throughput: " + (taskCount * 1000.0 / platformTime) + " tasks/sec\n");

        // Virtual threads
        System.out.println("--- Virtual Threads (Unlimited) ---");
        long startVirtual = System.nanoTime();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(sleepMs);
                        virtualCompleted.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endVirtual = System.nanoTime();
        long virtualTime = (endVirtual - startVirtual) / 1_000_000;

        System.out.println("  Completed " + virtualCompleted.get() + " tasks");
        System.out.println("  Time: " + virtualTime + "ms");
        System.out.println("  Throughput: " + (taskCount * 1000.0 / virtualTime) + " tasks/sec");

        System.out.println("\n--- I/O Performance Summary ---");
        System.out.println("  Platform threads limited by pool size");
        System.out.println("  Virtual threads: one thread per task");
        System.out.println("  Virtual threads excel at I/O-bound work!");
    }

    /**
     * Compares CPU-bound work performance
     */
    private static void compareCPUBoundWork() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         CPU-BOUND WORK COMPARISON                ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        int taskCount = Runtime.getRuntime().availableProcessors();
        long computeTimeMs = 100;

        System.out.println("CPU cores available: " + taskCount);
        System.out.println("Compute time per task: " + computeTimeMs + "ms\n");

        // Platform threads
        System.out.println("--- Platform Threads (CPU-bound) ---");
        long startPlatform = System.nanoTime();

        try (var executor = Executors.newFixedThreadPool(taskCount)) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    // Simulate CPU-bound work
                    long end = System.nanoTime() + computeTimeMs * 1_000_000;
                    while (System.nanoTime() < end) {
                        // Busy wait (simulates computation)
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endPlatform = System.nanoTime();
        long platformTime = (endPlatform - startPlatform) / 1_000_000;

        System.out.println("  Time: " + platformTime + "ms");
        System.out.println("  Efficiency: " + (computeTimeMs * 100.0 / platformTime) + "%\n");

        // Virtual threads
        System.out.println("--- Virtual Threads (CPU-bound) ---");
        long startVirtual = System.nanoTime();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    // Same CPU-bound work
                    long end = System.nanoTime() + computeTimeMs * 1_000_000;
                    while (System.nanoTime() < end) {
                        // Busy wait (simulates computation)
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endVirtual = System.nanoTime();
        long virtualTime = (endVirtual - startVirtual) / 1_000_000;

        System.out.println("  Time: " + virtualTime + "ms");
        System.out.println("  Efficiency: " + (computeTimeMs * 100.0 / virtualTime) + "%");

        System.out.println("\n--- CPU-Bound Summary ---");
        System.out.println("  Platform threads: Better for CPU-bound work");
        System.out.println("  Virtual threads: No advantage for CPU-bound");
        System.out.println("  Both limited by available CPU cores");
    }

    /**
     * Compares thread creation speed
     */
    private static void compareThreadCreation() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         THREAD CREATION SPEED                    ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        int count = 10000;

        // Platform threads
        System.out.println("--- Platform Thread Creation ---");
        long startPlatform = System.nanoTime();

        Thread[] threads = new Thread[count];
        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(() -> {});
            threads[i].start();
        }

        long endPlatform = System.nanoTime();
        long platformTime = (endPlatform - startPlatform) / 1_000;

        System.out.println("  Created " + count + " threads in " + platformTime + "μs");
        System.out.println("  Per thread: " + (platformTime / count) + "μs");

        // Cleanup
        for (Thread t : threads) {
            t.interrupt();
        }

        // Virtual threads
        System.out.println("\n--- Virtual Thread Creation ---");
        long startVirtual = System.nanoTime();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executor.submit(() -> {});
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endVirtual = System.nanoTime();
        long virtualTime = (endVirtual - startVirtual) / 1_000;

        System.out.println("  Created " + count + " threads in " + virtualTime + "μs");
        System.out.println("  Per thread: " + Math.max(1, virtualTime / count) + "μs");

        System.out.println("\n--- Creation Speed Summary ---");
        System.out.println("  Platform threads: Slower creation");
        System.out.println("  Virtual threads: Much faster creation");
        System.out.println("  Virtual threads win for high thread counts!");
    }

    /**
     * Shows decision guide for choosing between platform and virtual threads
     */
    private static void showDecisionGuide() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         DECISION GUIDE                           ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                    WHEN TO USE WHAT                        │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│                                                           │");
        System.out.println("│  USE VIRTUAL THREADS WHEN:                                │");
        System.out.println("│  ─────────────────────────                                │");
        System.out.println("│  ✓ I/O-bound work (HTTP, DB, file operations)            │");
        System.out.println("│  ✓ High thread count needed (>1000)                      │");
        System.out.println("│  ✓ Thread-per-request architecture                       │");
        System.out.println("│  ✓ Long-lived connections with waiting                   │");
        System.out.println("│  ✓ Replacing thread pools for I/O tasks                  │");
        System.out.println("│                                                           │");
        System.out.println("│  USE PLATFORM THREADS WHEN:                               │");
        System.out.println("│  ────────────────────────                                 │");
        System.out.println("│  ✓ CPU-bound computation                                  │");
        System.out.println("│  ✓ Low-latency requirements (<1ms)                       │");
        System.out.println("│  ✓ Need thread affinity or CPU affinity                  │");
        System.out.println("│  ✓ Native/JNI code execution                             │");
        System.out.println("│  ✓ Synchronized blocks with blocking I/O                 │");
        System.out.println("│                                                           │");
        System.out.println("│  USE BOTH WHEN:                                           │");
        System.out.println("│  ──────────────                                           │");
        System.out.println("│  ✓ Mixed workload (I/O + CPU)                            │");
        System.out.println("│  ✓ Need to limit CPU-bound threads                       │");
        System.out.println("│  ✓ Structured concurrency with different task types       │");
        System.out.println("│                                                           │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        System.out.println("\n--- Quick Reference ---");
        System.out.println("┌──────────────────────┬──────────────────┬──────────────────┐");
        System.out.println("│ Scenario             │ Platform         │ Virtual          │");
        System.out.println("├──────────────────────┼──────────────────┼──────────────────┤");
        System.out.println("│ I/O-bound            │ ✗ Limited        │ ✓ Excellent      │");
        System.out.println("│ CPU-bound            │ ✓ Optimal        │ ✗ No advantage   │");
        System.out.println("│ High thread count    │ ✗ Expensive      │ ✓ Cheap          │");
        System.out.println("│ Low latency          │ ✓ Better         │ ✗ Overhead       │");
        System.out.println("│ Memory efficiency    │ ✗ High cost      │ ✓ Low cost       │");
        System.out.println("│ Thread-per-request   │ ✗ Limited        │ ✓ Perfect        │");
        System.out.println("│ Legacy code          │ ✓ Compatible     │ ✓ Drop-in        │");
        System.out.println("└──────────────────────┴──────────────────┴──────────────────┘");
    }
}
