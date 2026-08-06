package academy.javaengineering.senior.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * GC Tuning Guide - Comprehensive JVM garbage collection configuration.
 *
 * JVM flags reference:
 *   -Xms / -Xmx      : Heap initial / max size
 *   -Xmn              : Young generation size
 *   -XX:+UseG1GC      : Enable G1 collector
 *   -XX:+UseZGC       : Enable ZGC (Java 15+)
 *   -XX:+UseShenandoahGC : Enable Shenandoah
 *   -Xlog:gc*         : GC logging (Java 9+)
 */
public class GCTuningGuide {

    // --- G1GC Tuning (default since Java 9) ---
    // Good for heaps 4GB-16GB, balanced latency/throughput.
    // -XX:+UseG1GC -Xms8g -Xmx8g -Xmn2g
    // -XX:MaxGCPauseMillis=200
    // -XX:G1HeapRegionSize=16m
    // -XX:InitiatingHeapOccupancyPercent=45
    public static void g1GCTuningExample() {
        System.out.println("=== G1GC Tuning ===");
        System.out.println("Best for: Heaps 4-16GB, balanced latency/throughput");
        System.out.println("Key flags:");
        System.out.println("  -XX:+UseG1GC");
        System.out.println("  -Xms8g -Xmx8g        (fixed heap avoids resize pauses)");
        System.out.println("  -Xmn2g                (young gen ~25% of heap)");
        System.out.println("  -XX:MaxGCPauseMillis=200");
        System.out.println("  -XX:G1HeapRegionSize=16m");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45");
        System.out.println();
    }

    // --- ZGC Tuning (Java 15+, production ready) ---
    // Ultra-low latency (<1ms pauses), supports TB-scale heaps.
    // -XX:+UseZGC -Xms4g -Xmx16g
    // -XX:ConcGCThreads=4
    // -XX:+ZGenerational (Java 21+)
    public static void zgcTuningExample() {
        System.out.println("=== ZGC Tuning ===");
        System.out.println("Best for: Large heaps, ultra-low latency (<1ms pauses)");
        System.out.println("Key flags:");
        System.out.println("  -XX:+UseZGC");
        System.out.println("  -Xms4g -Xmx16g       (can handle heap resizing well)");
        System.out.println("  -XX:ConcGCThreads=4  (concurrent GC thread count)");
        System.out.println("  -XX:+ZGenerational    (Java 21+, generational mode)");
        System.out.println("  -XX:SoftMaxHeapSize=8g (hint for soft limit)");
        System.out.println();
    }

    // --- Shenandoah Tuning (Red Hat, OpenJDK) ---
    // Similar to ZGC, good pause-time predictability.
    // -XX:+UseShenandoahGC -Xms4g -Xmx16g
    // -XX:ShenandoahGCHeuristics=compact
    public static void shenandoahTuningExample() {
        System.out.println("=== Shenandoah Tuning ===");
        System.out.println("Best for: Predictable low pauses, OpenJDK users");
        System.out.println("Key flags:");
        System.out.println("  -XX:+UseShenandoahGC");
        System.out.println("  -Xms4g -Xmx16g");
        System.out.println("  -XX:ShenandoahGCHeuristics=compact");
        System.out.println("  -XX:ShenandoahMinFreeThreshold=10");
        System.out.println();
    }

    // --- GC Logging (Java 9+) ---
    // -Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=5,filesize=100m
    // -Xlog:gc+heap=debug:stdout
    public static void gcLoggingExample() {
        System.out.println("=== GC Logging Flags ===");
        System.out.println("Unified logging (Java 9+):");
        System.out.println("  -Xlog:gc*:file=gc.log:time,uptime,level,tags");
        System.out.println("  -Xlog:gc*=info:stdout                      (log to stdout)");
        System.out.println("  -Xlog:gc*=debug:file=gc-debug.log          (verbose)");
        System.out.println("  -Xlog:gc+heap=trace:file=gc-heap.log       (heap details)");
        System.out.println();
        System.out.println("Legacy (Java 8):");
        System.out.println("  -XX:+PrintGCDetails -XX:+PrintGCDateStamps");
        System.out.println("  -Xloggc:gc.log -XX:+UseGCLogFileRotation");
        System.out.println("  -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100m");
        System.out.println();
    }

    // --- Real-world Tuning Example ---
    // Scenario: Web service, 8GB heap, target p99 < 50ms
    public static void realWorldTuningScenario() {
        System.out.println("=== Real-World Scenario: Web Service ===");
        System.out.println("Requirements: 8GB heap, p99 latency < 50ms");
        System.out.println("Recommended: G1GC with tuned pause target");
        System.out.println();
        System.out.println("Production JVM flags:");
        System.out.println("  java -server");
        System.out.println("       -XX:+UseG1GC");
        System.out.println("       -Xms8g -Xmx8g");
        System.out.println("       -Xmn2g");
        System.out.println("       -XX:MaxGCPauseMillis=50");
        System.out.println("       -XX:G1HeapRegionSize=8m");
        System.out.println("       -XX:InitiatingHeapOccupancyPercent=40");
        System.out.println("       -XX:ParallelGCThreads=8");
        System.out.println("       -XX:ConcGCThreads=4");
        System.out.println("       -XX:G1ReservePercent=15");
        System.out.println("       -XX:+ParallelRefProcEnabled");
        System.out.println("       -Xlog:gc*:file=gc.log:time,uptime,level,tags");
        System.out.println("       -jar app.jar");
        System.out.println();
        System.out.println("Why these settings:");
        System.out.println("  - Fixed heap (-Xms == -Xmx) avoids resize overhead");
        System.out.println("  - Xmn=2g (25% of heap) gives enough young gen");
        System.out.println("  - IHOP=40 starts concurrent cycle earlier");
        System.out.println("  - ConcGCThreads=4 for background collection");
        System.out.println("  - G1ReservePercent=15 prevents to-space exhaustion");
        System.out.println();
    }

    // --- When to use which GC ---
    public static void gcSelectionGuide() {
        System.out.println("=== GC Selection Guide ===");
        System.out.println("┌─────────────────┬──────────────────┬─────────────────────────┐");
        System.out.println("│ GC Algorithm    │ Heap Size        │ Best For                │");
        System.out.println("├─────────────────┼──────────────────┼─────────────────────────┤");
        System.out.println("│ SerialGC        │ < 256MB          │ Single-threaded, small  │");
        System.out.println("│ ParallelGC      │ 256MB - 4GB      │ Throughput-critical     │");
        System.out.println("│ G1GC            │ 4GB - 32GB       │ Balanced (default)      │");
        System.out.println("│ ZGC             │ 4GB - 16TB       │ Ultra-low latency      │");
        System.out.println("│ Shenandoah      │ 4GB - 16TB       │ Low pause, OpenJDK     │");
        System.out.println("└─────────────────┴──────────────────┴─────────────────────────┘");
        System.out.println();

        System.out.println("Decision matrix:");
        System.out.println("  - Latency sensitive?     → ZGC or Shenandoah");
        System.out.println("  - Throughput critical?   → ParallelGC");
        System.out.println("  - General purpose?       → G1GC (default)");
        System.out.println("  - Containerized/micro?   → G1GC or ZGC");
        System.out.println("  - Legacy Java 8?         → G1GC or CMS (deprecated)");
    }

    public static void main(String[] args) {
        g1GCTuningExample();
        zgcTuningExample();
        shenandoahTuningExample();
        gcLoggingExample();
        realWorldTuningScenario();
        gcSelectionGuide();
    }
}