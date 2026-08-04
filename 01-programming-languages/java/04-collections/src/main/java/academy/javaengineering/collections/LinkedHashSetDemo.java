package academy.javaengineering.collections;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Demonstrates LinkedHashSet operations with insertion-order iteration.
 * LinkedHashSet maintains a doubly-linked list across all entries for predictable iteration order.
 */
public class LinkedHashSetDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateInsertionOrder();
        demonstrateLRUCache();
        demonstrateHistoryTracking();
    }

    private static void demonstrateBasicOperations() {
        System.out.println("=== LinkedHashSet Basic Operations ===");

        LinkedHashSet<String> colors = new LinkedHashSet<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Red"); // Duplicate ignored

        System.out.println("Set: " + colors);
        System.out.println("Size: " + colors.size());
        System.out.println("Contains Red: " + colors.contains("Red"));

        colors.remove("Green");
        System.out.println("After removing Green: " + colors);
        System.out.println();
    }

    private static void demonstrateInsertionOrder() {
        System.out.println("=== Insertion-Order Iteration ===");

        LinkedHashSet<String> ordered = new LinkedHashSet<>();
        ordered.add("Charlie");
        ordered.add("Alice");
        ordered.add("Bob");
        ordered.add("Diana");

        System.out.println("Iteration order (insertion order):");
        for (String name : ordered) {
            System.out.println("  " + name);
        }

        System.out.println("\nHashSet (no guaranteed order):");
        Set<String> hashSet = new java.util.HashSet<>(ordered);
        for (String name : hashSet) {
            System.out.println("  " + name);
        }
        System.out.println();
    }

    private static void demonstrateLRUCache() {
        System.out.println("=== Simple LRU Cache Pattern ===");

        int maxSize = 3;
        LinkedHashSet<String> lruCache = new LinkedHashSet<>(maxSize) {
            @Override
            public boolean add(String e) {
                if (contains(e)) {
                    remove(e);
                }
                boolean result = super.add(e);
                while (size() > maxSize) {
                    Iterator<String> it = iterator();
                    it.next();
                    it.remove();
                }
                return result;
            }
        };

        lruCache.add("A");
        lruCache.add("B");
        lruCache.add("C");
        System.out.println("After adding A, B, C: " + lruCache);

        lruCache.add("D"); // Should remove "A"
        System.out.println("After adding D: " + lruCache);

        lruCache.add("E"); // Should remove "B"
        System.out.println("After adding E: " + lruCache);
        System.out.println();
    }

    private static void demonstrateHistoryTracking() {
        System.out.println("=== Page History Tracking ===");

        LinkedHashSet<String> history = new LinkedHashSet<>();

        visitPage(history, "home");
        visitPage(history, "about");
        visitPage(history, "contact");
        visitPage(history, "home"); // Already visited
        visitPage(history, "products");

        System.out.println("Visit history: " + history);
        System.out.println("Most recent: " + getLast(history));
        System.out.println("Oldest: " + getFirst(history));
    }

    private static void visitPage(LinkedHashSet<String> history, String page) {
        history.remove(page); // Remove if exists to re-add at end
        history.add(page);
        System.out.println("Visited: " + page);
    }

    private static <T> T getLast(LinkedHashSet<T> set) {
        T last = null;
        for (T item : set) {
            last = item;
        }
        return last;
    }

    private static <T> T getFirst(LinkedHashSet<T> set) {
        for (T item : set) {
            return item;
        }
        return null;
    }
}
