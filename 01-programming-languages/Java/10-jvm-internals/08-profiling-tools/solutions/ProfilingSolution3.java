package academy.javaengineering.jvm.profiling;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Solution 3: Memory Profiling Exercise - Memory analysis and leak detection
 */
public class ProfilingSolution3 {

    private static final int PROFILING_DURATION_MS = 3000;
    private static final int MEASUREMENT_INTERVAL_MS = 200;

    private static final List<byte[]> memoryLeaker = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Memory Profiling Solution ===\n");

        memorySnapshot();
        System.out.println();

        trackMemoryOverTime();
        System.out.println();

        detectMemoryLeak();
        System.out.println();

        measureAllocationRate();
        System.out.println();

        findMemoryHeavyOperations();
    }

    static void memorySnapshot() {
        System.out.println("--- Memory Snapshot ---");
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();

        System.out.printf("Heap:     used=%dMB, committed=%dMB, max=%dMB, utilization=%.1f%%%n",
                heap.getUsed() / (1024 * 1024),
                heap.getCommitted() / (1024 * 1024),
                heap.getMax() / (1024 * 1024),
                (heap.getUsed() * 100.0) / heap.getMax());
        System.out.printf("Non-Heap: used=%dMB, committed=%dMB%n",
                nonHeap.getUsed() / (1024 * 1024),
                nonHeap.getCommitted() / (1024 * 1024));

        // Memory pool details
        System.out.println("\nMemory Pools:");
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            MemoryUsage usage = pool.getUsage();
            String poolType = pool.getType() == MemoryType.HEAP ? "HEAP" : "NON-HEAP";
            System.out.printf("  [%s] %-30s used=%dMB, max=%dMB%n",
                    poolType, pool.getName(),
                    usage.getUsed() / (1024 * 1024),
                    usage.getMax() / (1024 * 1024));
        }
    }

    static void trackMemoryOverTime() {
        System.out.println("--- Memory Over Time (during allocation workload) ---");
        System.out.printf("%-12s %-12s %-12s %-12s%n", "Time (ms)", "Used (MB)", "Delta (MB)", "Action");
        System.out.println("-".repeat(52));

        long startTime = System.currentTimeMillis();
        long previousUsed = getUsedHeapMB();
        List<List<byte[]>> holders = new ArrayList<>();

        // Phase 1: Allocate memory
        System.out.printf("%-12d %-12d %-12s %s%n",
                System.currentTimeMillis() - startTime, getUsedHeapMB(), 0, "START");
        for (int i = 0; i < 20; i++) {
            List<byte[]> batch = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                batch.add(new byte[1024 * 100]); // 100KB each
            }
            holders.add(batch);
            long currentUsed = getUsedHeapMB();
            long delta = currentUsed - previousUsed;
            System.out.printf("%-12d %-12d %-+12d %s%n",
                    System.currentTimeMillis() - startTime, currentUsed, delta, "ALLOCATE");
            previousUsed = currentUsed;
            try {
                Thread.sleep(MEASUREMENT_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Phase 2: Release memory
        System.out.println("\n  --- Releasing memory ---");
        holders.clear();
        forceGarbageCollection();
        long currentUsed = getUsedHeapMB();
        System.out.printf("%-12d %-12d %-+12d %s%n",
                System.currentTimeMillis() - startTime, currentUsed, currentUsed - previousUsed, "RELEASED");
        previousUsed = currentUsed;

        // Phase 3: More allocations after release
        System.out.println("\n  --- Re-allocating after release ---");
        for (int i = 0; i < 10; i++) {
            List<byte[]> batch = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                batch.add(new byte[1024 * 100]);
            }
            holders.add(batch);
            currentUsed = getUsedHeapMB();
            long delta = currentUsed - previousUsed;
            System.out.printf("%-12d %-12d %-+12d %s%n",
                    System.currentTimeMillis() - startTime, currentUsed, delta, "ALLOCATE");
            previousUsed = currentUsed;
        }

        holders.clear();
        System.out.println("\n  All batches released. Memory should be reclaimed after GC.");
    }

    static void detectMemoryLeak() {
        System.out.println("--- Memory Leak Detection ---");
        System.out.println("Simulating a memory leak (objects not released)...\n");

        long[] samples = new long[10];
        for (int i = 0; i < 10; i++) {
            // Leak: add objects that are never removed
            for (int j = 0; j < 100; j++) {
                memoryLeaker.add(new byte[1024]); // 1KB each
            }
            forceGarbageCollection();
            samples[i] = getUsedHeapMB();
            System.out.printf("  Iteration %2d: heap=%dMB (leaked objects: %d)%n",
                    i + 1, samples[i], memoryLeaker.size());
        }

        // Analyze trend
        long totalGrowth = samples[samples.length - 1] - samples[0];
        double avgGrowthPerIteration = (double) totalGrowth / samples.length;

        System.out.println();
        if (totalGrowth > 0) {
            System.out.printf("  LEAK DETECTED: Memory grew by %dMB over %d iterations%n",
                    totalGrowth, samples.length);
            System.out.printf("  Average growth: %.1f MB/iteration%n", avgGrowthPerIteration);
            System.out.println("  Root cause: memoryLeaker list grows without bound");
        } else {
            System.out.println("  No leak detected");
        }

        // Clean up
        memoryLeaker.clear();
        forceGarbageCollection();
    }

    static void measureAllocationRate() {
        System.out.println("--- Allocation Rate Measurement ---");

        String[] strategies = {"byte[] 1KB", "byte[] 10KB", "byte[] 100KB", "Integer boxing", "String concat"};
        for (String strategy : strategies) {
            forceGarbageCollection();
            long beforeMB = getUsedHeapMB();
            long startNanos = System.nanoTime();

            switch (strategy) {
                case "byte[] 1KB" -> {
                    for (int i = 0; i < 10_000; i++) new byte[1024];
                }
                case "byte[] 10KB" -> {
                    for (int i = 0; i < 10_000; i++) new byte[10240];
                }
                case "byte[] 100KB" -> {
                    for (int i = 0; i < 10_000; i++) new byte[102400];
                }
                case "Integer boxing" -> {
                    List<Integer> list = new ArrayList<>();
                    for (int i = 0; i < 10_000; i++) list.add(i);
                }
                case "String concat" -> {
                    String s = "";
                    for (int i = 0; i < 10_000; i++) s = s + "x";
                }
            }

            long elapsedNanos = System.nanoTime() - startNanos;
            long afterMB = getUsedHeapMB();
            long allocatedMB = afterMB - beforeMB;

            System.out.printf("  %-20s: %,d ms, ~%dMB allocated, rate=%d MB/sec%n",
                    strategy, elapsedNanos / 1_000_000,
                    Math.max(0, allocatedMB),
                    allocatedMB > 0 ? (allocatedMB * 1_000_000_000L / elapsedNanos) : 0);
        }
    }

    static void findMemoryHeavyOperations() {
        System.out.println("--- Memory-Heavy Operation Comparison ---\n");

        // ArrayList vs LinkedList memory per element
        forceGarbageCollection();
        long before = getUsedHeapMB();
        List<Integer> arrayList = IntStream.range(0, 100_000).boxed().collect(Collectors.toList());
        long after = getUsedHeapMB();
        System.out.printf("ArrayList (100K ints): ~%dMB%n", after - before);

        forceGarbageCollection();
        before = getUsedHeapMB();
        List<Integer> linkedList = IntStream.range(0, 100_000)
                .boxed().collect(Collectors.toCollection(LinkedList::new));
        after = getUsedHeapMB();
        System.out.printf("LinkedList (100K ints): ~%dMB%n", after - before);

        System.out.println("\nKey insights:");
        System.out.println("  - ArrayList: ~40 bytes per element (object header + pointer + Integer object ~16 bytes)");
        System.out.println("  - LinkedList: ~56 bytes per element (above + 2 node pointers ~24 bytes each)");
        System.out.println("  - For primitive-like data, prefer primitive streams or specialized libraries");
        System.out.println("  - Memory-heavy operations: boxing, string concatenation, deep object graphs");

        // Clean up
        arrayList = null;
        linkedList = null;
        forceGarbageCollection();
    }

    static long getUsedHeapMB() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / (1024 * 1024);
    }

    static void forceGarbageCollection() {
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SuppressWarnings("unused")
    static class LinkedList<T> {
        Node<T> head;
        int size = 0;

        static class Node<T> {
            T data;
            Node<T> next;
            Node<T> prev;
        }

        void add(T item) {
            Node<T> node = new Node<>();
            node.data = item;
            if (head != null) {
                Node<T> last = head;
                while (last.next != null) last = last.next;
                last.next = node;
                node.prev = last;
            } else {
                head = node;
            }
            size++;
        }
    }
}
