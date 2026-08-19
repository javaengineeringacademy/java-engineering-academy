package academy.javaengineering.jvm.practices;

import java.lang.ref.*;
import java.util.*;

/**
 * Garbage Collection Exercises
 * Complete each exercise by implementing the required method.
 * Focus on GC concepts, reference types, and tuning.
 */
public class GarbageCollectionExercises {

    private static final List<byte[]> memoryStore = new ArrayList<>();

    /**
     * Exercise 1: Demonstrate generational collection
     * Create short-lived and long-lived objects to demonstrate
     * how objects move between Eden, Survivor, and Old generation.
     *
     * Steps:
     * 1. Allocate many short-lived objects (should be collected in Young GC)
     * 2. Allocate some long-lived objects (should be promoted to Old gen)
     * 3. Use ManagementFactory to monitor GC activity
     * 4. Print before/after memory stats
     */
    public static void demonstrateGenerationalCollection() {
        // TODO: Implement this exercise
        // HINT: Use ManagementFactory.getGarbageCollectorMXBeans()
        // HINT: Use Runtime.getRuntime().freeMemory() / totalMemory()
    }

    /**
     * Exercise 2: Implement an LRU cache using WeakReference
     * Create a cache that:
     * 1. Stores entries using WeakReference
     * 2. Automatically evicts entries when GC runs
     * 3. Uses ReferenceQueue to detect when entries are collected
     *
     * Requirements:
     * - Cache should accept String keys and Object values
     * - When an entry is GC'd, log it
     * - Provide a method to check cache size
     */
    static class WeakCache<K, V> {
        private final Map<K, WeakReference<V>> cache = new HashMap<>();
        private final ReferenceQueue<V> refQueue = new ReferenceQueue<>();

        // TODO: Implement put method
        public void put(K key, V value) {
            // 1. Create WeakReference with refQueue
            // 2. Add to cache map
            // 3. Process any collected entries
        }

        // TODO: Implement get method
        public V get(K key) {
            // 1. Get WeakReference from map
            // 2. Get value (may be null if GC'd)
            // 3. Process any collected entries
            return null;
        }

        // TODO: Implement processCollectedEntries
        private void processCollectedEntries() {
            // Poll the reference queue and log collected entries
        }

        // TODO: Implement size
        public int size() {
            return 0;
        }
    }

    /**
     * Exercise 3: Detect memory leak patterns
     * The following code contains common memory leak patterns.
     * Identify and fix each leak.
     *
     * Leak patterns to detect:
     * 1. Static collection growing without bounds
     * 2. Unclosed resources
     * 3. Inner class holding reference to outer
     * 4. String.intern() misuse
     * 5. ThreadLocal not cleaned up
     */
    static class MemoryLeakExamples {
        private static final Map<String, Object> staticMap = new HashMap<>();

        // TODO: Identify and fix leak 1 - Static collection
        public static void leak1_StaticCollection(String key, Object value) {
            staticMap.put(key, value); // This grows forever!
        }

        // TODO: Identify and fix leak 2 - Unclosed resource
        public static void leak2_UnclosedResource() {
            // scanner reads from System.in
            // Scanner scanner = new Scanner(System.in);
            // String line = scanner.nextLine();
            // Missing: scanner.close();
        }

        // TODO: Identify and fix leak 3 - Inner class reference
        public Object leak3_InnerClass() {
            class InnerClass {
                private final Object data = new byte[1024];
            }
            // Inner class holds implicit reference to enclosing instance
            return new InnerClass();
        }

        // TODO: Identify and fix leak 4 - ThreadLocal
        private static final ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();

        public static void leak4_ThreadLocal() {
            threadLocal.set(new byte[1024 * 1024]); // 1MB per thread!
            // Never cleaned up in thread pools!
        }
    }

    /**
     * Exercise 4: Implement a SoftReference cache with size limit
     * Create a cache that:
     * 1. Uses SoftReference for entries
     * 2. Has a maximum size limit
     * 3. Evicts oldest entries when limit reached
     * 4. Allows GC to clear entries when memory is low
     */
    static class BoundedSoftCache<K, V> {
        private final Map<K, SoftReference<V>> cache = new LinkedHashMap<>();
        private final int maxSize;

        public BoundedSoftCache(int maxSize) {
            this.maxSize = maxSize;
        }

        // TODO: Implement put with eviction
        public void put(K key, V value) {
            // 1. If cache at max, remove oldest entry
            // 2. Add new entry with SoftReference
        }

        // TODO: Implement get
        public V get(K key) {
            // 1. Get SoftReference
            // 2. Return value or null
            return null;
        }

        // TODO: Implement cleanup
        public void cleanup() {
            // Remove entries where get() returns null
        }
    }

    /**
     * Exercise 5: Monitor and analyze GC behavior
     * Write code that:
     * 1. Allocates objects in a loop
     * 2. Monitors GC count and time
     * 3. Detects when GC frequency increases
     * 4. Prints GC statistics
     *
     * This simulates how profilers detect memory pressure.
     */
    public static void monitorGcBehavior() {
        // TODO: Implement this exercise
        // HINT: Use ManagementFactory.getGarbageCollectorMXBeans()
        // HINT: Record GC count before and after allocation loop
        // HINT: Calculate GC overhead (GC time / total time)
    }

    public static void main(String[] args) {
        System.out.println("=== Garbage Collection Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Generational Collection");
        demonstrateGenerationalCollection();

        // Test Exercise 2
        System.out.println("\nExercise 2: WeakCache");
        WeakCache<String, String> weakCache = new WeakCache<>();
        weakCache.put("key1", "value1");
        weakCache.put("key2", "value2");
        System.out.println("Cache size: " + weakCache.size());
        System.out.println("Get key1: " + weakCache.get("key1"));

        // Test Exercise 3
        System.out.println("\nExercise 3: Memory Leak Patterns");
        for (int i = 0; i < 100; i++) {
            MemoryLeakExamples.leak1_StaticCollection("key" + i, new byte[1024]);
        }
        System.out.println("Static map size: " + MemoryLeakExamples.staticMap.size());

        // Test Exercise 4
        System.out.println("\nExercise 4: Bounded SoftCache");
        BoundedSoftCache<Integer, String> boundedCache = new BoundedSoftCache<>(5);
        for (int i = 0; i < 10; i++) {
            boundedCache.put(i, "value" + i);
        }
        System.out.println("Cache size: " + boundedCache.cache.size());

        // Test Exercise 5
        System.out.println("\nExercise 5: Monitor GC Behavior");
        monitorGcBehavior();
    }
}
