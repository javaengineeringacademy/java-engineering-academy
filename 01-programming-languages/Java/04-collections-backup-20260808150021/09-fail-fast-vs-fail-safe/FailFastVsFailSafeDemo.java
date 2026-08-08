import java.util.*;
import java.util.concurrent.*;

/**
 * Demonstrates fail-fast vs fail-safe iterator behavior.
 *
 * <p><b>Fail-fast</b> iterators throw ConcurrentModificationException if the
 * collection is modified structurally during iteration (e.g., ArrayList, HashSet).</p>
 *
 * <p><b>Fail-safe</b> iterators operate on a snapshot or copy, so they never throw
 * ConcurrentModificationException (e.g., CopyOnWriteArrayList, ConcurrentHashMap).</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Fail-fast: ArrayList, HashMap, HashSet — detect concurrent modification</li>
 *   <li>Fail-safe: CopyOnWriteArrayList, ConcurrentHashMap — work on snapshot</li>
 *   <li>ConcurrentModificationException for fail-fast</li>
 *   <li>Weakly consistent iteration for fail-safe</li>
 *   <li>modCount tracking mechanism</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class FailFastVsFailSafeDemo {

    public static void main(String[] args) {
        demonstrateFailFast();
        demonstrateFailSafe();
        demonstrateModCountMechanism();
        demonstrateBestPractices();
    }

    /**
     * Demonstrates fail-fast behavior with ArrayList.
     */
    private static void demonstrateFailFast() {
        System.out.println("=== Fail-Fast Behavior ===");

        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

        // Bad: Modified during iteration
        try {
            for (String s : list) {
                if (s.equals("C")) {
                    list.remove(s); // ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
        }

        // Good: Use Iterator.remove()
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals("B")) {
                it.remove(); // Safe removal
            }
        }
        System.out.println("After safe removal: " + list);
        System.out.println();
    }

    /**
     * Demonstrates fail-safe behavior with CopyOnWriteArrayList.
     */
    private static void demonstrateFailSafe() {
        System.out.println("=== Fail-Safe Behavior ===");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));

        // Safe: Modified during iteration
        for (String s : list) {
            System.out.println("Processing: " + s);
            if (s.equals("B")) {
                list.add("D"); // No exception — iterator uses snapshot
            }
        }

        System.out.println("Final list: " + list);
        System.out.println("Note: No ConcurrentModificationException!");
        System.out.println();
    }

    /**
     * Demonstrates the modCount mechanism.
     */
    private static void demonstrateModCountMechanism() {
        System.out.println("=== modCount Mechanism ===");

        // ArrayList tracks modCount
        ArrayList<String> arrayList = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("ArrayList modCount is tracked internally");
        System.out.println("Any structural modification increments modCount");
        System.out.println("Iterator checks modCount at each next() call");

        // ConcurrentHashMap does NOT track modCount
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.put("B", 2);

        System.out.println("ConcurrentHashMap uses lock-free algorithms");
        System.out.println("No modCount — weakly consistent iteration");
        System.out.println();
    }

    /**
     * Demonstrates best practices for each scenario.
     */
    private static void demonstrateBestPractices() {
        System.out.println("=== Best Practices ===");

        System.out.println("Fail-fast best practices:");
        System.out.println("  1. Use Iterator.remove() instead of Collection.remove()");
        System.out.println("  2. Copy list before modifying: new ArrayList<>(original)");
        System.out.println("  3. Use removeIf() method (Java 8+)");

        System.out.println("\nFail-safe best practices:");
        System.out.println("  1. Use CopyOnWriteArrayList for read-heavy scenarios");
        System.out.println("  2. Use ConcurrentHashMap for concurrent access");
        System.out.println("  3. Be aware of eventual consistency (weakly consistent)");

        // Demonstrate removeIf
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        numbers.removeIf(n -> n % 2 == 0);
        System.out.println("\nremoveIf result: " + numbers);
    }
}
