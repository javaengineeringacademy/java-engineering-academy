package academy.javaengineering.jvm.solutions;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Tuning Solutions - Complete implementations
 */
public class JvmTuningSolutions {

    /**
     * Exercise 1 Solution: Measure and compare heap sizes
     */
    public static void measureHeapSize() {
        System.out.println("=== Heap Size Measurement ===\n");

        Runtime rt = Runtime.getRuntime();
        long maxHeap = rt.maxMemory();
        long targetUsage = maxHeap / 2;

        System.out.println("Current Heap Configuration:");
        System.out.println("  Max:     " + (maxHeap / 1024 / 1024) + " MB (-Xmx)");
        System.out.println("  Initial: " + (rt.totalMemory() / 1024 / 1024) + " MB (-Xms)");
        System.out.println("  Target 50%: " + (targetUsage / 1024 / 1024) + " MB");

        // Allocate until 50% full
        List<byte[]> objects = new ArrayList<>();
        long allocated = 0;
        int gcCount = 0;

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        long baselineGc = 0;
        for (GarbageCollectorMXBean gc : gcBeans) {
            baselineGc += gc.getCollectionCount();
        }

        long startTime = System.nanoTime();
        while (allocated < targetUsage) {
            byte[] chunk = new byte[1024 * 1024]; // 1MB
            objects.add(chunk);
            allocated += chunk.length;

            // Check for GC
            long currentGc = 0;
            for (GarbageCollectorMXBean gc : gcBeans) {
                currentGc += gc.getCollectionCount();
            }
            if (currentGc > baselineGc) {
                gcCount++;
                baselineGc = currentGc;
            }
        }
        long elapsed = System.nanoTime() - startTime;

        System.out.println("\nResults:");
        System.out.println("  Allocated: " + (allocated / 1024 / 1024) + " MB");
        System.out.println("  Time: " + (elapsed / 1000000) + "ms");
        System.out.println("  GC events: " + gcCount);

        // Recommendation
        System.out.println("\nRecommendation:");
        System.out.println("  -Xms" + (maxHeap / 1024 / 1024) + "m");
        System.out.println("  -Xmx" + (maxHeap / 1024 / 1024) + "m");
        System.out.println("  (Set -Xms = -Xmx to avoid resize overhead)");

        objects.clear();
    }

    /**
     * Exercise 2 Solution: Compare GC algorithms
     */
    public static void compareGcAlgorithms() {
        System.out.println("=== GC Algorithm Comparison ===\n");

        RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = rtBean.getInputArguments();
        System.out.println("JVM Arguments: " + jvmArgs);

        // Generate workload
        List<byte[]> objects = new ArrayList<>();
        long startTime = System.nanoTime();
        long gcTimeBefore = getGcTime();

        for (int i = 0; i < 200; i++) {
            objects.add(new byte[1024 * 100]); // 100KB
            if (i % 20 == 0) {
                objects.subList(0, 10).clear();
                System.gc();
            }
        }

        long elapsed = System.nanoTime() - startTime;
        long gcTimeAfter = getGcTime();
        long gcTime = gcTimeAfter - gcTimeBefore;

        // GC statistics
        System.out.println("\nGC Statistics:");
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.printf("  %s: %d collections, %dms total%n",
                    gc.getName(), gc.getCollectionCount(), gc.getCollectionTime());
        }

        System.out.println("\nPerformance:");
        System.out.println("  Execution time: " + (elapsed / 1000000) + "ms");
        System.out.println("  GC time: " + gcTime + "ms");
        System.out.println("  GC overhead: " + (gcTime * 100.0 / (elapsed / 1000000)) + "%");

        System.out.println("\nTo compare different GC algorithms, run separately:");
        System.out.println("  java -XX:+UseSerialGC -Xms256m -Xmx256m JvmTuningSolutions");
        System.out.println("  java -XX:+UseParallelGC -Xms256m -Xmx256m JvmTuningSolutions");
        System.out.println("  java -XX:+UseG1GC -Xms256m -Xmx256m JvmTuningSolutions");
        System.out.println("  java -XX:+UseZGC -Xms256m -Xmx256m JvmTuningSolutions");
    }

    private static long getGcTime() {
        long total = 0;
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            total += gc.getCollectionTime();
        }
        return total;
    }

    /**
     * Exercise 3 Solution: Tune G1 GC parameters
     */
    public static void tuneG1Parameters() {
        System.out.println("=== G1 GC Tuning ===\n");

        // Show current G1 configuration
        RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("Current G1 configuration:");
        for (String arg : rtBean.getInputArguments()) {
            if (arg.contains("G1") || arg.contains("g1")) {
                System.out.println("  " + arg);
            }
        }

        // Mixed workload
        System.out.println("\nMixed workload (short-lived + long-lived):");
        List<Object> longLived = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            longLived.add(new byte[1024 * 10]); // 10KB, kept alive
        }

        List<byte[]> shortLived = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            shortLived.add(new byte[1024]); // 1KB, immediately discarded
            if (i % 50 == 0) {
                shortLived.clear();
            }
        }

        System.out.println("\nG1 Tuning Parameters:");
        System.out.println("  -XX:MaxGCPauseMillis=200   (default)");
        System.out.println("  -XX:G1HeapRegionSize=16m   (for large heaps)");
        System.out.println("  -XX:G1NewSizePercent=5     (min young gen)");
        System.out.println("  -XX:G1MaxNewSizePercent=60 (max young gen)");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45");
    }

    /**
     * Exercise 4 Solution: Container-aware tuning
     */
    public static void containerAwareTuning() {
        System.out.println("=== Container-Aware Tuning ===\n");

        Runtime rt = Runtime.getRuntime();
        int cpus = rt.availableProcessors();
        long maxMemory = rt.maxMemory();

        System.out.println("Detected Configuration:");
        System.out.println("  CPUs: " + cpus);
        System.out.println("  Max Memory: " + (maxMemory / 1024 / 1024) + " MB");

        // Check if running in container
        boolean inContainer = System.getenv("DOCKER_CONTAINER") != null ||
                new File("/proc/1/cgroup").exists();
        System.out.println("  Container detected: " + inContainer);

        // Calculate optimal settings
        int parallelThreads = Math.max(Math.min(cpus / 2, 8), 2);
        int concThreads = Math.max(parallelThreads / 4, 1);
        long heapSize = (long) (maxMemory * 0.75);

        System.out.println("\nRecommended Settings:");
        System.out.println("  -XX:+UseContainerSupport");
        System.out.println("  -XX:MaxRAMPercentage=75.0");
        System.out.println("  -XX:ActiveProcessorCount=" + cpus);
        System.out.println("  -XX:ParallelGCThreads=" + parallelThreads);
        System.out.println("  -XX:ConcGCThreads=" + concThreads);
        System.out.println("  -Xms" + (heapSize / 1024 / 1024) + "m");
        System.out.println("  -Xmx" + (heapSize / 1024 / 1024) + "m");

        System.out.println("\nContainer Memory Budget:");
        System.out.println("  JVM Heap: " + (heapSize / 1024 / 1024) + " MB (75%)");
        System.out.println("  Native/OS: " + ((maxMemory - heapSize) / 1024 / 1024) + " MB (25%)");
        System.out.println("  Thread stacks: ~" + (parallelThreads * 1) + " MB");
        System.out.println("  Metaspace: ~256 MB");
    }

    /**
     * Exercise 5 Solution: Tuning benchmark
     */
    public static void tuningBenchmark() {
        System.out.println("=== Tuning Benchmark ===\n");

        RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("JVM: " + System.getProperty("java.vm.name") + " " +
                System.getProperty("java.version"));

        // Warmup
        System.out.println("Warming up...");
        for (int i = 0; i < 1000; i++) {
            cpuBoundWorkload();
        }

        // CPU-bound benchmark
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            cpuBoundWorkload();
        }
        long cpuTime = System.nanoTime() - start;

        // Memory-bound benchmark
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            memoryBoundWorkload();
        }
        long memTime = System.nanoTime() - start;

        // Print results
        System.out.println("\nBenchmark Results:");
        System.out.println("  CPU workload:   " + (cpuTime / 1000000) + "ms");
        System.out.println("  Memory workload: " + (memTime / 1000000) + "ms");

        // GC stats
        System.out.println("\nGC Statistics:");
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.printf("  %s: %d collections, %dms total%n",
                    gc.getName(), gc.getCollectionCount(), gc.getCollectionTime());
        }

        // Memory stats
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();
        System.out.println("\nFinal Heap Usage:");
        System.out.printf("  Used/Max: %dMB / %dMB (%.1f%%)%n",
                heap.getUsed() / 1024 / 1024,
                heap.getMax() / 1024 / 1024,
                heap.getUsed() * 100.0 / heap.getMax());
    }

    private static double cpuBoundWorkload() {
        double result = 0;
        for (int i = 0; i < 1000; i++) {
            result += Math.sin(i) * Math.cos(i);
        }
        return result;
    }

    private static void memoryBoundWorkload() {
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            objects.add(new byte[1024]); // 1KB each
        }
        objects.clear();
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Tuning Solutions ===\n");

        // Exercise 1
        System.out.println("Exercise 1: Heap Size Measurement");
        measureHeapSize();

        // Exercise 2
        System.out.println("\n---");
        System.out.println("Exercise 2: GC Algorithm Comparison");
        compareGcAlgorithms();

        // Exercise 3
        System.out.println("\n---");
        System.out.println("Exercise 3: G1 GC Tuning");
        tuneG1Parameters();

        // Exercise 4
        System.out.println("\n---");
        System.out.println("Exercise 4: Container-Aware Tuning");
        containerAwareTuning();

        // Exercise 5
        System.out.println("\n---");
        System.out.println("Exercise 5: Tuning Benchmark");
        tuningBenchmark();
    }
}
