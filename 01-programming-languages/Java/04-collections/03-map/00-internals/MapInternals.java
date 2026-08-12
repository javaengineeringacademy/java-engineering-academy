package academy.javaengineering.collections.map.internals;

import java.util.*;
import java.util.concurrent.*;

public class MapInternals {

    public static void main(String[] args) {
        System.out.println("=== Map Interface Internals ===\n");

        // 1. HashMap bucket structure
        System.out.println("--- HashMap Bucket Structure ---");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Python", 2);
        map.put("JavaScript", 3);
        System.out.println("Internal: Array of buckets");
        System.out.println("Each bucket: Node(key, value, hash, next)");
        System.out.println("Collision: Linked list in bucket");
        System.out.println("Treeify: When bucket > 8 nodes");

        // 2. HashMap capacity and load factor
        System.out.println("\n--- Capacity & Load Factor ---");
        HashMap<Integer, Integer> capacity = new HashMap<>();
        System.out.println("Default capacity: 16");
        System.out.println("Default load factor: 0.75");
        System.out.println("Resize when: size > capacity * loadFactor");
        for (int i = 0; i < 20; i++) capacity.put(i, i * 10);
        System.out.println("After 20 entries: capacity doubled");

        // 3. TreeMap red-black tree
        System.out.println("\n--- TreeMap Structure ---");
        TreeMap<String, Integer> tree = new TreeMap<>();
        tree.put("Banana", 2);
        tree.put("Apple", 5);
        tree.put("Cherry", 3);
        System.out.println("Sorted keys: " + tree.keySet());
        System.out.println("Red-Black tree: O(log n) operations");

        // 4. LinkedHashMap access order
        System.out.println("\n--- LinkedHashMap Access Order ---");
        LinkedHashMap<String, Integer> accessOrder = new LinkedHashMap<>(16, 0.75f, true);
        accessOrder.put("A", 1);
        accessOrder.put("B", 2);
        accessOrder.put("C", 3);
        accessOrder.get("A"); // Move A to end
        System.out.println("Access order (LRU): " + accessOrder.keySet());

        // 5. ConcurrentHashMap segments
        System.out.println("\n--- ConcurrentHashMap ---");
        ConcurrentHashMap<String, Integer> concurrent = new ConcurrentHashMap<>();
        concurrent.put("Thread1", 1);
        concurrent.put("Thread2", 2);
        System.out.println("Lock striping: Different buckets lock separately");
        System.out.println("No null keys/values allowed");

        // 6. Hashtable legacy
        System.out.println("\n--- Hashtable (Legacy) ---");
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("Legacy", 1);
        System.out.println("All methods synchronized");
        System.out.println("Use ConcurrentHashMap instead");
    }
}
