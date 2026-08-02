package academy.javaengineering.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.StructuredTaskScope;
import java.util.List;
import java.util.ArrayList;

/**
 * Demonstrates Java 21 virtual threads for lightweight concurrency.
 * Shows creation, pinning avoidance, and structured concurrency.
 */
public class VirtualThreadExamples {

    public static void main(String[] args) throws Exception {
        demonstrateVirtualThreadCreation();
        demonstrateVirtualThreadWithExecutor();
        demonstratePinningAvoidance();
        demonstrateStructuredConcurrency();
    }

    /**
     * Demonstrates basic virtual thread creation.
     */
    public static void demonstrateVirtualThreadCreation() throws InterruptedException {
        Thread virtualThread = Thread.ofVirtual().name("my-virtual-thread").start(() -> {
            System.out.println("Virtual thread running: " + Thread.currentThread().getName());
            System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
        });

        virtualThread.join();
        // Expected output:
        // Virtual thread running: my-virtual-thread
        // Is virtual: true
    }

    /**
     * Demonstrates virtual threads with ExecutorService.
     */
    public static void demonstrateVirtualThreadWithExecutor() throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Virtual Task " + taskId + " on " + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Virtual thread executor completed");
        // Expected output: Tasks executed on virtual threads
    }

    /**
     * Demonstrates avoiding pinning by using non-blocking operations.
     */
    public static void demonstratePinningAvoidance() throws InterruptedException {
        // Bad: Using synchronized causes pinning
        // Good: Using ReentrantLock allows unmounting
        final var lock = new java.util.concurrent.locks.ReentrantLock();

        Thread virtualThread = Thread.ofVirtual().start(() -> {
            lock.lock();
            try {
                System.out.println("Virtual thread with lock (no pinning)");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        virtualThread.join();
        System.out.println("Pinning avoidance demonstrated");
        // Expected output: Virtual thread with lock (no pinning)
    }

    /**
     * Demonstrates structured concurrency (preview feature in Java 21).
     */
    public static void demonstrateStructuredConcurrency() throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<Future<String>> tasks = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                final int taskId = i;
                tasks.add(scope.fork(() -> {
                    Thread.sleep(50);
                    return "Result " + taskId;
                }));
            }

            scope.join();

            for (Future<String> task : tasks) {
                System.out.println("Structured task result: " + task.resultNow());
            }
        }
        // Expected output:
        // Structured task result: Result 0
        // Structured task result: Result 1
        // Structured task result: Result 2
    }
}
