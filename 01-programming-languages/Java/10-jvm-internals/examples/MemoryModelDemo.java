package academy.javaengineering.jvm.examples;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Memory Model and Happens-Before Demo
 * Demonstrates Java Memory Model (JMM), happens-before relationships,
 * volatile semantics, synchronized blocks, and concurrent visibility issues.
 */
public class MemoryModelDemo {

    // Without volatile - thread may never see updated value
    private static boolean running = true;

    // With volatile - guaranteed visibility across threads
    private static volatile boolean volatileRunning = true;

    // Shared mutable state
    private static int sharedCounter = 0;
    private static final Object lock = new Object();
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);

    /**
     * DEMO 1: Visibility Problem
     * Without volatile/synchronized, changes may not be visible to other threads
     */
    public static void demonstrateVisibilityProblem() {
        System.out.println("=== Visibility Problem ===");

        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) {
                count++;
            }
            System.out.println("[Worker] Stopped after " + count + " iterations");
        });

        worker.start();
        try {
            Thread.sleep(10); // Let worker run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        running = false; // May never be seen by worker!

        try {
            worker.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (worker.isAlive()) {
            System.out.println("[Main] Worker still running! Visibility problem occurred.");
            worker.interrupt();
        }
    }

    /**
     * DEMO 2: Volatile Fix
     */
    public static void demonstrateVolatileFix() {
        System.out.println("\n=== Volatile Visibility Fix ===");

        running = true; // Reset for next demo
        Thread worker = new Thread(() -> {
            int count = 0;
            while (volatileRunning) {
                count++;
            }
            System.out.println("[VolatileWorker] Stopped after " + count + " iterations");
        });

        worker.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        volatileRunning = false; // Guaranteed to be visible!

        try {
            worker.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[Main] Volatile variable ensured visibility");
    }

    /**
     * DEMO 3: Happens-Before Relationships
     * If action A happens-before action B, then A's effects are visible to B.
     */
    public static void demonstrateHappensBefore() {
        System.out.println("\n=== Happens-Before Relationships ===");
        System.out.println("Key rules:");
        System.out.println("1. Program Order: Each action in a thread happens-before");
        System.out.println("   subsequent actions in the same thread");
        System.out.println("2. Monitor Lock: An unlock on a monitor happens-before");
        System.out.println("   every subsequent lock on that monitor");
        System.out.println("3. Volatile: A write to a volatile field happens-before");
        System.out.println("   every subsequent read of that field");
        System.out.println("4. Thread Start: Thread.start() happens-before any action");
        System.out.println("   in the started thread");
        System.out.println("5. Thread Join: Any action in a thread happens-before");
        System.out.println("   another thread successfully returns from join()");
        System.out.println("6. Transitivity: If A happens-before B and B happens-before C,");
        System.out.println("   then A happens-before C");

        // Demonstrate synchronized happens-before
        demonstrateSynchronizedHappensBefore();
    }

    private static void demonstrateSynchronizedHappensBefore() {
        System.out.println("\n--- Synchronized Happens-Before ---");

        Thread writer = new Thread(() -> {
            synchronized (lock) {
                sharedCounter = 42; // Write under lock
                System.out.println("[Writer] Set counter to 42 under synchronized");
            } // Unlock happens-before next lock
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(5); // Ensure writer runs first
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lock) { // Lock after unlock -> happens-before
                System.out.println("[Reader] Read counter: " + sharedCounter
                        + " (guaranteed to see 42)");
            }
        });

        writer.start();
        reader.start();
        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * DEMO 4: Volatile vs Synchronized
     * Volatile: guarantees visibility but NOT atomicity
     * Synchronized: guarantees BOTH visibility AND atomicity
     */
    public static void demonstrateVolatileVsSynchronized() {
        System.out.println("\n=== Volatile vs Synchronized ===");

        // Volatile: increment is NOT atomic
        // (read-modify-write is three separate operations)
        System.out.println("Volatile increment (NOT atomic):");
        System.out.println("  Thread A: reads value = 5");
        System.out.println("  Thread B: reads value = 5");
        System.out.println("  Thread A: writes 6");
        System.out.println("  Thread B: writes 6  <- lost update!");
        System.out.println("  Expected: 7, Actual: 6");

        // AtomicInteger is thread-safe
        System.out.println("\nAtomicInteger (atomic via CAS):");
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executor.submit(atomicCounter::incrementAndGet);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  AtomicInteger result: " + atomicCounter.get()
                + " (always correct)");
    }

    /**
     * DEMO 5: Memory Fence / Barrier Operations
     */
    public static void demonstrateMemoryBarriers() {
        System.out.println("\n=== Memory Barriers ===");
        System.out.println("Three types of memory barriers:");
        System.out.println("1. Load Barrier: ensures all loads before barrier complete");
        System.out.println("   before loads after barrier execute");
        System.out.println("2. Store Barrier: ensures all stores before barrier complete");
        System.out.println("   before stores after barrier execute");
        System.out.println("3. Full Barrier: both load and store barriers");
        System.out.println("\nVolatile write: StoreStore + StoreLoad barriers");
        System.out.println("Volatile read: LoadLoad + LoadStore barriers");
        System.out.println("\nPractical impact:");
        System.out.println("  - Prevents instruction reordering");
        System.out.println("  - Flushes CPU caches");
        System.out.println("  - Ensures cross-thread visibility");
    }

    /**
     * DEMO 6: DCL (Double-Checked Locking) Pattern
     */
    public static void demonstrateDCL() {
        System.out.println("\n=== Double-Checked Locking ===");
        System.out.println("Broken DCL (without volatile):");
        System.out.println("  if (instance == null) {           // 1st check (no lock)");
        System.out.println("    synchronized(lock) {");
        System.out.println("      if (instance == null) {       // 2nd check (with lock)");
        System.out.println("        instance = new Singleton(); // PROBLEM: non-atomic");
        System.out.println("      }");
        System.out.println("    }");
        System.out.println("  }");
        System.out.println("\nProblem: Reference assignment can happen before constructor");
        System.out.println("completes, giving partially constructed object.");

        System.out.println("\nCorrect DCL (with volatile):");
        System.out.println("  private static volatile Singleton instance;");
        System.out.println("  volatile prevents reordering of writes");
        System.out.println("  ensuring object is fully constructed before reference");
    }

    /**
     * DEMO 7: Unsafe Memory Operations
     */
    public static void demonstrateUnsafeOperations() {
        System.out.println("\n=== Unsafe Memory Operations ===");
        System.out.println("sun.misc.Unsafe provides low-level memory access:");
        System.out.println("  - allocateMemory / freeMemory");
        System.out.println("  - putInt / getInt (direct memory access)");
        System.out.println("  - compareAndSwapInt (CAS operations)");
        System.out.println("  - park / unpark (thread parking)");
        System.out.println("\nWarning: Bypasses all safety checks!");
        System.out.println("Used internally by: ConcurrentHashMap, ThreadPoolExecutor");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║  MEMORY MODEL & HAPPENS-BEFORE DEMO ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateVisibilityProblem();
        demonstrateVolatileFix();
        demonstrateHappensBefore();
        demonstrateVolatileVsSynchronized();
        demonstrateMemoryBarriers();
        demonstrateDCL();
        demonstrateUnsafeOperations();
    }
}
