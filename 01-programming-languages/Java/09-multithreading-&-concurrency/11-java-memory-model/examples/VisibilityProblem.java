package academy.javaengineering.concurrency.memorymodel.examples;

/**
 * Demonstrates the visibility problem that occurs without synchronization.
 * Without volatile, synchronized, or final, a reader thread may see stale values.
 */
public class VisibilityProblem {

    // Classic visibility bug: writer updates a flag, reader spins on it
    // Without volatile, the reader may never see the update.
    static void staleReadBug() throws InterruptedException {
        System.out.println("=== Stale Read Bug ===");
        System.out.println("Without volatile, the reader may spin forever on a stale value.");

        boolean[] running = {true};

        Thread writer = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            running[0] = false; // non-volatile write
            System.out.println("Writer: set running = false");
        });

        Thread reader = new Thread(() -> {
            int iterations = 0;
            while (running[0]) { // may never see the update!
                iterations++;
                if (iterations % 5000000 == 0) {
                    System.out.println("Reader: still spinning... (" + iterations + " iterations)");
                }
            }
            System.out.println("Reader: saw running = false after " + iterations + " iterations");
        });

        writer.start();
        reader.start();
        writer.join(2000);
        reader.interrupt(); // force stop if still spinning
        reader.join(1000);
        System.out.println("Bug: the reader may never see the writer's update.\n");
    }

    // Visibility issue with a shared counter
    static void counterVisibilityBug() throws InterruptedException {
        System.out.println("=== Counter Visibility Bug ===");
        System.out.println("Without volatile, the reader may see a stale counter value.");

        int[] counter = {0};

        Thread writer = new Thread(() -> {
            for (int i = 1; i <= 1000; i++) {
                counter[0] = i; // non-volatile write
            }
            System.out.println("Writer: counter = " + counter[0]);
        });

        Thread reader = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            // Without volatile, this may see a stale value
            System.out.println("Reader: counter = " + counter[0] + " (may not be 1000)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Without a happens-before, the reader has no guarantee.\n");
    }

    // Why this happens: CPU cache hierarchy
    static void cacheHierarchyExplanation() {
        System.out.println("=== Why Visibility Issues Occur ===");
        System.out.println();
        System.out.println("CPU Cache Hierarchy:");
        System.out.println("  ┌─────────┐  ┌─────────┐");
        System.out.println("  │  Core 0  │  │  Core 1  │");
        System.out.println("  │  (L1)    │  │  (L1)    │");
        System.out.println("  │  L2      │  │  L2      │");
        System.out.println("  └────┬─────┘  └────┬─────┘");
        System.out.println("       │              │");
        System.out.println("    ┌──┴──────────────┴──┐");
        System.out.println("    │      L3 Cache       │");
        System.out.println("    └──────────┬──────────┘");
        System.out.println("               │");
        System.out.println("        ┌──────┴──────┐");
        System.out.println("        │ Main Memory  │");
        System.out.println("        └──────────────┘");
        System.out.println();
        System.out.println("Each core has its own L1/L2 cache.");
        System.out.println("When Thread 1 writes to 'x', the write goes to Core 0's cache.");
        System.out.println("Core 1 may still see the OLD value in its own cache.");
        System.out.println("Without a happens-before, there is no mechanism to force");
        System.out.println("Core 1 to invalidate its cached copy.");
        System.out.println();
        System.out.println("Additionally, CPUs use store buffers — small FIFO queues");
        System.out.println("between the execution unit and the cache. A write may sit");
        System.out.println("in the store buffer and not yet be visible to other cores.\n");
    }

    // Fix using volatile
    static void volatileFix() throws InterruptedException {
        System.out.println("=== Fix: Use volatile ===");
        System.out.println("volatile ensures the write is flushed and the reader invalidates its cache.");

        volatile boolean[] running = {true};

        Thread writer = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            running[0] = false; // volatile write — guaranteed visible
            System.out.println("Writer: set running = false (volatile)");
        });

        Thread reader = new Thread(() -> {
            while (running[0]) { /* volatile read — guaranteed to see latest */ }
            System.out.println("Reader: saw running = false (volatile)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("volatile guarantees visibility.\n");
    }

    // Fix using synchronized
    static void synchronizedFix() throws InterruptedException {
        System.out.println("=== Fix: Use synchronized ===");

        Object lock = new Object();
        boolean[] running = {true};

        Thread writer = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (lock) {
                running[0] = false; // write under lock
            } // unlock flushes the write
        });

        Thread reader = new Thread(() -> {
            boolean shouldRun = true;
            while (shouldRun) {
                synchronized (lock) {
                    shouldRun = running[0]; // read under same lock
                }
            }
            System.out.println("Reader: saw running = false (synchronized)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("synchronized ensures visibility through happens-before.\n");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         VISIBILITY PROBLEMS IN THE JMM                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        staleReadBug();
        counterVisibilityBug();
        cacheHierarchyExplanation();
        volatileFix();
        synchronizedFix();

        System.out.println("Key takeaway: Without a happens-before relationship,");
        System.out.println("a thread may see stale values from CPU caches indefinitely.");
    }
}
