package academy.javaengineering.collections;

import java.util.TreeMap;
import java.util.Comparator;
import java.util.Map;

/**
 * Demonstrates TreeMap operations for sorted key-value storage.
 * TreeMap provides O(log n) operations and guarantees sorted key order.
 */
public class TreeMapDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateSortedOperations();
        demonstrateNavigationMethods();
        demonstrateAdvancedPatterns();
    }

    /**
     * Demonstrates basic TreeMap operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== TreeMap Basic Operations ===");

        // Create with natural ordering
        TreeMap<String, Integer> ages = new TreeMap<>();
        ages.put("Charlie", 35);
        ages.put("Alice", 30);
        ages.put("Bob", 25);
        ages.put("Diana", 40);

        System.out.println("Map (sorted): " + ages);
        System.out.println("First key: " + ages.firstKey());
        System.out.println("Last key: " + ages.lastKey());
        System.out.println("First entry: " + ages.firstEntry());
        System.out.println("Last entry: " + ages.lastEntry());
        System.out.println();
    }

    /**
     * Demonstrates sorted map operations.
     */
    private static void demonstrateSortedOperations() {
        System.out.println("=== Sorted Operations ===");

        TreeMap<String, Integer> scores = new TreeMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        scores.put("Diana", 88);
        scores.put("Eve", 90);

        // Head map (keys less than)
        System.out.println("Head (before C): " + scores.headMap("C"));

        // Tail map (keys greater than or equal to)
        System.out.println("Tail (from C): " + scores.tailMap("C"));

        // Sub map (range)
        System.out.println("Sub (B to D): " + scores.subMap("B", "D"));

        // Custom comparator (reverse order)
        TreeMap<String, Integer> reverseScores = new TreeMap<>(Comparator.reverseOrder());
        reverseScores.putAll(scores);
        System.out.println("Reverse order: " + reverseScores);
        System.out.println();
    }

    /**
     * Demonstrates navigation methods.
     */
    private static void demonstrateNavigationMethods() {
        System.out.println("=== Navigation Methods ===");

        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(1, "One");
        map.put(3, "Three");
        map.put(5, "Five");
        map.put(7, "Seven");
        map.put(9, "Nine");

        // Floor: greatest key <= given key
        System.out.println("Floor of 4: " + map.floorEntry(4));

        // Ceiling: smallest key >= given key
        System.out.println("Ceiling of 4: " + map.ceilingEntry(4));

        // Lower: greatest key < given key
        System.out.println("Lower of 5: " + map.lowerEntry(5));

        // Higher: smallest key > given key
        System.out.println("Higher of 5: " + map.higherEntry(5));

        // Descending map
        System.out.print("Descending: ");
        for (Map.Entry<Integer, String> entry : map.descendingMap().entrySet()) {
            System.out.print(entry.getKey() + "=" + entry.getValue() + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates advanced TreeMap patterns.
     */
    private static void demonstrateAdvancedPatterns() {
        System.out.println("=== Advanced Patterns ===");

        // Pattern 1: Leaderboard
        System.out.println("Leaderboard:");
        TreeMap<String, Integer> leaderboard = new TreeMap<>();
        leaderboard.put("Alice", 1500);
        leaderboard.put("Bob", 1200);
        leaderboard.put("Charlie", 1800);
        leaderboard.put("Diana", 1400);

        int rank = 1;
        for (Map.Entry<String, Integer> entry : leaderboard.descendingMap().entrySet()) {
            System.out.printf("  #%d %s: %d points%n", rank++, entry.getKey(), entry.getValue());
        }

        // Pattern 2: Price ranges
        System.out.println("\nPrice Categories:");
        TreeMap<Double, String> priceCategories = new TreeMap<>();
        priceCategories.put(0.0, "Budget");
        priceCategories.put(50.0, "Mid-range");
        priceCategories.put(100.0, "Premium");
        priceCategories.put(500.0, "Luxury");

        double[] prices = {25.0, 75.0, 150.0, 600.0};
        for (double price : prices) {
            String category = priceCategories.floorEntry(price).getValue();
            System.out.printf("  $%.2f → %s%n", price, category);
        }

        // Pattern 3: Running statistics
        System.out.println("\nRunning Statistics:");
        TreeMap<Integer, Integer> data = new TreeMap<>();
        data.put(1, 10);
        data.put(2, 20);
        data.put(3, 15);
        data.put(4, 25);
        data.put(5, 30);

        System.out.println("  Min key: " + data.firstKey());
        System.out.println("  Max key: " + data.lastKey());
        System.out.println("  SubMap [2,4]: " + data.subMap(2, true, 4, true));
        System.out.println("  HeadMap (<3): " + data.headMap(3));
        System.out.println("  TailMap (>=3): " + data.tailMap(3));
    }
}
