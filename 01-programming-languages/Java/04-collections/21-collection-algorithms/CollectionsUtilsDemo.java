import java.util.*;
import java.util.stream.Collectors;

/**
 * Demonstrates Collections utility class operations.
 * Covers sorting, searching, shuffling, and other utility methods.
 */
public class CollectionsUtilsDemo {

    public static void main(String[] args) {
        demonstrateSorting();
        demonstrateSearching();
        demonstrateOtherUtilities();
    }

    /**
     * Demonstrates sorting operations.
     */
    private static void demonstrateSorting() {
        System.out.println("=== Sorting Operations ===");

        // Natural ordering
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        Collections.sort(numbers);
        System.out.println("Natural order: " + numbers);

        // Reverse order
        Collections.sort(numbers, Comparator.reverseOrder());
        System.out.println("Reverse order: " + numbers);

        // Custom comparator
        List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob", "Diana"));
        Collections.sort(names, Comparator.comparingInt(String::length));
        System.out.println("By length: " + names);

        // Shuffle
        Collections.shuffle(numbers);
        System.out.println("Shuffled: " + numbers);

        // Rotate
        Collections.rotate(numbers, 2);
        System.out.println("Rotated by 2: " + numbers);
        System.out.println();
    }

    /**
     * Demonstrates searching operations.
     */
    private static void demonstrateSearching() {
        System.out.println("=== Searching Operations ===");

        List<Integer> sorted = new ArrayList<>(List.of(1, 3, 5, 7, 9, 11));
        int index = Collections.binarySearch(sorted, 7);
        System.out.println("Binary search for 7: index " + index);

        int notFound = Collections.binarySearch(sorted, 6);
        System.out.println("Binary search for 6: index " + notFound + " (insertion point: " + (~notFound) + ")");

        // Frequency
        List<String> names = List.of("Alice", "Bob", "Alice", "Charlie", "Alice");
        int freq = Collections.frequency(names, "Alice");
        System.out.println("Frequency of Alice: " + freq);

        // Min/Max
        System.out.println("Min: " + Collections.min(List.of(5, 2, 8, 1)));
        System.out.println("Max: " + Collections.max(List.of(5, 2, 8, 1)));
        System.out.println();
    }

    /**
     * Demonstrates other utility operations.
     */
    private static void demonstrateOtherUtilities() {
        System.out.println("=== Other Utilities ===");

        // Unmodifiable collections
        List<String> mutable = new ArrayList<>(List.of("A", "B", "C"));
        List<String> unmodifiable = Collections.unmodifiableList(mutable);
        System.out.println("Unmodifiable: " + unmodifiable);

        // Synchronized collections
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

        // Singleton collections
        List<String> single = Collections.singletonList("Only");
        Set<String> singleSet = Collections.singleton("Only");
        Map<String, Integer> singleMap = Collections.singletonMap("key", 1);

        // Empty collections
        List<String> empty = Collections.emptyList();
        Set<String> emptySet = Collections.emptySet();
        Map<String, Integer> emptyMap = Collections.emptyMap();

        // Frequency count
        List<String> words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");
        Map<String, Long> frequency = words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        System.out.println("Frequency: " + frequency);

        // Disjoint check
        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(4, 5, 6);
        System.out.println("Disjoint: " + Collections.disjoint(list1, list2));

        // Replace all
        List<String> replaceAll = new ArrayList<>(List.of("A", "B", "C", "B"));
        Collections.replaceAll(replaceAll, "B", "X");
        System.out.println("ReplaceAll: " + replaceAll);

        // Fill
        List<String> fill = new ArrayList<>(Arrays.asList(new String[5]));
        Collections.fill(fill, "X");
        System.out.println("Fill: " + fill);

        // N copies
        List<String> nCopies = Collections.nCopies(3, "Y");
        System.out.println("NCopies: " + nCopies);
    }
}
