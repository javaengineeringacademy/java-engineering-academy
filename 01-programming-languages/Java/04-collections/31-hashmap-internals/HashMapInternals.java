import java.lang.reflect.Field;
import java.util.*;

/**
 * HashMap Internals
 * Internal structure, hash function, collision handling, load factor.
 */
public class HashMapInternals {

    public static void main(String[] args) throws Exception {
        System.out.println("=== HashMap Internals ===\n");

        internalStructure();
        hashFunction();
        collisionHandling();
        loadFactor();
        capacityPowerOf2();
        treeificationThreshold();

        System.out.println("\n=== Complete ===");
    }

    // --- Internal Node[] table ---
    static void internalStructure() throws Exception {
        System.out.println("--- Internal Node[] Table ---");

        HashMap<String, Integer> map = new HashMap<>();

        // Access internal table via reflection
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);

        Object[] table = (Object[]) tableField.get(map);
        System.out.println("Initial table: " + (table == null ? "null" : table.length));

        // Add entries
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);

        table = (Object[]) tableField.get(map);
        System.out.println("After adding 3 entries:");
        System.out.println("  Table length: " + table.length);

        // Show bucket distribution
        System.out.println("  Bucket distribution:");
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println("    Bucket " + i + ": " + table[i]);
            }
        }

        // Node structure
        System.out.println("\nNode structure (HashMap$Node):");
        System.out.println("  - int hash");
        System.out.println("  - K key");
        System.out.println("  - V value");
        System.out.println("  - Node next");

        System.out.println();
    }

    // --- Hash function ---
    static void hashFunction() {
        System.out.println("--- Hash Function ---");

        // Standard hashCode
        String key1 = "hello";
        String key2 = "world";
        System.out.println("\"hello\" hashCode: " + key1.hashCode());
        System.out.println("\"world\" hashCode: " + key2.hashCode());

        // HashMap hash spreading
        System.out.println("\nHash spreading: hash = key.hashCode() ^ (h >>> 16)");
        System.out.println("This ensures high bits affect low bits:");

        for (int i = 0; i < 5; i++) {
            int h = i;
            int spread = h ^ (h >>> 16);
            System.out.printf("  Hash %d: %d (spread: %d)%n", h, h, spread);
        }

        // Demonstrate collision
        System.out.println("\nCollision example:");
        // Two different strings that hash to same bucket
        String a = "Aa";
        String b = "BB";
        System.out.println("\"Aa\" hashCode: " + a.hashCode());
        System.out.println("\"BB\" hashCode: " + b.hashCode());

        // Show how index is calculated
        System.out.println("\nIndex calculation:");
        System.out.println("  index = hash & (capacity - 1)");
        System.out.println("  This works because capacity is power of 2");

        for (int cap = 4; cap <= 16; cap *= 2) {
            int mask = cap - 1;
            System.out.printf("  Cap %d: mask = %d (binary: %s)%n",
                cap, mask, Integer.toBinaryString(mask));
        }

        System.out.println();
    }

    // --- Collision handling ---
    static void collisionHandling() throws Exception {
        System.out.println("--- Collision Handling ---");

        // Create map that will have collisions
        HashMap<Integer, String> map = new HashMap<>(4); // Small capacity

        for (int i = 0; i < 20; i++) {
            map.put(i, "value" + i);
        }

        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);

        Object[] table = (Object[]) tableField.get(map);
        System.out.println("Table with collisions (capacity " + table.length + "):");

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println("  Bucket " + i + ": " + table[i]);
            }
        }

        System.out.println("\nLinked list in bucket:");
        System.out.println("  Node -> Node -> Node -> null");

        System.out.println();
    }

    // --- Load factor ---
    static void loadFactor() throws Exception {
        System.out.println("--- Load Factor ---");

        Field thresholdField = HashMap.class.getDeclaredField("threshold");
        thresholdField.setAccessible(true);

        // Default load factor
        HashMap<Integer, Integer> map1 = new HashMap<>();
        int threshold = (int) thresholdField.get(map1);
        System.out.println("Default: capacity=16, loadFactor=0.75, threshold=" + threshold);

        // Custom load factor
        HashMap<Integer, Integer> map2 = new HashMap<>(16, 0.5f);
        threshold = (int) thresholdField.get(map2);
        System.out.println("Custom: capacity=16, loadFactor=0.5, threshold=" + threshold);

        // When threshold exceeded, resize
        System.out.println("\nResize happens when: size > capacity * loadFactor");

        for (int i = 0; i < 13; i++) {
            map1.put(i, i * 10);
            Object[] table = (Object[]) ((HashMap<?, ?>) map1)
                .getClass().getDeclaredField("table")
                .get(null);
            System.out.println("  Added " + i + " entries, table length: " +
                ((Object[]) ((HashMap<?, ?>) map1)
                    .getClass().getDeclaredField("table")
                    .apply(map1)));
        }

        System.out.println();
    }

    // --- Capacity is power of 2 ---
    static void capacityPowerOf2() {
        System.out.println("--- Capacity Power of 2 ---");

        // Why power of 2?
        System.out.println("Power of 2 allows bitwise AND for index:");
        System.out.println("  index = hash & (capacity - 1)");
        System.out.println("  This is equivalent to: hash % capacity");
        System.out.println("  But much faster (bitwise vs division)");

        // Example
        int capacity = 16;
        int hash = 42;
        int indexBitwise = hash & (capacity - 1);
        int indexModulo = hash % capacity;

        System.out.println("\nExample: hash=42, capacity=16");
        System.out.println("  Bitwise: " + indexBitwise);
        System.out.println("  Modulo: " + indexModulo);
        System.out.println("  Binary: " + Integer.toBinaryString(hash) +
            " & " + Integer.toBinaryString(capacity - 1) +
            " = " + Integer.toBinaryString(indexBitwise));

        // Capacity rounds up to power of 2
        System.out.println("\nCapacity rounding:");
        int[] requested = {0, 1, 3, 7, 15, 16, 17, 31, 32, 33};
        for (int req : requested) {
            int cap = 1;
            while (cap < req) cap <<= 1;
            System.out.println("  Requested " + req + " -> capacity " + cap);
        }

        System.out.println();
    }

    // --- Treeification threshold ---
    static void treeificationThreshold() {
        System.out.println("--- Treeification Threshold ---");

        System.out.println("When bucket length > 8, linked list becomes tree:");
        System.out.println("  - TREEIFY_THRESHOLD = 8");
        System.out.println("  - UNTREEIFY_THRESHOLD = 6");
        System.out.println("  - MIN_TREEIFY_CAPACITY = 64");

        System.out.println("\nTree structure (TreeNode):");
        System.out.println("  - TreeNode extends LinkedHashMap.Entry");
        System.out.println("  - Has parent, left, right, red/black fields");
        System.out.println("  - Lookup becomes O(log n) instead of O(n)");

        // Demonstrate
        HashMap<String, Integer> map = new HashMap<>();
        // Add many keys to force treeification
        for (int i = 0; i < 100; i++) {
            map.put("key" + i, i);
        }

        System.out.println("\nAfter 100 entries:");
        System.out.println("  Some buckets have >8 entries (treeified)");
        System.out.println("  Others still have linked lists (<8 entries)");

        // Performance comparison
        System.out.println("\nPerformance:");
        System.out.println("  Linked list: O(n) lookup in worst case");
        System.out.println("  Tree (red-black): O(log n) lookup in worst case");
        System.out.println("  With 8 entries: 8 comparisons vs ~3 (log2(8))");

        System.out.println("\n=== Complete ===");
    }
}
