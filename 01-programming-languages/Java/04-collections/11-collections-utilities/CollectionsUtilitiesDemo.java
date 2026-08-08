import java.util.*;
import java.util.stream.*;

/**
 * Demonstrates Collections utility class methods.
 *
 * <p>The Collections class provides static utility methods for operating
 * on collections, including sorting, searching, shuffling, and creating
 * unmodifiable or synchronized collections.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Unmodifiable collections (immutable wrappers)</li>
 *   <li>Synchronized collections (thread-safe wrappers)</li>
 *   <li>Checked collections (type-safe wrappers)</li>
 *   <li>Frequency, disjoint, and disjoint operations</li>
 *   <li>Binary search and sorting utilities</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class CollectionsUtilitiesDemo {

    public static void main(String[] args) {
        demonstrateUnmodifiableCollections();
        demonstrateSynchronizedCollections();
        demonstrateSearchAndSort();
        demonstrateFrequencyAndDisjoint();
        demonstrateMinMaxAndRotate();
    }

    /**
     * Demonstrates unmodifiable (immutable) collection wrappers.
     */
    private static void demonstrateUnmodifiableCollections() {
        System.out.println("=== Unmodifiable Collections ===");

        List<String> mutable = new ArrayList<>(List.of("A", "B", "C"));
        List<String> immutable = Collections.unmodifiableList(mutable);

        System.out.println("Immutable list: " + immutable);

        // Attempting to modify throws UnsupportedOperationException
        try {
            immutable.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify: " + e.getClass().getSimpleName());
        }

        // Original list is still mutable
        mutable.add("D");
        System.out.println("Original (mutable): " + mutable);
        System.out.println("Immutable sees change: " + immutable);
        System.out.println();
    }

    /**
     * Demonstrates synchronized collection wrappers.
     */
    private static void demonstrateSynchronizedCollections() {
        System.out.println("=== Synchronized Collections ===");

        List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        Map<String, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        Set<Integer> synchronizedSet = Collections.synchronizedSet(new HashSet<>());

        // Safe for single operations
        synchronizedList.add("A");
        synchronizedMap.put("key", 1);
        synchronizedSet.add(42);

        // Must synchronize for iteration
        synchronized (synchronizedList) {
            for (String s : synchronizedList) {
                System.out.println("  " + s);
            }
        }
        System.out.println();
    }

    /**
     * Demonstrates search and sort utilities.
     */
    private static void demonstrateSearchAndSort() {
        System.out.println("=== Search and Sort ===");

        List<Integer> list = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));

        // Sort
        Collections.sort(list);
        System.out.println("Sorted: " + list);

        // Binary search (must be sorted)
        int index = Collections.binarySearch(list, 8);
        System.out.println("Binary search for 8: index=" + index);

        // Reverse
        Collections.reverse(list);
        System.out.println("Reversed: " + list);

        // Shuffle
        Collections.shuffle(list);
        System.out.println("Shuffled: " + list);

        // Fill
        List<String> filled = new ArrayList<>(Arrays.asList(new String[5]));
        Collections.fill(filled, "X");
        System.out.println("Filled: " + filled);

        // NCopies
        List<String> copies = Collections.nCopies(3, "Hello");
        System.out.println("NCopies: " + copies);
        System.out.println();
    }

    /**
     * Demonstrates frequency and disjoint operations.
     */
    private static void demonstrateFrequencyAndDisjoint() {
        System.out.println("=== Frequency and Disjoint ===");

        List<String> list1 = new ArrayList<>(List.of("A", "B", "C", "A", "A"));
        List<String> list2 = new ArrayList<>(List.of("C", "D", "E"));

        // Frequency
        int freq = Collections.frequency(list1, "A");
        System.out.println("Frequency of 'A': " + freq);

        // Disjoint (no common elements)
        boolean disjoint = Collections.disjoint(list1, list2);
        System.out.println("Disjoint: " + disjoint);

        List<String> list3 = new ArrayList<>(List.of("X", "Y", "Z"));
        System.out.println("Disjoint (list1, list3): " + Collections.disjoint(list1, list3));
        System.out.println();
    }

    /**
     * Demonstrates min, max, and rotate operations.
     */
    private static void demonstrateMinMaxAndRotate() {
        System.out.println("=== Min, Max, and Rotate ===");

        List<Integer> list = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9, 2, 6));

        // Min and Max
        System.out.println("Min: " + Collections.min(list));
        System.out.println("Max: " + Collections.max(list));

        // Swap
        Collections.swap(list, 0, 1);
        System.out.println("After swap(0,1): " + list);

        // Rotate
        Collections.rotate(list, 2);
        System.out.println("After rotate(2): " + list);

        // Replace all
        Collections.replaceAll(list, 1, 99);
        System.out.println("After replaceAll(1,99): " + list);

        // Index of subList
        List<String> main = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        List<String> sub = List.of("C", "D");
        int indexOfSub = Collections.indexOfSubList(main, sub);
        System.out.println("indexOfSubList: " + indexOfSub);
    }
}
