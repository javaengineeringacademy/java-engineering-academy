package academy.javaengineering.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Demonstrates ArrayList operations and performance characteristics.
 * ArrayList uses a dynamic array internally for O(1) random access.
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateAdvancedOperations();
        demonstratePerformanceComparison();
        demonstrateCapacityOptimization();
    }

    /**
     * Demonstrates basic ArrayList operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== ArrayList Basic Operations ===");

        // Creation
        List<String> names = new ArrayList<>();
        List<Integer> withCapacity = new ArrayList<>(100);
        List<String> fromList = new ArrayList<>(List.of("A", "B", "C"));

        // Adding elements
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        names.add(0, "Diana"); // Insert at index
        names.add("Eve");

        System.out.println("List: " + names);
        System.out.println("Size: " + names.size());

        // Accessing elements
        System.out.println("First: " + names.get(0));
        System.out.println("Last: " + names.get(names.size() - 1));
        System.out.println("Index of Charlie: " + names.indexOf("Charlie"));

        // Removing elements
        names.remove("Diana");
        names.remove(0);
        System.out.println("After removals: " + names);

        // Searching
        System.out.println("Contains Bob: " + names.contains("Bob"));
        System.out.println("Index of Eve: " + names.indexOf("Eve"));

        // Sorting
        names.sort(String::compareToIgnoreCase);
        System.out.println("Sorted: " + names);

        // SubList (view, not copy)
        List<String> sub = names.subList(0, 2);
        System.out.println("SubList: " + sub);
        System.out.println();
    }

    /**
     * Demonstrates advanced ArrayList operations.
     */
    private static void demonstrateAdvancedOperations() {
        System.out.println("=== Advanced Operations ===");

        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3, 7, 4, 6));

        // Find second largest
        Optional<Integer> secondLargest = numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();
        System.out.println("Second largest: " + secondLargest.orElse(null));

        // Remove duplicates while maintaining order
        List<Integer> unique = new ArrayList<>();
        for (Integer num : numbers) {
            if (!unique.contains(num)) {
                unique.add(num);
            }
        }
        System.out.println("Unique (preserved order): " + unique);

        // Chunk list
        List<List<Integer>> chunks = chunk(numbers, 3);
        System.out.println("Chunks: " + chunks);

        // Interleave two lists
        List<String> list1 = new ArrayList<>(List.of("A", "B", "C"));
        List<String> list2 = new ArrayList<>(List.of("1", "2", "3"));
        List<String> interleaved = interleave(list1, list2);
        System.out.println("Interleaved: " + interleaved);
        System.out.println();
    }

    /**
     * Demonstrates ArrayList vs LinkedList performance.
     */
    private static void demonstratePerformanceComparison() {
        System.out.println("=== Performance Comparison ===");

        int size = 100_000;

        // ArrayList
        List<Integer> arrayList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }
        long addTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.get(i);
        }
        long getTime = System.nanoTime() - start;

        System.out.printf("ArrayList: add=%d ms, get=%d ms%n",
                addTime / 1_000_000, getTime / 1_000_000);

        // LinkedList
        List<Integer> linkedList = new java.util.LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.add(i);
        }
        addTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.get(i);
        }
        getTime = System.nanoTime() - start;

        System.out.printf("LinkedList: add=%d ms, get=%d ms%n",
                addTime / 1_000_000, getTime / 1_000_000);
        System.out.println();
    }

    /**
     * Demonstrates capacity optimization.
     */
    private static void demonstrateCapacityOptimization() {
        System.out.println("=== Capacity Optimization ===");

        // Without initial capacity (multiple resizes)
        List<String> badList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            badList.add("item" + i);
        }

        // With initial capacity (single allocation)
        List<String> goodList = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            goodList.add("item" + i);
        }

        System.out.println("Bad list size: " + badList.size());
        System.out.println("Good list size: " + goodList.size());
        System.out.println("Both have same elements: " + badList.equals(goodList));
    }

    /**
     * Splits a list into chunks of specified size.
     */
    private static <T> List<List<T>> chunk(List<T> list, int size) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            chunks.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
        }
        return chunks;
    }

    /**
     * Interleaves two lists.
     */
    private static <T> List<T> interleave(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>(a.size() + b.size());
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            result.add(a.get(i++));
            result.add(b.get(j++));
        }
        while (i < a.size()) {
            result.add(a.get(i++));
        }
        while (j < b.size()) {
            result.add(b.get(j++));
        }
        return result;
    }
}
