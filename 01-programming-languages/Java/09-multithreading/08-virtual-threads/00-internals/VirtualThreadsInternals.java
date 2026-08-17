package academy.javaengineering.concurrency.virtualthreads.internals;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates virtual thread internal mechanisms:
 * - Creation methods
 * - Mount/unmount behavior
 * - Pinning detection
 * - Carrier thread interaction
 */
public class VirtualThreadsInternals {

    public static void main(String[] args) throws Exception {
        creationMethods();
        carrierThreadInteraction();
        pinningDetection();
        stackGrowth();
    }

    private static void creationMethods() throws InterruptedException {
        System.out.println("=== Virtual Thread Creation Methods ===\n");

        // Method 1: Thread.ofVirtual().start()
        Thread vt1 = Thread.ofVirtual().name("vt-1").start(() -> {
            System.out.println("  Created via Thread.ofVirtual().start() - " + Thread.currentThread());
        });

        // Method 2: Thread.startVirtualThread()
        Thread vt2 = Thread.startVirtualThread(() -> {
            System.out.println("  Created via startVirtualThread() - " + Thread.currentThread());
        });

        // Method 3: Builder pattern
        Thread vt3 = Thread.ofVirtual()
            .name("vt-3")
            .start(() -> {
                System.out.println("  Created via builder - " + Thread.currentThread());
            });

        // Method 4: Virtual thread executor
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                System.out.println("  Created via executor - " + Thread.currentThread());
            });
        }

        vt1.join();
        vt2.join();
        vt3.join();
        System.out.println();
    }

    private static void carrierThreadInteraction() throws InterruptedException {
        System.out.println("=== Carrier Thread Interaction ===\n");

        AtomicInteger mounted = new AtomicInteger(0);
        AtomicInteger unmounted = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
                executor.submit(() -> {
                    mounted.incrementAndGet();
                    try {
                        Thread.sleep(10); // Unmounts during sleep
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    unmounted.incrementAndGet();
                });
            }
        }

        System.out.println("  100 virtual threads completed");
        System.out.println("  Mounted: " + mounted.get());
        System.out.println("  Unmounted/resumed: " + unmounted.get());
        System.out.println("  Carrier threads: " + Runtime.getRuntime().availableProcessors());
        System.out.println("  Note: Virtual threads share carrier threads via mount/unmount\n");
    }

    private static void pinningDetection() throws InterruptedException {
        System.out.println("=== Pinning Detection ===\n");

        Object lock = new Object();
        int taskCount = 50;
        long sleepMs = 20;

        // Pinning with synchronized
        Instant start1 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    synchronized (lock) {
                        try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                });
            }
        }
        Duration pinnedTime = Duration.between(start1, Instant.now());

        // No pinning with ReentrantLock
        java.util.concurrent.locks.ReentrantLock rlock = new java.util.concurrent.locks.ReentrantLock();
        Instant start2 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    rlock.lock();
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        rlock.unlock();
                    }
                });
            }
        }
        Duration unpinnedTime = Duration.between(start2, Instant.now());

        System.out.println("  " + taskCount + " tasks, each sleep(" + sleepMs + "ms) inside lock:");
        System.out.println("  synchronized (pinned): " + pinnedTime.toMillis() + "ms");
        System.out.println("  ReentrantLock (unpinned): " + unpinnedTime.toMillis() + "ms");
        System.out.println("  Pinning makes it ~" + (pinnedTime.toMillis() / Math.max(unpinnedTime.toMillis(), 1)) + "x slower");
        System.out.println();
    }

    private static void stackGrowth() {
        System.out.println("=== Virtual Thread Stack Growth ===\n");

        System.out.println("  Platform thread: 1MB stack (reserved upfront)");
        System.out.println("  Virtual thread: 1KB stack (grows on demand)");
        System.out.println("  Memory comparison:");
        System.out.println("    10,000 platform threads: ~10GB stack memory");
        System.out.println("    10,000 virtual threads: ~10MB stack memory");
        System.out.println("    Reduction: 1000x");
        System.out.println();
    }
}
