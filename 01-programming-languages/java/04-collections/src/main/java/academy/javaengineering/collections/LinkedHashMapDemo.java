package academy.javaengineering.collections;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Demonstrates LinkedHashMap operations with insertion-order and access-order iteration.
 * LinkedHashMap extends HashMap with a doubly-linked list for predictable iteration order.
 */
public class LinkedHashMapDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateInsertionOrder();
        demonstrateAccessOrder();
        demonstrateLRUCache();
    }

    private static void demonstrateBasicOperations() {
        System.out.println("=== LinkedHashMap Basic Operations ===");

        LinkedHashMap<String, Integer> capitals = new LinkedHashMap<>();
        capitals.put("France", 1);
        capitals.put("Germany", 2);
        capitals.put("Italy", 3);

        System.out.println("Map: " + capitals);
        System.out.println("Size: " + capitals.size());
        System.out.println("Get France: " + capitals.get("France"));

        capitals.putIfAbsent("Spain", 4);
        capitals.put("France", 5); // Update existing
        System.out.println("After updates: " + capitals);

        capitals.remove("Italy");
        System.out.println("After removing Italy: " + capitals);
        System.out.println();
    }

    private static void demonstrateInsertionOrder() {
        System.out.println("=== Insertion-Order Iteration ===");

        LinkedHashMap<String, String> linked = new LinkedHashMap<>();
        linked.put("Banana", "Yellow");
        linked.put("Apple", "Red");
        linked.put("Grape", "Purple");

        System.out.println("LinkedHashMap (insertion order):");
        linked.forEach((fruit, color) ->
            System.out.println("  " + fruit + " -> " + color)
        );

        System.out.println("\nHashMap (no guaranteed order):");
        Map<String, String> regular = new java.util.HashMap<>(linked);
        regular.forEach((fruit, color) ->
            System.out.println("  " + fruit + " -> " + color)
        );
        System.out.println();
    }

    private static void demonstrateAccessOrder() {
        System.out.println("=== Access-Order (LRU) ===");

        LinkedHashMap<String, Integer> accessOrdered = new LinkedHashMap<>(16, 0.75f, true);
        accessOrdered.put("A", 1);
        accessOrdered.put("B", 2);
        accessOrdered.put("C", 3);

        System.out.println("Initial order: " + accessOrdered.keySet());

        accessOrdered.get("A"); // Access A, moves to end
        System.out.println("After accessing A: " + accessOrdered.keySet());

        accessOrdered.get("B"); // Access B, moves to end
        System.out.println("After accessing B: " + accessOrdered.keySet());

        accessOrdered.put("D", 4);
        System.out.println("After adding D: " + accessOrdered.keySet());
        System.out.println();
    }

    private static void demonstrateLRUCache() {
        System.out.println("=== LRU Cache Implementation ===");

        int capacity = 3;
        LinkedHashMap<String, String> lruCache = new LinkedHashMap<String, String>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > capacity;
            }
        };

        lruCache.put("key1", "value1");
        lruCache.put("key2", "value2");
        lruCache.put("key3", "value3");
        System.out.println("Initial cache: " + lruCache);

        lruCache.get("key1"); // Access key1, moves to end
        lruCache.put("key4", "value4"); // Should evict key2 (LRU)
        System.out.println("After adding key4 (evicts key2): " + lruCache);

        lruCache.get("key3"); // Access key3
        lruCache.put("key5", "value5"); // Should evict key1 (LRU)
        System.out.println("After adding key5 (evicts key1): " + lruCache);
        System.out.println();
    }
}
