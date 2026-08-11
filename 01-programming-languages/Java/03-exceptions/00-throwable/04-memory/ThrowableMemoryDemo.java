package academy.javaengineering.exceptions.throwable.memory;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * Measures the memory footprint of {@link Throwable} objects.
 *
 * <p><b>Complexity:</b> O(n) where n is the number of objects measured.</p>
 * <p><b>Thread-safety:</b> Single-threaded measurement; not thread-safe.</p>
 * <p><b>Key characteristics:</b> Uses Instrumentation API for accurate object sizing
 * if available, otherwise estimates based on known layout constants.</p>
 */
public class ThrowableMemoryDemo {

    private static Instrumentation instrumentation;

    /**
     * Java agent premain for obtaining Instrumentation reference.
     * Run with: -javaagent:ThrowableMemoryDemo.jar
     */
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void main(String[] args) {
        demoDirectMeasurement();
        demoStackDepthImpact();
        demoSuppressedOverhead();
        demoAccumulationWarning();
    }

    private static void demoDirectMeasurement() {
        System.out.println("=== Throwable memory footprint ===");

        if (instrumentation != null) {
            Throwable t = new RuntimeException("test");
            long size = instrumentation.getObjectSize(t);
            System.out.println("RuntimeException object size: " + size + " bytes");

            StackTraceElement[] stack = t.getStackTrace();
            long stackSize = instrumentation.getObjectSize(stack);
            System.out.println("StackTraceElement[] array size: " + stackSize + " bytes");
            System.out.println("Stack depth: " + stack.length);
        } else {
            System.out.println("Instrumentation not available.");
            System.out.println("Estimated Throwable overhead: ~32 bytes (header + fields)");
            System.out.println("Estimated per-frame cost: ~36 bytes");
        }
        System.out.println();
    }

    private static void demoStackDepthImpact() {
        System.out.println("=== Stack depth impact ===");

        int[] depths = {5, 10, 20, 50};
        for (int depth : depths) {
            Throwable t = createAtDepth(0, depth);
            long estimatedBytes = estimateThrowableSize(t);
            System.out.printf("  Depth %2d: ~%,d bytes%n", depth, estimatedBytes);
        }
        System.out.println();
    }

    private static Throwable createAtDepth(int current, int depth) {
        if (current >= depth) {
            return new RuntimeException("depth=" + depth);
        }
        return createAtDepth(current + 1, depth);
    }

    private static long estimateThrowableSize(Throwable t) {
        // Base Throwable object: 16 (header) + 4*4 (fields) + 4 (padding) = 36 bytes
        long size = 36;

        // Message string
        if (t.getMessage() != null) {
            size += 24 + t.getMessage().length() * 2L; // String header + char array
        }

        // Stack trace
        StackTraceElement[] stack = t.getStackTrace();
        if (stack != null) {
            size += 20; // array header + length
            size += (long) stack.length * 4; // references
            size += (long) stack.length * 32; // StackTraceElement objects
        }

        // Suppressed exceptions (empty ArrayList = ~40 bytes)
        size += 40;

        return size;
    }

    private static void demoSuppressedOverhead() {
        System.out.println("=== Suppressed exceptions overhead ===");

        Throwable base = new RuntimeException("base");
        long baseSize = estimateThrowableSize(base);
        System.out.printf("  Base (0 suppressed): ~%,d bytes%n", baseSize);

        Throwable withSuppressed = new RuntimeException("base");
        for (int i = 0; i < 5; i++) {
            withSuppressed.addSuppressed(new IOException("suppressed " + i));
        }
        long suppressedSize = estimateThrowableSize(withSuppressed);
        System.out.printf("  With 5 suppressed:   ~%,d bytes%n", suppressedSize);
        System.out.printf("  Overhead per suppressed: ~%,d bytes%n",
                (suppressedSize - baseSize) / 5);
        System.out.println();
    }

    private static void demoAccumulationWarning() {
        System.out.println("=== Accumulation warning ===");

        int count = 10_000;
        List<Throwable> throwables = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            throwables.add(new RuntimeException("error " + i));
        }

        long totalEstimated = 0;
        for (Throwable t : throwables) {
            totalEstimated += estimateThrowableSize(t);
        }

        System.out.printf("  %,d Throwable objects: ~%,d bytes (~%,d KB)%n",
                count, totalEstimated, totalEstimated / 1024);
        System.out.println("  In production, use bounded error logs!");
        System.out.println();
    }
}
