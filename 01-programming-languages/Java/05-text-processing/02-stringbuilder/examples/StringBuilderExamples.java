package academy.javaengineering.text.examples;

/**
 * StringBuilder Examples - Practical demonstrations of StringBuilder usage.
 * 
 * WHY STRINGBUILDER:
 * - String concatenation creates new objects (performance overhead)
 * - StringBuilder is mutable, so it modifies the same object
 * - Unsynchronized for better performance in single-threaded contexts
 * 
 * WHEN TO USE:
 * - String concatenation in loops
 * - Building strings step by step
 * - Single-threaded applications
 * 
 * WHEN NOT TO USE:
 * - Multi-threaded applications (use StringBuffer)
 * - Simple concatenation (compiler optimizes with +)
 */
public class StringBuilderExamples {

    public static void main(String[] args) {
        System.out.println("=== StringBuilder Examples ===\n");

        // Example 1: Basic Operations
        example1_BasicOperations();

        // Example 2: Performance Comparison
        example2_PerformanceComparison();

        // Example 3: Capacity Management
        example3_CapacityManagement();

        // Example 4: Chaining Methods
        example4_MethodChaining();
    }

    /**
     * WHY: StringBuilder provides efficient string manipulation.
     * 
     * INTERNAL: StringBuilder uses a resizable array. When capacity is exceeded,
     * it doubles the array size and copies elements.
     */
    private static void example1_BasicOperations() {
        System.out.println("--- Example 1: Basic Operations ---");

        StringBuilder sb = new StringBuilder();

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
     * WHY: StringBuilder is faster for concatenation in loops.
     * 
     * ENGINEERING DECISION: For loops with many iterations, use StringBuilder.
     * For simple concatenation, + operator is fine (compiler optimizes).
     */
    private static void example2_PerformanceComparison() {
        System.out.println("\n--- Example 2: Performance Comparison ---");

        int iterations = 100000;

        // String concatenation (slow)
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < iterations; i++) {
            str += "a";
        }
        long stringTime = System.currentTimeMillis() - start;

        // StringBuilder (fast)
        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String result = sb.toString();
        long builderTime = System.currentTimeMillis() - start;

        System.out.println("String concatenation: " + stringTime + " ms");
        System.out.println("StringBuilder: " + builderTime + " ms");
        System.out.println("Speedup: " + (stringTime / Math.max(builderTime, 1)) + "x");
    }

    /**
     * WHY: Understanding capacity helps avoid unnecessary resizing.
     * 
     * PERFORMANCE TIP: If you know the approximate size, specify initial capacity.
     */
    private static void example3_CapacityManagement() {
        System.out.println("\n--- Example 3: Capacity Management ---");

        StringBuilder sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity());
        System.out.println("Initial length: " + sb.length());

        for (int i = 0; i < 100; i++) {
            sb.append("a");
        }

        System.out.println("After 100 appends:");
        System.out.println("Capacity: " + sb.capacity());
        System.out.println("Length: " + sb.length());
    }

    /**
     * WHY: Method chaining makes code more concise.
     * 
     * DESIGN: StringBuilder methods return 'this' for chaining.
     */
    private static void example4_MethodChaining() {
        System.out.println("\n--- Example 4: Method Chaining ---");

        String result = new StringBuilder()
            .append("SELECT")
            .append(" *")
            .append(" FROM users")
            .append(" WHERE age > 18")
            .append(" ORDER BY name")
            .toString();

        System.out.println("SQL Query: " + result);
    }
}
