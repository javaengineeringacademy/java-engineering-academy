package academy.javaengineering.senior.solutions;

import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class PerformanceSolutions {

    // Exercise 1: Micro-Benchmark Framework
    record BenchmarkResult(long avgNs, long medianNs, long p95Ns, long p99Ns, long minNs, long maxNs) {}

    static BenchmarkResult benchmark(Runnable task, int warmup, int measurement) {
        // Warmup
        for (int i = 0; i < warmup; i++) {
            task.run();
        }

        // Measurement
        long[] latencies = new long[measurement];
        for (int i = 0; i < measurement; i++) {
            long start = System.nanoTime();
            task.run();
            latencies[i] = System.nanoTime() - start;
        }

        Arrays.sort(latencies);
        long sum = 0;
        for (long l : latencies) sum += l;

        return new BenchmarkResult(
            sum / measurement,
            latencies[measurement / 2],
            latencies[(int)(measurement * 0.95)],
            latencies[(int)(measurement * 0.99)],
            latencies[0],
            latencies[measurement - 1]
        );
    }

    // Exercise 2: Memory Leak Detector
    static class LeakDetector {
        private final List<Long> allocationRates = new ArrayList<>();

        double detectLeak(Object suspect) {
            WeakReference<Object> ref = new WeakReference<>(suspect);
            suspect = null;

            for (int i = 0; i < 5; i++) {
                System.gc();
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            allocationRates.add(before);

            return ref.get() != null ? 0.8 : 0.1;
        }

        double allocationRate() {
            if (allocationRates.size() < 2) return 0.0;
            long first = allocationRates.get(0);
            long last = allocationRates.get(allocationRates.size() - 1);
            return (double)(last - first) / allocationRates.size();
        }
    }

    // Exercise 3: Cache Performance Analyzer
    record CacheMetrics(long hits, long misses, long evictions, double hitRate) {}

    static class AnalyticalCache<K, V> {
        private final int capacity;
        private final long ttlMs;
        private final LinkedHashMap<K, V> map;
        private final Map<K, Long> timestamps = new ConcurrentHashMap<>();
        private long hits = 0, misses = 0, evictions = 0;

        AnalyticalCache(int capacity, long ttlMs) {
            this.capacity = capacity;
            this.ttlMs = ttlMs;
            this.map = new LinkedHashMap<>(capacity, 0.75f, true);
        }

        synchronized V get(K key) {
            Long ts = timestamps.get(key);
            if (ts != null && ttlMs > 0 && System.currentTimeMillis() - ts > ttlMs) {
                map.remove(key);
                timestamps.remove(key);
                misses++;
                return null;
            }
            V val = map.get(key);
            if (val != null) {
                hits++;
                return val;
            }
            misses++;
            return null;
        }

        synchronized void put(K key, V value) {
            if (map.size() >= capacity && !map.containsKey(key)) {
                Map.Entry<K, V> eldest = map.entrySet().iterator().next();
                map.remove(eldest.getKey());
                timestamps.remove(eldest.getKey());
                evictions++;
            }
            map.put(key, value);
            timestamps.put(key, System.currentTimeMillis());
        }

        CacheMetrics metrics() {
            long total = hits + misses;
            return new CacheMetrics(hits, misses, evictions, total > 0 ? (double) hits / total : 0.0);
        }

        void reset() { hits = misses = evictions = 0; }
    }

    // Exercise 4: Object Pool Optimizer
    record PoolStats(int active, int idle, int total, int peak, double utilization) {}

    static class OptimizedPool<T> {
        private final java.util.function.Supplier<T> factory;
        private final Queue<T> idle = new ConcurrentLinkedQueue<>();
        private final Set<T> active = ConcurrentHashMap.newKeySet();
        private final AtomicInteger totalCreated = new AtomicInteger(0);
        private final AtomicInteger peakActive = new AtomicInteger(0);

        OptimizedPool(java.util.function.Supplier<T> factory, int initialSize) {
            this.factory = factory;
            for (int i = 0; i < initialSize; i++) {
                idle.offer(factory.get());
                totalCreated.incrementAndGet();
            }
        }

        T borrow() {
            T obj = idle.poll();
            if (obj == null) {
                obj = factory.get();
                totalCreated.incrementAndGet();
            }
            active.add(obj);
            peakActive.updateAndGet(current -> Math.max(current, active.size()));
            return obj;
        }

        void release(T obj) {
            active.remove(obj);
            idle.offer(obj);
        }

        PoolStats stats() {
            int a = active.size();
            int i = idle.size();
            return new PoolStats(a, i, a + i, peakActive.get(),
                (a + i) > 0 ? (double) a / (a + i) : 0.0);
        }
    }

    // Exercise 5: Throughput Test Harness
    record ThroughputResult(double opsPerSecond, long avgLatencyNs, long maxLatencyNs, int totalOps) {}

    static ThroughputResult measureThroughput(
            java.util.function.IntSupplier operation,
            int threadCount,
            int opsPerThread) {

        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicLong totalLatency = new AtomicLong(0);
        AtomicLong maxLatency = new AtomicLong(0);
        int totalOps = threadCount * opsPerThread;

        List<Thread> threads = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            threads.add(Thread.ofVirtual().start(() -> {
                try {
                    barrier.await();
                } catch (Exception e) { return; }

                long threadMax = 0;
                for (int i = 0; i < opsPerThread; i++) {
                    long start = System.nanoTime();
                    operation.getAsInt();
                    long elapsed = System.nanoTime() - start;
                    totalLatency.addAndGet(elapsed);
                    threadMax = Math.max(threadMax, elapsed);
                }
                maxLatency.updateAndGet(current -> Math.max(current, threadMax));
            }));
        }

        long wallStart = System.nanoTime();
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        long wallTime = System.nanoTime() - wallStart;

        double opsPerSec = (double) totalOps / wallTime * 1_000_000_000L;
        long avgLatency = totalLatency.get() / totalOps;

        return new ThroughputResult(opsPerSec, avgLatency, maxLatency.get(), totalOps);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Performance Solutions ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: Micro-Benchmark ---");
        BenchmarkResult r1 = benchmark(() -> {
            int s = 0; for (int i = 0; i < 1000; i++) s += i;
        }, 1000, 10000);
        System.out.printf("  Avg: %d ns, P99: %d ns, Min: %d ns, Max: %d ns%n",
            r1.avgNs(), r1.p99Ns(), r1.minNs(), r1.maxNs());

        // Exercise 2
        System.out.println("\n--- Exercise 2: Leak Detector ---");
        LeakDetector detector = new LeakDetector();
        Object obj = new byte[1024 * 1024];
        double leakProb = detector.detectLeak(obj);
        System.out.println("  Leak probability: " + leakProb);

        // Exercise 3
        System.out.println("\n--- Exercise 3: Cache ---");
        AnalyticalCache<String, String> cache = new AnalyticalCache<>(100, 5000);
        cache.put("key1", "value1");
        cache.get("key1"); // hit
        cache.get("key2"); // miss
        cache.get("key1"); // hit
        CacheMetrics m = cache.metrics();
        System.out.printf("  Hits: %d, Misses: %d, Hit rate: %.2f%%%n",
            m.hits(), m.misses(), m.hitRate() * 100);

        // Exercise 4
        System.out.println("\n--- Exercise 4: Object Pool ---");
        OptimizedPool<StringBuilder> pool = new OptimizedPool<>(() -> new StringBuilder(64), 10);
        StringBuilder s1 = pool.borrow();
        StringBuilder s2 = pool.borrow();
        System.out.println("  After borrow 2: " + pool.stats());
        pool.release(s1);
        System.out.println("  After release 1: " + pool.stats());

        // Exercise 5
        System.out.println("\n--- Exercise 5: Throughput ---");
        ThroughputResult r5 = measureThroughput(
            () -> { int s = 0; for (int i = 0; i < 100; i++) s += i; return s; },
            4, 10000
        );
        System.out.printf("  Throughput: %.0f ops/sec, Avg: %d ns, Max: %d ns%n",
            r5.opsPerSecond(), r5.avgLatencyNs(), r5.maxLatencyNs());

        System.out.println("\n=== All Solutions Complete ===");
    }
}
