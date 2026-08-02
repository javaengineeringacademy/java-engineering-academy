package academy.javaengineering.collections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Demonstrates List interface operations and implementations.
 * Covers ArrayList, LinkedList, and List operations.
 */
public class ListDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateArrayListVsLinkedList();
        demonstrateAdvancedPatterns();
    }

    /**
     * Demonstrates basic List operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== Basic List Operations ===");

        // Create and populate
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        names.add("Diana");

        System.out.println("List: " + names);
        System.out.println("Size: " + names.size());
        System.out.println("First: " + names.get(0));
        System.out.println("Last: " + names.get(names.size() - 1));

        // Search
        System.out.println("Contains Bob: " + names.contains("Bob"));
        System.out.println("Index of Charlie: " + names.indexOf("Charlie"));

        // Remove
        names.remove("Diana");
        names.remove(0);
        System.out.println("After removal: " + names);

        // Add at index
        names.add(0, "Eve");
        System.out.println("After add: " + names);

        // Sort
        names.sort(String::compareToIgnoreCase);
        System.out.println("Sorted: " + names);
        System.out.println();
    }

    /**
     * Demonstrates ArrayList vs LinkedList performance.
     */
    private static void demonstrateArrayListVsLinkedList() {
        System.out.println("=== ArrayList vs LinkedList ===");

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
        List<Integer> linkedList = new LinkedList<>();
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
     * Demonstrates advanced List patterns.
     */
    private static void demonstrateAdvancedPatterns() {
        System.out.println("=== Advanced Patterns ===");

        // Remove duplicates while preserving order
        List<Integer> withDuplicates = List.of(1, 2, 3, 1, 2, 4, 5, 3);
        List<Integer> withoutDuplicates = new ArrayList<>();
        for (Integer num : withDuplicates) {
            if (!withoutDuplicates.contains(num)) {
                withoutDuplicates.add(num);
            }
        }
        System.out.println("Without duplicates: " + withoutDuplicates);

        // Rotate list
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.rotate(numbers, 2);
        System.out.println("Rotated: " + numbers);

        // Chunk list
        List<List<Integer>> chunks = chunk(List.of(1, 2, 3, 4, 5, 6, 7), 3);
        System.out.println("Chunks: " + chunks);

        // Interleave lists
        List<String> list1 = List.of("A", "B", "C");
        List<String> list2 = List.of("1", "2", "3");
        List<String> interleaved = interleave(list1, list2);
        System.out.println("Interleaved: " + interleaved);

        // SubList view
        List<String> original = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        List<String> view = original.subList(1, 4);
        System.out.println("Original: " + original);
        System.out.println("View: " + view);
        view.set(0, "X");
        System.out.println("After modifying view: " + original);
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
        while (i < a.size()) result.add(a.get(i++));
        while (j < b.size()) result.add(b.get(j++));
        return result;
    }
}
