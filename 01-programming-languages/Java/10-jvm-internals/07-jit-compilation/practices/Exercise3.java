package academy.javaengineering.jvm.jit;

/**
 * Exercise 3: Deoptimization Detection
 *
 * Task: Create code that triggers JIT deoptimization and observe the effects.
 */
public class Exercise3 {

    // TODO: Create a method that causes deoptimization
    // Hint: Use polymorphic call sites with many implementations

    public static void main(String[] args) {
        System.out.println("=== Deoptimization Detection ===\n");

        System.out.println("Run with: java -XX:+PrintCompilation Exercise3");
        System.out.println("Look for 'Deoptimization' events in output\n");

        // TODO: Implement code that triggers deoptimization
        // TODO: Show performance degradation during deoptimization
        // TODO: Show recovery after recompilation
    }
}
