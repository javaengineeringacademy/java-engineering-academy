package academy.javaengineering.senior.examples;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

public class PerformanceEngineeringDemo {

    // JMH-style manual benchmarking
    static long benchmarkStringConcat(int iterations) {
        long start = System.nanoTime();
        String result = "";
        for (int i = 0; i < iterations; i++) {
            result += "item" + i;
        }
        return System.nanoTime() - start;
    }

    static long benchmarkStringBuilder(int iterations) {
        long start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("item").append(i);
        }
        String result = sb.toString();
        return System.nanoTime() - start;
    }

    // Memory allocation tracking
    static long measureAllocation(Runnable task) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long before = runtime.totalMemory() - runtime.freeMemory();
        task.run();
        runtime.gc();
        long after = runtime.totalMemory() - runtime.freeMemory();
        return after - before;
    }

    // Cache performance comparison
    static Map<String, String> simpleCache = new HashMap<>();
    static Map<String, String> concurrentCache = new ConcurrentHashMap<>();

    static String expensiveComputation(String key) {
        try {
            Thread.sleep(1); // Simulate I/O
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "value-for-" + key;
    }

    // Object pooling pattern
    static class ObjectPool<T> {
        private final Queue<T> pool = new ConcurrentLinkedQueue<>();
        private final java.util.function.Supplier<T> factory;

        ObjectPool(java.util.function.Supplier<T> factory) {
            this.factory = factory;
        }

        T borrow() {
            T obj = pool.poll();
            return obj != null ? obj : factory.get();
        }

        void release(T obj) {
            pool.offer(obj);
        }

        int size() {
            return pool.size();
        }
    }

    // Boxing/unboxing overhead demo
    static long sumWithAutoboxing(int count) {
        Long sum = 0L;
        for (int i = 0; i < count; i++) {
            sum += i; // implicit unboxing and boxing
        }
        return sum;
    }

    static long sumWithPrimitives(int count) {
        long sum = 0L;
        for (int i = 0; i < count; i++) {
            sum += i; // primitive arithmetic
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Performance Engineering Demo ===\n");

        // 1. String concatenation benchmark
        System.out.println("--- String Concatenation Benchmark ---");
        int iterations = 100_000;

        long stringTime = benchmarkStringConcat(iterations);
        long builderTime = benchmarkStringBuilder(iterations);

        System.out.printf("  String concatenation: %,d ns%n", stringTime);
        System.out.printf("  StringBuilder:        %,d ns%n", builderTime);
        System.out.printf("  Speedup:              %.1fx%n%n", (double) stringTime / builderTime);

        // 2. Memory allocation
        System.out.println("--- Memory Allocation Tracking ---");
        long alloc1 = measureAllocation(() -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 10_000; i++) {
                list.add("item-" + i);
            }
        });

        long alloc2 = measureAllocation(() -> {
            List<String> list = new ArrayList<>(10_000); // pre-sized
            for (int i = 0; i < 10_000; i++) {
                list.add("item-" + i);
            }
        });

        System.out.printf("  Without pre-sizing: %,d bytes%n", alloc1);
        System.out.printf("  With pre-sizing:    %,d bytes%n", alloc2);
        System.out.printf("  Savings:            %d bytes%n%n", alloc1 - alloc2);

        // 3. Boxing overhead
        System.out.println("--- Boxing/Unboxing Overhead ---");
        int boxCount = 10_000_000;

        long boxTime = System.nanoTime();
        sumWithAutoboxing(boxCount);
        long boxed = System.nanoTime() - boxTime;

        long primTime = System.nanoTime();
        sumWithPrimitives(boxCount);
        long primed = System.nanoTime() - primTime;

        System.out.printf("  With autoboxing:  %,d ns%n", boxed);
        System.out.printf("  With primitives:  %,d ns%n", primed);
        System.out.printf("  Overhead:         %.1fx%n%n", (double) boxed / primed);

        // 4. ConcurrentHashMap vs HashMap contention
        System.out.println("--- Concurrent Access Performance ---");
        int threadCount = 4;
        int opsPerThread = 100_000;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // Benchmark ConcurrentHashMap
        long concStart = System.nanoTime();
        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            final int id = t;
            futures.add(executor.submit(() -> {
                for (int i = 0; i < opsPerThread; i++) {
                    concurrentCache.put("key-" + id + "-" + i, "value-" + i);
                }
            }));
        }
        for (Future<?> f : futures) f.get();
        long concTime = System.nanoTime() - concStart;

        System.out.printf("  ConcurrentHashMap: %,d ns for %d ops%n", concTime, threadCount * opsPerThread);
        System.out.printf("  Throughput:         %,d ops/sec%n",
            (long)((double)(threadCount * opsPerThread) / concTime * 1_000_000_000L));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // 5. Object pooling
        System.out.println("\n--- Object Pooling ---");
        ObjectPool<StringBuilder> pool = new ObjectPool<>(() -> new StringBuilder(256));

        long poolStart = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            StringBuilder sb = pool.borrow();
            sb.setLength(0);
            sb.append("request-").append(i);
            String result = sb.toString();
            pool.release(sb);
        }
        long poolTime = System.nanoTime() - poolStart;

        long allocStart = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            StringBuilder sb = new StringBuilder(256);
            sb.append("request-").append(i);
            String result = sb.toString();
        }
        long allocTime = System.nanoTime() - allocStart;

        System.out.printf("  With pooling:    %,d ns%n", poolTime);
        System.out.printf("  Without pooling: %,d ns%n", allocTime);
        System.out.printf("  Pool size:       %d objects%n", pool.size());

        // 6. JVM memory and GC info
        System.out.println("\n--- JVM Memory Snapshot ---");
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();

        System.out.printf("  Heap used:      %,d MB / %,d MB%n",
            heap.getUsed() / (1024 * 1024),
            heap.getMax() / (1024 * 1024));
        System.out.printf("  Non-heap used:  %,d MB%n", nonHeap.getUsed() / (1024 * 1024));
        System.out.printf("  Available CPUs: %d%n", runtime.availableProcessors());

        // GC info
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcBeans) {
            System.out.printf("  GC [%s]: %,d collections, %,d ms total%n",
                gc.getName(), gc.getCollectionCount(), gc.getCollectionTime());
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
