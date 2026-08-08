import java.util.Vector;

/**
 * Demonstrates Vector operations and thread-safe list behavior.
 *
 * <p>Vector is a synchronized (thread-safe) implementation of the List interface.
 * It uses a dynamic array internally, similar to ArrayList, but all methods
 * are synchronized.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Synchronized (thread-safe) list operations</li>
 *   <li>Dynamic array with growth factor</li>
 *   <li>Legacy class — prefer ArrayList or CopyOnWriteArrayList</li>
 *   <li>Performance overhead from synchronization</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class VectorDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateSynchronization();
        demonstrateCapacityManagement();
        demonstrateIteration();
    }

    /**
     * Demonstrates basic Vector operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== Vector Basic Operations ===");

        Vector<String> vector = new Vector<>();
        vector.add("Alice");
        vector.add("Bob");
        vector.add("Charlie");
        vector.add(0, "Diana");
        vector.add("Eve");

        System.out.println("Vector: " + vector);
        System.out.println("Size: " + vector.size());
        System.out.println("First: " + vector.firstElement());
        System.out.println("Last: " + vector.lastElement());

        // Access by index
        System.out.println("Element at 2: " + vector.elementAt(2));

        // Remove
        vector.remove("Diana");
        vector.remove(0);
        System.out.println("After removals: " + vector);

        // Search
        System.out.println("Contains Bob: " + vector.contains("Bob"));
        System.out.println("Index of Charlie: " + vector.indexOf("Charlie"));

        // Capacity
        System.out.println("Capacity: " + vector.capacity());
        System.out.println();
    }

    /**
     * Demonstrates thread-safe operations with Vector.
     */
    private static void demonstrateSynchronization() {
        System.out.println("=== Synchronization ===");

        Vector<Integer> vector = new Vector<>();
        Thread[] threads = new Thread[5];

        // Multiple threads adding elements concurrently
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    vector.add(threadId * 100 + j);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Total elements after concurrent adds: " + vector.size());
        System.out.println("Expected: " + (5 * 100));
        System.out.println();
    }

    /**
     * Demonstrates capacity management in Vector.
     */
    private static void demonstrateCapacityManagement() {
        System.out.println("=== Capacity Management ===");

        Vector<Integer> defaultCap = new Vector<>();
        Vector<Integer> customCap = new Vector<>(50);

        System.out.println("Default initial capacity: " + defaultCap.capacity());
        System.out.println("Custom initial capacity: " + customCap.capacity());

        // Adding elements triggers capacity growth
        for (int i = 0; i < 20; i++) {
            defaultCap.add(i);
        }
        System.out.println("After adding 20 elements:");
        System.out.println("  Size: " + defaultCap.size());
        System.out.println("  Capacity: " + defaultCap.capacity());

        // Ensure capacity
        customCap.ensureCapacity(100);
        System.out.println("After ensureCapacity(100): " + customCap.capacity());

        // Trim to size
        defaultCap.trimToSize();
        System.out.println("After trimToSize: " + defaultCap.capacity());
        System.out.println();
    }

    /**
     * Demonstrates iteration over Vector.
     */
    private static void demonstrateIteration() {
        System.out.println("=== Iteration ===");

        Vector<String> vector = new Vector<>(List.of("Alpha", "Beta", "Gamma", "Delta"));

        // Classic for loop
        System.out.print("For loop: ");
        for (int i = 0; i < vector.size(); i++) {
            System.out.print(vector.get(i) + " ");
        }
        System.out.println();

        // Enhanced for loop
        System.out.print("Enhanced for: ");
        for (String s : vector) {
            System.out.print(s + " ");
        }
        System.out.println();

        // Iterator
        System.out.print("Iterator: ");
        java.util.Iterator<String> it = vector.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // Enumeration (legacy)
        System.out.print("Enumeration: ");
        java.util.Enumeration<String> enumElements = vector.elements();
        while (enumElements.hasMoreElements()) {
            System.out.print(enumElements.nextElement() + " ");
        }
        System.out.println();
    }
}
