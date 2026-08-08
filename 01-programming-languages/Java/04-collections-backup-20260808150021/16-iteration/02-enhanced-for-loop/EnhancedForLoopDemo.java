import java.util.*;

/**
 * Demonstrates enhanced for loop iteration patterns.
 */
public class EnhancedForLoopDemo {

    public static void main(String[] args) {
        iterateArrays();
        iterateCollections();
        iterateSets();
        iterateMaps();
        iterableView();
    }

    static void iterateArrays() {
        System.out.println("=== Array Iteration ===");
        int[] numbers = {10, 20, 30, 40, 50};

        for (int num : numbers) {
            System.out.println("Value: " + num);
        }
        System.out.println();
    }

    static void iterateCollections() {
        System.out.println("=== Collection Iteration ===");
        List<String> names = List.of("Alice", "Bob", "Charlie");

        for (String name : names) {
            System.out.println("Name: " + name);
        }
        System.out.println();
    }

    static void iterateSets() {
        System.out.println("=== Set Iteration ===");
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

        for (int num : numbers) {
            System.out.println("Number: " + num);
        }
        System.out.println();
    }

    static void iterateMaps() {
        System.out.println("=== Map Iteration ===");
        Map<String, Integer> ages = Map.of("Alice", 30, "Bob", 25, "Charlie", 35);

        System.out.println("Entries:");
        for (Map.Entry<String, Integer> entry : ages.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        System.out.println("Keys only:");
        for (String key : ages.keySet()) {
            System.out.println("  " + key);
        }

        System.out.println("Values only:");
        for (Integer value : ages.values()) {
            System.out.println("  " + value);
        }
        System.out.println();
    }

    static void iterableView() {
        System.out.println("=== SubList View ===");
        List<String> all = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        List<String> sub = all.subList(1, 4);  // [B, C, D]

        for (String s : sub) {
            System.out.println("Sub: " + s);
        }

        // Modifying original affects subList view
        all.add(1, "X");
        System.out.println("After insert, sub: " + sub);
        System.out.println();
    }
}
