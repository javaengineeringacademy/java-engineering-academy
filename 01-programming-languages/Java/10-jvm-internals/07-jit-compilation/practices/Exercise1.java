package academy.javaengineering.jvm.jit;

/**
 * Exercise 1: Observe JIT Compilation
 *
 * Task: Run with -XX:+PrintCompilation and analyze which methods get compiled.
 */
public class Exercise1 {

    public static void main(String[] args) {
        System.out.println("=== JIT Compilation Observation ===\n");

        // Task 1: Call a method many times to trigger JIT
        System.out.println("--- Task 1: Trigger Compilation ---");
        // TODO: Call hotMethod() 20000+ times
        // TODO: Observe compilation events in output
        for (int i = 0; i < 30000; i++) {
            hotMethod(i);
        }

        // Task 2: Compare interpreted vs compiled performance
        System.out.println("\n--- Task 2: Performance Measurement ---");
        // TODO: Measure time for first 1000 calls (interpreted)
        // TODO: Measure time for next 1000 calls (compiled)
        // TODO: Compare the two

        System.out.println("\nRun with: java -XX:+PrintCompilation Exercise1");
    }

    static int hotMethod(int x) {
        return x * x + x;
    }
}
