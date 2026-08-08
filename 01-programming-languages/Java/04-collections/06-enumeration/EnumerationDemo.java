import java.util.*;

/**
 * Demonstrates Enumeration interface usage.
 *
 * <p>Enumeration is a legacy interface for traversing Vector and Hashtable.
 * It has been largely replaced by Iterator, but is still used in some
 * legacy code and thread-safe collections.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Legacy traversal interface (pre-Java 1.2)</li>
 *   <li>Read-only traversal — no remove operation</li>
 *   <li>Used by Vector.elements() and Hashtable.keys()</li>
 *   <li>Collections.enumeration() creates Enumeration from any collection</li>
 *   <li>Consider using Iterator instead for new code</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class EnumerationDemo {

    public static void main(String[] args) {
        demonstrateBasicEnumeration();
        demonstrateEnumerationOnVector();
        demonstrateEnumerationOnHashtable();
        demonstrateEnumerationFromCollections();
    }

    /**
     * Demonstrates basic Enumeration usage.
     */
    private static void demonstrateBasicEnumeration() {
        System.out.println("=== Basic Enumeration ===");

        Vector<String> vector = new Vector<>(List.of("Alice", "Bob", "Charlie"));

        Enumeration<String> enumElements = vector.elements();
        System.out.print("Elements: ");
        while (enumElements.hasMoreElements()) {
            System.out.print(enumElements.nextElement() + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates Enumeration on Vector.
     */
    private static void demonstrateEnumerationOnVector() {
        System.out.println("=== Enumeration on Vector ===");

        Vector<Integer> numbers = new Vector<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i * 10);
        }

        Enumeration<Integer> enumElements = numbers.elements();
        System.out.println("Enumerating Vector:");
        while (enumElements.hasMoreElements()) {
            Integer num = enumElements.nextElement();
            System.out.println("  " + num);
        }

        // No remove method — read-only traversal
        System.out.println("Note: Enumeration has no remove() method");
        System.out.println();
    }

    /**
     * Demonstrates Enumeration on Hashtable.
     */
    private static void demonstrateEnumerationOnHashtable() {
        System.out.println("=== Enumeration on Hashtable ===");

        Hashtable<String, Integer> scores = new Hashtable<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);

        // Keys enumeration
        Enumeration<String> keys = scores.keys();
        System.out.println("Keys:");
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            System.out.println("  " + key + " -> " + scores.get(key));
        }

        // Values enumeration
        Enumeration<Integer> values = scores.elements();
        System.out.print("Values: ");
        while (values.hasMoreElements()) {
            System.out.print(values.nextElement() + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates creating Enumeration from any collection.
     */
    private static void demonstrateEnumerationFromCollections() {
        System.out.println("=== Collections.enumeration() ===");

        // Create Enumeration from ArrayList
        List<String> list = new ArrayList<>(List.of("X", "Y", "Z"));
        Enumeration<String> enumFromList = Collections.enumeration(list);
        System.out.print("From ArrayList: ");
        while (enumFromList.hasMoreElements()) {
            System.out.print(enumFromList.nextElement() + " ");
        }
        System.out.println();

        // Create Enumeration from LinkedList
        LinkedList<Integer> linkedList = new LinkedList<>(List.of(1, 2, 3, 4, 5));
        Enumeration<Integer> enumFromLinkedList = Collections.enumeration(linkedList);
        System.out.print("From LinkedList: ");
        while (enumFromLinkedList.hasMoreElements()) {
            System.out.print(enumFromLinkedList.nextElement() + " ");
        }
        System.out.println();

        // Create Enumeration from HashSet
        Set<Double> set = new HashSet<>(List.of(1.1, 2.2, 3.3));
        Enumeration<Double> enumFromSet = Collections.enumeration(set);
        System.out.print("From HashSet: ");
        while (enumFromSet.hasMoreElements()) {
            System.out.print(enumFromSet.nextElement() + " ");
        }
        System.out.println();
    }
}
