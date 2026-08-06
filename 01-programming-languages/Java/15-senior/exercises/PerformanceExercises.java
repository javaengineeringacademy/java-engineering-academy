package academy.javaengineering.exercises;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * Exercises: Performance Optimization (JMH concepts, Profiling)
 *
 * Complete the TODO sections below.
 */
public class PerformanceExercises {

    // TODO 1: Implement a simple benchmark runner
    // Measure execution time of a operation over multiple iterations
    public static class Benchmark {
        private final int warmupIterations;
        private final int measurementIterations;

        public Benchmark(int warmupIterations, int measurementIterations) {
            this.warmupIterations = warmupIterations;
            this.measurementIterations = measurementIterations;
        }

        public BenchmarkResult run(String name, Runnable operation) {
            // TODO: implement
            // 1. Run warmup iterations
            // 2. Run measurement iterations and record times
            // 3. Calculate average, min, max
            return new BenchmarkResult(name, 0, 0, 0, 0);
        }
    }

    public static class BenchmarkResult {
        private final String name;
        private final double avgNanos;
        private final long minNanos;
        private final long maxNanos;
        private final int iterations;

        public BenchmarkResult(String name, double avgNanos, long minNanos, long maxNanos, int iterations) {
            this.name = name;
            this.avgNanos = avgNanos;
            this.minNanos = minNanos;
            this.maxNanos = maxNanos;
            this.iterations = iterations;
        }

        public String getName() { return name; }
        public double getAvgNanos() { return avgNanos; }
        public double getAvgMicros() { return avgNanos / 1000.0; }
        public long getMinNanos() { return minNanos; }
        public long getMaxNanos() { return maxNanos; }
        public int getIterations() { return iterations; }

        @Override
        public String toString() {
            return String.format("%s: avg=%.2fμs, min=%dμs, max=%dμs (%d iters)",
                name, getAvgMicros(), minNanos / 1000, maxNanos / 1000, iterations);
        }
    }

    // TODO 2: Implement an object pool for performance
    public static class ObjectPool<T> {
        private final Queue<T> pool = new ConcurrentLinkedQueue<>();
        private final Supplier<T> factory;
        private final int maxSize;
        private final AtomicInteger activeCount = new AtomicInteger(0);

        public ObjectPool(Supplier<T> factory, int initialSize, int maxSize) {
            this.factory = factory;
            this.maxSize = maxSize;
            for (int i = 0; i < initialSize; i++) {
                pool.add(factory.get());
            }
        }

        public T borrow() {
            // TODO: implement - get from pool or create new
            return null;
        }

        public void release(T object) {
            // TODO: implement - return to pool if under max size
        }

        public int getActiveCount() {
            return activeCount.get();
        }

        public int getPoolSize() {
            return pool.size();
        }
    }

    // TODO 3: Implement a cache with LRU eviction using LinkedHashMap
    public static class LruCache<K, V> {
        private final int maxSize;
        private final LinkedHashMap<K, V> map;

        public LruCache(int maxSize) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<>(maxSize, 0.75f, true);
        }

        public V get(K key) {
            // TODO: implement
            return null;
        }

        public void put(K key, V value) {
            // TODO: implement with LRU eviction
        }

        public int size() {
            return map.size();
        }
    }

    // TODO 4: Implement a string concatenation benchmark
    // Compare String + vs StringBuilder performance
    public static class StringConcatBenchmark {
        public String concatWithString(int iterations) {
            // TODO: implement using String +
            return "";
        }

        public String concatWithStringBuilder(int iterations) {
            // TODO: implement using StringBuilder
            return "";
        }

        public String concatWithStringJoin(int iterations) {
            // TODO: implement using String.join
            return "";
        }
    }

    // TODO 5: Implement a memory-efficient data structure
    // Packed int array - store multiple small ints in a single long
    public static class PackedIntArray {
        private final long[] data;
        private final int bitsPerValue;
        private final long mask;

        public PackedIntArray(int size, int bitsPerValue) {
            this.bitsPerValue = bitsPerValue;
            this.mask = (1L << bitsPerValue) - 1;
            int longsPerValue = 64 / bitsPerValue;
            this.data = new long[(size + longsPerValue - 1) / longsPerValue];
        }

        public void set(int index, int value) {
            // TODO: implement - pack value into long array
        }

        public int get(int index) {
            // TODO: implement - unpack value from long array
            return 0;
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        PerformanceExercises exercises = new PerformanceExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== PerformanceExercises Tests ===\n");

        // Test 1 - Benchmark
        total++;
        Benchmark bench = new Benchmark(2, 5);
        BenchmarkResult result = bench.run("testOp", () -> {
            long sum = 0;
            for (int i = 0; i < 1000; i++) sum += i;
        });
        if (result.getIterations() == 5 && result.getAvgNanos() > 0) {
            System.out.println("Test 1 PASSED: Benchmark - " + result);
            passed++;
        } else {
            System.out.println("Test 1 FAILED: Benchmark - iters=" + result.getIterations());
        }

        // Test 2 - Object Pool
        total++;
        ObjectPool<StringBuilder> pool = new ObjectPool<>(StringBuilder::new, 2, 5);
        StringBuilder sb1 = pool.borrow();
        StringBuilder sb2 = pool.borrow();
        if (pool.getActiveCount() == 2 && pool.getPoolSize() == 0) {
            pool.release(sb1);
            pool.release(sb2);
            if (pool.getActiveCount() == 0 && pool.getPoolSize() == 2) {
                System.out.println("Test 2 PASSED: ObjectPool");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: ObjectPool release");
            }
        } else {
            System.out.println("Test 2 FAILED: ObjectPool borrow");
        }

        // Test 3 - LRU Cache
        total++;
        LruCache<String, Integer> cache = new LruCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.get("a");
        cache.put("d", 4);
        if (cache.get("b") == null && cache.get("a") != null && cache.get("d") != null) {
            System.out.println("Test 3 PASSED: LruCache eviction");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: LruCache eviction");
        }

        // Test 4 - String Concat
        total++;
        StringConcatBenchmark scb = new StringConcatBenchmark();
        String s1 = scb.concatWithStringBuilder(100);
        String s2 = scb.concatWithStringJoin(100);
        if (s1 != null && s1.length() > 0 && s2 != null && s2.length() > 0) {
            System.out.println("Test 4 PASSED: StringConcatBenchmark");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: StringConcatBenchmark");
        }

        // Test 5 - Packed Array
        total++;
        PackedIntArray packed = new PackedIntArray(100, 8);
        packed.set(0, 42);
        packed.set(50, 100);
        packed.set(99, 255);
        if (packed.get(0) == 42 && packed.get(50) == 100 && packed.get(99) == 255) {
            System.out.println("Test 5 PASSED: PackedIntArray");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: PackedIntArray - " + packed.get(0));
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
