import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Demonstrates CopyOnWriteArrayList for thread-safe iteration.
 *
 * <p>CopyOnWriteArrayList creates a new copy of the underlying array on
 * every mutation (add, set, remove). This makes iteration thread-safe
 * without explicit synchronization — iterators operate on a snapshot.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Thread-safe iteration without ConcurrentModificationException</li>
 *   <li>Copy-on-write semantics for each mutation</li>
 *   <li>Best for read-heavy, write-rare scenarios</li>
 *   <li>Iterator never throws ConcurrentModificationException</li>
 *   <li>Write operations are expensive (full array copy)</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateThreadSafeIteration();
        demonstrateSnapshotIteration();
        demonstratePerformanceCharacteristics();
    }

    /**
     * Demonstrates basic CopyOnWriteArrayList operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== CopyOnWriteArrayList Basic Operations ===");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Alice");
        list.add("Bob");
        list.add("Charlie");

        System.out.println("List: " + list);
        System.out.println("Size: " + list.size());

        // Add during iteration — safe, iterator sees old snapshot
        Iterator<String> it = list.iterator();
        list.add("Diana"); // Added after iterator creation
        System.out.print("Iterator sees: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();
        System.out.println("Actual list: " + list);
        System.out.println();
    }

    /**
     * Demonstrates thread-safe iteration with concurrent modifications.
     */
    private static void demonstrateThreadSafeIteration() {
        System.out.println("=== Thread-Safe Iteration ===");

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        // Reader thread
        Thread reader = new Thread(() -> {
            System.out.println("Reader: Starting iteration...");
            for (Integer num : list) {
                System.out.println("Reader: Processing " + num);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Reader: Done.");
        });

        // Writer thread
        Thread writer = new Thread(() -> {
            for (int i = 10; i < 13; i++) {
                list.add(i);
                System.out.println("Writer: Added " + i);
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        reader.start();
        writer.start();

        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final list: " + list);
        System.out.println();
    }

    /**
     * Demonstrates iterator snapshot behavior.
     */
    private static void demonstrateSnapshotIteration() {
        System.out.println("=== Snapshot Iteration ===");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        // Get iterator — snapshot taken here
        Iterator<String> snapshot = list.iterator();

        // Modify list
        list.add("D");
        list.remove("B");

        // Iterator still sees original snapshot
        System.out.print("Snapshot iterator: ");
        while (snapshot.hasNext()) {
            System.out.print(snapshot.next() + " ");
        }
        System.out.println();

        // New iterator sees updated list
        System.out.print("New iterator: ");
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates performance characteristics.
     */
    private static void demonstratePerformanceCharacteristics() {
        System.out.println("=== Performance Characteristics ===");

        int size = 10_000;

        // Write performance
        CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            cowList.add(i);
        }
        long cowWriteTime = System.nanoTime() - start;

        // Read performance
        start = System.nanoTime();
        for (int i = 0; i < cowList.size(); i++) {
            cowList.get(i);
        }
        long cowReadTime = System.nanoTime() - start;

        System.out.printf("CopyOnWriteArrayList: write=%d ms, read=%d ms%n",
                cowWriteTime / 1_000_000, cowReadTime / 1_000_000);
        System.out.println("Best for: read-heavy, write-rare scenarios");
        System.out.println("Avoid: write-heavy scenarios (expensive copies)");
    }
}
