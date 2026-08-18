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
 * Scheduler Behavior Demo
 * 
 * Demonstrates:
 * - OS scheduler behavior
 * - Thread states and transitions
 * - Context switching
 * - Fairness vs throughput
 * - Scheduling edge cases
 */
public class SchedulerBehaviorDemo {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SCHEDULER BEHAVIOR DEMO ===\n");

        demonstrateThreadStates();
        demonstrateContextSwitching();
        demonstrateFairnessVsThroughput();
        demonstrateSchedulingEdgeCases();
        demonstratePlatformSpecificBehavior();
    }

    /**
     * Demonstrates thread state transitions
     */
    private static void demonstrateThreadStates() throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         THREAD STATE TRANSITIONS                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Thread states and transitions:\n");

        // Create a thread but don't start it
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("1. NEW state (created, not started)");
        System.out.println("   Thread state: " + thread.getState());

        // Start the thread
        thread.start();
        System.out.println("\n2. RUNNABLE state (started, may be running or ready)");
        System.out.println("   Thread state: " + thread.getState());

        // Wait for thread to finish
        thread.join();
        System.out.println("\n3. TERMINATED state (finished execution)");
        System.out.println("   Thread state: " + thread.getState());

        // Demonstrate waiting state
        System.out.println("\n--- Waiting State ---");
        Object monitor = new Object();
        Thread waitingThread = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait();  // Enter WAITING state
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        waitingThread.start();
        Thread.sleep(50);  // Let thread enter wait
        System.out.println("   Thread state (waiting): " + waitingThread.getState());

        synchronized (monitor) {
            monitor.notify();  // Wake up thread
        }
        waitingThread.join();
        System.out.println("   Thread state (after notify): " + waitingThread.getState());

        System.out.println("\n--- State Summary ---");
        System.out.println("  NEW: Created, not started");
        System.out.println("  RUNNABLE: Ready to run or running");
        System.out.println("  BLOCKED: Waiting for monitor lock");
        System.out.println("  WAITING: Waiting indefinitely (wait(), join())");
        System.out.println("  TIMED_WAITING: Waiting with timeout (sleep(), wait(t))");
        System.out.println("  TERMINATED: Finished execution");
    }

    /**
     * Demonstrates context switching overhead
     */
    private static void demonstrateContextSwitching() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         CONTEXT SWITCHING OVERHEAD               ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Context switching has measurable overhead.\n");

        int switchCount = 10000;
        AtomicInteger counter = new AtomicInteger(0);

        // Measure context switching overhead
        System.out.println("Measuring " + switchCount + " context switches...\n");

        long startTime = System.nanoTime();

        Thread[] threads = new Thread[2];
        CountDownLatch latch = new CountDownLatch(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        // Thread 1
        threads[0] = new Thread(() -> {
            try {
                startLatch.await();
                for (int i = 0; i < switchCount / 2; i++) {
                    counter.incrementAndGet();
                    Thread.yield();  // Suggest scheduler to switch
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        // Thread 2
        threads[1] = new Thread(() -> {
            try {
                startLatch.await();
                for (int i = 0; i < switchCount / 2; i++) {
                    counter.incrementAndGet();
                    Thread.yield();  // Suggest scheduler to switch
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        threads[0].start();
        threads[1].start();

        startLatch.countDown();  // Start both threads
        latch.await();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000;

        System.out.println("  Total operations: " + counter.get());
        System.out.println("  Time: " + duration + "μs");
        System.out.println("  Average per switch: " + (duration * 1000.0 / switchCount) + "ns\n");

        System.out.println("Context Switch Details:");
        System.out.println("  1. Save current thread state (registers, PC, stack pointer)");
        System.out.println("  2. Switch to kernel mode");
        System.out.println("  3. Load new thread state");
        System.out.println("  4. Switch back to user mode");
        System.out.println("  Cost: ~1-10 microseconds per switch");
        System.out.println("  Cache effects: TLB flush, cache pollution");
    }

    /**
     * Demonstrates fairness vs throughput tradeoff
     */
    private static void demonstrateFairnessVsThroughput() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         FAIRNESS vs THROUGHPUT                   ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Schedulers balance fairness and throughput.\n");

        // Fair lock
        System.out.println("--- Fair ReentrantLock ---");
        ReentrantLock fairLock = new ReentrantLock(true);  // fair = true

        int threadCount = 5;
        int increments = 1000;
        AtomicInteger fairCounter = new AtomicInteger(0);
        CountDownLatch fairLatch = new CountDownLatch(threadCount);

        long fairStart = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    fairLock.lock();
                    try {
                        fairCounter.incrementAndGet();
                    } finally {
                        fairLock.unlock();
                    }
                }
                fairLatch.countDown();
            }).start();
        }

        fairLatch.await();
        long fairTime = (System.nanoTime() - fairStart) / 1_000;

        System.out.println("  Total: " + fairCounter.get());
        System.out.println("  Time: " + fairTime + "μs");

        // Unfair lock
        System.out.println("\n--- Unfair ReentrantLock ---");
        ReentrantLock unfairLock = new ReentrantLock(false);  // fair = false

        AtomicInteger unfairCounter = new AtomicInteger(0);
        CountDownLatch unfairLatch = new CountDownLatch(threadCount);

        long unfairStart = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    unfairLock.lock();
                    try {
                        unfairCounter.incrementAndGet();
                    } finally {
                        unfairLock.unlock();
                    }
                }
                unfairLatch.countDown();
            }).start();
        }

        unfairLatch.await();
        long unfairTime = (System.nanoTime() - unfairStart) / 1_000;

        System.out.println("  Total: " + unfairCounter.get());
        System.out.println("  Time: " + unfairTime + "μs");

        System.out.println("\n--- Fairness Comparison ---");
        System.out.println("  Fair lock: " + fairTime + "μs (guarantees ordering)");
        System.out.println("  Unfair lock: " + unfairTime + "μs (better throughput)");
        System.out.println("  Unfair is ~" + (fairTime * 100.0 / unfairTime) + "% faster");

        System.out.println("\nFairness Tradeoffs:");
        System.out.println("  • Fair: Guarantees FIFO ordering, prevents starvation");
        System.out.println("  • Unfair: Better throughput, allows barging");
        System.out.println("  • Use fair when: Ordering matters, prevent starvation");
        System.out.println("  • Use unfair when: Throughput matters, no starvation risk");
    }

    /**
     * Demonstrates scheduling edge cases
     */
    private static void demonstrateSchedulingEdgeCases() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         SCHEDULING EDGE CASES                    ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // Starvation
        System.out.println("--- Thread Starvation ---\n");
        System.out.println("High-priority threads can starve low-priority threads.\n");

        AtomicInteger highCount = new AtomicInteger(0);
        AtomicInteger lowCount = new AtomicInteger(0);

        Thread highPriority = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                highCount.incrementAndGet();
            }
        }, "HighPriority");
        highPriority.setPriority(Thread.MAX_PRIORITY);

        Thread lowPriority = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lowCount.incrementAndGet();
            }
        }, "LowPriority");
        lowPriority.setPriority(Thread.MIN_PRIORITY);

        highPriority.start();
        lowPriority.start();

        highPriority.join();
        lowPriority.join();

        System.out.println("  High priority completed: " + highCount.get());
        System.out.println("  Low priority completed: " + lowCount.get());

        // Deadlock scenario
        System.out.println("\n--- Deadlock Scenario ---\n");
        System.out.println("Deadlock occurs when threads wait for each other's locks.\n");

        Object lockA = new Object();
        Object lockB = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("  [T1] Holding lockA, waiting for lockB...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (lockB) {
                    System.out.println("  [T1] Acquired lockB");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("  [T2] Holding lockB, waiting for lockA...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (lockA) {
                    System.out.println("  [T2] Acquired lockA");
                }
            }
        });

        System.out.println("Starting deadlock scenario (will timeout)...");
        t1.start();
        t2.start();

        // Wait briefly then check
        Thread.sleep(200);

        if (t1.isAlive() && t2.isAlive()) {
            System.out.println("  Deadlock detected! Both threads are blocked.");
            t1.interrupt();
            t2.interrupt();
        }

        t1.join(100);
        t2.join(100);

        System.out.println("\nDeadlock Prevention:");
        System.out.println("  • Always acquire locks in consistent order");
        System.out.println("  • Use tryLock() with timeout");
        System.out.println("  • Use lock ordering algorithms");
        System.out.println("  • Avoid nested locks when possible");
    }

    /**
     * Demonstrates platform-specific scheduling behavior
     */
    private static void demonstratePlatformSpecificBehavior() throws InterruptedException {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         PLATFORM-SPECIFIC BEHAVIOR               ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Scheduling behavior varies by OS:\n");

        // Detect OS
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("Current OS: " + System.getProperty("os.name"));
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());

        if (os.contains("linux")) {
            System.out.println("\n--- Linux CFS (Completely Fair Scheduler) ---");
            System.out.println("  • Uses red-black tree for O(log n) scheduling");
            System.out.println("  • Tracks virtual runtime per thread");
            System.out.println("  • No fixed time slices");
            System.out.println("  • Nice values affect time slice allocation");
            System.out.println("  • Default: Completely fair, no starvation");
        } else if (os.contains("windows")) {
            System.out.println("\n--- Windows Thread Scheduler ---");
            System.out.println("  • 32 priority levels (0-31)");
            System.out.println("  • Multi-level feedback queue");
            System.out.println("  • Priority boosting for I/O and foreground");
            System.out.println("  • Time quantum: ~20ms (short) or ~60ms (long)");
        } else if (os.contains("mac") || os.contains("darwin")) {
            System.out.println("\n--- macOS XNU Scheduler ---");
            System.out.println("  • Based on Mach + BSD schedulers");
            System.out.println("  • 5 QoS classes (Interactive → Background)");
            System.out.println("  • Work stealing across cores");
            System.out.println("  • Thermal-aware scheduling");
        }

        System.out.println("\n--- JVM Interaction with OS Scheduler ---");
        System.out.println("  • JVM maps Java threads to OS threads");
        System.out.println("  • Thread priority maps to OS priority");
        System.out.println("  • JIT compiler affects scheduling (safepoints)");
        System.out.println("  • GC pauses affect all threads");
        System.out.println("  • Virtual threads use ForkJoinPool (work-stealing)");

        System.out.println("\n--- Scheduling Best Practices ---");
        System.out.println("  1. Don't rely on thread ordering");
        System.out.println("  2. Use synchronization for correctness");
        System.out.println("  3. Prefer ExecutorService over manual thread creation");
        System.out.println("  4. Use virtual threads for I/O-bound work");
        System.out.println("  5. Profile before optimizing scheduling");
        System.out.println("  6. Consider thread affinity for CPU-bound work");
    }
}
