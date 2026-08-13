package academy.javaengineering.text.examples;

/**
 * StringBuffer Examples - Practical demonstrations of StringBuffer usage.
 * 
 * WHY STRINGBUFFER EXISTS:
 * - Thread safety: All methods are synchronized
 * - Used in multi-threaded string manipulation
 * - Trade-off: Slower than StringBuilder due to synchronization
 * 
 * WHEN TO USE:
 * - Multi-threaded applications
 * - Shared string buffers
 * 
 * WHEN NOT TO USE:
 * - Single-threaded applications (use StringBuilder)
 * - Performance-critical code (use StringBuilder)
 */
public class StringBufferExamples {

    public static void main(String[] args) {
        System.out.println("=== StringBuffer Examples ===\n");

        // Example 1: Thread Safety
        example1_ThreadSafety();

        // Example 2: Basic Operations
        example2_BasicOperations();

        // Example 3: Performance Comparison
        example3_PerformanceComparison();
    }

    /**
     * WHY: StringBuffer is thread-safe due to synchronization.
     * 
     * INTERNAL: Every method in StringBuffer is synchronized, meaning only one
     * thread can execute any method at a time. This ensures thread safety
     * but reduces performance.
     */
    private static void example1_ThreadSafety() {
        System.out.println("--- Example 1: Thread Safety ---");

        StringBuffer sb = new StringBuffer("Hello");

        // Simulating multi-threaded access
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sb.append("a");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sb.append("b");
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final length: " + sb.length());
        System.out.println("Expected: 2005 (5 + 1000 + 1000)");
    }

    /**
     * WHY: StringBuffer has similar API to StringBuilder.
     * 
     * ENGINEERING DECISION: Use StringBuffer only when thread safety is required.
     */
    private static void example2_BasicOperations() {
        System.out.println("\n--- Example 2: Basic Operations ---");

        StringBuffer sb = new StringBuffer();

        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("After append: " + sb);

        sb.insert(5, ",");
        System.out.println("After insert: " + sb);

        sb.delete(5, 6);
        System.out.println("After delete: " + sb);

        sb.replace(6, 11, "Java");
        System.out.println("After replace: " + sb);

        sb.reverse();
        System.out.println("After reverse: " + sb);
    }

    /**
     * WHY: StringBuffer is slower than StringBuilder due to synchronization.
     * 
     * PERFORMANCE: Use StringBuilder for single-threaded code.
     */
    private static void example3_PerformanceComparison() {
        System.out.println("\n--- Example 3: Performance Comparison ---");

        int iterations = 100000;

        // StringBuilder (faster)
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String result = sb.toString();
        long builderTime = System.currentTimeMillis() - start;

        // StringBuffer (slower)
        start = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbf.append("a");
        }
        String result2 = sbf.toString();
        long bufferTime = System.currentTimeMillis() - start;

        System.out.println("StringBuilder: " + builderTime + " ms");
        System.out.println("StringBuffer: " + bufferTime + " ms");
        System.out.println("Ratio: " + (bufferTime / Math.max(builderTime, 1)) + "x slower");
    }
}
