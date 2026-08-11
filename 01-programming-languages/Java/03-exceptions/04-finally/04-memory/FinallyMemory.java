import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates memory implications of finally blocks in Java.
 *
 * Run with: java -XX:+PrintCodeCache FinallyMemory
 * Run with: javap -c FinallyMemory to see bytecode duplication
 */
public class FinallyMemory {

    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Finally Block Memory Implications ===\n");

        demonstrateBytecodeDuplication();
        demonstrateHelperMethodSavings();
        demonstrateFinallyVsTryWithResources();
        demonstrateNestedFinallyCost();
        demonstrateStringConcatenationInFinally();
    }

    /**
     * Demonstrates how finally bytecode is duplicated across exit paths.
     * The cleanup logic appears in the bytecode for both normal and exceptional paths.
     */
    static void demonstrateBytecodeDuplication() {
        System.out.println("--- 1. Bytecode Duplication in finally ---");
        System.out.println("The finally block's bytecode is copied to every exit point.");
        System.out.println("Run 'javap -c' on this class to see the duplication.\n");

        System.out.println("Example: try-finally with 2 exit paths");
        System.out.println("  try {");
        System.out.println("      if (flag) return 1;    // exit path 1");
        System.out.println("      return 2;              // exit path 2");
        System.out.println("  } finally {");
        System.out.println("      cleanup();             // duplicated at both exits");
        System.out.println("  }");
        System.out.println("  cleanup() bytecode appears 2 times in compiled output.\n");

        // Demonstrate the pattern
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            multiExitFinally(i % 2 == 0);
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Multi-exit finally loop: " + elapsed + " ms\n");
    }

    static int multiExitFinally(boolean flag) {
        try {
            if (flag) {
                return 1;
            }
            return 2;
        } finally {
            // This cleanup code is duplicated in bytecode at each return
            dummyOperation();
        }
    }

    /**
     * Extracting finally logic to a helper method eliminates bytecode duplication.
     * The helper is compiled once and inlined by the JIT.
     */
    static void demonstrateHelperMethodSavings() {
        System.out.println("--- 2. Helper Method vs. Inline finally ---\n");

        long inlineTime = measureInlineFinally();
        long helperTime = measureHelperFinally();

        System.out.println("Inline finally:     " + inlineTime + " ms");
        System.out.println("Helper method:     " + helperTime + " ms");
        System.out.println("Code cache savings: helper compiled once, inlined at call sites.\n");
    }

    static long measureInlineFinally() {
        long start = System.nanoTime();
        List<Integer> results = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
            int result;
            try {
                result = computeValue(i);
            } finally {
                // Inline cleanup: duplicated in bytecode for normal and exceptional paths
                dummyOperation();
            }
            results.add(result);
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static long measureHelperFinally() {
        long start = System.nanoTime();
        List<Integer> results = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
            int result;
            try {
                result = computeValue(i);
            } finally {
                // Helper cleanup: single compilation unit, inlined by JIT
                cleanupHelper();
            }
            results.add(result);
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static void cleanupHelper() {
        dummyOperation();
    }

    /**
     * Compares memory patterns of try-finally vs try-with-resources.
     * try-with-resources generates different bytecode structure.
     */
    static void demonstrateFinallyVsTryWithResources() throws IOException {
        System.out.println("--- 3. finally vs. try-with-resources Memory ---\n");

        // Measure with finally
        long finallyTime = measureWithFinally();

        // Measure with try-with-resources
        long twrTime = measureWithTryWithResources();

        System.out.println("finally pattern:         " + finallyTime + " ms");
        System.out.println("try-with-resources:      " + twrTime + " ms");
        System.out.println();
        System.out.println("try-with-resources bytecode:");
        System.out.println("  - Compiler generates single close() call");
        System.out.println("  - Adds suppressed exception handling");
        System.out.println("  - More structured than manual finally duplication\n");
    }

    static long measureWithFinally() throws IOException {
        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            InputStream is = null;
            try {
                is = new ByteArrayInputStream(new byte[]{1, 2, 3});
                is.read();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // swallowed
                    }
                }
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static long measureWithTryWithResources() throws IOException {
        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            try (InputStream is = new ByteArrayInputStream(new byte[]{1, 2, 3})) {
                is.read();
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    /**
     * Nested try-finally chains multiply bytecode duplication.
     * Each outer finally wraps all inner exit points.
     */
    static void demonstrateNestedFinallyCost() {
        System.out.println("--- 4. Nested try-finally Cost ---\n");

        long shallow = measureNestedDepth(2);
        long medium = measureNestedDepth(4);
        long deep = measureNestedDepth(6);

        System.out.println("2-deep nesting: " + shallow + " ms");
        System.out.println("4-deep nesting: " + medium + " ms");
        System.out.println("6-deep nesting: " + deep + " ms");
        System.out.println("Cost grows as each outer finally duplicates all inner cleanup.\n");
    }

    static long measureNestedDepth(int depth) {
        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS / 10; i++) {
            nestedTryFinally(depth, i);
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static void nestedTryFinally(int depth, int id) {
        if (depth <= 0) {
            dummyOperation();
            return;
        }
        try {
            nestedTryFinally(depth - 1, id);
        } finally {
            dummyOperation();
        }
    }

    /**
     * String concatenation in finally creates temporary objects.
     * Each exit path duplicates the concatenation bytecode.
     */
    static void demonstrateStringConcatenationInFinally() {
        System.out.println("--- 5. String Concatenation in finally ---\n");

        long withConcat = measureWithConcat();
        long withoutConcat = measureWithoutConcat();

        System.out.println("finally with concatenation:    " + withConcat + " ms");
        System.out.println("finally without concatenation: " + withoutConcat + " ms");
        System.out.println("Concatenation allocates StringBuilder + String per exit path.");
        System.out.println("In a 2-exit try block, the concatenation code runs twice as often.\n");
    }

    static long measureWithConcat() {
        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            try {
                dummyOperation();
            } finally {
                String msg = "cleanup at " + System.nanoTime();
                dummyOperation();
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static long measureWithoutConcat() {
        long start = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            try {
                dummyOperation();
            } finally {
                dummyOperation();
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    static int computeValue(int i) {
        return i * 2;
    }

    static void dummyOperation() {
        // Placeholder to prevent dead code elimination
    }
}
