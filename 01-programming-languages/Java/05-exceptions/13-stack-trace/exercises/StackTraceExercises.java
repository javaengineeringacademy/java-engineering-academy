package academy.javaengineering.exceptions.stacktrace.exercises;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Exercises on stack trace manipulation and analysis.
 */
public class StackTraceExercises {

    // ============================================================
    // Exercise 1: Print Current Stack Trace Without Exception
    // ============================================================
    // Write a method that prints the current call stack (method names only)
    // without throwing an exception.
    //
    // Expected output format:
    //   [0] main
    //   [1] exerciseMethod
    //   [2] printMethodNames
    // ============================================================
    public static void printMethodNames() {
        // TODO: Implement using Thread.currentThread().getStackTrace()
    }

    // ============================================================
    // Exercise 2: Count Frames by Package
    // ============================================================
    // Write a method that counts how many frames in an exception's stack trace
    // belong to a given package prefix.
    //
    // Example: countFrames(e, "java.lang.") should count all java.lang frames.
    // ============================================================
    public static int countFrames(Throwable t, String packagePrefix) {
        // TODO: Implement
        return 0;
    }

    // ============================================================
    // Exercise 3: Extract Method Names as a List
    // ============================================================
    // Write a method that returns a List<String> of all method names from an
    // exception's stack trace, in order (newest first).
    // ============================================================
    public static java.util.List<String> extractMethodNames(Throwable t) {
        // TODO: Implement using Arrays.stream()
        return java.util.Collections.emptyList();
    }

    // ============================================================
    // Exercise 4: Create a Filtered Copy of Stack Trace
    // ============================================================
    // Write a method that returns a new StackTraceElement[] excluding frames
    // from the specified class names.
    // ============================================================
    public static StackTraceElement[] excludeClasses(
            Throwable t, String... classNames) {
        // TODO: Implement using stream filter
        return new StackTraceElement[0];
    }

    // ============================================================
    // Exercise 5: Format Exception with Indentation
    // ============================================================
    // Write a method that formats an exception's stack trace with 4-space
    // indentation on each frame line.
    // ============================================================
    public static String formatIndented(Throwable t) {
        // TODO: Implement
        return "";
    }

    // ============================================================
    // Exercise 6: Find the Deepest Application Frame
    // ============================================================
    // Write a method that returns the last (deepest) frame whose class name
    // starts with a given package prefix. Return null if none found.
    // ============================================================
    public static StackTraceElement findDeepestAppFrame(Throwable t, String prefix) {
        // TODO: Implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Print Method Names ===");
        printMethodNames();

        System.out.println("\n=== Exercise 2: Count Frames ===");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            int javaLang = countFrames(e, "java.lang.");
            int javaUtil = countFrames(e, "java.util.");
            System.out.println("java.lang. frames: " + javaLang);
            System.out.println("java.util. frames: " + javaUtil);
        }

        System.out.println("\n=== Exercise 3: Extract Method Names ===");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            System.out.println(extractMethodNames(e));
        }

        System.out.println("\n=== Exercise 4: Exclude Classes ===");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            StackTraceElement[] filtered = excludeClasses(e, "java.lang.Thread");
            for (StackTraceElement f : filtered) {
                System.out.println("  " + f);
            }
        }

        System.out.println("\n=== Exercise 5: Indented Format ===");
        try {
            throw new IllegalArgumentException("indented");
        } catch (Exception e) {
            System.out.println(formatIndented(e));
        }

        System.out.println("\n=== Exercise 6: Deepest App Frame ===");
        try {
            throw new RuntimeException("deep");
        } catch (RuntimeException e) {
            StackTraceElement deepest = findDeepestAppFrame(e, "academy.");
            System.out.println("Deepest: " + deepest);
        }
    }
}
