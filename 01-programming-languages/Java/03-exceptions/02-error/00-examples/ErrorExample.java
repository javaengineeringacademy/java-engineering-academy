package academy.javaengineering.exceptions.error.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * Practical examples of Java Error types and when they occur.
 *
 * <p>Each example demonstrates a specific Error condition with
 * explanation of why it happens and what it means.
 */
public class ErrorExample {

    public static void main(String[] args) {
        System.out.println("=== Error Examples ===");
        System.out.println();

        example1_OutOfMemoryError();
        example2_StackOverflowError();
        example3_AssertionError();
        example4_VirtualMachineErrorHierarchy();
        example5_ErrorVsException();
        example6_WhenToCatch();
    }

    // ----------------------------------------------------------------
    // Example 1: OutOfMemoryError
    // ----------------------------------------------------------------

    /**
     * Demonstrates the most common Error: OutOfMemoryError.
     *
     * <p>This occurs when the JVM cannot allocate memory for a new object.
     * The JVM first tries to garbage collect, and if that doesn't free
     * enough space, it throws OutOfMemoryError.
     */
    static void example1_OutOfMemoryError() {
        System.out.println("Example 1: OutOfMemoryError");
        System.out.println("-".repeat(40));

        System.out.println("Scenario: Processing a large file that exceeds heap capacity");
        System.out.println();

        // Simulate reading a large file into memory
        List<byte[]> chunks = new ArrayList<>();
        int chunkSize = 1024 * 1024; // 1MB per chunk

        System.out.println("Reading file in 1MB chunks...");
        System.out.println("If the file is larger than the heap, OOM occurs.");
        System.out.println();

        // In production, this would be:
        // try (InputStream is = new FileInputStream("huge-file.bin")) {
        //     byte[] buffer = new byte[chunkSize];
        //     while (is.read(buffer) != -1) {
        //         chunks.add(buffer.clone());
        //     }
        // }
        //
        // java.lang.OutOfMemoryError: Java heap space

        System.out.println("Prevention strategies:");
        System.out.println("  1. Process data in streams, not all at once");
        System.out.println("  2. Use BufferedInputStream with bounded buffer");
        System.out.println("  3. Increase heap size: -Xmx4g");
        System.out.println("  4. Monitor heap usage proactively");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Example 2: StackOverflowError
    // ----------------------------------------------------------------

    /**
     * Demonstrates StackOverflowError from infinite recursion.
     */
    static void example2_StackOverflowError() {
        System.out.println("Example 2: StackOverflowError");
        System.out.println("-".repeat(40));

        System.out.println("Scenario: Recursive method without a base case");
        System.out.println();

        System.out.println("public static int factorial(int n) {");
        System.out.println("    return n * factorial(n - 1); // No base case!");
        System.out.println("}");
        System.out.println();

        System.out.println("Call sequence:");
        System.out.println("  factorial(5) → factorial(4) → factorial(3)");
        System.out.println("  → factorial(2) → factorial(1) → factorial(0)");
        System.out.println("  → factorial(-1) → ... → StackOverflowError");
        System.out.println();

        System.out.println("The correct version:");
        System.out.println("public static int factorial(int n) {");
        System.out.println("    if (n <= 1) return 1; // Base case");
        System.out.println("    return n * factorial(n - 1);");
        System.out.println("}");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Example 3: AssertionError
    // ----------------------------------------------------------------

    /**
     * Demonstrates AssertionError from failed assertions.
     */
    static void example3_AssertionError() {
        System.out.println("Example 3: AssertionError");
        System.out.println("-".repeat(40));

        System.out.println("Scenario: Defending against invalid state with assertions");
        System.out.println();

        int age = -5;

        System.out.println("Code: assert age >= 0 : \"Age cannot be negative: \" + age");
        System.out.println("Value: age = " + age);
        System.out.println();

        try {
            assert age >= 0 : "Age cannot be negative: " + age;
            System.out.println("Assertion passed (assertions disabled)");
        } catch (AssertionError e) {
            System.out.println("AssertionError: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Note: Assertions are disabled by default.");
        System.out.println("Enable with: java -ea ErrorExample");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Example 4: VirtualMachineError Hierarchy
    // ----------------------------------------------------------------

    /**
     * Shows the different types of VirtualMachineError.
     */
    static void example4_VirtualMachineErrorHierarchy() {
        System.out.println("Example 4: VirtualMachineError Hierarchy");
        System.out.println("-".repeat(40));

        System.out.println("VirtualMachineError");
        System.out.println("├── OutOfMemoryError");
        System.out.println("│   ├── Java heap space");
        System.out.println("│   ├── Metaspace");
        System.out.println("│   ├── GC overhead limit exceeded");
        System.out.println("│   └── unable to create new native thread");
        System.out.println("├── StackOverflowError");
        System.out.println("├── InternalError");
        System.out.println("├── UnknownError");
        System.out.println("├── ClassCircularityError");
        System.out.println("├── ClassFormatError");
        System.out.println("├── VerifyError");
        System.out.println("└── ThreadDeath");
        System.out.println();

        System.out.println("All of these indicate the JVM has encountered a fatal condition.");
        System.out.println("None should be caught by application code.");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Example 5: Error vs Exception
    // ----------------------------------------------------------------

    /**
     * Clarifies the distinction between Error and Exception.
     */
    static void example5_ErrorVsException() {
        System.out.println("Example 5: Error vs Exception");
        System.out.println("-".repeat(40));

        System.out.println("Exception:");
        System.out.println("  - Recoverable condition");
        System.out.println("  - Your code should catch and handle it");
        System.out.println("  - Example: FileNotFoundException, IOException");
        System.out.println("  - Indicates: something went wrong in your code");
        System.out.println();

        System.out.println("Error:");
        System.out.println("  - Unrecoverable condition");
        System.out.println("  - Your code should NOT catch it");
        System.out.println("  - Example: OutOfMemoryError, StackOverflowError");
        System.out.println("  - Indicates: the JVM or environment is broken");
        System.out.println();

        System.out.println("Analogy:");
        System.out.println("  Exception = your car has a flat tire (you can fix it)");
        System.out.println("  Error = the road has collapsed (you cannot fix this)");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Example 6: When to Catch Error
    // ----------------------------------------------------------------

    /**
     * Shows the only acceptable scenarios for catching Error.
     */
    static void example6_WhenToCatch() {
        System.out.println("Example 6: When to Catch Error");
        System.out.println("-".repeat(40));

        System.out.println("ACCEPTABLE: Container/framework shutdown");
        System.out.println("  try {");
        System.out.println("      application.start();");
        System.out.println("  } catch (OutOfMemoryError e) {");
        System.out.println("      logger.fatal(\"OOM\", e);");
        System.out.println("      cleanupResources();");
        System.out.println("      System.exit(1);");
        System.out.println("  }");
        System.out.println();

        System.out.println("ACCEPTABLE: Cache eviction on OOM");
        System.out.println("  try {");
        System.out.println("      processLargeDataSet();");
        System.out.println("  } catch (OutOfMemoryError e) {");
        System.out.println("      cache.evictAll();");
        System.out.println("      processLargeDataSet(); // Retry once");
        System.out.println("  }");
        System.out.println();

        System.out.println("WRONG: Catching Error to continue normal flow");
        System.out.println("  try {");
        System.out.println("      riskyOperation();");
        System.out.println("  } catch (Error e) {");
        System.out.println("      System.out.println(\"Error occurred, continuing...\");");
        System.out.println("      // This is dangerous — state may be corrupted");
        System.out.println("  }");
        System.out.println();

        System.out.println("Rule: If you catch an Error, you must shutdown or degrade");
        System.out.println("      gracefully. Never continue normal operation.");
        System.out.println();
    }
}