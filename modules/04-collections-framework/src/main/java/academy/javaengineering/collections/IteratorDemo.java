package academy.javaengineering.collections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Demonstrates Iterator patterns and fail-fast vs fail-safe behavior.
 * Covers Iterator, ListIterator, and concurrent iteration patterns.
 */
public class IteratorDemo {

    public static void main(String[] args) {
        demonstrateBasicIterator();
        demonstrateListIterator();
        demonstrateFailFast();
        demonstrateFailSafe();
    }

    /**
     * Demonstrates basic Iterator usage.
     */
    private static void demonstrateBasicIterator() {
        System.out.println("=== Basic Iterator ===");

        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Diana"));

        // Forward iteration
        System.out.print("Forward: ");
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // Remove elements while iterating
        Iterator<String> removeIt = names.iterator();
        while (removeIt.hasNext()) {
            String name = removeIt.next();
            if (name.startsWith("B")) {
                removeIt.remove(); // Safe removal
            }
        }
        System.out.println("After removal: " + names);

        // Enhanced for loop (uses Iterator internally)
        System.out.print("Enhanced for: ");
        for (String name : names) {
            System.out.print(name + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates ListIterator for bidirectional traversal.
     */
    private static void demonstrateListIterator() {
        System.out.println("=== ListIterator ===");

        LinkedList<Integer> numbers = new LinkedList<>(List.of(1, 2, 3, 4, 5));

        // Forward traversal
        System.out.print("Forward: ");
        ListIterator<Integer> forward = numbers.listIterator();
        while (forward.hasNext()) {
            System.out.print(forward.next() + " ");
        }
        System.out.println();

        // Backward traversal
        System.out.print("Backward: ");
        ListIterator<Integer> backward = numbers.listIterator(numbers.size());
        while (backward.hasPrevious()) {
            System.out.print(backward.previous() + " ");
        }
        System.out.println();

        // Replace elements
        ListIterator<Integer> replaceIt = numbers.listIterator();
        while (replaceIt.hasNext()) {
            int num = replaceIt.next();
            replaceIt.set(num * 10);
        }
        System.out.println("Doubled: " + numbers);

        // Add elements
        ListIterator<Integer> addIt = numbers.listIterator();
        while (addIt.hasNext()) {
            int num = addIt.next();
            if (num == 30) {
                addIt.add(25); // Add before 30
            }
        }
        System.out.println("After adding: " + numbers);
        System.out.println();
    }

    /**
     * Demonstrates fail-fast iterator behavior.
     */
    private static void demonstrateFailFast() {
        System.out.println("=== Fail-Fast Behavior ===");

        List<String> list = new ArrayList<>(List.of("A", "B", "C"));

        try {
            // Bad - ConcurrentModificationException
            for (String s : list) {
                if (s.equals("B")) {
                    list.remove(s); // Modifies structure!
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Caught ConcurrentModificationException: " + e.getMessage());
        }

        // Good - use Iterator.remove()
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals("B")) {
                it.remove(); // Safe
            }
        }
        System.out.println("After safe removal: " + list);
        System.out.println();
    }

    /**
     * Demonstrates fail-safe iterator with ConcurrentHashMap.
     */
    private static void demonstrateFailSafe() {
        System.out.println("=== Fail-Safe Behavior ===");

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        // Fail-safe iterator (weakly consistent)
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Processing: " + entry.getKey());
            if (entry.getKey().equals("B")) {
                map.put("D", 4); // Modification during iteration
            }
        }

        System.out.println("Final map: " + map);
        System.out.println("Note: No ConcurrentModificationException!");
    }
}
