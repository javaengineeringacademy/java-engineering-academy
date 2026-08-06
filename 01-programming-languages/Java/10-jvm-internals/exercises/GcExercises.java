package academy.javaengineering.exercises;

import java.lang.ref.*;
import java.util.*;

/**
 * Exercises: Garbage Collection Concepts
 *
 * Complete the TODO sections below.
 */
public class GcExercises {

    // TODO 1: Implement a WeakReference cache
    // Store values that can be GC'd when memory is low
    public static class WeakCache<K, V> {
        private Map<K, WeakReference<V>> cache = new HashMap<>();

        public void put(K key, V value) {
            // TODO: implement using WeakReference
        }

        public V get(K key) {
            // TODO: implement - return null if reference has been cleared
            return null;
        }

        public int size() {
            return cache.size();
        }
    }

    // TODO 2: Implement a SoftReference buffer
    // Stores items that are cleared only under memory pressure
    public static class SoftBuffer<V> {
        private List<SoftReference<V>> buffer = new ArrayList<>();

        public void add(V item) {
            // TODO: implement using SoftReference
        }

        public V get(int index) {
            // TODO: implement - return null if reference cleared
            return null;
        }

        public int size() {
            return buffer.size();
        }
    }

    // TODO 3: Implement a PhantomReference cleanup tracker
    // Track when objects are about to be collected
    public static class CleanupTracker {
        private List<PhantomReference<?>> phantomRefs = new ArrayList<>();
        private ReferenceQueue<Object> queue = new ReferenceQueue<>();

        public void track(Object obj) {
            // TODO: implement using PhantomReference
        }

        public int getPendingCount() {
            // TODO: poll the queue and count pending references
            return 0;
        }
    }

    // TODO 4: Demonstrate finalizer behavior
    // Create a class with a finalizer that sets a flag
    public static class FinalizeExample {
        private boolean finalized = false;

        @Override
        protected void finalize() throws Throwable {
            // TODO: set finalized to true
            super.finalize();
        }

        public boolean isFinalized() {
            return finalized;
        }
    }

    // TODO 5: Implement a memory-aware list that suggests GC when full
    public static class MemoryAwareList<E> {
        private List<E> list = new ArrayList<>();
        private final int maxSize;

        public MemoryAwareList(int maxSize) {
            this.maxSize = maxSize;
        }

        public boolean add(E element) {
            // TODO: implement - if at capacity, suggest GC before adding
            return list.add(element);
        }

        public int size() {
            return list.size();
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        GcExercises exercises = new GcExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== GcExercises Tests ===\n");

        // Test 1 - WeakCache
        total++;
        WeakCache<String, String> cache = new WeakCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        if ("value1".equals(cache.get("key1")) && "value2".equals(cache.get("key2"))) {
            System.out.println("Test 1a PASSED: WeakCache put/get");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: WeakCache put/get");
        }

        total++;
        cache.put("temp", "data");
        cache.put("temp", null);
        if (cache.get("temp") == null) {
            System.out.println("Test 1b PASSED: WeakCache null value");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: WeakCache null value");
        }

        // Test 2 - SoftBuffer
        total++;
        SoftBuffer<String> buffer = new SoftBuffer<>();
        buffer.add("item1");
        buffer.add("item2");
        if ("item1".equals(buffer.get(0)) && "item2".equals(buffer.get(1))) {
            System.out.println("Test 2 PASSED: SoftBuffer");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: SoftBuffer");
        }

        // Test 3 - CleanupTracker
        total++;
        CleanupTracker tracker = new CleanupTracker();
        tracker.track(new Object());
        tracker.track(new Object());
        tracker.track(new Object());
        if (tracker.getPendingCount() >= 0) {
            System.out.println("Test 3 PASSED: CleanupTracker");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: CleanupTracker");
        }

        // Test 4 - FinalizeExample
        total++;
        FinalizeExample obj = new FinalizeExample();
        obj = null;
        System.gc();
        Thread.sleep(100);
        FinalizeExample obj2 = new FinalizeExample();
        if (!obj2.isFinalized()) {
            System.out.println("Test 4 PASSED: FinalizeExample (new object not finalized)");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: FinalizeExample");
        }

        // Test 5 - MemoryAwareList
        total++;
        MemoryAwareList<String> mal = new MemoryAwareList<>(5);
        for (int i = 0; i < 5; i++) {
            mal.add("item" + i);
        }
        if (mal.size() == 5) {
            System.out.println("Test 5 PASSED: MemoryAwareList");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: MemoryAwareList - size=" + mal.size());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
