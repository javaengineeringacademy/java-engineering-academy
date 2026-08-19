package academy.javaengineering.jvm.practices;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Tuning Exercises
 * Complete each exercise by implementing the required method.
 * Focus on heap sizing, GC selection, and performance tuning.
 */
public class JvmTuningExercises {

    /**
     * Exercise 1: Measure and compare heap sizes
     * Write code that:
     * 1. Allocates objects until heap is 50% full
     * 2. Measures allocation time and GC frequency
     * 3. Prints recommendations for -Xms and -Xmx
     *
     * Use Runtime.getRuntime().maxMemory() to get max heap
     */
    public static void measureHeapSize() {
        // TODO: Implement heap size measurement
        Runtime rt = Runtime.getRuntime();
        long maxHeap = rt.maxMemory();
        long targetUsage = maxHeap / 2;

        System.out.println("Max heap: " + (maxHeap / 1024 / 1024) + " MB");
        System.out.println("Target 50% usage: " + (targetUsage / 1024 / 1024) + " MB");

        // Allocate until 50% full
        List<byte[]> objects = new ArrayList<>();
        long allocated = 0;

        long startTime = System.nanoTime();
        while (allocated < targetUsage) {
            byte[] chunk = new byte[1024 * 1024]; // 1MB
            objects.add(chunk);
            allocated += chunk.length;
        }
        long elapsed = System.nanoTime() - startTime;

        System.out.println("Allocated " + (allocated / 1024 / 1024) + " MB in " + (elapsed / 1000000) + "ms");
        System.out.println("Recommendation: Set -Xms = -Xmx = " + (maxHeap / 1024 / 1024) + "m");

        objects.clear();
    }

    /**
     * Exercise 2: Compare GC algorithms
     * Write code that:
     * 1. Runs workload with different GC algorithms
     * 2. Measures pause times and throughput
     * 3. Prints comparison
     *
     * Run with different flags:
     * -XX:+UseSerialGC
     * -XX:+UseParallelGC
     * -XX:+UseG1GC
     * -XX:+UseZGC
     */
    public static void compareGcAlgorithms() {
        // TODO: Implement GC comparison
        // This method should be run multiple times with different GC flags

        System.out.println("Run this program with different GC flags and compare results:");
        System.out.println("  java -XX:+UseSerialGC JvmTuningExercises");
        System.out.println("  java -XX:+UseParallelGC JvmTuningExercises");
        System.out.println("  java -XX:+UseG1GC JvmTuningExercises");
        System.out.println("  java -XX:+UseZGC JvmTuningExercises");

        // Generate GC activity
        List<byte[]> objects = new ArrayList<>();
        long startTime = System.nanoTime();
        long gcTimeBefore = getGcTime();

        for (int i = 0; i < 100; i++) {
            objects.add(new byte[1024 * 100]); // 100KB
            if (i % 10 == 0) {
                objects.subList(0, 5).clear();
            }
        }

        long elapsed = System.nanoTime() - startTime;
        long gcTimeAfter = getGcTime();

        System.out.println("\nResults:");
        System.out.println("  Execution time: " + (elapsed / 1000000) + "ms");
        System.out.println("  GC time: " + (gcTimeAfter - gcTimeBefore) + "ms");
        System.out.println("  GC overhead: " + ((gcTimeAfter - gcTimeBefore) * 100.0 / (elapsed / 1000000)) + "%");
    }

    private static long getGcTime() {
        long totalGcTime = 0;
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            totalGcTime += gc.getCollectionTime();
        }
        return totalGcTime;
    }

    /**
     * Exercise 3: Tune G1 GC parameters
     * Write code that:
     * 1. Creates workload with specific characteristics
     * 2. Shows effect of different G1 parameters
     * 3. Prints tuning recommendations
     *
     * Parameters to test:
     * -XX:MaxGCPauseMillis=50
     * -XX:MaxGCPauseMillis=200
     * -XX:G1HeapRegionSize=4m
     * -XX:G1HeapRegionSize=16m
     */
    public static void tuneG1Parameters() {
        // TODO: Implement G1 tuning exercise
        System.out.println("Run with different G1 parameters:");
        System.out.println("  -XX:+UseG1GC -XX:MaxGCPauseMillis=50");
        System.out.println("  -XX:+UseG1GC -XX:MaxGCPauseMillis=200");
        System.out.println("  -XX:+UseG1GC -XX:G1HeapRegionSize=4m");
        System.out.println("  -XX:+UseG1GC -XX:G1HeapRegionSize=16m");

        // Mixed workload: short-lived + long-lived objects
        List<Object> longLived = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            longLived.add(new byte[1024 * 10]); // 10KB, kept in memory
        }

        List<byte[]> shortLived = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            shortLived.add(new byte[1024]); // 1KB, immediately discarded
            if (i % 100 == 0) {
                shortLived.clear();
                System.out.println("Cycle " + i + " completed");
            }
        }
    }

    /**
     * Exercise 4: Container-aware JVM tuning
     * Write code that:
     * 1. Detects container memory/CPU limits
     * 2. Calculates optimal JVM settings
     * 3. Prints recommended flags
     *
     * Consider:
     * - Container memory limits
     * - CPU quotas
     * - JVM overhead
     */
    public static void containerAwareTuning() {
        // TODO: Implement container-aware tuning
        Runtime rt = Runtime.getRuntime();
        int cpus = rt.availableProcessors();
        long maxMemory = rt.maxMemory();

        System.out.println("Container Configuration:");
        System.out.println("  CPUs: " + cpus);
        System.out.println("  Max Memory: " + (maxMemory / 1024 / 1024) + " MB");

        // Calculate recommended settings
        int parallelThreads = Math.min(cpus / 2, 8);
        int concThreads = Math.max(parallelThreads / 4, 1);
        long recommendedHeap = (long) (maxMemory * 0.75);

        System.out.println("\nRecommended JVM Flags:");
        System.out.println("  -XX:+UseContainerSupport");
        System.out.println("  -XX:MaxRAMPercentage=75.0");
        System.out.println("  -XX:ActiveProcessorCount=" + cpus);
        System.out.println("  -XX:ParallelGCThreads=" + parallelThreads);
        System.out.println("  -XX:ConcGCThreads=" + concThreads);
        System.out.println("  -Xms" + (recommendedHeap / 1024 / 1024) + "m");
        System.out.println("  -Xmx" + (recommendedHeap / 1024 / 1024) + "m");
    }

    /**
     * Exercise 5: Create a tuning benchmark
     * Write code that:
     * 1. Defines a workload (CPU-bound, memory-bound, I/O-bound)
     * 2. Measures key metrics (throughput, latency, memory)
     * 3. Prints structured results
     *
     * Metrics to collect:
     * - Throughput (ops/sec)
     * - Average latency (ms)
     * - 95th percentile latency
     * - GC pause time
     * - Memory usage
     */
    public static void tuningBenchmark() {
        // TODO: Implement tuning benchmark
        System.out.println("Running tuning benchmark...");

        // CPU-bound workload
        long start = System.nanoTime();
        double result = 0;
        for (int i = 0; i < 1000000; i++) {
            result += Math.sin(i) * Math.cos(i);
        }
        long cpuTime = System.nanoTime() - start;

        // Memory-bound workload
        start = System.nanoTime();
        List<byte[]> objects = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            objects.add(new byte[1024]);
        }
        objects.clear();
        long memoryTime = System.nanoTime() - start;

        // Print results
        System.out.println("\nBenchmark Results:");
        System.out.println("  CPU workload: " + (cpuTime / 1000000) + "ms");
        System.out.println("  Memory workload: " + (memoryTime / 1000000) + "ms");
        System.out.println("  Result: " + result);

        // GC stats
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.println("  " + gc.getName() + ": " + gc.getCollectionCount() + " collections");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Tuning Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Heap Size Measurement");
        measureHeapSize();

        // Test Exercise 2
        System.out.println("\nExercise 2: GC Algorithm Comparison");
        compareGcAlgorithms();

        // Test Exercise 3
        System.out.println("\nExercise 3: G1 GC Tuning");
        tuneG1Parameters();

        // Test Exercise 4
        System.out.println("\nExercise 4: Container-Aware Tuning");
        containerAwareTuning();

        // Test Exercise 5
        System.out.println("\nExercise 5: Tuning Benchmark");
        tuningBenchmark();
    }
}
