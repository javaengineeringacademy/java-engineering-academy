import java.util.*;

/**
 * Demonstrates sorting algorithms and strategies for collections.
 *
 * <p>Covers various sorting approaches including natural ordering,
 * custom comparators, parallel sort, and sorting primitives.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Collections.sort() and Arrays.sort()</li>
 *   <li>Timsort algorithm (hybrid merge sort + insertion sort)</li>
 *   <li>Parallel sort for large arrays</li>
 *   <li>Stable sorting with Comparator</li>
 *   <li>Sorting primitives vs objects</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class SortingDemo {

    public static void main(String[] args) {
        demonstrateCollectionsSort();
        demonstrateArraysSort();
        demonstrateParallelSort();
        demonstrateStableSort();
    }

    /**
     * Demonstrates Collections.sort() with various comparators.
     */
    private static void demonstrateCollectionsSort() {
        System.out.println("=== Collections.sort() ===");

        // Natural ordering
        List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob", "Diana"));
        Collections.sort(names);
        System.out.println("Natural: " + names);

        // Custom comparator
        names.sort(Comparator.comparingInt(String::length));
        System.out.println("By length: " + names);

        // Chained comparator
        names.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        System.out.println("By length then alpha: " + names);

        // Reverse
        names.sort(Comparator.reverseOrder());
        System.out.println("Reverse: " + names);
        System.out.println();
    }

    /**
     * Demonstrates Arrays.sort() for primitives and objects.
     */
    private static void demonstrateArraysSort() {
        System.out.println("=== Arrays.sort() ===");

        // Primitive array
        int[] numbers = {5, 2, 8, 1, 9, 3};
        Arrays.sort(numbers);
        System.out.println("Sorted primitives: " + Arrays.toString(numbers));

        // Object array
        String[] words = {"Banana", "Apple", "Cherry"};
        Arrays.sort(words);
        System.out.println("Sorted objects: " + Arrays.toString(words));

        // Partial sort
        int[] partial = {5, 2, 8, 1, 9, 3};
        Arrays.sort(partial, 1, 4); // Sort index 1 to 3
        System.out.println("Partial sort [1,4): " + Arrays.toString(partial));
        System.out.println();
    }

    /**
     * Demonstrates parallel sort for large datasets.
     */
    private static void demonstrateParallelSort() {
        System.out.println("=== Parallel Sort ===");

        int size = 1_000_000;
        int[] arr1 = new int[size];
        int[] arr2 = new int[size];
        Random random = new Random(42);

        for (int i = 0; i < size; i++) {
            arr1[i] = random.nextInt();
            arr2[i] = arr1[i];
        }

        // Sequential sort
        long start = System.nanoTime();
        Arrays.sort(arr1);
        long sequentialTime = System.nanoTime() - start;

        // Parallel sort
        start = System.nanoTime();
        Arrays.parallelSort(arr2);
        long parallelTime = System.nanoTime() - start;

        System.out.printf("Sequential: %d ms%n", sequentialTime / 1_000_000);
        System.out.printf("Parallel: %d ms%n", parallelTime / 1_000_000);
        System.out.println("Note: Parallel sort benefits large arrays (>8192 elements)");
        System.out.println();
    }

    /**
     * Demonstrates stable sorting properties.
     */
    private static void demonstrateStableSort() {
        System.out.println("=== Stable Sorting ===");

        record Item(String name, int group) {}

        List<Item> items = new ArrayList<>();
        items.add(new Item("A1", 2));
        items.add(new Item("B1", 1));
        items.add(new Item("A2", 2));
        items.add(new Item("B2", 1));
        items.add(new Item("A3", 2));

        System.out.println("Before: " + items);

        // Sort by group — stable sort preserves relative order within groups
        items.sort(Comparator.comparingInt(Item::group));
        System.out.println("After stable sort by group: " + items);
        System.out.println("Note: A1, A2, A3 maintain relative order within group 2");
    }
}
