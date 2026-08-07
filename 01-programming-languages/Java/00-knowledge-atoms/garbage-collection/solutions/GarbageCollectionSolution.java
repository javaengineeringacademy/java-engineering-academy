import java.lang.management.*;
import java.util.*;

/**
 * Solution 1: Memory Monitor
 * Complete implementation of memory monitoring
 */
public class GarbageCollectionSolution {

    static class MemoryMonitor {
        private int objectCount = 0;
        private long totalAllocatedBytes = 0;
        private final List<byte[]> objects = new ArrayList<>();

        public byte[] createObject(int sizeBytes) {
            byte[] obj = new byte[sizeBytes];
            objects.add(obj);
            objectCount++;
            totalAllocatedBytes += sizeBytes;
            return obj;
        }

        public void clearObjects() {
            objects.clear();
            System.gc();
        }

        public Map<String, Object> getStats() {
            Runtime runtime = Runtime.getRuntime();
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("objectsCreated", objectCount);
            stats.put("totalAllocatedMB", totalAllocatedBytes / (1024 * 1024));
            stats.put("freeMemoryMB", runtime.freeMemory() / (1024 * 1024));
            stats.put("totalMemoryMB", runtime.totalMemory() / (1024 * 1024));
            stats.put("maxMemoryMB", runtime.maxMemory() / (1024 * 1024));
            return stats;
        }

        public void printReport() {
            Map<String, Object> stats = getStats();
            System.out.println("Memory Report:");
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                System.out.printf("  %-20s: %s%n", entry.getKey(), entry.getValue());
            }
        }
    }

    static class SimpleCache<K, V> {
        private final WeakHashMap<K, WeakReference<V>> cache = new WeakHashMap<>();

        public void put(K key, V value) {
            cache.put(key, new WeakReference<>(value));
        }

        public V get(K key) {
            WeakReference<V> ref = cache.get(key);
            return ref != null ? ref.get() : null;
        }

        public int size() {
            return cache.size();
        }

        public void cleanup() {
            cache.entrySet().removeIf(entry -> entry.getValue().get() == null);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Garbage Collection Solutions ===\n");

        // Solution 1: Memory Monitor
        System.out.println("--- Solution 1: Memory Monitor ---");
        MemoryMonitor monitor = new MemoryMonitor();

        System.out.println("Creating 100 objects of 10KB each...");
        for (int i = 0; i < 100; i++) {
            monitor.createObject(10240);
        }
        monitor.printReport();

        System.out.println("\nClearing objects and requesting GC...");
        monitor.clearObjects();
        monitor.printReport();

        // Solution 2: Simple Cache
        System.out.println("\n--- Solution 2: Simple Cache ---");
        SimpleCache<String, byte[]> cache = new SimpleCache<>();

        cache.put("key1", new byte[1024]);
        cache.put("key2", new byte[2048]);
        System.out.println("Cache size after adding 2 entries: " + cache.size());
        System.out.println("get(\"key1\"): " + (cache.get("key1") != null ? "found" : "null"));

        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("Cache size after GC: " + cache.size());
        System.out.println("get(\"key1\"): " + (cache.get("key1") != null ? "found" : "null"));

        // Solution 3: GC Stats
        System.out.println("\n--- Solution 3: GC Algorithm Benchmark ---");
        System.out.println("Run with: java -XX:+UseG1GC GarbageCollectionSolution");
        System.out.println("    or:   java -XX:+UseZGC GarbageCollectionSolution");
        System.out.println("    or:   java -XX:+UseParallelGC GarbageCollectionSolution");

        Runtime runtime = Runtime.getRuntime();
        long startMem = runtime.freeMemory();
        long startTime = System.nanoTime();

        List<byte[]> workload = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            workload.add(new byte[1024]);
            if (i % 1000 == 0) {
                workload.subList(0, 500).clear();
            }
        }

        long elapsed = System.nanoTime() - startTime;
        long endMem = runtime.freeMemory();

        System.out.printf("Workload completed in %.2f ms%n", elapsed / 1_000_000.0);
        System.out.printf("Memory delta: %d KB%n", (endMem - startMem) / 1024);

        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcBeans) {
            System.out.printf("GC: %s - %d collections, %dms%n",
                gc.getName(), gc.getCollectionCount(), gc.getCollectionTime());
        }

        System.out.println("\n=== End of Solutions ===");
    }
}
