package academy.javaengineering.concurrency.memorymodel.memory;

/**
 * Deep dive into memory visibility, cache hierarchy, and store/load buffers.
 * Demonstrates real visibility problems and how the JMM solves them.
 */
public class MemoryModelMemory {

    // ═══════════════════════════════════════════════════════════════
    // 1. CPU Cache Hierarchy and Visibility
    // ═══════════════════════════════════════════════════════════════

    static void cacheHierarchy() {
        System.out.println("=== CPU Cache Hierarchy ===");
        System.out.println();
        System.out.println("Modern CPUs have multiple levels of cache:");
        System.out.println();
        System.out.println("  ┌─────────┐  ┌─────────┐");
        System.out.println("  │  Core 0  │  │  Core 1  │");
        System.out.println("  │  L1: 32K │  │  L1: 32K │");
        System.out.println("  │  L2: 256K│  │  L2: 256K│");
        System.out.println("  └────┬─────┘  └────┬─────┘");
        System.out.println("       │              │");
        System.out.println("    ┌──┴──────────────┴──┐");
        System.out.println("    │    L3 Cache: 8MB    │");
        System.out.println("    └──────────┬──────────┘");
        System.out.println("               │");
        System.out.println("        ┌──────┴──────┐");
        System.out.println("        │ Main Memory  │");
        System.out.println("        │   (RAM)      │");
        System.out.println("        └──────────────┘");
        System.out.println();
        System.out.println("Each core has its own L1/L2 cache.");
        System.out.println("When Thread 1 writes to a variable, the write goes to Core 0's cache.");
        System.out.println("Core 1 may still see the OLD value in its own L1 cache.");
        System.out.println();
        System.out.println("Cache coherence protocols (MESI) help, but they don't eliminate the problem:");
        System.out.println("- Invalidations are asynchronous");
        System.out.println("- Store buffers delay visibility");
        System.out.println("- The JMM abstracts over these hardware details");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 2. Store Buffers and Load Buffers
    // ═══════════════════════════════════════════════════════════════

    static void storeAndLoadBuffers() {
        System.out.println("=== Store Buffers and Load Buffers ===");
        System.out.println();
        System.out.println("CPUs use buffers to decouple execution from memory:");
        System.out.println();
        System.out.println("  Execution Unit → Store Buffer → L1 Cache → L3 → Main Memory");
        System.out.println("  Execution Unit ← Load Buffer  ← L1 Cache ← L3 ← Main Memory");
        System.out.println();
        System.out.println("Store Buffer:");
        System.out.println("- A write sits here until the cache is ready");
        System.out.println("- The write is NOT yet visible to other cores");
        System.out.println("- This is why 'committed' writes may not be visible");
        System.out.println();
        System.out.println("Load Buffer:");
        System.out.println("- A read may be speculatively loaded here");
        System.out.println("- The load may be cancelled if a branch is mispredicted");
        System.out.println();
        System.out.println("Memory barriers flush these buffers:");
        System.out.println("- StoreLoad barrier: flushes the store buffer (most expensive)");
        System.out.println("- LoadLoad barrier: drains the load buffer");
        System.out.println("- These barriers are what volatile and synchronized insert");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 3. Visibility Problem Without volatile
    // ═══════════════════════════════════════════════════════════════

    static void visibilityProblem() throws InterruptedException {
        System.out.println("=== Visibility Problem Without volatile ===");

        int[] count = {0};
        boolean[] ready = {false};

        Thread writer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                count[0] = i;
            }
            ready[0] = true; // non-volatile
            System.out.println("Writer: count=" + count[0] + ", ready=true");
        });

        Thread reader = new Thread(() -> {
            while (!ready[0]) {}
            // Without volatile, the reader may see:
            // - ready = true (or false, or stale)
            // - count = 0 (or some intermediate value, or 1000)
            System.out.println("Reader: count=" + count[0] + " (may not be 1000)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Without volatile, the reader has no visibility guarantee.\n");
    }

    // ═══════════════════════════════════════════════════════════════
    // 4. Visibility Fix with volatile
    // ═══════════════════════════════════════════════════════════════

    static void volatileFix() throws InterruptedException {
        System.out.println("=== Visibility Fix with volatile ===");

        volatile int[] count = {0};
        volatile boolean[] ready = {false};

        Thread writer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                count[0] = i;
            }
            ready[0] = true; // volatile write — flushes to main memory
            System.out.println("Writer: count=" + count[0] + ", ready=true");
        });

        Thread reader = new Thread(() -> {
            while (!ready[0]) {} // volatile read — invalidates cache
            // volatile write HB volatile read
            // count write HB ready write (program order)
            // Therefore: count write HB ready read
            System.out.println("Reader: count=" + count[0] + " (guaranteed 1000)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 5. Visibility Fix with synchronized
    // ═══════════════════════════════════════════════════════════════

    static void synchronizedFix() throws InterruptedException {
        System.out.println("=== Visibility Fix with synchronized ===");

        Object lock = new Object();
        int[] count = {0};
        boolean[] ready = {false};

        Thread writer = new Thread(() -> {
            synchronized (lock) {
                for (int i = 1; i <= 1000; i++) {
                    count[0] = i;
                }
                ready[0] = true;
            } // unlock — all writes flushed
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            synchronized (lock) { // lock — sees all previous writes
                System.out.println("Reader: count=" + count[0] + ", ready=" + ready[0]);
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 6. volatile for Atomicity — NOT Enough
    // ═══════════════════════════════════════════════════════════════

    static void volatileAtomicity() throws InterruptedException {
        System.out.println("=== volatile Does NOT Guarantee Atomicity ===");
        System.out.println("volatile ensures visibility but not atomicity of compound operations.");

        volatile int[] counter = {0};

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) counter[0]++;
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) counter[0]++;
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        int expected = 20000;
        System.out.println("Expected: " + expected + ", Actual: " + counter[0]);
        System.out.println("Lost: " + (expected - counter[0]) + " (volatile doesn't help here)");
        System.out.println("Use AtomicInteger or synchronized for atomic increment.\n");
    }

    // ═══════════════════════════════════════════════════════════════
    // 7. volatile vs Atomic — When to Use What
    // ═══════════════════════════════════════════════════════════════

    static void volatileVsAtomic() throws InterruptedException {
        System.out.println("=== volatile vs AtomicInteger ===");
        System.out.println();

        // volatile: good for simple flags
        volatile boolean[] shutdown = {false};

        Thread flagWorker = new Thread(() -> {
            while (!shutdown[0]) { /* spin */ }
            System.out.println("Flag worker stopped (volatile flag)");
        });

        // AtomicInteger: good for counters
        java.util.concurrent.atomic.AtomicInteger atomicCount =
            new java.util.concurrent.atomic.AtomicInteger(0);

        Thread atomicWorker = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                atomicCount.incrementAndGet(); // atomic CAS operation
            }
        });

        flagWorker.start();
        atomicWorker.start();

        Thread.sleep(50);
        shutdown[0] = true;
        atomicWorker.join();
        flagWorker.join();

        System.out.println("volatile flag: used for simple boolean flag");
        System.out.println("AtomicInteger count: " + atomicCount.get() + " (guaranteed 10000)");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 8. Final Fields — Free Safe Publication
    // ═══════════════════════════════════════════════════════════════

    static void finalFields() throws InterruptedException {
        System.out.println("=== Final Fields — Free Safe Publication ===");
        System.out.println("final fields are guaranteed visible after constructor completes.");
        System.out.println("No synchronization needed!");

        class ImmutableConfig {
            final String name;
            final int value;
            final boolean enabled;

            ImmutableConfig(String name, int value, boolean enabled) {
                this.name = name;
                this.value = value;
                this.enabled = enabled;
            }
        }

        ImmutableConfig[] ref = {null};

        Thread publisher = new Thread(() -> {
            ref[0] = new ImmutableConfig("db", 5432, true);
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            ImmutableConfig c = ref[0];
            if (c != null) {
                System.out.println("name=" + c.name + ", value=" + c.value + ", enabled=" + c.enabled);
                System.out.println("All final fields guaranteed visible!");
            }
        });

        publisher.start();
        reader.start();
        publisher.join();
        reader.join();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════
    // 9. Out-of-Thin-Air Values
    // ═══════════════════════════════════════════════════════════════

    static void outOfThinAir() {
        System.out.println("=== Out-of-Thin-Air Values ===");
        System.out.println();
        System.out.println("The JMM prohibits values that were never written by any thread.");
        System.out.println();
        System.out.println("Example that is ILLEGAL under the JMM:");
        System.out.println("  int x = 0, y = 0;");
        System.out.println("  // Thread 1:          // Thread 2:");
        System.out.println("  if (x == 0)           if (y == 0)");
        System.out.println("    y = 1;                x = 1;");
        System.out.println();
        System.out.println("An aggressive optimizer could 'prove':");
        System.out.println("  'If x==0 then y=1, and if y==0 then x=1.");
        System.out.println("   Therefore both x and y must be 1.'");
        System.out.println();
        System.out.println("The JMM explicitly forbids this circular reasoning.");
        System.out.println("A value of 1 for both x and y would be 'out of thin air'.\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║      MEMORY VISIBILITY — DEEP DIVE                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        cacheHierarchy();
        storeAndLoadBuffers();
        visibilityProblem();
        volatileFix();
        synchronizedFix();
        volatileAtomicity();
        volatileVsAtomic();
        finalFields();
        outOfThinAir();

        System.out.println("Summary:");
        System.out.println("1. CPU caches cause visibility issues between threads");
        System.out.println("2. Store buffers delay write visibility");
        System.out.println("3. volatile flushes stores and invalidates caches");
        System.out.println("4. volatile doesn't fix compound operation races");
        System.out.println("5. final fields provide free safe publication");
        System.out.println("6. The JMM prohibits out-of-thin-air values");
    }
}
