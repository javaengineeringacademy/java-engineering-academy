package academy.javaengineering.exceptions.throwable.exercises;

import java.io.IOException;

/**
 * Exercises on {@link Throwable} API and usage patterns.
 *
 * <p><b>Complexity:</b> Varies by exercise.</p>
 * <p><b>Thread-safety:</b> Single-threaded exercises.</p>
 * <p><b>Key characteristics:</b> Complete the TODO sections to practice
 * Throwable creation, cause chaining, stack trace manipulation, and
 * suppressed exception handling.</p>
 */
public class ThrowableExercises {

    /**
     * Exercise 1: Create a Throwable with a descriptive message.
     * TODO: Create a RuntimeException with message "user not found"
     * and return it.
     */
    public static RuntimeException exercise1() {
        // TODO: implement
        return null;
    }

    /**
     * Exercise 2: Create a Throwable with a cause chain.
     * TODO: Create an IOException with message "file read failed" and cause
     * being an IllegalArgumentException with message "invalid path".
     */
    public static IOException exercise2() {
        // TODO: implement
        return null;
    }

    /**
     * Exercise 3: Extract the root cause from a cause chain.
     * TODO: Given a Throwable, walk the cause chain and return
     * the deepest (root) cause. If t has no cause, return t itself.
     */
    public static Throwable exercise3(Throwable t) {
        // TODO: implement
        return null;
    }

    /**
     * Exercise 4: Read the stack trace depth.
     * TODO: Return the number of frames in the stack trace
     * of the given Throwable.
     */
    public static int exercise4(Throwable t) {
        // TODO: implement
        return -1;
    }

    /**
     * Exercise 5: Trim the stack trace to N frames.
     * TODO: Set the stack trace of the given Throwable to contain
     * only the first n frames.
     */
    public static void exercise5(Throwable t, int n) {
        // TODO: implement
    }

    /**
     * Exercise 6: Add suppressed exceptions.
     * TODO: Create a RuntimeException with message "primary" and add
     * three suppressed IOExceptions with messages "res1", "res2", "res3".
     */
    public static RuntimeException exercise6() {
        // TODO: implement
        return null;
    }

    /**
     * Exercise 7: Check if a Throwable is an instance of Exception
     * (not Error).
     * TODO: Return true if t is an Exception (but not an Error).
     */
    public static boolean exercise7(Throwable t) {
        // TODO: implement
        return false;
    }

    /**
     * Exercise 8: Build a formatted stack trace string.
     * TODO: Return a string with each frame on its own line in the format:
     * "  at ClassName.methodName(FileName:lineNumber)"
     */
    public static String exercise8(Throwable t) {
        // TODO: implement
        return null;
    }

    /**
     * Exercise 9: Count the total number of exceptions in a cause chain
     * (including the original and all causes).
     */
    public static int exercise9(Throwable t) {
        // TODO: implement
        return 0;
    }

    /**
     * Exercise 10: Create a custom exception that skips stack trace capture
     * by overriding fillInStackTrace().
     */
    public static RuntimeException exercise10(String message) {
        // TODO: implement
        return null;
    }
}
