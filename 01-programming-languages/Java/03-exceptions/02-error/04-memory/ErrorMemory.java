package academy.javaengineering.exceptions.error.memory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates memory-related Error conditions in Java.
 *
 * <p>Shows heap monitoring, memory leaks, memory exhaustion,
 * and GC overhead scenarios.
 */
public class ErrorMemory {

    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    // Simulated bounded cache
    private static final Map<String, byte[]> boundedCache = new LinkedHashMap<>(100, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
            return size() > 100; // Max 100 entries
        }
    };

    // Unbounded cache (memory leak)
    private static final Map<String, byte[]> unboundedCache = new LinkedHashMap<>();

    public static void main(String[] args) {
        System.out.println("=== Memory Error Examples ===");
        System.out.println();

        demonstrateHeapMonitoring();
        demonstrateMemoryExhaustion();
        demonstrateMemoryLeak();
        demonstrateBoundedCache();
        demonstrateGCOverhead();
    }

    // ----------------------------------------------------------------
    // 1. Heap Monitoring
    // ----------------------------------------------------------------

    /**
     * Shows how to monitor heap memory usage.
     */
    static void demonstrateHeapMonitoring() {
        System.out.println("1. Heap Monitoring");
        System.out.println("-".repeat(40));

        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        System.out.println("Current Heap State:");
        System.out.println("  Init:      " + formatBytes(heap.getInit()));
        System.out.println("  Used:      " + formatBytes(heap.getUsed()));
        System.out.println("  Committed: " + formatBytes(heap.getCommitted()));
        System.out.println("  Max:       " + formatBytes(heap.getMax()));
        System.out.println();

        double usedPercent = (double) heap.getUsed() / heap.getMax() * 100;
        System.out.printf("Usage: %.1f%%%n", usedPercent);
        System.out.println();

        // Allocate some objects to show memory change
        List<byte[]> temp = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            temp.add(new byte[1024 * 1024]); // 1MB each
        }

        heap = memoryBean.getHeapMemoryUsage();
        System.out.println("After allocating 10MB:");
        System.out.println("  Used: " + formatBytes(heap.getUsed()));
        System.out.println();

        // Clear the list to allow GC
        temp.clear();
        temp = null;
        System.gc();

        heap = memoryBean.getHeapMemoryUsage();
        System.out.println("After GC:");
        System.out.println("  Used: " + formatBytes(heap.getUsed()));
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 2. Memory Exhaustion
    // ----------------------------------------------------------------

    /**
     * Demonstrates what happens when the heap is exhausted.
     * WARNING: This will throw OutOfMemoryError.
     */
    static void demonstrateMemoryExhaustion() {
        System.out.println("2. Memory Exhaustion (OOM)");
        System.out.println("-".repeat(40));

        System.out.println("Memory exhaustion occurs when:");
        System.out.println("  1. Heap is full");
        System.out.println("  2. Full GC cannot reclaim enough space");
        System.out.println("  3. New allocation request arrives");
        System.out.println("  4. JVM throws OutOfMemoryError");
        System.out.println();

        System.out.println("Common causes:");
        System.out.println("  - Legitimate large data processing");
        System.out.println("  - Heap too small for workload");
        System.out.println("  - Memory leak (see section 3)");
        System.out.println();

        System.out.println("To trigger OOM, uncomment the call in main().");
        System.out.println();

        // Uncomment to trigger:
        // fillHeap();
    }

    /**
     * Fills the heap until OOM occurs.
     */
    static void fillHeap() {
        List<byte[]> memoryHog = new ArrayList<>();
        int count = 0;
        while (true) {
            memoryHog.add(new byte[1024 * 1024]); // 1MB chunks
            count++;
            if (count % 50 == 0) {
                MemoryUsage heap = memoryBean.getHeapMemoryUsage();
                System.out.printf("Allocated %dMB, heap used: %s%n",
                        count, formatBytes(heap.getUsed()));
            }
        }
    }

    // ----------------------------------------------------------------
    // 3. Memory Leak
    // ----------------------------------------------------------------

    /**
     * Demonstrates a memory leak through an unbounded cache.
     */
    static void demonstrateMemoryLeak() {
        System.out.println("3. Memory Leak");
        System.out.println("-".repeat(40));

        System.out.println("A memory leak occurs when objects are no longer needed");
        System.out.println("but remain reachable (and thus not garbage collected).");
        System.out.println();

        System.out.println("Simulating leak: adding entries to unbounded cache...");
        for (int i = 0; i < 1000; i++) {
            // Each entry is 1KB, total 1MB
            unboundedCache.put("key-" + i, new byte[1024]);
        }

        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        System.out.println("After adding 1000 entries (1KB each):");
        System.out.println("  Cache size: " + unboundedCache.size());
        System.out.println("  Heap used:  " + formatBytes(heap.getUsed()));
        System.out.println();

        System.out.println("The cache will never be cleaned up because:");
        System.out.println("  - unboundedCache is static (always reachable)");
        System.out.println("  - No eviction policy");
        System.out.println("  - No size limit");
        System.out.println("  - GC cannot reclaim these objects");
        System.out.println();

        System.out.println("Fix: Use a bounded cache with eviction (see section 4).");
        System.out.println();

        // Clear to avoid actual OOM
        unboundedCache.clear();
    }

    // ----------------------------------------------------------------
    // 4. Bounded Cache
    // ----------------------------------------------------------------

    /**
     * Shows how to implement a bounded cache that prevents memory leaks.
     */
    static void demonstrateBoundedCache() {
        System.out.println("4. Bounded Cache (Leak Prevention)");
        System.out.println("-".repeat(40));

        System.out.println("Bounded cache with LRU eviction (max 100 entries):");
        for (int i = 0; i < 200; i++) {
            boundedCache.put("key-" + i, new byte[1024]);
        }

        System.out.println("  Requested: 200 entries");
        System.out.println("  Actual:    " + boundedCache.size() + " entries");
        System.out.println("  Evicted:   " + (200 - boundedCache.size()) + " entries");
        System.out.println();

        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        System.out.println("  Heap used: " + formatBytes(heap.getUsed()));
        System.out.println();

        System.out.println("Benefits of bounded cache:");
        System.out.println("  - Memory usage is predictable");
        System.out.println("  - Old entries are evicted automatically");
        System.out.println("  - No memory leak possible");
        System.out.println("  - OOM risk is reduced");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 5. GC Overhead
    // ----------------------------------------------------------------

    /**
     * Demonstrates GC overhead limit exceeded scenario.
     */
    static void demonstrateGCOverhead() {
        System.out.println("5. GC Overhead Limit Exceeded");
        System.out.println("-".repeat(40));

        System.out.println("GC overhead limit exceeded occurs when:");
        System.out.println("  - JVM spends >98% of time in GC");
        System.out.println("  - GC recovers <2% of heap space");
        System.out.println("  - Application is essentially stalled");
        System.out.println();

        System.out.println("This is different from regular OOM:");
        System.out.println("  - Regular OOM: heap is full, allocation fails");
        System.out.println("  - GC overhead: heap has space, but GC is too slow");
        System.out.println();

        System.out.println("Common causes:");
        System.out.println("  - Memory leak (most common)");
        System.out.println("  - Heap too small for working set");
        System.out.println("  - Excessive weak/soft references");
        System.out.println("  - Large number of finalizers");
        System.out.println();

        System.out.println("JVM flag: -XX:GCOverheadLimit=98 (default)");
        System.out.println("Disable (not recommended): -XX:-UseGCOverheadLimit");
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