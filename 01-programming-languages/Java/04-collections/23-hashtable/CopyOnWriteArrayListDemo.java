import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Demonstrates CopyOnWriteArrayList for thread-safe read-heavy scenarios.
 * CopyOnWriteArrayList creates a fresh copy of the underlying array on each mutation.
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) throws InterruptedException {
        demonstrateBasicOperations();
        demonstrateThreadSafety();
        demonstrateIterationBehavior();
        demonstrateUseCases();
    }

    private static void demonstrateBasicOperations() {
        System.out.println("=== CopyOnWriteArrayList Basic Operations ===");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Alice");
        list.add("Bob");
        list.add("Charlie");

        System.out.println("List: " + list);
        System.out.println("Size: " + list.size());
        System.out.println("Get(0): " + list.get(0));

        list.set(0, "Anna");
        System.out.println("After set(0, Anna): " + list);

        list.remove("Bob");
        System.out.println("After removing Bob: " + list);
        System.out.println();
    }

    private static void demonstrateThreadSafety() throws InterruptedException {
        System.out.println("=== Thread Safety ===");

        CopyOnWriteArrayList<Integer> sharedList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 5; i++) {
            sharedList.add(i);
        }

        Thread writer = new Thread(() -> {
            for (int i = 10; i < 15; i++) {
                sharedList.add(i);
                System.out.println("Writer added: " + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Reader sees: " + sharedList);
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        System.out.println("Final list: " + sharedList);
        System.out.println();
    }

    private static void demonstrateIterationBehavior() {
        System.out.println("=== Iterator Behavior (Snapshot) ===");

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        System.out.println("Original: " + list);

        // Iterator works on snapshot
        Iterator<String> it = list.iterator();
        list.add("D"); // Modify during iteration

        System.out.print("Iterator sees: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();
        System.out.println("List after modification: " + list);
        System.out.println();
    }

    private static void demonstrateUseCases() {
        System.out.println("=== Use Cases ===");

        // Use case 1: Observer pattern
        CopyOnWriteArrayList<Runnable> observers = new CopyOnWriteArrayList<>();
        observers.add(() -> System.out.println("  Observer 1 notified"));
        observers.add(() -> System.out.println("  Observer 2 notified"));

        System.out.println("Notifier pattern:");
        for (Runnable observer : observers) {
            observer.run();
        }

        // Use case 2: Configuration listener list
        CopyOnWriteArrayList<String> configListeners = new CopyOnWriteArrayList<>();
        configListeners.add("Logger");
        configListeners.add("MetricsCollector");
        configListeners.add("AlertSystem");

        System.out.println("Config listeners: " + configListeners);

        // Use case 3: Read-heavy cache with rare updates
        CopyOnWriteArrayList<String> readOnlyCache = new CopyOnWriteArrayList<>();
        readOnlyCache.add("data1");
        readOnlyCache.add("data2");
        readOnlyCache.add("data3");
        System.out.println("Cache size: " + readOnlyCache.size());
        System.out.println();
    }
}
