import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates false sharing and its impact on performance.
 * False sharing occurs when threads access different variables that happen
 * to be on the same cache line, causing unnecessary cache invalidation.
 */
public class FalseSharingDemo {

    private static final int ITERATIONS = 100_000_000;
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== False Sharing Demonstration ===\n");

        demonstrateFalseSharing();
        demonstratePaddingSolution();
        demonstrateContendedAnnotation();
        demonstratePerformanceComparison();

        System.out.println("\n=== All demonstrations completed ===");
    }

    /**
     * Demonstrates false sharing with non-padded counters.
     * Counter objects are likely on the same cache line.
     */
    private static void demonstrateFalseSharing() throws InterruptedException {
        System.out.println("1. False Sharing Problem:");
        System.out.println("   Non-padded counters share cache lines.");

        FalseSharingCounter[] counters = new FalseSharingCounter[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            counters[i] = new FalseSharingCounter();
        }

        Thread[] threads = new Thread[THREAD_COUNT];
        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    counters[index].increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long duration = System.nanoTime() - startTime;
        System.out.println("   Time: " + duration / 1_000_000 + " ms");
        System.out.println("   (This is slow due to false sharing)\n");
    }

    /**
     * Demonstrates solution using padding to avoid false sharing.
     */
    private static void demonstratePaddingSolution() throws InterruptedException {
        System.out.println("2. Padding Solution:");
        System.out.println("   Padded counters avoid cache line sharing.");

        PaddedCounter[] counters = new PaddedCounter[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            counters[i] = new PaddedCounter();
        }

        Thread[] threads = new Thread[THREAD_COUNT];
        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    counters[index].increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long duration = System.nanoTime() - startTime;
        System.out.println("   Time: " + duration / 1_000_000 + " ms");
        System.out.println("   (Much faster with padding)\n");
    }

    /**
     * Demonstrates @Contended annotation (JDK 8+).
     * Automatically adds padding to avoid false sharing.
     */
    private static void demonstrateContendedAnnotation() throws InterruptedException {
        System.out.println("3. @Contended Annotation:");
        System.out.println("   JDK 8+ automatically handles padding.");

        ContendedCounter[] counters = new ContendedCounter[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            counters[i] = new ContendedCounter();
        }

        Thread[] threads = new Thread[THREAD_COUNT];
        long startTime = System.nanoTime();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    counters[index].increment();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long duration = System.nanoTime() - startTime;
        System.out.println("   Time: " + duration / 1_000_000 + " ms");
        System.out.println("   (@Contended requires -XX:-RestrictContended JVM flag)\n");
    }

    /**
     * Performance comparison of all approaches.
     */
    private static void demonstratePerformanceComparison() throws InterruptedException {
        System.out.println("4. Performance Comparison:");
        System.out.println("   Comparing all approaches side by side.\n");

        long[] results = new long[3];

        // Test 1: No padding
        FalseSharingCounter[] noPadding = new FalseSharingCounter[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) noPadding[i] = new FalseSharingCounter();

        long start = System.nanoTime();
        Thread[] t1 = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int idx = i;
            t1[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) noPadding[idx].increment();
            });
            t1[i].start();
        }
        for (Thread t : t1) t.join();
        results[0] = System.nanoTime() - start;

        // Test 2: Padded
        PaddedCounter[] padded = new PaddedCounter[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) padded[i] = new PaddedCounter();

        start = System.nanoTime();
        Thread[] t2 = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int idx = i;
            t2[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) padded[idx].increment();
            });
            t2[i].start();
        }
        for (Thread t : t2) t.join();
        results[1] = System.nanoTime() - start;

        // Test 3: Contended
        ContendedCounter[] contended = new ContendedCounter[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) contended[i] = new ContendedCounter();

        start = System.nanoTime();
        Thread[] t3 = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int idx = i;
            t3[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) contended[idx].increment();
            });
            t3[i].start();
        }
        for (Thread t : t3) t.join();
        results[2] = System.nanoTime() - start;

        System.out.println("   No padding:  " + results[0] / 1_000_000 + " ms");
        System.out.println("   Padded:      " + results[1] / 1_000_000 + " ms");
        System.out.println("   @Contended:  " + results[2] / 1_000_000 + " ms");
        System.out.printf("   Padding speedup: %.2fx%n", (double) results[0] / results[1]);
        System.out.printf("   @Contended speedup: %.2fx%n", (double) results[0] / results[2]);
    }

    /**
     * Counter without padding - subject to false sharing.
     */
    static class FalseSharingCounter {
        volatile long value = 0;

        void increment() {
            value++;
        }

        long get() {
            return value;
        }
    }

    /**
     * Counter with padding to occupy entire cache line (64 bytes).
     * Each long is 8 bytes, so we need 7 extra longs for padding.
     */
    static class PaddedCounter {
        // Padding to fill cache line (64 bytes = 8 longs)
        long p1, p2, p3, p4, p5, p6, p7;
        volatile long value = 0;
        long p8, p9, p10, p11, p12, p13, p14;

        void increment() {
            value++;
        }

        long get() {
            return value;
        }
    }

    /**
     * Counter using @Contended annotation.
     * JVM automatically adds padding to avoid false sharing.
     * Requires -XX:-RestrictContended JVM flag.
     */
    static class ContendedCounter {
        @sun.misc.Contended
        volatile long value = 0;

        void increment() {
            value++;
        }

        long get() {
            return value;
        }
    }
}
