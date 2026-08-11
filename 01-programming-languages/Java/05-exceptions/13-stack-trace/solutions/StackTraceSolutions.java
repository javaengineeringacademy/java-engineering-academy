package academy.javaengineering.exceptions.stacktrace.solutions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Solutions to stack trace exercises.
 */
public class StackTraceSolutions {

    // ============================================================
    // Solution 1: Print Method Names Without Exception
    // ============================================================
    public static void printMethodNames() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            System.out.printf("  [%d] %s%n", i, trace[i].getMethodName());
        }
    }

    // ============================================================
    // Solution 2: Count Frames by Package
    // ============================================================
    public static int countFrames(Throwable t, String packagePrefix) {
        return (int) Arrays.stream(t.getStackTrace())
                .filter(f -> f.getClassName().startsWith(packagePrefix))
                .count();
    }

    // ============================================================
    // Solution 3: Extract Method Names as a List
    // ============================================================
    public static List<String> extractMethodNames(Throwable t) {
        return Arrays.stream(t.getStackTrace())
                .map(StackTraceElement::getMethodName)
                .collect(Collectors.toList());
    }

    // ============================================================
    // Solution 4: Exclude Classes from Stack Trace
    // ============================================================
    public static StackTraceElement[] excludeClasses(
            Throwable t, String... classNames) {
        return Arrays.stream(t.getStackTrace())
                .filter(f -> {
                    for (String name : classNames) {
                        if (f.getClassName().equals(name)) {
                            return false;
                        }
                    }
                    return true;
                })
                .toArray(StackTraceElement[]::new);
    }

    // ============================================================
    // Solution 5: Format with Indentation
    // ============================================================
    public static String formatIndented(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        if (t.getMessage() != null) {
            sb.append(": ").append(t.getMessage());
        }
        for (StackTraceElement frame : t.getStackTrace()) {
            sb.append("\n    at ").append(frame.toString());
        }
        return sb.toString();
    }

    // ============================================================
    // Solution 6: Find Deepest Application Frame
    // ============================================================
    public static StackTraceElement findDeepestAppFrame(Throwable t, String prefix) {
        StackTraceElement[] frames = t.getStackTrace();
        for (int i = frames.length - 1; i >= 0; i--) {
            if (frames[i].getClassName().startsWith(prefix)) {
                return frames[i];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== Solution 1: Print Method Names ===");
        printMethodNames();

        System.out.println("\n=== Solution 2: Count Frames ===");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            System.out.println("java.lang. frames: " + countFrames(e, "java.lang."));
            System.out.println("java.util. frames: " + countFrames(e, "java.util."));
        }

        System.out.println("\n=== Solution 3: Extract Method Names ===");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            System.out.println(extractMethodNames(e));
        }

        System.out.println("\n=== Solution 4: Exclude Classes ===");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            StackTraceElement[] filtered =
                    excludeClasses(e, "java.lang.Thread", "academy.javaengineering.exceptions.stacktrace.solutions.StackTraceSolutions");
            for (StackTraceElement f : filtered) {
                System.out.println("  " + f);
            }
        }

        System.out.println("\n=== Solution 5: Indented Format ===");
        try {
            throw new IllegalArgumentException("indented");
        } catch (Exception e) {
            System.out.println(formatIndented(e));
        }

        System.out.println("\n=== Solution 6: Deepest App Frame ===");
        try {
            throw new RuntimeException("deep");
        } catch (RuntimeException e) {
            StackTraceElement deepest = findDeepestAppFrame(e, "academy.");
            System.out.println("Deepest: " + deepest);
        }
    }
}
