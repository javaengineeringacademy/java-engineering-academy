package academy.javaengineering.exercises;

import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

/**
 * Exercises: Memory Model and Volatile
 *
 * Complete the TODO sections below.
 */
public class MemoryExercises {

    // TODO 1: Implement a thread-safe counter without using synchronized
    // Use AtomicInteger
    public static class AtomicCounter {
        private AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            // TODO: implement
        }

        public int getCount() {
            // TODO: implement
            return 0;
        }
    }

    // TODO 2: Implement a volatile flag-based shutdown mechanism
    public static class ShutdownFlag {
        private volatile boolean running = true;

        public void shutdown() {
            // TODO: set the flag to false
        }

        public boolean isRunning() {
            // TODO: return the flag
            return true;
        }
    }

    // TODO 3: Implement a thread-safe lazy initialization holder
    // Using the Initialization-on-demand holder idiom
    public static class LazyHolder {
        private LazyHolder() {}

        // TODO: implement the holder class and getInstance()
        public static LazyHolder getInstance() {
            // TODO: implement
            return null;
        }
    }

    // TODO 4: Implement a SpinLock using AtomicInteger
    public static class SpinLock {
        private AtomicInteger state = new AtomicInteger(0);

        public void lock() {
            // TODO: implement using compareAndSet
            while (true) {
                // ...
            }
        }

        public void unlock() {
            // TODO: implement
        }
    }

    // TODO 5: Implement a thread-safe publish-subscribe mechanism
    // Using volatile reference
    public static class Publisher<T> {
        private volatile T latestValue;

        public void publish(T value) {
            // TODO: implement
        }

        public T getLatest() {
            // TODO: implement
            return null;
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        MemoryExercises exercises = new MemoryExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== MemoryExercises Tests ===\n");

        // Test 1
        total++;
        AtomicCounter counter = new AtomicCounter();
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
        if (counter.getCount() == 1000) {
            System.out.println("Test 1 PASSED: AtomicCounter");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: AtomicCounter - " + counter.getCount());
        }

        // Test 2
        total++;
        ShutdownFlag flag = new ShutdownFlag();
        if (flag.isRunning()) {
            flag.shutdown();
            if (!flag.isRunning()) {
                System.out.println("Test 2 PASSED: ShutdownFlag");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: ShutdownFlag still running after shutdown");
            }
        } else {
            System.out.println("Test 2 FAILED: ShutdownFlag not running initially");
        }

        // Test 3
        total++;
        LazyHolder h1 = LazyHolder.getInstance();
        LazyHolder h2 = LazyHolder.getInstance();
        if (h1 != null && h1 == h2) {
            System.out.println("Test 3 PASSED: LazyHolder singleton");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: LazyHolder");
        }

        // Test 5
        total++;
        Publisher<String> publisher = new Publisher<>();
        publisher.publish("first");
        if ("first".equals(publisher.getLatest())) {
            publisher.publish("second");
            if ("second".equals(publisher.getLatest())) {
                System.out.println("Test 5 PASSED: Publisher");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: Publisher - second");
            }
        } else {
            System.out.println("Test 5 FAILED: Publisher - first");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
