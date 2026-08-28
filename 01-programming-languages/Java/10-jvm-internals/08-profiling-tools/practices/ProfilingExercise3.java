package academy.javaengineering.jvm.profiling;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3: Memory Profiling Exercise
 *
 * Task: Build a memory profiler that tracks object allocations,
 * detects memory patterns, and identifies potential leaks.
 * Since we can't use a real profiler, use MXBeans and manual
 * memory measurement techniques.
 *
 * Requirements:
 * 1. Track heap memory usage over time
 * 2. Force GC and measure reclaimed memory
 * 3. Detect growing memory patterns (potential leaks)
 * 4. Measure allocation rates
 * 5. Identify memory-heavy operations
 * 6. Calculate object retention rates
 *
 * Run this class and observe the output.
 */
public class ProfilingExercise3 {

    private static final int PROFILING_DURATION_MS = 5000;
    private static final int MEASUREMENT_INTERVAL_MS = 100;

    public static void main(String[] args) {
        System.out.println("=== Memory Profiling Exercise ===\n");

        // TODO: Implement memorySnapshot - capture current heap usage
        // TODO: Implement trackMemoryOverTime - monitor memory during workload
        // TODO: Implement detectMemoryLeak - identify growing memory patterns
        // TODO: Implement measureAllocationRate - calculate bytes/sec allocation
        // TODO: Implement findMemoryHeavyOperations - identify expensive allocations

        System.out.println("Implement the profiling methods below.");
        System.out.println("Use ManagementFactory to get memory MXBeans.");
    }

    /**
     * TODO: Take a memory snapshot and return current heap usage info.
     * Return: used, committed, max heap in MB
     */
    static void memorySnapshot() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();

        System.out.printf("Heap: used=%dMB, committed=%dMB, max=%dMB, utilization=%.1f%%%n",
                heap.getUsed() / (1024 * 1024),
                heap.getCommitted() / (1024 * 1024),
                heap.getMax() / (1024 * 1024),
                (heap.getUsed() * 100.0) / heap.getMax());
    }

    /**
     * TODO: Monitor memory usage while running a workload.
     * Steps:
     * 1. Record initial memory
     * 2. Run workload (allocate objects)
     * 3. Sample memory at regular intervals
     * 4. Print memory usage timeline
     * 5. Force GC and show final memory
     */
    static void trackMemoryOverTime() {
        System.out.println("Memory over time (during allocation workload):");
        // TODO: Implement memory tracking with periodic sampling
        System.out.println("  TODO: Implement trackMemoryOverTime");
    }

    /**
     * TODO: Detect potential memory leaks by running repeated operations
     * and checking if memory grows consistently after GC.
     *
     * A memory leak is indicated when:
     * 1. Memory increases after each iteration
     * 2. GC does not reclaim the memory
     * 3. The growth is consistent (not random fluctuation)
     */
    static void detectMemoryLeak() {
        System.out.println("Memory leak detection:");
        // TODO: Implement leak detection
        System.out.println("  TODO: Implement detectMemoryLeak");
    }

    /**
     * TODO: Measure allocation rate in bytes per second.
     * Steps:
     * 1. Record memory before
     * 2. Allocate known amount of data
     * 3. Record time taken
     * 4. Calculate bytes/sec
     */
    static void measureAllocationRate() {
        System.out.println("Allocation rate measurement:");
        // TODO: Implement allocation rate measurement
        System.out.println("  TODO: Implement measureAllocationRate");
    }

    /**
     * TODO: Compare different allocation strategies and measure their
     * memory overhead.
     *
     * Compare:
     * - ArrayList vs LinkedList (memory per element)
     * - HashMap vs TreeMap (memory per entry)
     * - String concatenation vs StringBuilder
     * - boxed vs primitive collections
     */
    static void findMemoryHeavyOperations() {
        System.out.println("Memory-heavy operation comparison:");
        // TODO: Implement memory comparison
        System.out.println("  TODO: Implement findMemoryHeavyOperations");
    }

    static long getUsedHeapMB() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / (1024 * 1024);
    }

    static void forceGarbageCollection() {
        System.gc();
        try {
            Thread.sleep(100); // Allow GC to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
