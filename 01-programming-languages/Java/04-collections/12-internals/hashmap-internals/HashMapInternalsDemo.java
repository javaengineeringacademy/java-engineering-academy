import java.lang.reflect.Field;
import java.util.*;

/**
 * HashMap Internals Demo
 * Demonstrates internal Node[] table, hash function, collision handling, and load factor.
 */
public class HashMapInternalsDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== HashMap Internals Demo ===\n");

        demonstrateInternalStructure();
        demonstrateHashFunction();
        demonstrateCollisionHandling();
        demonstrateLoadFactor();
    }

    private static void demonstrateInternalStructure() throws Exception {
        System.out.println("--- Internal Node[] Table ---");
        HashMap<String, Integer> map = new HashMap<>();
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);

        Object[] table = (Object[]) tableField.get(map);
        System.out.println("Initial table: " + (table == null ? "null" : table.length));

        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);

        table = (Object[]) tableField.get(map);
        System.out.println("After 3 entries - table length: " + table.length);
        System.out.println();
    }

    private static void demonstrateHashFunction() {
        System.out.println("--- Hash Function ---");
        System.out.println("Hash spreading: hash = key.hashCode() ^ (h >>> 16)");

        for (int i = 0; i < 5; i++) {
            int h = i;
            int spread = h ^ (h >>> 16);
            System.out.printf("  Hash %d: spread=%d%n", h, spread);
        }

        System.out.println("\nIndex calculation: index = hash & (capacity - 1)");
        int capacity = 16;
        int hash = 42;
        System.out.println("  hash=42, capacity=16, index=" + (hash & (capacity - 1)));
        System.out.println();
    }

    private static void demonstrateCollisionHandling() {
        System.out.println("--- Collision Handling ---");
        HashMap<Integer, String> map = new HashMap<>(4);
        for (int i = 0; i < 20; i++) map.put(i, "value" + i);
        System.out.println("20 entries in capacity-4 map: linked lists formed in buckets");
        System.out.println();
    }

    private static void demonstrateLoadFactor() throws Exception {
        System.out.println("--- Load Factor ---");
        Field thresholdField = HashMap.class.getDeclaredField("threshold");
        thresholdField.setAccessible(true);

        HashMap<Integer, Integer> map1 = new HashMap<>();
        int threshold = (int) thresholdField.get(map1);
        System.out.println("Default: capacity=16, loadFactor=0.75, threshold=" + threshold);

        HashMap<Integer, Integer> map2 = new HashMap<>(16, 0.5f);
        threshold = (int) thresholdField.get(map2);
        System.out.println("Custom: capacity=16, loadFactor=0.5, threshold=" + threshold);
    }
}
