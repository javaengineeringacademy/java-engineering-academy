package academy.javaengineering.exceptions.error.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercises for understanding Java Error types.
 *
 * <p>Complete each exercise to practice identifying and responding
 * to different Error conditions.
 */
public class ErrorExercises {

    public static void main(String[] args) {
        System.out.println("=== Error Exercises ===");
        System.out.println();

        exercise1_IdentifyErrorType();
        exercise2_CorrectTheCode();
        exercise3_ProductionResponse();
        exercise4_MemoryMonitoring();
        exercise5_ErrorHierarchy();
    }

    // ----------------------------------------------------------------
    // Exercise 1: Identify the Error Type
    // ----------------------------------------------------------------

    /**
     * For each scenario, identify the most likely Error type.
     *
     * Scenario A: A method calls itself without a base case.
     * Scenario B: The JVM cannot allocate a new byte array.
     * Scenario C: A class file was corrupted during deployment.
     * Scenario D: A static initializer throws an exception.
     * Scenario E: An assert statement fails.
     *
     * Write your answers below and run to check.
     */
    static void exercise1_IdentifyErrorType() {
        System.out.println("Exercise 1: Identify the Error Type");
        System.out.println("-".repeat(40));

        System.out.println("For each scenario, identify the Error type:");
        System.out.println();
        System.out.println("A) Method calls itself without base case:");
        System.out.println("   Your answer: _______________");
        System.out.println("   Correct:     StackOverflowError");
        System.out.println();
        System.out.println("B) JVM cannot allocate new byte array:");
        System.out.println("   Your answer: _______________");
        System.out.println("   Correct:     OutOfMemoryError");
        System.out.println();
        System.out.println("C) Class file corrupted during deployment:");
        System.out.println("   Your answer: _______________");
        System.out.println("   Correct:     ClassFormatError");
        System.out.println();
        System.out.println("D) Static initializer throws exception:");
        System.out.println("   Your answer: _______________");
        System.out.println("   Correct:     ExceptionInInitializerError");
        System.out.println();
        System.out.println("E) Assert statement fails:");
        System.out.println("   Your answer: _______________");
        System.out.println("   Correct:     AssertionError");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Exercise 2: Correct the Code
    // ----------------------------------------------------------------

    /**
     * The following code will throw an Error. Fix it.
     *
     * <p>Original code:
     * <pre>
     * public static int sum(int n) {
     *     return n + sum(n - 1);
     * }
     * </pre>
     *
     * <p>What Error does this throw? How do you fix it?
     */
    static void exercise2_CorrectTheCode() {
        System.out.println("Exercise 2: Correct the Code");
        System.out.println("-".repeat(40));

        System.out.println("Original code:");
        System.out.println("  public static int sum(int n) {");
        System.out.println("      return n + sum(n - 1);");
        System.out.println("  }");
        System.out.println();

        System.out.println("Error thrown: StackOverflowError");
        System.out.println("Cause: No base case — infinite recursion");
        System.out.println();

        System.out.println("Fixed code:");
        System.out.println("  public static int sum(int n) {");
        System.out.println("      if (n <= 0) return 0; // Base case");
        System.out.println("      return n + sum(n - 1);");
        System.out.println("  }");
        System.out.println();

        // Verify the fix
        System.out.println("sum(5) = " + sum(5));
        System.out.println("sum(10) = " + sum(10));
        System.out.println();
    }

    static int sum(int n) {
        if (n <= 0) return 0;
        return n + sum(n - 1);
    }

    // ----------------------------------------------------------------
    // Exercise 3: Production Response
    // ----------------------------------------------------------------

    /**
     * For each production scenario, describe the correct response.
     *
     * Scenario A: Your application throws OutOfMemoryError during peak load.
     * Scenario B: A customer reports NoClassDefFoundError after deployment.
     * Scenario C: Your service logs StackOverflowError in production.
     * Scenario D: You see "GC overhead limit exceeded" in monitoring.
     */
    static void exercise3_ProductionResponse() {
        System.out.println("Exercise 3: Production Response");
        System.out.println("-".repeat(40));

        System.out.println("A) OutOfMemoryError during peak load:");
        System.out.println("   1. Check if heap dump was captured");
        System.out.println("   2. Analyze heap dump for memory leak");
        System.out.println("   3. Check if heap size is appropriate");
        System.out.println("   4. Review recent code changes");
        System.out.println("   5. Consider increasing -Xmx if legitimate");
        System.out.println();

        System.out.println("B) NoClassDefFoundError after deployment:");
        System.out.println("   1. Check classpath — is the JAR present?");
        System.out.println("   2. Verify all dependencies are included");
        System.out.println("   3. Check for version conflicts");
        System.out.println("   4. Verify static initializers succeed");
        System.out.println("   5. Redeploy with correct dependencies");
        System.out.println();

        System.out.println("C) StackOverflowError in production:");
        System.out.println("   1. Capture thread dump");
        System.out.println("   2. Identify the recursion pattern");
        System.out.println("   3. Add or fix the base case");
        System.out.println("   4. Review recent code changes");
        System.out.println("   5. Consider increasing -Xss as temporary fix");
        System.out.println();

        System.out.println("D) GC overhead limit exceeded:");
        System.out.println("   1. Check heap usage — is it near max?");
        System.out.println("   2. Analyze for memory leak");
        System.out.println("   3. Check GC logs for collection frequency");
        System.out.println("   4. Review object lifecycle management");
        System.out.println("   5. Consider increasing heap or fixing leak");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Exercise 4: Memory Monitoring
    // ----------------------------------------------------------------

    /**
     * Write code to monitor heap usage and warn when it exceeds 80%.
     *
     * <p>Use java.lang.management.MemoryMXBean.
     */
    static void exercise4_MemoryMonitoring() {
        System.out.println("Exercise 4: Memory Monitoring");
        System.out.println("-".repeat(40));

        System.out.println("Solution:");
        System.out.println();

        System.out.println("import java.lang.management.ManagementFactory;");
        System.out.println("import java.lang.management.MemoryMXBean;");
        System.out.println("import java.lang.management.MemoryUsage;");
        System.out.println();
        System.out.println("MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();");
        System.out.println("MemoryUsage heap = memoryBean.getHeapMemoryUsage();");
        System.out.println("long used = heap.getUsed();");
        System.out.println("long max = heap.getMax();");
        System.out.println("double percent = (double) used / max * 100;");
        System.out.println();
        System.out.println("if (percent > 80) {");
        System.out.println("    logger.warn(\"Heap usage high: {}%\", percent);");
        System.out.println("}");
        System.out.println();

        // Demonstrate
        java.lang.management.MemoryMXBean memoryBean =
                java.lang.management.ManagementFactory.getMemoryMXBean();
        java.lang.management.MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        double percent = (double) heap.getUsed() / heap.getMax() * 100;
        System.out.printf("Current heap usage: %.1f%%%n", percent);
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Exercise 5: Error Hierarchy
    // ----------------------------------------------------------------

    /**
     * Draw the Error class hierarchy and explain the purpose of each class.
     */
    static void exercise5_ErrorHierarchy() {
        System.out.println("Exercise 5: Error Hierarchy");
        System.out.println("-".repeat(40));

        System.out.println("Throwable");
        System.out.println("├── Error");
        System.out.println("│   ├── VirtualMachineError (abstract)");
        System.out.println("│   │   ├── OutOfMemoryError");
        System.out.println("│   │   ├── StackOverflowError");
        System.out.println("│   │   ├── InternalError");
        System.out.println("│   │   └── UnknownError");
        System.out.println("│   ├── LinkageError");
        System.out.println("│   │   ├── NoClassDefFoundError");
        System.out.println("│   │   ├── ClassFormatError");
        System.out.println("│   │   ├── VerifyError");
        System.out.println("│   │   └── UnsatisfiedLinkError");
        System.out.println("│   ├── AssertionError");
        System.out.println("│   └── ThreadDeath");
        System.out.println("└── Exception");
        System.out.println("    ├── RuntimeException");
        System.out.println("    └── IOException, etc.");
        System.out.println();

        System.out.println("Key distinctions:");
        System.out.println("  - VirtualMachineError: JVM internal problems");
        System.out.println("  - LinkageError: Class loading/linking problems");
        System.out.println("  - AssertionError: Debugging assertion failures");
        System.out.println("  - ThreadDeath: Thread forcibly stopped");
        System.out.println();
    }
}