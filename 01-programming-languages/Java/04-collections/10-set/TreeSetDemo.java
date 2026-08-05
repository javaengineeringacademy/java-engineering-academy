import java.util.TreeSet;
import java.util.Comparator;
import java.util.Set;

/**
 * Demonstrates TreeSet operations for sorted unique element storage.
 * TreeSet provides O(log n) operations and guarantees sorted element order.
 */
public class TreeSetDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateSortedOperations();
        demonstrateNavigationMethods();
    }

    /**
     * Demonstrates basic TreeSet operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== TreeSet Basic Operations ===");

        // Create with natural ordering
        TreeSet<Integer> numbers = new TreeSet<>();
        numbers.add(5);
        numbers.add(2);
        numbers.add(8);
        numbers.add(1);
        numbers.add(5); // Duplicate ignored

        System.out.println("Set (sorted): " + numbers);
        System.out.println("First: " + numbers.first());
        System.out.println("Last: " + numbers.last());
        System.out.println("Size: " + numbers.size());
        System.out.println();
    }

    /**
     * Demonstrates sorted set operations.
     */
    private static void demonstrateSortedOperations() {
        System.out.println("=== Sorted Operations ===");

        TreeSet<String> words = new TreeSet<>();
        words.add("Banana");
        words.add("Apple");
        words.add("Cherry");
        words.add("Date");

        System.out.println("Sorted words: " + words);

        // Head set (elements less than)
        System.out.println("Head (<Cherry): " + words.headSet("Cherry"));

        // Tail set (elements greater than or equal to)
        System.out.println("Tail (>=Banana): " + words.tailSet("Banana"));

        // Sub set (range)
        System.out.println("Sub (Apple,Date): " + words.subSet("Apple", "Date"));

        // Custom comparator (by length)
        TreeSet<String> byLength = new TreeSet<>(
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder())
        );
        byLength.addAll(words);
        System.out.println("Sorted by length: " + byLength);
        System.out.println();
    }

    /**
     * Demonstrates navigation methods.
     */
    private static void demonstrateNavigationMethods() {
        System.out.println("=== Navigation Methods ===");

        TreeSet<Integer> set = new TreeSet<>(Set.of(1, 3, 5, 7, 9));

        // Floor: greatest element <= given element
        System.out.println("Floor of 4: " + set.floor(4));

        // Ceiling: smallest element >= given element
        System.out.println("Ceiling of 4: " + set.ceiling(4));

        // Lower: greatest element < given element
        System.out.println("Lower of 5: " + set.lower(5));

        // Higher: smallest element > given element
        System.out.println("Higher of 5: " + set.higher(5));

        // Descending set
        System.out.print("Descending: ");
        for (Integer num : set.descendingSet()) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
