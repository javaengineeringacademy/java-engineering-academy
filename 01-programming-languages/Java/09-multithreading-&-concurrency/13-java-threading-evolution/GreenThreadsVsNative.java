package academy.javaengineering.concurrency.evolution;

/**
 * Green Threads vs Native Threads - Visual Comparison
 * 
 * This program demonstrates the fundamental differences between
 * green threads (N:1 model) and native threads (1:1 model).
 */
public class GreenThreadsVsNative {

    public static void main(String[] args) {
        System.out.println("=== GREEN THREADS vs NATIVE THREADS ===\n");

        demonstrateGreenThreadsModel();
        demonstrateNativeThreadsModel();
        demonstrateKeyDifferences();
    }

    /**
     * Demonstrates how green threads would work (N:1 model)
     * In green threads, multiple Java threads run on ONE OS thread
     */
    private static void demonstrateGreenThreadsModel() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         GREEN THREADS MODEL (N:1)               ║");
        System.out.println("║         Java 1.0 - 1.3                          ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│            JAVA APPLICATION                 │");
        System.out.println("│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐          │");
        System.out.println("│  │ T1  │ │ T2  │ │ T3  │ │ T4  │          │");
        System.out.println("│  └──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘          │");
        System.out.println("│     │       │       │       │               │");
        System.out.println("│  ┌──▼───────▼───────▼───────▼──┐            │");
        System.out.println("│  │   GREEN THREAD SCHEDULER    │            │");
        System.out.println("│  │   (User-Space in JVM)       │            │");
        System.out.println("│  └──────────────┬──────────────┘            │");
        System.out.println("│                 │                           │");
        System.out.println("│  ┌──────────────▼──────────────┐            │");
        System.out.println("│  │      SINGLE OS THREAD       │            │");
        System.out.println("│  │      (Kernel Space)         │            │");
        System.out.println("│  └─────────────────────────────┘            │");
        System.out.println("│                                             │");
        System.out.println("│  ┌─────────────────────────────┐            │");
        System.out.println("│  │      SINGLE CPU CORE        │            │");
        System.out.println("│  └─────────────────────────────┘            │");
        System.out.println("└─────────────────────────────────────────────┘");

        System.out.println("\n--- Simulating Green Thread Behavior ---");
        System.out.println("Only ONE thread executes at a time!\n");

        String[] threadNames = {"T1", "T2", "T3", "T4"};
        long[] threadTimes = {50, 30, 40, 20};
        long totalSequentialTime = 0;

        for (int i = 0; i < threadNames.length; i++) {
            System.out.println("  [" + threadNames[i] + "] Running on OS thread... (" + threadTimes[i] + "ms)");
            totalSequentialTime += threadTimes[i];
        }

        System.out.println("\n  Total sequential time: " + totalSequentialTime + "ms");
        System.out.println("  (All threads share ONE CPU core, run sequentially)\n");

        System.out.println("Green Thread Characteristics:");
        System.out.println("  ✓ Cooperative scheduling (threads yield voluntarily)");
        System.out.println("  ✓ Very low creation cost (~kilobytes)");
        System.out.println("  ✓ Fast context switching (~microseconds)");
        System.out.println("  ✗ NO parallelism (single-core only)");
        System.out.println("  ✗ Blocking I/O freezes ALL threads");
        System.out.println("  ✗ Unreliable cooperative scheduling");
    }

    /**
     * Demonstrates how native threads work (1:1 model)
     * In native threads, each Java thread maps to ONE OS thread
     */
    private static void demonstrateNativeThreadsModel() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         NATIVE THREADS MODEL (1:1)              ║");
        System.out.println("║         Java 1.3+ (Current)                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│            JAVA APPLICATION                 │");
        System.out.println("│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐          │");
        System.out.println("│  │ T1  │ │ T2  │ │ T3  │ │ T4  │          │");
        System.out.println("│  └──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘          │");
        System.out.println("│     │       │       │       │               │");
        System.out.println("├─────┼───────┼───────┼───────┼───────────────┤");
        System.out.println("│     │  JVM (minimal mapping) │               │");
        System.out.println("├─────┼───────┼───────┼───────┼───────────────┤");
        System.out.println("│     ▼       ▼       ▼       ▼               │");
        System.out.println("│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐          │");
        System.out.println("│  │OST1 │ │OST2 │ │OST3 │ │OST4 │          │");
        System.out.println("│  └──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘          │");
        System.out.println("│     │       │       │       │               │");
        System.out.println("│  ┌──▼───────▼───────▼───────▼──┐            │");
        System.out.println("│  │    OS THREAD SCHEDULER       │            │");
        System.out.println("│  │    (Kernel Space)            │            │");
        System.out.println("│  └──────────────┬──────────────┘            │");
        System.out.println("│                 │                           │");
        System.out.println("│  ┌──────┬───────┼───────┬──────┐           │");
        System.out.println("│  │Core 1│Core 2 │Core 3 │Core 4│           │");
        System.out.println("│  └──────┘───────┘───────┘──────┘           │");
        System.out.println("└─────────────────────────────────────────────┘");

        System.out.println("\n--- Simulating Native Thread Behavior ---");
        System.out.println("Multiple threads run on different cores!\n");

        long startTime = System.nanoTime();

        Thread[] threads = new Thread[4];
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            final long workTime = 50;
            threads[i] = new Thread(() -> {
                String coreName = "Core-" + (threadId + 1);
                System.out.println("  [T" + (threadId + 1) + "] Running on " + coreName + "... (" + workTime + "ms)");
                try {
                    Thread.sleep(workTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.nanoTime();
        long actualTime = (endTime - startTime) / 1_000_000;

        System.out.println("\n  Actual parallel execution time: " + actualTime + "ms");
        System.out.println("  (4 threads ran on 4 different CPU cores simultaneously)\n");

        System.out.println("Native Thread Characteristics:");
        System.out.println("  ✓ True parallelism (multi-core)");
        System.out.println("  ✓ Preemptive scheduling (OS-managed)");
        System.out.println("  ✓ Non-blocking I/O (per thread)");
        System.out.println("  ✗ Higher creation cost (~1MB per thread)");
        System.out.println("  ✗ Expensive context switching (~10-100μs)");
        System.out.println("  ✗ Resource contention with many threads");
    }

    /**
     * Shows key differences between the two models
     */
    private static void demonstrateKeyDifferences() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         KEY DIFFERENCES SUMMARY                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        String[][] differences = {
            {"Property", "Green Threads (N:1)", "Native Threads (1:1)"},
            {"Thread Model", "Many Java → 1 OS thread", "1 Java → 1 OS thread"},
            {"Scheduling", "Cooperative (yield-based)", "Preemptive (OS-managed)"},
            {"Parallelism", "None (single-core)", "True multi-core parallelism"},
            {"I/O Handling", "Blocking (all threads freeze)", "Non-blocking (per thread)"},
            {"Context Switch", "~microseconds", "~10-100 microseconds"},
            {"Creation Cost", "~kilobytes", "~1 megabyte"},
            {"Max Threads", "Limited by memory", "Limited by OS (thousands)"},
            {"Portability", "Platform-independent", "Platform-dependent behavior"},
            {"Use Case", "Single-core systems", "Multi-core systems"}
        };

        System.out.println("┌────────────────┬─────────────────────────┬─────────────────────────┐");
        for (int i = 0; i < differences.length; i++) {
            String col1 = String.format("%-16s", differences[i][0]);
            String col2 = String.format("%-23s", differences[i][1]);
            String col3 = String.format("%-23s", differences[i][2]);
            System.out.println("│ " + col1 + " │ " + col2 + " │ " + col3 + " │");
            if (i == 0) {
                System.out.println("├────────────────┼─────────────────────────┼─────────────────────────┤");
            }
        }
        System.out.println("└────────────────┴─────────────────────────┴─────────────────────────┘");

        System.out.println("\n--- Why Green Threads Were Removed ---");
        System.out.println("1. Multi-core processors became standard");
        System.out.println("2. Blocking I/O behavior unacceptable for servers");
        System.out.println("3. Cooperative scheduling unreliable in practice");
        System.out.println("4. Platform inconsistencies (worked on Solaris, failed on Windows)");
        System.out.println("5. Java 1.4 (2002): Green threads completely removed");
    }
}
