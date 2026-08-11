package academy.javaengineering.exceptions.throwable.examples;

import java.io.IOException;
import java.util.Arrays;

/**
 * Basic examples demonstrating {@link Throwable} usage patterns.
 *
 * <p><b>Complexity:</b> O(1) per example; O(depth) for stack trace operations.</p>
 * <p><b>Thread-safety:</b> All examples are single-threaded and stateless.</p>
 * <p><b>Key characteristics:</b> Covers creation, message, cause, stack trace,
 * suppressed exceptions, and type checking.</p>
 */
public class ThrowableExample {

    public static void main(String[] args) {
        exampleCreation();
        exampleCauseChaining();
        exampleStackTraceReading();
        exampleSuppressedExceptions();
        exampleTypeChecking();
        exampleToStringFormat();
    }

    private static void exampleCreation() {
        System.out.println("--- Example: Throwable creation ---");

        Throwable withMessage = new Throwable("something failed");
        Throwable withoutMessage = new Throwable();
        Throwable withCause = new Throwable("wrapped", new IOException("root cause"));

        System.out.println("With message: " + withMessage.getMessage());
        System.out.println("Without message: " + withoutMessage.getMessage());
        System.out.println("With cause: " + withCause.getCause());
        System.out.println();
    }

    private static void exampleCauseChaining() {
        System.out.println("--- Example: Cause chaining ---");

        IOException root = new IOException("connection timeout");
        RuntimeException mid = new RuntimeException("service unavailable", root);
        Error top = new Error("system failure", mid);

        System.out.println("Top: " + top.getMessage());
        System.out.println("Chain: " + top.getClass().getSimpleName()
                + " -> " + top.getCause().getClass().getSimpleName()
                + " -> " + top.getCause().getCause().getClass().getSimpleName());
        System.out.println("Root cause: " + root.getMessage());
        System.out.println();
    }

    private static void exampleStackTraceReading() {
        System.out.println("--- Example: Stack trace reading ---");

        Throwable t = new RuntimeException("test");
        StackTraceElement[] stack = t.getStackTrace();

        System.out.println("Stack depth: " + stack.length);
        System.out.println("Top frame class: " + stack[0].getClassName());
        System.out.println("Top frame method: " + stack[0].getMethodName());
        System.out.println("Top frame file: " + stack[0].getFileName());
        System.out.println("Top frame line: " + stack[0].getLineNumber());
        System.out.println();
    }

    private static void exampleSuppressedExceptions() {
        System.out.println("--- Example: Suppressed exceptions ---");

        RuntimeException primary = new RuntimeException("primary");
        primary.addSuppressed(new IOException("resource A"));
        primary.addSuppressed(new IOException("resource B"));

        System.out.println("Primary: " + primary.getMessage());
        System.out.println("Suppressed count: " + primary.getSuppressed().length);
        System.out.println();
    }

    private static void exampleTypeChecking() {
        System.out.println("--- Example: Type checking ---");

        Throwable t = new IllegalArgumentException("bad arg");

        System.out.println("instanceof Throwable: " + (t instanceof Throwable));
        System.out.println("instanceof Exception: " + (t instanceof Exception));
        System.out.println("instanceof RuntimeException: " + (t instanceof RuntimeException));
        System.out.println("instanceof Error: " + (t instanceof Error));
        System.out.println();
    }

    private static void exampleToStringFormat() {
        System.out.println("--- Example: toString format ---");

        Throwable t = new IllegalStateException("state invalid");
        System.out.println("toString(): " + t.toString());
        System.out.println("getMessage(): " + t.getMessage());
        System.out.println("Class: " + t.getClass().getName());
        System.out.println();
    }
}
