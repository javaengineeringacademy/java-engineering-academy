package academy.javaengineering.jvm.solutions;

import java.lang.management.*;
import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Garbage Collection Solutions - Complete implementations
 */
public class GarbageCollectionSolutions {

    /**
     * Exercise 1 Solution: Demonstrate generational collection
     */
    public static void demonstrateGenerationalCollection() {
        System.out.println("=== Generational Collection Demo ===");

        // Get GC stats before
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        long totalGcCountBefore = 0;
        long totalGcTimeBefore = 0;
        for (GarbageCollectorMXBean gc : gcBeans) {
            totalGcCountBefore += gc.getCollectionCount();
            totalGcTimeBefore += gc.getCollectionTime();
        }

        // Allocate short-lived objects (should be collected in Young Gen)
        System.out.println("Allocating short-lived objects...");
        for (int i = 0; i < 10000; i++) {
            String s = new String("short-lived-" + i); // Immediately eligible for GC
        }

        // Allocate long-lived objects (should be promoted to Old Gen)
        System.out.println("Allocating long-lived objects...");
        List<Object> longLived = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            longLived.add(new byte[1024]); // Kept alive
        }

        // Get GC stats after
        long totalGcCountAfter = 0;
        long totalGcTimeAfter = 0;
        for (GarbageCollectorMXBean gc : gcBeans) {
            totalGcCountAfter += gc.getCollectionCount();
            totalGcTimeAfter += gc.getCollectionTime();
        }

        System.out.println("\nGC Statistics:");
        System.out.println("  Young GC count: " + (totalGcCountAfter - totalGcCountBefore));
        System.out.println("  GC time: " + (totalGcTimeAfter - totalGcTimeBefore) + "ms");
        System.out.println("  Long-lived objects kept: " + longLived.size());

        // Clean up
        longLived.clear();
    }

    /**
     * Exercise 2 Solution: WeakCache implementation
     */
    static class WeakCache<K, V> {
        private final Map<K, WeakReference<V>> cache = new HashMap<>();
        private final ReferenceQueue<V> refQueue = new ReferenceQueue<>();

        public void put(K key, V value) {
            processCollectedEntries();
            cache.put(key, new WeakReference<>(value, refQueue));
        }

        public V get(K key) {
            processCollectedEntries();
            WeakReference<V> ref = cache.get(key);
            if (ref == null) return null;
            V value = ref.get();
            if (value == null) {
                cache.remove(key);
            }
            return value;
        }

        private void processCollectedEntries() {
            Reference<? extends V> ref;
            while ((ref = refQueue.poll()) != null) {
                // Find and remove the corresponding key
                cache.entrySet().removeIf(entry -> entry.getValue() == ref);
                System.out.println("  [GC] Entry collected and removed from cache");
            }
        }

        public int size() {
            processCollectedEntries();
            return cache.size();
        }
    }

    /**
     * Exercise 3 Solution: Memory leak fixes
     */
    static class MemoryLeakExamples {
        private static final Map<String, Object> staticMap = new ConcurrentHashMap<>();
        private static final int MAX_CACHE_SIZE = 1000; // Fix: Add size limit

        // Fix 1: Add size limit to static collection
        public static void leak1_StaticCollection(String key, Object value) {
            if (staticMap.size() >= MAX_CACHE_SIZE) {
                // Evict oldest entries or throw exception
                staticMap.clear();
            }
            staticMap.put(key, value);
        }

        // Fix 2: Use try-with-resources for unclosed resources
        public static void leak2_UnclosedResource() {
            try (Scanner scanner = new Scanner(System.in)) {
                String line = scanner.nextLine();
                System.out.println("Read: " + line);
            } // Scanner automatically closed here
        }

        // Fix 3: Use static inner class (doesn't hold reference to outer)
        public Object leak3_InnerClass() {
            static class StaticInnerClass {
                private final Object data = new byte[1024];
            }
            return new StaticInnerClass();
        }

        // Fix 4: Clean up ThreadLocal in finally block
        private static final ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();

        public static void leak4_ThreadLocal() {
            threadLocal.set(new byte[1024 * 1024]);
            try {
                // Use the ThreadLocal
            } finally {
                threadLocal.remove(); // Always clean up!
            }
        }
    }

    /**
     * Exercise 4 Solution: BoundedSoftCache
     */
    static class BoundedSoftCache<K, V> {
        private final LinkedHashMap<K, SoftReference<V>> cache;
        private final int maxSize;

        public BoundedSoftCache(int maxSize) {
            this.maxSize = maxSize;
            this.cache = new LinkedHashMap<K, SoftReference<V>>(maxSize, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {
                    return size() > BoundedSoftCache.this.maxSize;
                }
            };
        }

        public void put(K key, V value) {
            // LinkedHashMap with removeEldestEntry handles eviction automatically
            cache.put(key, new SoftReference<>(value));
        }

        public V get(K key) {
            SoftReference<V> ref = cache.get(key);
            if (ref == null) return null;
            V value = ref.get();
            if (value == null) {
                cache.remove(key);
            }
            return value;
        }

        public void cleanup() {
            cache.entrySet().removeIf(entry -> entry.getValue().get() == null);
        }

        public int size() {
            cleanup();
            return cache.size();
        }
    }

    /**
     * Exercise 5 Solution: Monitor GC behavior
     */
    public static void monitorGcBehavior() {
        System.out.println("=== GC Behavior Monitor ===");

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        // Record baseline
        long baselineGcCount = 0;
        long baselineGcTime = 0;
        for (GarbageCollectorMXBean gc : gcBeans) {
            baselineGcCount += gc.getCollectionCount();
            baselineGcTime += gc.getCollectionTime();
        }

        // Generate GC pressure
        System.out.println("Generating memory pressure...");
        List<byte[]> garbage = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            garbage.add(new byte[1024 * 100]); // 100KB each
            if (i % 10 == 0) {
                garbage.clear();
                System.gc();
            }
        }

        // Measure GC activity
        long afterGcCount = 0;
        long afterGcTime = 0;
        for (GarbageCollectorMXBean gc : gcBeans) {
            afterGcCount += gc.getCollectionCount();
            afterGcTime += gc.getCollectionTime();
        }

        long gcCount = afterGcCount - baselineGcCount;
        long gcTime = afterGcTime - baselineGcTime;

        System.out.println("\nGC Analysis:");
        System.out.println("  GC count increase: " + gcCount);
        System.out.println("  GC time increase: " + gcTime + "ms");
        System.out.println("  GC overhead: " + (gcCount > 0 ? (gcTime / gcCount) : 0) + "ms per GC");

        if (gcCount > 10) {
            System.out.println("  WARNING: High GC frequency detected!");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Garbage Collection Solutions ===\n");

        // Exercise 1
        System.out.println("Exercise 1: Generational Collection");
        demonstrateGenerationalCollection();

        // Exercise 2
        System.out.println("\nExercise 2: WeakCache");
        WeakCache<String, String> weakCache = new WeakCache<>();
        weakCache.put("key1", "value1");
        weakCache.put("key2", "value2");
        System.out.println("Cache size: " + weakCache.size());
        System.out.println("Get key1: " + weakCache.get("key1"));

        // Exercise 3
        System.out.println("\nExercise 3: Memory Leak Fixes");
        for (int i = 0; i < 100; i++) {
            MemoryLeakExamples.leak1_StaticCollection("key" + i, new byte[1024]);
        }
        System.out.println("Static map size (with limit): " + MemoryLeakExamples.staticMap.size());

        // Exercise 4
        System.out.println("\nExercise 4: Bounded SoftCache");
        BoundedSoftCache<Integer, String> boundedCache = new BoundedSoftCache<>(5);
        for (int i = 0; i < 10; i++) {
            boundedCache.put(i, "value" + i);
        }
        System.out.println("Cache size (max 5): " + boundedCache.size());

        // Exercise 5
        System.out.println("\nExercise 5: GC Behavior Monitor");
        monitorGcBehavior();
    }
}
