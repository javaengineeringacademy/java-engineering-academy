package academy.javaengineering.knowledgeatoms.equalshashcode;

import java.util.*;

public class EqualsHashCodeMemory {

    public static void main(String[] args) {
        System.out.println("=== Equals & HashCode Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. HashMap entry memory overhead
        System.out.println("--- HashMap Entry Overhead ---");
        System.out.println("Each HashMap.Node contains:");
        System.out.println("  - int hash:        4 bytes");
        System.out.println("  - K key ref:       8 bytes (compressed oops)");
        System.out.println("  - V value ref:     8 bytes (compressed oops)");
        System.out.println("  - Node next ref:   8 bytes (compressed oops)");
        System.out.println("  - Object header:  16 bytes");
        System.out.println("  - Total:         ~44 bytes per entry (before alignment)");
        System.out.println("  - With alignment: ~48 bytes per entry");

        // 2. Memory impact of hashCode quality
        System.out.println("\n--- hashCode Quality Impact ---");
        System.out.println("Good hashCode: spread across N buckets, minimal collisions");
        System.out.println("Poor hashCode: all in one bucket, linked list traversal");
        System.out.println("Treeified bucket (8+ collisions): TreeNode overhead ~56 bytes each");

        // 3. HashSet memory = HashMap with dummy value
        System.out.println("\n--- HashSet Memory ---");
        System.out.println("HashSet is backed by HashMap with PRESENT dummy value");
        System.out.println("Each element: key (object) + value (PRESENT ref) + hash + next");
        System.out.println("Overhead per element: ~48 bytes (HashMap.Node)");

        // 4. Memory with mutable hashCode fields
        System.out.println("\n--- Mutable hashCode Memory Leak ---");
        System.out.println("If hashCode() uses mutable field:");
        System.out.println("  1. Insert object with hashCode=100 into bucket 100");
        System.out.println("  2. Mutate field so hashCode=200");
        System.out.println("  3. Object is now in wrong bucket");
        System.out.println("  4. Cannot be found or removed -> memory leak");

        // 5. Measure collection memory
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            map.put("key" + i, "value" + i);
        }

        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("\n--- Measured Memory (100K entries) ---");
        System.out.println("HashMap<String, String>: ~" + (after - before) / 1024 + " KB");
        System.out.println("Estimated per entry: ~" + (after - before) / 100_000 + " bytes");
    }
}
