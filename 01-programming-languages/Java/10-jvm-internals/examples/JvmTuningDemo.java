package academy.javaengineering.jvm.examples;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Tuning Demo
 * Demonstrates heap sizing, GC selection, tuning flags,
 * performance metrics, and practical tuning strategies.
 */
public class JvmTuningDemo {

    /**
     * DEMO 1: Heap Sizing
     */
    public static void demonstrateHeapSizing() {
        System.out.println("=== Heap Sizing ===");
        System.out.println("Key flags:");
        System.out.println("  -Xms<size>      Initial heap size");
        System.out.println("  -Xmx<size>      Maximum heap size");
        System.out.println("  -Xmn<size>      Young generation size");
        System.out.println("  -XX:NewSize=<size>    Young gen initial");
        System.out.println("  -XX:MaxNewSize=<size> Young gen max");
        System.out.println();

        System.out.println("Sizing guidelines:");
        System.out.println("  - Set -Xms = -Xmx (avoid resize overhead)");
        System.out.println("  - Young gen: 1/3 to 1/4 of total heap");
        System.out.println("  - Old gen: 2/3 to 3/4 of total heap");
        System.out.println("  - Monitor: GC frequency vs pause time");
        System.out.println();

        System.out.println("Container-aware JVM (JDK 10+):");
        System.out.println("  -XX:+UseContainerSupport    (default, enabled)");
        System.out.println("  -XX:ActiveProcessorCount=N  Override CPU count");
        System.out.println("  -XX:MaxRAMPercentage=75.0   % of container memory");
        System.out.println("  -XX:InitialRAMPercentage=50.0");
        System.out.println();

        // Show current heap
        Runtime rt = Runtime.getRuntime();
        System.out.println("Current heap:");
        System.out.println("  Max:     " + (rt.maxMemory() / 1024 / 1024) + " MB");
        System.out.println("  Total:   " + (rt.totalMemory() / 1024 / 1024) + " MB");
        System.out.println("  Free:    " + (rt.freeMemory() / 1024 / 1024) + " MB");
        System.out.println("  Used:    " + ((rt.totalMemory() - rt.freeMemory()) / 1024 / 1024) + " MB");
    }

    /**
     * DEMO 2: GC Tuning Parameters
     */
    public static void demonstrateGcTuning() {
        System.out.println("\n=== GC Tuning Parameters ===");

        System.out.println("G1 GC tuning:");
        System.out.println("  -XX:MaxGCPauseMillis=200         Target pause time");
        System.out.println("  -XX:G1HeapRegionSize=N           Region size (1-32MB)");
        System.out.println("  -XX:G1NewSizePercent=5           Min young gen %");
        System.out.println("  -XX:G1MaxNewSizePercent=60       Max young gen %");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45  IHOP");
        System.out.println("  -XX:G1MixedGCCountTarget=8       Mixed GC target");
        System.out.println("  -XX:G1ReservePercent=10          To-space reserve");
        System.out.println();

        System.out.println("ZGC tuning:");
        System.out.println("  -XX:SoftMaxHeapSize=N            Soft max limit");
        System.out.println("  -XX:ZCollectionInterval=N        Proactive GC (sec)");
        System.out.println("  -XX:ZAllocationSpikeTolerance=N  Spike tolerance");
        System.out.println("  -XX:+ZGenerational               Gen ZGC (JDK 21+)");
        System.out.println();

        System.out.println("Parallel GC tuning:");
        System.out.println("  -XX:ParallelGCThreads=N          GC threads");
        System.out.println("  -XX:MaxGCPauseMillis=200         Target pause");
        System.out.println("  -XX:GCTimeRatio=99               GC time ratio");
        System.out.println("  -XX:AdaptiveSizeAdjustmentPolicy=3");
        System.out.println();

        System.out.println("Universal flags:");
        System.out.println("  -XX:ConcGCThreads=N              Concurrent GC threads");
        System.out.println("  -XX:ParallelGCThreads=N          Parallel GC threads");
        System.out.println("  -XX:MaxTenuringThreshold=N       Tenuring threshold");
        System.out.println("  -XX:PretenureSizeThreshold=N     Direct to old");
    }

    /**
     * DEMO 3: Performance Metrics to Monitor
     */
    public static void demonstrateMetrics() {
        System.out.println("\n=== Performance Metrics ===");

        ManagementFactory.getGarbageCollectorMXBeans();

        System.out.println("GC metrics:");
        System.out.println("  - GC pause time (avg, p95, p99)");
        System.out.println("  - GC throughput (% time not in GC)");
        System.out.println("  - GC frequency (count per minute)");
        System.out.println("  - Promotion rate (objects/sec to old gen)");
        System.out.println("  - Allocation rate (objects/sec)");
        System.out.println();

        System.out.println("Memory metrics:");
        System.out.println("  - Heap usage (used/max ratio)");
        System.out.println("  - Metaspace usage");
        System.out.println("  - Old gen occupancy trend");
        System.out.println("  - Survivor size trend");
        System.out.println();

        System.out.println("Application metrics:");
        System.out.println("  - Response time (p50, p95, p99)");
        System.out.println("  - Throughput (requests/sec)");
        System.out.println("  - Thread count and states");
        System.out.println("  - CPU usage");
        System.out.println();

        // Live metrics
        System.out.println("Current JVM metrics:");
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();

        System.out.println("  Heap used/max: " + heap.getUsed() / 1024 / 1024
                + "/" + heap.getMax() / 1024 / 1024 + " MB");
        System.out.println("  Non-heap used: " + nonHeap.getUsed() / 1024 / 1024 + " MB");

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcBeans) {
            System.out.println("  " + gc.getName() + ": " + gc.getCollectionCount()
                    + " collections, " + gc.getCollectionTime() + "ms total");
        }
    }

    /**
     * DEMO 4: Tuning Strategy
     */
    public static void demonstrateStrategy() {
        System.out.println("\n=== Tuning Strategy ===");
        System.out.println("1. Define goals:");
        System.out.println("   - Latency target (max pause time)");
        System.out.println("   - Throughput target (requests/sec)");
        System.out.println("   - Memory constraint");
        System.out.println();

        System.out.println("2. Baseline measurement:");
        System.out.println("   - Run application with default settings");
        System.out.println("   - Measure key metrics");
        System.out.println("   - Identify bottlenecks");
        System.out.println();

        System.out.println("3. Iterate:");
        System.out.println("   - Change ONE variable at a time");
        System.out.println("   - Measure impact");
        System.out.println("   - Keep if improvement, revert if not");
        System.out.println();

        System.out.println("4. Common bottlenecks:");
        System.out.println("   - Too much GC -> increase heap or change GC");
        System.out.println("   - Long pauses -> ZGC/Shenandoah");
        System.out.println("   - Low throughput -> Parallel GC");
        System.out.println("   - Memory leak -> heap dump analysis");
        System.out.println("   - High CPU -> thread profiling");
    }

    /**
     * DEMO 5: Container Tuning
     */
    public static void demonstrateContainerTuning() {
        System.out.println("\n=== Container Tuning ===");
        System.out.println("Docker/Kubernetes considerations:");
        System.out.println();
        System.out.println("1. Memory limits:");
        System.out.println("   -XX:+UseContainerSupport (default since JDK 10)");
        System.out.println("   -XX:MaxRAMPercentage=75.0");
        System.out.println("   Leave 25% for native memory, OS, buffers");
        System.out.println();
        System.out.println("2. CPU limits:");
        System.out.println("   -XX:ActiveProcessorCount=N (override detection)");
        System.out.println("   ParallelGCThreads = min(cpus/4, 8)");
        System.out.println("   ConcGCThreads = ParallelGCThreads/4");
        System.out.println();
        System.out.println("3. GC selection for containers:");
        System.out.println("   Small (<512MB): Serial or G1");
        System.out.println("   Medium (512MB-4GB): G1");
        System.out.println("   Large (>4GB): G1 or ZGC");
        System.out.println();
        System.out.println("4. Monitoring in containers:");
        System.out.println("   - Use JMX over Jolokia (HTTP)");
        System.out.println("   - Export metrics via Prometheus");
        System.out.println("   - Use Kubernetes pods metrics");
    }

    /**
     * DEMO 6: Common JVM Flags Reference
     */
    public static void demonstrateFlagsReference() {
        System.out.println("\n=== Common JVM Flags Reference ===");
        System.out.println("Memory:");
        System.out.println("  -Xms512m -Xmx2g -Xmn512m");
        System.out.println("  -XX:MaxMetaspaceSize=256m");
        System.out.println("  -XX:MaxDirectMemorySize=512m");
        System.out.println();
        System.out.println("GC:");
        System.out.println("  -XX:+UseG1GC -XX:MaxGCPauseMillis=200");
        System.out.println("  -XX:+UseZGC -XX:+ZGenerational");
        System.out.println("  -Xlog:gc*");
        System.out.println();
        System.out.println("Performance:");
        System.out.println("  -XX:+AggressiveOpts");
        System.out.println("  -XX:+UseCompressedOops");
        System.out.println("  -XX:+UseFastAccessorMethods");
        System.out.println();
        System.out.println("Diagnostics:");
        System.out.println("  -XX:+HeapDumpOnOutOfMemoryError");
        System.out.println("  -XX:HeapDumpPath=/tmp/heap.hprof");
        System.out.println("  -XX:+PrintCompilation");
        System.out.println("  -XX:+PrintGCDetails");
    }

    /**
     * DEMO 7: Live Tuning Demo
     */
    public static void runLiveDemo() {
        System.out.println("\n=== Live Tuning Demo ===");

        // Allocate objects to create GC pressure
        System.out.println("Creating memory pressure...");
        List<byte[]> objects = new ArrayList<>();

        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            objects.add(new byte[1024]); // 1KB each
            if (i % 100 == 0) {
                Runtime rt = Runtime.getRuntime();
                long used = (rt.totalMemory() - rt.freeMemory()) / 1024;
                System.out.println("  [" + i + "] Used: " + used + " KB");
            }
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("Allocation time: " + (elapsed / 1000) + " μs");

        // Clean up
        objects.clear();
        System.gc();
        System.out.println("GC triggered, memory released");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      JVM TUNING DEMO                ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateHeapSizing();
        demonstrateGcTuning();
        demonstrateMetrics();
        demonstrateStrategy();
        demonstrateContainerTuning();
        demonstrateFlagsReference();
        runLiveDemo();

        System.out.println("\n=== Tuning Checklist ===");
        System.out.println("□ Set -Xms = -Xmx");
        System.out.println("□ Choose appropriate GC");
        System.out.println("□ Set MaxGCPauseMillis for latency");
        System.out.println("□ Enable GC logging");
        System.out.println("□ Monitor heap and GC metrics");
        System.out.println("□ Profile before tuning");
        System.out.println("□ Change one variable at a time");
        System.out.println("□ Measure impact of each change");
    }
}
