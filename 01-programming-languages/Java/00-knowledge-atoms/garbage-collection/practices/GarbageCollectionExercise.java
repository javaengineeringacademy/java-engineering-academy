import java.lang.management.*;
import java.util.*;

/**
 * Exercise 1: Memory Monitor
 * Create a class that monitors memory usage during object creation
 */
public class GarbageCollectionExercise {

    // TODO: Implement MemoryMonitor class
    // Should track object creation count and memory usage
    static class MemoryMonitor {
        // Add fields here

        // TODO: Implement createObject() method
        // - Create an object of given size (bytes)
        // - Track creation count
        // - Return the created object

        // TODO: Implement getStats() method
        // - Return a summary of memory usage

        // TODO: Implement printReport() method
        // - Print formatted report of memory statistics
    }

    // TODO: Implement SimpleCache using WeakReference
    static class SimpleCache<K, V> {
        // Add fields to store weak references

        // TODO: Implement put(K key, V value)
        // - Store value using WeakReference

        // TODO: Implement get(K key)
        // - Retrieve and return value (or null if collected)

        // TODO: Implement size()
        // - Return number of entries

        // TODO: Implement cleanup()
        // - Remove stale entries
    }

    public static void main(String[] args) {
        System.out.println("=== Garbage Collection Exercises ===\n");

        // Exercise 1: Memory Monitor
        System.out.println("--- Exercise 1: Memory Monitor ---");
        System.out.println("TODO: Implement MemoryMonitor class");
        System.out.println("Expected: Track object creation and memory usage\n");

        // Exercise 2: Simple Cache
        System.out.println("--- Exercise 2: Simple Cache ---");
        System.out.println("TODO: Implement SimpleCache with WeakReference");
        System.out.println("Expected: Cache entries removed after GC\n");

        // Exercise 3: GC Stats
        System.out.println("--- Exercise 3: GC Statistics ---");
        displayGCInfo();
        System.out.println("\nTODO: Write benchmark comparing GC algorithms");
    }

    private static void displayGCInfo() {
        System.out.println("Current GC configuration:");
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcBeans) {
            System.out.printf("  %s: %d collections, %dms%n",
                gc.getName(), gc.getCollectionCount(), gc.getCollectionTime());
        }

        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memory.getHeapMemoryUsage();
        System.out.printf("Heap: used=%dMB, max=%dMB%n",
            heap.getUsed() / (1024 * 1024), heap.getMax() / (1024 * 1024));
    }
}
