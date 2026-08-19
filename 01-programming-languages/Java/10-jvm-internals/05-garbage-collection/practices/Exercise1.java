package academy.javaengineering.jvm.gc;

/**
 * Exercise 1: GC Observation
 *
 * Task: Create programs that demonstrate GC behavior with different collectors.
 * Run each with: java -Xlog:gc* <ClassName>
 */
public class Exercise1 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC Observation ===\n");

        // Task 1: Allocate many objects and trigger Minor GC
        System.out.println("--- Task 1: Trigger Minor GC ---");
        // TODO: Create many short-lived objects in a loop
        // TODO: Observe Minor GC in GC log
        // Example: for (int i = 0; i < 100000; i++) new Object();

        // Task 2: Fill Old Generation
        System.out.println("\n--- Task 2: Fill Old Generation ---");
        // TODO: Create long-lived objects that survive many GC cycles
        // TODO: Observe Major/Full GC in GC log

        // Task 3: Compare collectors
        System.out.println("\n--- Task 3: Compare Collectors ---");
        // TODO: Time the same workload with different collectors
        // Run with: java -XX:+UseSerialGC Exercise1
        // Run with: java -XX:+UseParallelGC Exercise1
        // Run with: java -XX:+UseG1GC Exercise1

        System.out.println("\n[Complete the TODO sections above]");
    }
}
