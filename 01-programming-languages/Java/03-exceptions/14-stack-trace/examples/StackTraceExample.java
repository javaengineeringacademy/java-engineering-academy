package academy.javaengineering.exceptions.stacktrace.examples;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Practical stack trace examples for common scenarios.
 */
public class StackTraceExample {

    /**
     * Example 1: Capture caller information without throwing.
     */
    static String getCallerInfo() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        // trace[0] = getStackTrace, trace[1] = getCallerInfo, trace[2] = actual caller
        if (trace.length > 2) {
            StackTraceElement caller = trace[2];
            return caller.getClassName() + "." + caller.getMethodName()
                    + "() at line " + caller.getLineNumber();
        }
        return "unknown";
    }

    /**
     * Example 2: Log method entry with caller context.
     */
    static void logMethodEntry(String methodName) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        System.out.printf("[ENTRY] %s called from %s%n",
                methodName,
                trace.length > 2 ? trace[2] : "unknown");
    }

    /**
     * Example 3: Exception with filtered trace for user-facing messages.
     */
    static RuntimeException createUserException(String detail) {
        RuntimeException ex = new RuntimeException(detail);
        StackTraceElement[] filtered = Arrays.stream(ex.getStackTrace())
                .filter(f -> f.getClassName().startsWith("academy."))
                .toArray(StackTraceElement[]::new);
        ex.setStackTrace(filtered);
        return ex;
    }

    /**
     * Example 4: Build a structured error report from a stack trace.
     */
    static String buildErrorReport(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error Type: ").append(t.getClass().getSimpleName()).append("\n");
        sb.append("Message: ").append(t.getMessage()).append("\n");
        sb.append("Stack Depth: ").append(t.getStackTrace().length).append("\n");
        sb.append("Top Frame: ");
        if (t.getStackTrace().length > 0) {
            sb.append(t.getStackTrace()[0].toString());
        } else {
            sb.append("N/A");
        }
        return sb.toString();
    }

    /**
     * Example 5: Compare two exceptions by their top frames.
     */
    static boolean sameRootCause(Throwable a, Throwable b) {
        if (a.getStackTrace().length == 0 || b.getStackTrace().length == 0) {
            return false;
        }
        StackTraceElement topA = a.getStackTrace()[0];
        StackTraceElement topB = b.getStackTrace()[0];
        return topA.getClassName().equals(topB.getClassName())
                && topA.getMethodName().equals(topB.getMethodName())
                && topA.getLineNumber() == topB.getLineNumber();
    }

    /**
     * Example 6: Truncate stack trace for compact logging.
     */
    static String compactTrace(Throwable t, int maxFrames) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getSimpleName()).append(": ").append(t.getMessage());
        StackTraceElement[] trace = t.getStackTrace();
        int limit = Math.min(maxFrames, trace.length);
        for (int i = 0; i < limit; i++) {
            sb.append("\n  at ").append(trace[i]);
        }
        if (trace.length > maxFrames) {
            sb.append("\n  ... ").append(trace.length - maxFrames)
                    .append(" more frames");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Example 1: Caller Info ===");
        System.out.println("Called from: " + getCallerInfo());

        System.out.println("\n=== Example 2: Method Entry Log ===");
        logMethodEntry("processOrder");

        System.out.println("\n=== Example 3: Filtered User Exception ===");
        RuntimeException userEx = createUserException("Invalid input");
        System.out.println("Frames: " + userEx.getStackTrace().length);
        for (StackTraceElement f : userEx.getStackTrace()) {
            System.out.println("  " + f);
        }

        System.out.println("\n=== Example 4: Error Report ===");
        try {
            throw new IllegalStateException("connection timeout");
        } catch (Exception e) {
            System.out.println(buildErrorReport(e));
        }

        System.out.println("\n=== Example 5: Same Root Cause ===");
        try {
            throw new RuntimeException("a");
        } catch (RuntimeException e1) {
            try {
                throw new RuntimeException("b");
            } catch (RuntimeException e2) {
                System.out.println("Same root cause: " + sameRootCause(e1, e2));
            }
        }

        System.out.println("\n=== Example 6: Compact Trace ===");
        try {
            throw new IllegalArgumentException("compact demo");
        } catch (Exception e) {
            System.out.println(compactTrace(e, 3));
        }
    }
}
