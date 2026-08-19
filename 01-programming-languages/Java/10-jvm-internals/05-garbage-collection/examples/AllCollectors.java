package academy.javaengineering.jvm.garbagecollection;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Complete GC Collector Coverage
 * Covers Serial, Parallel, CMS, G1, ZGC, Shenandoah, and Epsilon collectors.
 */
public class AllCollectors {

    private static final Random RANDOM = new Random();
    private static final int ALLOCATION_SIZE = 1024 * 1024; // 1MB

    public static void main(String[] args) {
        System.out.println("=== Complete GC Collector Coverage ===\n");

        // Display current GC configuration
        displayCurrentGC();

        // 1. Serial GC
        demonstrateSerialGC();

        // 2. Parallel GC
        demonstrateParallelGC();

        // 3. CMS (deprecated)
        demonstrateCMS();

        // 4. G1 GC
        demonstrateG1GC();

        // 5. ZGC
        demonstrateZGC();

        // 6. Shenandoah
        demonstrateShenandoah();

        // 7. Epsilon
        demonstrateEpsilon();

        // 8. When to use each collector
        demonstrateWhenToUse();
    }

    private static void displayCurrentGC() {
        System.out.println("--- Current GC Configuration ---");

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("  GC Name: " + gcBean.getName());
            System.out.println("  GC Names: " + String.join(", ", gcBean.getMemoryPoolNames()));
        }
        System.out.println();
    }

    /**
     * Serial GC: Single-threaded, stop-the-world
     * - Best for small applications, single CPU
     * - Uses mark-sweep-compact
     * - Enable: -XX:+UseSerialGC
     */
    private static void demonstrateSerialGC() {
        System.out.println("--- 1. Serial GC ---");

        System.out.println("Characteristics:");
        System.out.println("  - Single-threaded (one GC thread)");
        System.out.println("  - Stop-the-world (all application threads pause)");
        System.out.println("  - Uses mark-sweep-compact algorithm");
        System.out.println("  - Compacts after collection");
        System.out.println("  - Low overhead, good for small heaps\n");

        System.out.println("Use cases:");
        System.out.println("  - Small applications (< 100MB)");
        System.out.println("  - Single CPU machines");
        System.out.println("  - Client-side applications");
        System.out.println("  - Development/testing environments\n");

        System.out.println("Configuration:");
        System.out.println("  -XX:+UseSerialGC             (enable Serial GC)");
        System.out.println("  -Xms256m -Xmx512m           (small heap recommended)\n");

        System.out.println("Advantages:");
        System.out.println("  + Simple, low overhead");
        System.out.println("  + Predictable pause times");
        System.out.println("  + Low memory footprint");
        System.out.println("  + Good for single-core machines\n");

        System.out.println("Disadvantages:");
        System.out.println("  - Long pause times for large heaps");
        System.out.println("  - No parallel collection");
        System.out.println("  - Not suitable for latency-sensitive apps\n");

        simulateGCPressure("Serial GC");
    }

    /**
     * Parallel GC: Multi-threaded, throughput-focused
     * - Default GC in Java 8
     * - Uses multiple GC threads
     * - Enable: -XX:+UseParallelGC
     */
    private static void demonstrateParallelGC() {
        System.out.println("--- 2. Parallel GC (Throughput Collector) ---");

        System.out.println("Characteristics:");
        System.out.println("  - Multi-threaded (uses all available cores)");
        System.out.println("  - Stop-the-world (parallel collection)");
        System.out.println("  - Uses mark-sweep-compact algorithm");
        System.out.println("  - Optimized for throughput");
        System.out.println("  - Default GC in Java 8\n");

        System.out.println("Use cases:");
        System.out.println("  - Batch processing");
        System.out.println("  - Scientific computing");
        System.out.println("  - Throughput-focused applications");
        System.out.println("  - Background data processing\n");

        System.out.println("Configuration:");
        System.out.println("  -XX:+UseParallelGC            (enable Parallel GC)");
        System.out.println("  -XX:ParallelThreads=8         (number of GC threads)");
        System.out.println("  -XX:MaxGCPauseMillis=200      (target pause time)");
        System.out.println("  -XX:GCTimeRatio=19            (GC time ratio)\n");

        System.out.println("Advantages:");
        System.out.println("  + High throughput");
        System.out.println("  + Multi-threaded collection");
        System.out.println("  + Good for batch processing");
        System.out.println("  + Mature and stable\n");

        System.out.println("Disadvantages:");
        System.out.println("  - Long pause times (stop-the-world)");
        System.out.println("  - Not suitable for latency-sensitive apps");
        System.out.println("  - Compaction can be slow\n");

        simulateGCPressure("Parallel GC");
    }

    /**
     * CMS (Concurrent Mark-Sweep): Deprecated in Java 9
     * - Low pause times
     * - Concurrent marking and sweeping
     * - Enable: -XX:+UseConcMarkSweepGC
     */
    private static void demonstrateCMS() {
        System.out.println("--- 3. CMS (Concurrent Mark-Sweep) [DEPRECATED] ---");

        System.out.println("Characteristics:");
        System.out.println("  - Concurrent marking and sweeping");
        System.out.println("  - Low pause times");
        System.out.println("  - Uses mark-sweep (no compaction)");
        System.out.println("  - Deprecated in Java 9, removed in Java 14\n");

        System.out.println("Use cases (historical):");
        System.out.println("  - Latency-sensitive applications");
        System.out.println("  - Web servers");
        System.out.println("  - Applications requiring low pause times\n");

        System.out.println("Configuration (pre-Java 14):");
        System.out.println("  -XX:+UseConcMarkSweepGC      (enable CMS)");
        System.out.println("  -XX:CMSInitiatingOccupancyFraction=70");
        System.out.println("  -XX:+UseCMSInitiatingOccupancyOnly\n");

        System.out.println("Why CMS was deprecated:");
        System.out.println("  - Memory fragmentation (no compaction)");
        System.out.println("  - Concurrent mode failure");
        System.out.println("  - Floating garbage");
        System.out.println("  - G1GC provides better alternatives\n");

        System.out.println("Replacement: Use G1GC or ZGC for low pause times\n");
    }

    /**
     * G1 GC (Garbage-First): Region-based, balanced
     * - Default GC in Java 9+
     * - Divides heap into regions
     * - Balanced latency and throughput
     * - Enable: -XX:+UseG1GC
     */
    private static void demonstrateG1GC() {
        System.out.println("--- 4. G1 GC (Garbage-First) ---");

        System.out.println("Characteristics:");
        System.out.println("  - Region-based heap layout");
        System.out.println("  - Concurrent and parallel collection");
        System.out.println("  - Predictable pause times");
        System.out.println("  - Default GC in Java 9+\n");

        System.out.println("G1 Regions:");
        System.out.println("  - Eden regions (new objects)");
        System.out.println("  - Survivor regions (survived objects)");
        System.out.println("  - Old regions (long-lived objects)");
        System.out.println("  - Humongous regions (large objects > 50% region size)\n");

        System.out.println("Use cases:");
        System.out.println("  - Balanced latency and throughput");
        System.out.println("  - Large heaps (4GB - 16GB)");
        System.out.println("  - Web applications");
        System.out.println("  - Applications requiring predictable pauses\n");

        System.out.println("Configuration:");
        System.out.println("  -XX:+UseG1GC                (enable G1)");
        System.out.println("  -XX:MaxGCPauseMillis=200    (target pause time)");
        System.out.println("  -XX:G1HeapRegionSize=16m    (region size)");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45 (IHOP)");
        System.out.println("  -XX:G1ReservePercent=10     (reserve for to-space)");
        System.out.println("  -XX:G1NewSizePercent=5      (min young gen)");
        System.out.println("  -XX:G1MaxNewSizePercent=60  (max young gen)\n");

        System.out.println("Advantages:");
        System.out.println("  + Predictable pause times");
        System.out.println("  + Compaction during concurrent cycle");
        System.out.println("  + Handles large heaps well");
        System.out.println("  + Default in Java 9+\n");

        System.out.println("Disadvantages:");
        System.out.println("  - Higher memory overhead");
        System.out.println("  - More complex than Parallel");
        System.out.println("  - May not achieve very low pause times\n");

        simulateGCPressure("G1 GC");
    }

    /**
     * ZGC: Ultra-low latency (<10ms pauses)
     * - Uses load barriers and colored pointers
     * - Concurrent and parallel
     * - Enable: -XX:+UseZGC
     */
    private static void demonstrateZGC() {
        System.out.println("--- 5. ZGC (Z Garbage Collector) ---");

        System.out.println("Characteristics:");
        System.out.println("  - Ultra-low latency (< 10ms pauses)");
        System.out.println("  - Concurrent and parallel");
        System.out.println("  - Uses load barriers and colored pointers");
        System.out.println("  - Supports heaps up to 16TB");
        System.out.println("  - Pause times do not increase with heap size\n");

        System.out.println("ZGC Techniques:");
        System.out.println("  - Colored pointers (metadata in pointer bits)");
        System.out.println("  - Load barriers (check pointer color during load)");
        System.out.println("  - Multi-phase marking");
        System.out.println("  - Concurrent compaction");
        System.out.println("  - No stop-the-world compaction\n");

        System.out.println("Use cases:");
        System.out.println("  - Ultra-low latency applications");
        System.out.println("  - Large heaps (16GB - 16TB)");
        System.out.println("  - Real-time systems");
        System.out.println("  - Financial trading platforms\n");

        System.out.println("Configuration:");
        System.out.println("  -XX:+UseZGC                  (enable ZGC)");
        System.out.println("  -XX:ZCollectionInterval=5    (seconds between cycles)");
        System.out.println("  -XX:ZAllocationSpikeTolerance=2.0");
        System.out.println("  -XX:SoftMaxHeapSize=8g       (soft limit)");
        System.out.println("  -Xmx16g                     (max heap)\n");

        System.out.println("Advantages:");
        System.out.println("  + Sub-10ms pause times");
        System.out.println("  + Pause times independent of heap size");
        System.out.println("  + Handles very large heaps");
        System.out.println("  + Concurrent compaction\n");

        System.out.println("Disadvantages:");
        System.out.println("  - Higher memory overhead");
        System.out.println("  - Higher CPU usage");
        System.out.println("  - Still experimental in some JDK versions\n");
    }

    /**
     * Shenandoah: Low-pause, concurrent compaction
     * - Similar to ZGC but different approach
     * - Uses Brooks pointers
     * - Enable: -XX:+UseShenandoahGC
     */
    private static void demonstrateShenandoah() {
        System.out.println("--- 6. Shenandoah GC ---");

        System.out.println("Characteristics:");
        System.out.println("  - Low-pause, concurrent compaction");
        System.out.println("  - Uses Brooks pointers (forwarding pointers)");
        System.out.println("  - Pause times do not increase with heap size");
        System.out.println("  - Similar goals to ZGC, different approach\n");

        System.out.println("Shenandoah Techniques:");
        System.out.println("  - Brooks pointers (embedded forwarding pointers)");
        System.out.println("  - Concurrent evacuation");
        System.out.println("  - Load reference barrier");
        System.out.println("  - Single-generation and multi-mode options\n");

        System.out.println("Use cases:");
        System.out.println("  - Latency-sensitive applications");
        System.out.println("  - Large heaps");
        System.out.println("  - Applications requiring consistent pause times\n");

        System.out.println("Configuration:");
        System.out.println("  -XX:+UseShenandoahGC           (enable Shenandoah)");
        System.out.println("  -XX:ShenandoahGCHeuristics=adaptive (heuristic mode)");
        System.out.println("  -XX:ShenandoahMinFreeThreshold=10");
        System.out.println("  -XX:ShenandoahUncommitDelay=300000 (ms)\n");

        System.out.println("Heuristic modes:");
        System.out.println("  - adaptive: Automatic (default)");
        System.out.println("  - compact: Always compact");
        System.out.println("  - static: Static threshold");
        System.out.println("  - aggressive: More frequent collection");
        System.out.println("  - passive: Only at safepoints\n");

        System.out.println("Advantages:");
        System.out.println("  + Ultra-low pause times");
        System.out.println("  + Concurrent compaction");
        System.out.println("  + Pause times independent of heap size\n");

        System.out.println("Disadvantages:");
        System.out.println("  - Higher CPU usage");
        System.out.println("  - Requires OpenJDK (not in Oracle JDK)");
        System.out.println("  - Higher memory overhead\n");
    }

    /**
     * Epsilon: No-op GC (testing only)
     * - Does nothing (no garbage collection)
     * - For performance testing
     * - Enable: -XX:+UseEpsilonGC
     */
    private static void demonstrateEpsilon() {
        System.out.println("--- 7. Epsilon GC (No-Op) ---");

        System.out.println("Characteristics:");
        System.out.println("  - No garbage collection at all");
        System.out.println("  - Memory grows until OOM");
        System.out.println("  - For performance testing and benchmarking");
        System.out.println("  - Introduced in Java 11\n");

        System.out.println("Use cases:");
        System.out.println("  - Performance testing (measure GC overhead)");
        System.out.println("  - Short-lived applications");
        System.out.println("  - Applications with known memory lifetime");
        System.out.println("  - Latency-sensitive workloads with bounded memory\n");

        System.out.println("Configuration:");
        System.out.println("  -XX:+UseEpsilonGC             (enable Epsilon GC)");
        System.out.println("  -XX:+ExitOnOutOfMemoryError   (exit on OOM)\n");

        System.out.println("Advantages:");
        System.out.println("  + Zero GC overhead");
        System.out.println("  + Predictable memory usage");
        System.out.println("  + Fastest possible execution\n");

        System.out.println("Disadvantages:");
        System.out.println("  - OutOfMemoryError when heap fills");
        System.out.println("  - Not suitable for long-running apps");
        System.out.println("  - Manual memory management required\n");
    }

    /**
     * When to use each collector
     */
    private static void demonstrateWhenToUse() {
        System.out.println("--- 8. When to Use Each Collector ---");

        System.out.println("Decision Matrix:");
        System.out.println("┌─────────────────┬──────────────┬─────────────┬─────────────────┐");
        System.out.println("│ Collector       │ Pause Target │ Heap Size   │ Best For        │");
        System.out.println("├─────────────────┼──────────────┼─────────────┼─────────────────┤");
        System.out.println("│ Serial          │ >500ms       │ <256MB      │ Small apps      │");
        System.out.println("│ Parallel        │ >200ms       │ 256MB-4GB   │ Throughput      │");
        System.out.println("│ CMS (deprecated)│ <200ms       │ 256MB-4GB   │ Low latency     │");
        System.out.println("│ G1              │ <200ms       │ 4GB-16GB    │ Balanced        │");
        System.out.println("│ ZGC             │ <10ms        │ 16GB-16TB   │ Ultra-low lat.  │");
        System.out.println("│ Shenandoah      │ <10ms        │ 4GB-16GB    │ Low latency     │");
        System.out.println("│ Epsilon         │ N/A          │ Any         │ Testing         │");
        System.out.println("└─────────────────┴──────────────┴─────────────┴─────────────────┘\n");

        System.out.println("Quick guide:");
        System.out.println("  Small app (<256MB): Serial GC");
        System.out.println("  Throughput-focused: Parallel GC");
        System.out.println("  Balanced latency/throughput: G1 GC");
        System.out.println("  Ultra-low latency (<10ms): ZGC or Shenandoah");
        System.out.println("  Performance testing: Epsilon GC\n");

        System.out.println("Modern recommendations (Java 17+):");
        System.out.println("  Default choice: G1 GC");
        System.out.println("  Latency-critical: ZGC");
        System.out.println("  Large heaps (>16GB): ZGC");
        System.out.println("  Throughput-critical: Parallel GC\n");
    }

    private static void simulateGCPressure(String gcName) {
        System.out.println("  Simulating GC pressure for " + gcName + "...");
        List<byte[]> memory = new ArrayList<>();

        try {
            for (int i = 0; i < 10; i++) {
                memory.add(new byte[ALLOCATION_SIZE]);
                System.out.println("    Allocated " + (i + 1) + " MB");
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        memory.clear();
        System.out.println("  Released memory\n");
    }
}
