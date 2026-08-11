package academy.javaengineering.exceptions.stacktrace;

/**
 * Demonstrates stack trace access, manipulation, and formatting
 * including current thread traces, exception stack traces, trace
 * filtering, limiting, formatting, and fingerprinting for de-duplication.
 *
 * <p><b>Complexity:</b> O(n) where n is the stack depth.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static helper methods.</p>
 * <p><b>Key characteristics:</b> Covers stack trace manipulation,
 * lightweight exceptions, and exception fingerprinting.</p>
 */
package academy.javaengineering.exceptions.stacktrace;

import java.util.Arrays;

/**
 * Demonstrates stack trace access, manipulation, and formatting.
 */
public class StackTrace {

    /**
     * Prints the current thread's stack trace (no exception required).
     */
    public static void showCurrentStackTrace() {
        System.out.println("=== Current Thread Stack Trace ===");
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        printFrames(trace);
    }

    /**
     * Prints the stack trace of a thrown exception.
     */
    public static void showExceptionStackTrace(Exception e) {
        System.out.println("=== Exception Stack Trace ===");
        printFrames(e.getStackTrace());
    }

    /**
     * Replaces an exception's stack trace with a custom one.
     */
    public static Exception replaceStackTrace(Exception original, String newMessage) {
        Exception wrapped = new Exception(newMessage);
        wrapped.setStackTrace(original.getStackTrace());
        return wrapped;
    }

    /**
     * Demonstrates suppressing stack trace for performance.
     */
    public static Exception createLightweightException(String message) {
        LightweightException ex = new LightweightException(message);
        return ex;
    }

    /**
     * Filters frames by package prefix.
     */
    public static StackTraceElement[] filterByPackage(
            StackTraceElement[] trace, String packagePrefix) {
        return Arrays.stream(trace)
                .filter(f -> !f.getClassName().startsWith(packagePrefix))
                .toArray(StackTraceElement[]::new);
    }

    /**
     * Limits stack trace to the top N frames.
     */
    public static StackTraceElement[] limitFrames(StackTraceElement[] trace, int maxFrames) {
        return Arrays.stream(trace)
                .limit(maxFrames)
                .toArray(StackTraceElement[]::new);
    }

    /**
     * Formats a stack trace as a human-readable string.
     */
    public static String formatTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        if (t.getMessage() != null) {
            sb.append(": ").append(t.getMessage());
        }
        sb.append("\n");
        for (StackTraceElement frame : t.getStackTrace()) {
            sb.append("    at ").append(frame.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Generates a fingerprint for de-duplication of exceptions.
     */
    public static String fingerprint(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        StackTraceElement[] trace = t.getStackTrace();
        int limit = Math.min(5, trace.length);
        for (int i = 0; i < limit; i++) {
            sb.append(":").append(trace[i]);
        }
        return sb.toString();
    }

    private static void printFrames(StackTraceElement[] frames) {
        for (int i = 0; i < frames.length; i++) {
            System.out.printf("  [%d] %s%n", i, frames[i]);
        }
    }

    // --- Helper methods to build a non-trivial stack ---

    private static void methodC() {
        showCurrentStackTrace();
    }

    private static void methodB() {
        methodC();
    }

    private static void methodA() {
        methodB();
    }

    /**
     * Entry point for manual testing.
     */
    public static void main(String[] args) {
        System.out.println("--- 1. Current stack trace (no exception) ---");
        methodA();

        System.out.println("\n--- 2. Exception stack trace ---");
        try {
            throw new RuntimeException("demo error");
        } catch (Exception e) {
            showExceptionStackTrace(e);
        }

        System.out.println("\n--- 3. Replace stack trace ---");
        try {
            throw new RuntimeException("original");
        } catch (Exception e) {
            Exception replaced = replaceStackTrace(e, "wrapped");
            showExceptionStackTrace(replaced);
        }

        System.out.println("\n--- 4. Lightweight exception (no trace) ---");
        Exception light = createLightweightException("fast");
        System.out.println("Frames captured: " + light.getStackTrace().length);

        System.out.println("\n--- 5. Filter by package ---");
        try {
            throw new RuntimeException("filter demo");
        } catch (Exception e) {
            StackTraceElement[] filtered =
                    filterByPackage(e.getStackTrace(), "java.lang.");
            printFrames(filtered);
        }

        System.out.println("\n--- 6. Limit to 3 frames ---");
        try {
            throw new RuntimeException("limit demo");
        } catch (Exception e) {
            StackTraceElement[] limited = limitFrames(e.getStackTrace(), 3);
            printFrames(limited);
        }

        System.out.println("\n--- 7. Formatted trace ---");
        try {
            throw new IllegalArgumentException("formatted");
        } catch (Exception e) {
            System.out.print(formatTrace(e));
        }

        System.out.println("\n--- 8. Fingerprint ---");
        try {
            throw new RuntimeException("fp");
        } catch (Exception e) {
            System.out.println("Fingerprint: " + fingerprint(e));
        }
    }
}
