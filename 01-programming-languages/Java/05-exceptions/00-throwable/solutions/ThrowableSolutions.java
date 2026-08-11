package academy.javaengineering.exceptions.throwable.solutions;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * Solutions for Throwable exercises.
 *
 * <p><b>Complexity:</b> Varies by solution; all are O(1) or O(depth).</p>
 * <p><b>Thread-safety:</b> Single-threaded and stateless.</p>
 * <p><b>Key characteristics:</b> Each solution demonstrates correct Throwable API usage.</p>
 */
public class ThrowableSolutions {

    /**
     * Solution 1: Create a Throwable with a descriptive message.
     */
    public static RuntimeException exercise1() {
        return new RuntimeException("user not found");
    }

    /**
     * Solution 2: Create a Throwable with a cause chain.
     */
    public static IOException exercise2() {
        IllegalArgumentException cause = new IllegalArgumentException("invalid path");
        return new IOException("file read failed", cause);
    }

    /**
     * Solution 3: Extract the root cause from a cause chain.
     */
    public static Throwable exercise3(Throwable t) {
        Throwable current = t;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }

    /**
     * Solution 4: Read the stack trace depth.
     */
    public static int exercise4(Throwable t) {
        StackTraceElement[] stack = t.getStackTrace();
        return stack != null ? stack.length : 0;
    }

    /**
     * Solution 5: Trim the stack trace to N frames.
     */
    public static void exercise5(Throwable t, int n) {
        StackTraceElement[] original = t.getStackTrace();
        if (n >= original.length) {
            return;
        }
        StackTraceElement[] trimmed = new StackTraceElement[n];
        System.arraycopy(original, 0, trimmed, 0, n);
        t.setStackTrace(trimmed);
    }

    /**
     * Solution 6: Add suppressed exceptions.
     */
    public static RuntimeException exercise6() {
        RuntimeException primary = new RuntimeException("primary");
        primary.addSuppressed(new IOException("res1"));
        primary.addSuppressed(new IOException("res2"));
        primary.addSuppressed(new IOException("res3"));
        return primary;
    }

    /**
     * Solution 7: Check if a Throwable is an instance of Exception
     * (not Error).
     */
    public static boolean exercise7(Throwable t) {
        return t instanceof Exception && !(t instanceof Error);
    }

    /**
     * Solution 8: Build a formatted stack trace string.
     */
    public static String exercise8(Throwable t) {
        StackTraceElement[] stack = t.getStackTrace();
        StringJoiner joiner = new StringJoiner("\n");
        for (StackTraceElement frame : stack) {
            joiner.add("  at " + frame.getClassName()
                    + "." + frame.getMethodName()
                    + "(" + frame.getFileName()
                    + ":" + frame.getLineNumber() + ")");
        }
        return t.toString() + "\n" + joiner.toString();
    }

    /**
     * Solution 9: Count the total number of exceptions in a cause chain.
     */
    public static int exercise9(Throwable t) {
        int count = 0;
        Throwable current = t;
        while (current != null) {
            count++;
            current = current.getCause();
        }
        return count;
    }

    /**
     * Solution 10: Create a custom exception that skips stack trace capture.
     */
    public static RuntimeException exercise10(String message) {
        return new RuntimeException(message) {
            @Override
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        };
    }
}
