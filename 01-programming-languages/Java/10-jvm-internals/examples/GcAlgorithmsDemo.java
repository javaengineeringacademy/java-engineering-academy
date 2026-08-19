package academy.javaengineering.jvm.examples;

import java.util.*;
import java.util.concurrent.*;

/**
 * GC Algorithms Demo - G1, ZGC, Shenandoah
 * Demonstrates modern GC algorithms, their characteristics,
 * tuning parameters, and when to use each.
 */
public class GcAlgorithmsDemo {

    private static final int HEAP_SIZE_MB = 256;
    private static final List<byte[]> cache = new ArrayList<>();

    /**
     * DEMO 1: G1 (Garbage First) Collector
     * Default collector since JDK 9. Region-based, predictable pauses.
     */
    public static void demonstrateG1() {
        System.out.println("=== G1 (Garbage First) Collector ===");
        System.out.println("Architecture:");
        System.out.println("  ┌──────────────────────────────────────┐");
        System.out.println("  │  Eden  │ Eden  │ Surv │ Surv │ Old  │");
        System.out.println("  │  (E)   │  (E)  │ (S)  │ (S)  │ (O)  │");
        System.out.println("  ├────────┼───────┼──────┼──────┼──────┤");
        System.out.println("  │  Old   │  Old  │ Hum  │ Large│ Free │");
        System.out.println("  │  (O)   │  (O)  │(Hum) │ (L)  │      │");
        System.out.println("  └────────┴───────┴──────┴──────┴──────┘");

        System.out.println("\nKey Features:");
        System.out.println("  - Heap divided into equal-size regions (1-32MB)");
        System.out.println("  - Collects most garbage-filled regions first");
        System.out.println("  - Concurrent marking phase");
        System.out.println("  - Predictable pause times via -XX:MaxGCPauseMillis");
        System.out.println("  - Humongous objects (> 50% region size) get special regions");

        System.out.println("\nG1 Tuning Flags:");
        System.out.println("  -XX:+UseG1GC                    Enable G1");
        System.out.println("  -XX:MaxGCPauseMillis=200        Target max pause (default 200ms)");
        System.out.println("  -XX:G1HeapRegionSize=N          Region size (1MB to 32MB)");
        System.out.println("  -XX:G1NewSizePercent=5          Min young gen percentage");
        System.out.println("  -XX:G1MaxNewSizePercent=60      Max young gen percentage");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45  IHOP threshold");
        System.out.println("  -XX:G1MixedGCCountTarget=8      Mixed GC count target");
        System.out.println("  -XX:G1ReservePercent=10         Reserve for to-space");
    }

    /**
     * DEMO 2: ZGC (Z Garbage Collector)
     * Ultra-low latency (<1ms pauses), scalable to TB heaps.
     */
    public static void demonstrateZGC() {
        System.out.println("\n=== ZGC (Z Garbage Collector) ===");
        System.out.println("Architecture:");
        System.out.println("  - Concurrent and parallel execution");
        System.out.println("  - Colored pointers (pointer tagging)");
        System.out.println("  - Load barriers (not write barriers)");
        System.out.println("  - Multi-phase relocation");

        System.out.println("\nKey Features:");
        System.out.println("  - Max pause time < 1ms (typically < 100μs)");
        System.out.println("  - Pause times do NOT increase with heap size");
        System.out.println("  - Supports heaps from 8MB to 16TB");
        System.out.println("  - Concurrent phases: mark, relocate, process weak refs");
        System.out.println("  - Sub-millisecond pauses regardless of heap size");

        System.out.println("\nZGC Tuning Flags:");
        System.out.println("  -XX:+UseZGC                     Enable ZGC");
        System.out.println("  -XX:+ZGenerational              Enable generational ZGC (JDK 21+)");
        System.out.println("  -XX:SoftMaxHeapSize=N           Soft max heap limit");
        System.out.println("  -XX:ZCollectionInterval=N      Proactive collection interval (sec)");
        System.out.println("  -XX:ZAllocationSpikeTolerance=N Allocation spike tolerance");

        System.out.println("\nZGC Phases:");
        System.out.println("  1. Pause Mark Start (< 1ms)");
        System.out.println("  2. Concurrent Mark (no pause)");
        System.out.println("  3. Pause Mark End (< 1ms)");
        System.out.println("  4. Concurrent Prepare for Relocate (no pause)");
        System.out.println("  5. Pause Relocate Start (< 1ms)");
        System.out.println("  6. Concurrent Relocate (no pause)");
    }

    /**
     * DEMO 3: Shenandoah GC
     * Low-pause concurrent collector, developed by Red Hat.
     */
    public static void demonstrateShenandoah() {
        System.out.println("\n=== Shenandoah GC ===");
        System.out.println("Architecture:");
        System.out.println("  - Brooks pointers (forwarding pointers)");
        System.out.println("  - Concurrent compaction");
        System.out.println("  - Load Reference Barriers (LRB)");

        System.out.println("\nKey Features:");
        System.out.println("  - Pause times in low milliseconds");
        System.out.println("  - Pause times do NOT increase with live data size");
        System.out.println("  - Concurrent compaction (unique feature)");
        System.out.println("  - Compact heaps during concurrent phase");
        System.out.println("  - Available since JDK 12 (JEP 189)");

        System.out.println("\nShenandoah Tuning Flags:");
        System.out.println("  -XX:+UseShenandoahGC            Enable Shenandoah");
        System.out.println("  -XX:ShenandoahGCHeuristics=     Adaptive/Compact/Aggressive/Static");
        System.out.println("  -XX:ShenandoahMinFreeThreshold=10 Min free before GC trigger");
        System.out.println("  -XX:ShenandoahGuaranteedGCInterval=N Guaranteed GC interval");

        System.out.println("\nShenandoah vs ZGC:");
        System.out.println("  Shenandoah: concurrent compaction, more tunable");
        System.out.println("  ZGC: colored pointers, simpler, better raw performance");
    }

    /**
     * DEMO 4: Generational ZGC (JDK 21+)
     */
    public static void demonstrateGenerationalZGC() {
        System.out.println("\n=== Generational ZGC (JDK 21+) ===");
        System.out.println("Improvements over non-generational ZGC:");
        System.out.println("  - Lower heap overhead (5-10% vs 15-20%)");
        System.out.println("  - Better throughput (10-15% improvement)");
        System.out.println("  - More efficient young gen collection");
        System.out.println("  - Reduced GC pauses");
        System.out.println("\nEnable: -XX:+UseZGC -XX:+ZGenerational");
    }

    /**
     * DEMO 5: When to Use Which GC
     */
    public static void demonstrateSelection() {
        System.out.println("\n=== GC Selection Guide ===");
        System.out.println("┌─────────────────┬──────────────────────────────────────┐");
        System.out.println("│ Use Case        │ Recommended GC                       │");
        System.out.println("├─────────────────┼──────────────────────────────────────┤");
        System.out.println("│ Small app       │ Serial (-XX:+UseSerialGC)           │");
        System.out.println("│ Batch/Etl       │ Parallel (-XX:+UseParallelGC)       │");
        System.out.println("│ Web server      │ G1 (-XX:+UseG1GC, default)          │");
        System.out.println("│ Latency-critical│ ZGC (-XX:+UseZGC)                   │");
        System.out.println("│ Real-time       │ Shenandoah (-XX:+UseShenandoahGC)   │");
        System.out.println("│ Container/Cloud │ G1 or ZGC with -XX:+UseContainerSupport│");
        System.out.println("└─────────────────┴──────────────────────────────────────┘");

        System.out.println("\nDecision factors:");
        System.out.println("  1. Latency requirements (pause time SLA)");
        System.out.println("  2. Throughput requirements (CPU utilization)");
        System.out.println("  3. Heap size (small vs massive)");
        System.out.println("  4. Object lifetime patterns (short vs long-lived)");
        System.out.println("  5. Available memory overhead");
    }

    /**
     * DEMO 6: GC Logging and Analysis
     */
    public static void demonstrateGCLogging() {
        System.out.println("\n=== GC Logging ===");
        System.out.println("Unified GC logging (JDK 9+):");
        System.out.println("  java -Xlog:gc* -jar app.jar");
        System.out.println("  java -Xlog:gc+heap=debug -jar app.jar");
        System.out.println("  java -Xlog:gc*=info:file=gc.log -jar app.jar");

        System.out.println("\nUseful GC log flags:");
        System.out.println("  -Xlog:gc*                    All GC logging");
        System.out.println("  -Xlog:gc+age=trace           Object age tracing");
        System.out.println("  -Xlog:gc+phases=debug        GC phase details");
        System.out.println("  -Xlog:gc+heap=debug          Heap size details");

        System.out.println("\nAnalysis tools:");
        System.out.println("  - GCEasy (gceasy.io) - online analyzer");
        System.out.println("  - GCViewer - desktop viewer");
        System.out.println("  - JClarity Censum - commercial");
    }

    /**
     * DEMO 7: Interactive GC Demo
     */
    public static void runInteractiveDemo() {
        System.out.println("\n=== Interactive GC Demo ===");
        System.out.println("Allocating objects to demonstrate GC behavior...");

        // Allocate objects
        for (int i = 0; i < 100; i++) {
            cache.add(new byte[1024 * 100]); // 100KB each
        }

        Runtime rt = Runtime.getRuntime();
        System.out.println("After allocating 10MB:");
        System.out.println("  Used: " + ((rt.totalMemory() - rt.freeMemory()) / 1024 / 1024) + " MB");

        // Trigger GC
        System.gc();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("After System.gc():");
        System.out.println("  Used: " + ((rt.totalMemory() - rt.freeMemory()) / 1024 / 1024) + " MB");
        cache.clear();
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║    GC ALGORITHMS DEMO (G1/ZGC/Shen) ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateG1();
        demonstrateZGC();
        demonstrateShenandoah();
        demonstrateGenerationalZGC();
        demonstrateSelection();
        demonstrateGCLogging();
        runInteractiveDemo();

        System.out.println("\n=== Quick Reference ===");
        System.out.println("G1:        -XX:+UseG1GC -XX:MaxGCPauseMillis=200");
        System.out.println("ZGC:       -XX:+UseZGC -XX:+ZGenerational");
        System.out.println("Shenandoah: -XX:+UseShenandoahGC");
        System.out.println("Parallel:   -XX:+UseParallelGC -XX:ParallelGCThreads=N");
    }
}
