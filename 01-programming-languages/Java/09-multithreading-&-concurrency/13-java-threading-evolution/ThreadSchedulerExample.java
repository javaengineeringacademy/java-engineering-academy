package academy.javaengineering.concurrency.evolution;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread Scheduler Example
 * 
 * Demonstrates:
 * - Thread scheduling behavior
 * - Priority scheduling
 * - Time slicing
 * - Work stealing
 * - Scheduling algorithms
 */
public class ThreadSchedulerExample {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== THREAD SCHEDULER EXAMPLE ===\n");

        demonstrateTimeSlicing();
        demonstratePriorityScheduling();
        demonstrateWorkStealing();
        demonstrateScheduledExecution();
        demonstrateThreadInterference();
    }

    /**
     * Demonstrates time slicing behavior
     */
    private static void demonstrateTimeSlicing() throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         TIME SLICING DEMONSTRATION               ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Time slicing allows multiple threads to share CPU time.\n");

        int threadCount = 4;
        int iterations = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        System.out.println("Creating " + threadCount + " threads, each doing " + iterations + " iterations...\n");

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    startLatch.await();  // Wait for signal to start

                    for (int j = 0; j < iterations; j++) {
                        System.out.println("  [T" + threadId + "] Iteration " + (j + 1) + 
                            " (CPU core: " + 
                            Thread.currentThread().getName() + ")");
                        Thread.sleep(10);  // Simulate work
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }, "Core-" + (i % Runtime.getRuntime().availableProcessors())).start();
        }

        System.out.println("Starting all threads simultaneously...\n");
        long startTime = System.nanoTime();
        startLatch.countDown();  // Release all threads

        endLatch.await();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("\n  Total time: " + duration + "ms");
        System.out.println("  Threads interleaved execution (time slicing)\n");

        System.out.println("Time Slicing Details:");
        System.out.println("  • OS scheduler gives each thread a time quantum (~1-10ms)");
        System.out.println("  • When quantum expires, thread is preempted");
        System.out.println("  • Preempted thread goes to back of ready queue");
        System.out.println("  • Next thread in queue gets CPU time");
        System.out.println("  • Creates illusion of parallelism on single core");
    }

    /**
     * Demonstrates priority-based scheduling
     */
    private static void demonstratePriorityScheduling() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         PRIORITY SCHEDULING DEMONSTRATION        ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Thread priority affects scheduling order (hint, not guarantee).\n");

        AtomicInteger highPriorityCount = new AtomicInteger(0);
        AtomicInteger normalPriorityCount = new AtomicInteger(0);
        AtomicInteger lowPriorityCount = new AtomicInteger(0);

        int iterations = 1000;

        // High priority thread
        Thread highPriority = new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                highPriorityCount.incrementAndGet();
            }
        }, "HighPriority");
        highPriority.setPriority(Thread.MAX_PRIORITY);  // 10

        // Normal priority thread
        Thread normalPriority = new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                normalPriorityCount.incrementAndGet();
            }
        }, "NormalPriority");
        normalPriority.setPriority(Thread.NORM_PRIORITY);  // 5

        // Low priority thread
        Thread lowPriority = new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                lowPriorityCount.incrementAndGet();
            }
        }, "LowPriority");
        lowPriority.setPriority(Thread.MIN_PRIORITY);  // 1

        System.out.println("Starting threads with different priorities...\n");

        long startTime = System.nanoTime();

        highPriority.start();
        normalPriority.start();
        lowPriority.start();

        highPriority.join();
        normalPriority.join();
        lowPriority.join();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000;

        System.out.println("  High priority iterations: " + highPriorityCount.get());
        System.out.println("  Normal priority iterations: " + normalPriorityCount.get());
        System.out.println("  Low priority iterations: " + lowPriorityCount.get());
        System.out.println("  Time: " + duration + "μs\n");

        System.out.println("Priority Details:");
        System.out.println("  • Java priority range: 1 (MIN) to 10 (MAX)");
        System.out.println("  • Default priority: 5 (NORM)");
        System.out.println("  • Priority is a HINT to OS scheduler");
        System.out.println("  • OS may ignore or reinterpret priorities");
        System.out.println("  • Not reliable for strict ordering");
    }

    /**
     * Demonstrates work-stealing algorithm
     */
    private static void demonstrateWorkStealing() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         WORK STEALING DEMONSTRATION              ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Work-stealing optimizes CPU utilization by balancing load.\n");

        int threadCount = 4;
        int tasksPerThread = 10;
        AtomicInteger stolenTasks = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(threadCount);

        System.out.println("Creating " + threadCount + " threads with " + 
            tasksPerThread + " tasks each...\n");

        ExecutorService executor = Executors.newWorkStealingPool(threadCount);

        long startTime = System.nanoTime();

        for (int i = 0; i < threadCount * tasksPerThread; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("  [Task-" + taskId + "] Executing on " + threadName);
                try {
                    Thread.sleep(10);  // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;

        System.out.println("\n  Total execution time: " + duration + "ms");

        System.out.println("\nWork-Stealing Details:");
        System.out.println("  • ForkJoinPool uses work-stealing algorithm");
        System.out.println("  • Each thread has its own task queue");
        System.out.println("  • Idle threads steal from busy threads' queues");
        System.out.println("  • Reduces thread idle time");
        System.out.println("  • Better load balancing than fixed thread pools");
        System.out.println("  • Used by parallel streams and CompletableFuture");
    }

    /**
     * Demonstrates scheduled execution
     */
    private static void demonstrateScheduledExecution() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         SCHEDULED EXECUTION DEMONSTRATION        ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("ScheduledExecutorService for delayed and periodic tasks.\n");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        AtomicInteger periodicCount = new AtomicInteger(0);

        System.out.println("Scheduling tasks:\n");

        // One-shot delayed task
        System.out.println("  1. One-shot task (delay: 100ms)");
        ScheduledFuture<?> oneShot = scheduler.schedule(() -> {
            System.out.println("     [OneShot] Executed at " + 
                System.currentTimeMillis() % 10000 + "ms");
        }, 100, TimeUnit.MILLISECONDS);

        // Periodic task
        System.out.println("  2. Periodic task (initial: 50ms, period: 50ms)");
        ScheduledFuture<?> periodic = scheduler.scheduleAtFixedRate(() -> {
            int count = periodicCount.incrementAndGet();
            System.out.println("     [Periodic] Execution #" + count + " at " + 
                System.currentTimeMillis() % 10000 + "ms");
        }, 50, 50, TimeUnit.MILLISECONDS);

        // Wait for tasks to execute
        Thread.sleep(350);

        // Cancel periodic task
        periodic.cancel(false);
        System.out.println("\n  Periodic task cancelled after " + periodicCount.get() + " executions");

        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("\nScheduled Execution Details:");
        System.out.println("  • schedule(): One-shot delayed execution");
        System.out.println("  • scheduleAtFixedRate(): Fixed-rate periodic execution");
        System.out.println("  • scheduleWithFixedDelay(): Fixed-delay periodic execution");
        System.out.println("  • Uses core pool size threads");
        System.out.println("  • Tasks are queued in DelayedWorkQueue");
    }

    /**
     * Demonstrates thread interference and scheduling issues
     */
    private static void demonstrateThreadInterference() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         THREAD INTERFERENCE DEMONSTRATION        ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Scheduling can cause unexpected thread interference.\n");

        // Unsynchronized counter
        AtomicInteger unsafeCounter = new AtomicInteger(0);
        int increments = 100000;
        int threadCount = 4;

        System.out.println("--- Without Synchronization ---");

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < increments; j++) {
                        unsafeCounter.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        endLatch.await();

        int expected = threadCount * increments;
        System.out.println("  Expected: " + expected);
        System.out.println("  Actual: " + unsafeCounter.get());
        System.out.println("  Lost updates: " + (expected - unsafeCounter.get()));

        // Synchronized counter
        AtomicInteger safeCounter = new AtomicInteger(0);
        startLatch = new CountDownLatch(1);
        endLatch = new CountDownLatch(threadCount);

        System.out.println("\n--- With Synchronization ---");

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < increments; j++) {
                        lock.lock();
                        try {
                            safeCounter.incrementAndGet();
                        } finally {
                            lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        endLatch.await();

        System.out.println("  Expected: " + expected);
        System.out.println("  Actual: " + safeCounter.get());
        System.out.println("  Lost updates: " + (expected - safeCounter.get()));

        System.out.println("\nThread Interference Details:");
        System.out.println("  • Race conditions occur when scheduling interleaves operations");
        System.out.println("  • Read-modify-write operations are vulnerable");
        System.out.println("  • Synchronization prevents interference");
        System.out.println("  • Use atomic operations for simple counters");
        System.out.println("  • Use locks for complex critical sections");
    }
}
