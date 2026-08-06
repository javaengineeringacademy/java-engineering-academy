/**
 * ScalarReplacementDemo - Understanding Scalar Replacement in JVM
 * 
 * Scalar replacement is a JIT optimization that eliminates object
 * allocation by breaking objects into their scalar components.
 * This works when escape analysis determines the object doesn't
 * escape the method or thread.
 * 
 * Key concepts:
 * - Escape analysis determines object lifetime
 * - Scalar replacement breaks objects into primitives
 * - Eliminates allocation and GC overhead
 * - Controlled by -XX:+EliminateAllocations flag
 */
public class ScalarReplacementDemo {

    private static final int WARMUP_ITERATIONS = 10_000;
    private static final int TEST_ITERATIONS = 1_000_000;

    /**
     * Simple Point class that can be scalar-replaced.
     * Fields are primitives, object doesn't escape method.
     */
    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int distanceToOrigin() {
            return x * x + y * y;
        }
    }

    /**
     * Demonstrates scalar replacement with a simple object.
     * The Point object doesn't escape the method, so JIT
     * can replace it with scalar variables.
     */
    public static int scalarReplacedExample() {
        // Point doesn't escape this method
        // JIT can eliminate allocation and use registers/stack
        Point p = new Point(10, 20);
        return p.distanceToOrigin();
    }

    /**
     * Example where scalar replacement DOES NOT work.
     * The object escapes the method through return value.
     */
    public static Point escapeExample() {
        // Point escapes through return - cannot be scalar replaced
        Point p = new Point(10, 20);
        return p;
    }

    /**
     * Example where scalar replacement DOES NOT work.
     * The object escapes to a static field.
     */
    static Point staticField;

    public static void staticEscapeExample() {
        // Point escapes to static field - cannot be scalar replaced
        Point p = new Point(10, 20);
        staticField = p;
    }

    /**
     * Example where scalar replacement DOES NOT work.
     * The object escapes through array storage.
     */
    public static void arrayEscapeExample() {
        Point[] points = new Point[10];
        // Point escapes to array - cannot be scalar replaced
        points[0] = new Point(10, 20);
    }

    /**
     * Demonstrates scalar replacement with multiple objects.
     * Both points don't escape, so both can be eliminated.
     */
    public static int multipleScalarsReplaced() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 40);
        // Both objects can be scalar replaced
        return p1.distanceToOrigin() + p2.distanceToOrigin();
    }

    /**
     * Shows escape analysis in action with method inlining.
     * When methods are inlined, more objects become candidates
     * for scalar replacement.
     */
    public static int inlinedMethodExample() {
        // After inlining, this becomes:
        // int x = 10; int y = 20; return x*x + y*y;
        Point p = new Point(10, 20);
        return p.distanceToOrigin();
    }

    /**
     * Demonstrates scalar replacement with conditional logic.
     * Both branches must not escape the object.
     */
    public static int conditionalExample(boolean useFirst) {
        Point p;
        if (useFirst) {
            p = new Point(10, 20);
        } else {
            p = new Point(30, 40);
        }
        // Object doesn't escape either branch
        return p.distanceToOrigin();
    }

    /**
     * Shows performance impact of scalar replacement.
     * Compares allocation vs scalar replacement.
     */
    public static void performanceComparison() {
        System.out.println("=== Performance Comparison ===");

        // Warmup JVM
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            scalarReplacedExample();
        }

        // Test scalar replacement
        long start = System.nanoTime();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            scalarReplacedExample();
        }
        long scalarTime = System.nanoTime() - start;

        // Test with escaping object (no scalar replacement)
        start = System.nanoTime();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            escapeExample();
        }
        long escapeTime = System.nanoTime() - start;

        System.out.println("Scalar replaced: " + scalarTime / 1_000_000 + " ms");
        System.out.println("With escape: " + escapeTime / 1_000_000 + " ms");
        System.out.println("Speedup: " + (double) escapeTime / scalarTime + "x");
    }

    /**
     * Demonstrates JVM flags for controlling scalar replacement.
     */
    public static void jvmFlagsInfo() {
        System.out.println("\n=== JVM Flags for Scalar Replacement ===");
        System.out.println("Enable scalar replacement:");
        System.out.println("  -XX:+EliminateAllocations (default: true)");
        System.out.println();
        System.out.println("Enable escape analysis:");
        System.out.println("  -XX:+DoEscapeAnalysis (default: true)");
        System.out.println();
        System.out.println("Enable allocation sinking:");
        System.out.println("  -XX:+AllocationsInLoop (default: true)");
        System.out.println();
        System.out.println("Control lock coarsening:");
        System.out.println("  -XX:+EliminateLocks (default: true)");
        System.out.println();
        System.out.println("Print escape analysis info:");
        System.out.println("  -XX:+PrintEscapeAnalysis");
    }

    /**
     * Main entry point demonstrating scalar replacement concepts.
     */
    public static void main(String[] args) {
        System.out.println("Scalar Replacement Deep Dive");
        System.out.println("===========================");

        // Basic scalar replacement
        System.out.println("\n--- Basic Scalar Replacement ---");
        int result1 = scalarReplacedExample();
        System.out.println("Result: " + result1);
        System.out.println("Note: Point object eliminated by JIT");

        // Multiple objects
        System.out.println("\n--- Multiple Objects ---");
        int result2 = multipleScalarsReplaced();
        System.out.println("Result: " + result2);
        System.out.println("Note: Both Point objects eliminated");

        // Inlining example
        System.out.println("\n--- Method Inlining ---");
        int result3 = inlinedMethodExample();
        System.out.println("Result: " + result3);
        System.out.println("Note: After inlining, object becomes scalars");

        // Conditional logic
        System.out.println("\n--- Conditional Logic ---");
        int result4 = conditionalExample(true);
        System.out.println("Result (true): " + result4);
        int result5 = conditionalExample(false);
        System.out.println("Result (false): " + result5);

        // Escape examples
        System.out.println("\n--- Escape Analysis ---");
        Point escaped = escapeExample();
        System.out.println("Escaped point: (" + escaped.x + ", " + escaped.y + ")");
        System.out.println("Note: Cannot scalar replace - object escapes");

        // Performance comparison
        performanceComparison();

        // JVM flags
        jvmFlagsInfo();

        System.out.println("\n=== Key Takeaways ===");
        System.out.println("1. Scalar replacement eliminates object allocation");
        System.out.println("2. Escape analysis determines if replacement is possible");
        System.out.println("3. Objects must not escape method/thread scope");
        System.out.println("4. Method inlining enables more scalar replacements");
        System.out.println("5. -XX:+EliminateAllocations controls this optimization");
        System.out.println("6. Significant performance improvement for short-lived objects");
    }
}
