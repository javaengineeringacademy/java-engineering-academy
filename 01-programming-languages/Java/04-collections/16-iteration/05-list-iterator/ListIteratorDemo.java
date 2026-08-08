import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Demonstrates ListIterator bidirectional iteration patterns.
 */
public class ListIteratorDemo {

    public static void main(String[] args) {
        forwardTraversal();
        backwardTraversal();
        addDuringIteration();
        setDuringIteration();
        startingFromIndex();
    }

    static void forwardTraversal() {
        System.out.println("=== Forward Traversal ===");
        List<String> names = List.of("Alice", "Bob", "Charlie");
        ListIterator<String> it = names.listIterator();

        while (it.hasNext()) {
            int index = it.nextIndex();
            String name = it.next();
            System.out.println("Index " + index + ": " + name);
        }
        System.out.println();
    }

    static void backwardTraversal() {
        System.out.println("=== Backward Traversal ===");
        List<String> names = List.of("Alice", "Bob", "Charlie");
        ListIterator<String> it = names.listIterator(names.size());

        while (it.hasPrevious()) {
            int index = it.previousIndex();
            String name = it.previous();
            System.out.println("Index " + index + ": " + name);
        }
        System.out.println();
    }

    static void addDuringIteration() {
        System.out.println("=== Add During Iteration ===");
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        ListIterator<String> it = names.listIterator();

        while (it.hasNext()) {
            String name = it.next();
            if (name.equals("Bob")) {
                it.add("Bob Jr.");  // Inserts after Bob
            }
        }
        System.out.println("Result: " + names);
        System.out.println();
    }

    static void setDuringIteration() {
        System.out.println("=== Set During Iteration ===");
        List<String> names = new ArrayList<>(List.of("alice", "bob", "charlie"));
        ListIterator<String> it = names.listIterator();

        while (it.hasNext()) {
            String name = it.next();
            it.set(name.toUpperCase());  // Replace current
        }
        System.out.println("Result: " + names);
        System.out.println();
    }

    static void startingFromIndex() {
        System.out.println("=== Start From Index ===");
        List<String> names = List.of("A", "B", "C", "D", "E");
        ListIterator<String> it = names.listIterator(2);  // Start at index 2

        System.out.println("Next index: " + it.nextIndex());
        System.out.println("Previous index: " + it.previousIndex());

        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println();
    }
}
