import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Demonstrates Iterator interface usage patterns.
 */
public class IteratorDemo {

    public static void main(String[] args) {
        basicIterator();
        safeRemoval();
        forEachRemaining();
        failFastDemo();
    }

    static void basicIterator() {
        System.out.println("=== Basic Iterator ===");
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Iterator<String> it = names.iterator();

        while (it.hasNext()) {
            String name = it.next();
            System.out.println("Name: " + name);
        }
        System.out.println();
    }

    static void safeRemoval() {
        System.out.println("=== Safe Removal with Iterator ===");
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana"));
        Iterator<String> it = names.iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (name.length() <= 3) {
                System.out.println("Removing: " + name);
                it.remove();  // Safe removal
            }
        }
        System.out.println("Remaining: " + names);
        System.out.println();
    }

    static void forEachRemaining() {
        System.out.println("=== forEachRemaining ===");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        Iterator<Integer> it = numbers.iterator();

        // Process first element
        if (it.hasNext()) {
            System.out.println("First: " + it.next());
        }

        // Process all remaining
        it.forEachRemaining(num -> System.out.println("Rest: " + num));
        System.out.println();
    }

    static void failFastDemo() {
        System.out.println("=== Fail-Fast Demo ===");
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));

        try {
            Iterator<String> it = names.iterator();
            while (it.hasNext()) {
                String name = it.next();
                if (name.equals("Bob")) {
                    names.add("Dave");  // Structural modification!
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            System.out.println("Caught ConcurrentModificationException: " + e.getMessage());
        }
        System.out.println();
    }
}
