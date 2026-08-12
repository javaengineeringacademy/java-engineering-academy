package academy.javaengineering.collections.map.memory;

import java.util.*;
import java.util.concurrent.*;

public class MapMemory {

    public static void main(String[] args) {
        System.out.println("=== Map Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. HashMap vs TreeMap vs LinkedHashMap
        System.out.println("--- Map Implementations Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Map<Integer, Integer> hash = new HashMap<>();
        for (int i = 0; i < 10000; i++) hash.put(i, i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("HashMap 10K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Map<Integer, Integer> tree = new TreeMap<>();
        for (int i = 0; i < 10000; i++) tree.put(i, i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("TreeMap 10K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Map<Integer, Integer> linked = new LinkedHashMap<>();
        for (int i = 0; i < 10000; i++) linked.put(i, i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("LinkedHashMap 10K: " + (after - before) + " bytes");

        // 2. ConcurrentHashMap vs Hashtable
        System.out.println("\n--- Concurrent Map Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Map<Integer, Integer> concurrent = new ConcurrentHashMap<>();
        for (int i = 0; i < 10000; i++) concurrent.put(i, i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("ConcurrentHashMap 10K: " + (after - before) + " bytes");

        // 3. Entry object overhead
        System.out.println("\n--- Entry Object Overhead ---");
        System.out.println("HashMap.Node: ~32 bytes per entry");
        System.out.println("  - int hash (4)");
        System.out.println("  - K key (8 ref)");
        System.out.println("  - V value (8 ref)");
        System.out.println("  - Node next (8 ref)");

        // 4. Initial capacity optimization
        System.out.println("\n--- Initial Capacity Optimization ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Map<String, Integer> defaultCap = new HashMap<>();
        for (int i = 0; i < 1000; i++) defaultCap.put("key" + i, i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Default capacity: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Map<String, Integer> preallocated = new HashMap<>(2048);
        for (int i = 0; i < 1000; i++) preallocated.put("key" + i, i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Pre-allocated 2048: " + (after - before) + " bytes");
    }
}
