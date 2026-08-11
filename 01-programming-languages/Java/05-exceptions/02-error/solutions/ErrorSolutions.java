package academy.javaengineering.exceptions.error.solutions;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Solutions for Error exercises.
 *
 * <p>Each solution demonstrates the correct approach to handling
 * or preventing different Error conditions.
 */
public class ErrorSolutions {

    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    public static void main(String[] args) {
        System.out.println("=== Error Solutions ===");
        System.out.println();

        solution1_FixStackOverflow();
        solution2_PreventOOM();
        solution3_MemoryMonitoring();
        solution4_BoundedCache();
        solution5_ProductionShutdown();
    }

    // ----------------------------------------------------------------
    // Solution 1: Fix StackOverflow
    // ----------------------------------------------------------------

    /**
     * Demonstrates how to fix StackOverflowError by adding a base case.
     */
    static void solution1_FixStackOverflow() {
        System.out.println("Solution 1: Fix StackOverflowError");
        System.out.println("-".repeat(40));

        System.out.println("Problem: Recursive method without base case");
        System.out.println("  public static int factorial(int n) {");
        System.out.println("      return n * factorial(n - 1);");
        System.out.println("  }");
        System.out.println();

        System.out.println("Solution: Add a base case");
        System.out.println("  public static int factorial(int n) {");
        System.out.println("      if (n <= 1) return 1; // Base case");
        System.out.println("      return n * factorial(n - 1);");
        System.out.println("  }");
        System.out.println();

        // Verify
        System.out.println("factorial(5) = " + factorial(5));
        System.out.println("factorial(10) = " + factorial(10));
        System.out.println("factorial(0) = " + factorial(0));
        System.out.println();
    }

    static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // ----------------------------------------------------------------
    // Solution 2: Prevent OOM
    // ----------------------------------------------------------------

    /**
     * Demonstrates how to prevent OutOfMemoryError through streaming.
     */
    static void solution2_PreventOOM() {
        System.out.println("Solution 2: Prevent OutOfMemoryError");
        System.out.println("-".repeat(40));

        System.out.println("Problem: Loading entire file into memory");
        System.out.println("  List<byte[]> chunks = new ArrayList<>();");
        System.out.println("  while (is.read(buffer) != -1) {");
        System.out.println("      chunks.add(buffer.clone());");
        System.out.println("  }");
        System.out.println();

        System.out.println("Solution: Process data in streams");
        System.out.println("  byte[] buffer = new byte[8192];");
        System.out.println("  while (is.read(buffer) != -1) {");
        System.out.println("      processChunk(buffer);");
        System.out.println("  }");
        System.out.println();

        System.out.println("Benefits:");
        System.out.println("  - Constant memory usage regardless of file size");
        System.out.println("  - No risk of OOM for large files");
        System.out.println("  - Better GC behavior");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Solution 3: Memory Monitoring
    // ----------------------------------------------------------------

    /**
     * Demonstrates how to monitor heap usage proactively.
     */
    static void solution3_MemoryMonitoring() {
        System.out.println("Solution 3: Memory Monitoring");
        System.out.println("-".repeat(40));

        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        long used = heap.getUsed();
        long max = heap.getMax();
        double percent = (double) used / max * 100;

        System.out.println("Current heap usage: " + formatBytes(used) +
                " / " + formatBytes(max) +
                String.format(" (%.1f%%)", percent));
        System.out.println();

        System.out.println("Monitoring implementation:");
        System.out.println();
        System.out.println("ScheduledExecutorService scheduler =");
        System.out.println("    Executors.newSingleThreadScheduledExecutor();");
        System.out.println();
        System.out.println("scheduler.scheduleAtFixedRate(() -> {");
        System.out.println("    MemoryUsage heap = memoryBean.getHeapMemoryUsage();");
        System.out.println("    double percent = (double) heap.getUsed() / heap.getMax() * 100;");
        System.out.println();
        System.out.println("    if (percent > 80) {");
        System.out.println("        logger.warn(\"Heap usage high: {}%\", percent);");
        System.out.println("    }");
        System.out.println("    if (percent > 95) {");
        System.out.println("        logger.error(\"Heap critical: {}%\", percent);");
        System.out.println("        triggerAlert();");
        System.out.println("    }");
        System.out.println("}, 0, 30, TimeUnit.SECONDS);");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Solution 4: Bounded Cache
    // ----------------------------------------------------------------

    /**
     * Demonstrates how to implement a bounded cache to prevent leaks.
     */
    static void solution4_BoundedCache() {
        System.out.println("Solution 4: Bounded Cache");
        System.out.println("-".repeat(40));

        System.out.println("Problem: Unbounded cache causes memory leak");
        System.out.println("  static Map<String, byte[]> cache = new HashMap<>();");
        System.out.println("  // Never cleaned up → memory leak");
        System.out.println();

        System.out.println("Solution: Bounded cache with LRU eviction");
        System.out.println("  static Map<String, byte[]> cache = new LinkedHashMap<>");
        System.out.println("      (100, 0.75f, true) {");
        System.out.println("      @Override");
        System.out.println("      protected boolean removeEldestEntry(");
        System.out.println("          Map.Entry<String, byte[]> eldest) {");
        System.out.println("          return size() > 100;");
        System.out.println("      }");
        System.out.println("  };");
        System.out.println();

        // Demonstrate
        Map<String, byte[]> boundedCache = new LinkedHashMap<>(100, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
                return size() > 100;
            }
        };

        for (int i = 0; i < 200; i++) {
            boundedCache.put("key-" + i, new byte[1024]);
        }

        System.out.println("Requested: 200 entries");
        System.out.println("Actual:    " + boundedCache.size() + " entries");
        System.out.println("Evicted:   " + (200 - boundedCache.size()) + " entries");
        System.out.println();

        System.out.println("Alternative approaches:");
        System.out.println("  - Use Guava Cache with expiration");
        System.out.println("  - Use Caffeine with size and time limits");
        System.out.println("  - Use WeakHashMap for automatic cleanup");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Solution 5: Production Shutdown
    // ----------------------------------------------------------------

    /**
     * Demonstrates how to handle Errors gracefully in production.
     */
    static void solution5_ProductionShutdown() {
        System.out.println("Solution 5: Production Shutdown Hook");
        System.out.println("-".repeat(40));

        System.out.println("Implementation:");
        System.out.println();
        System.out.println("// Set up uncaught handler");
        System.out.println("Thread.setDefaultUncaughtExceptionHandler(");
        System.out.println("    (thread, throwable) -> {");
        System.out.println("        if (throwable instanceof Error) {");
        System.out.println("            logger.fatal(\"Fatal error\", throwable);");
        System.out.println("            initiateShutdown();");
        System.out.println("        }");
        System.out.println("    });");
        System.out.println();
        System.out.println("// Register shutdown hook");
        System.out.println("Runtime.getRuntime().addShutdownHook(new Thread(() -> {");
        System.out.println("    logger.info(\"Shutting down gracefully...\");");
        System.out.println("    releaseResources();");
        System.out.println("    closeConnections();");
        System.out.println("    flushBuffers();");
        System.out.println("}));");
        System.out.println();

        // Register a demonstration hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[Shutdown Hook] Cleaning up resources...");
            System.out.println("[Shutdown Hook] Done.");
        }));

        System.out.println("Key principles:");
        System.out.println("  1. Log the error with full context");
        System.out.println("  2. Capture heap/thread dumps if possible");
        System.out.println("  3. Release resources in order");
        System.out.println("  4. Notify monitoring systems");
        System.out.println("  5. Exit with non-zero status code");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------

    static String formatBytes(long bytes) {
        if (bytes < 0) return "unlimited";
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}