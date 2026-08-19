package academy.javaengineering.jvm.memorymodel;

/**
 * Exercise 2: Thread-Safe Lazy Initialization
 *
 * Task: Implement thread-safe lazy initialization using three different approaches
 * and compare their correctness and performance.
 */
public class Exercise2 {

    private static volatile Instance instance;

    public static void main(String[] args) {
        System.out.println("=== Thread-Safe Lazy Initialization ===\n");

        // Task 1: Synchronized method (correct but slow)
        System.out.println("--- Task 1: Synchronized Method ---");
        // TODO: Implement synchronized getInstance()

        // Task 2: Double-checked locking (correct with volatile)
        System.out.println("\n--- Task 2: Double-Checked Locking ---");
        // TODO: Implement DCL with volatile

        // Task 3: Bill Pugh Singleton (best performance)
        System.out.println("\n--- Task 3: Bill Pugh Singleton ---");
        // TODO: Implement holder pattern

        System.out.println("\n[Complete the TODO sections above]");
    }

    static class Instance {
        Instance() { System.out.println("Instance created"); }
    }
}
