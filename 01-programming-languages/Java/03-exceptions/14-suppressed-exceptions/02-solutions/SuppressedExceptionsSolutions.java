package academy.javaengineering.exceptions.suppressed.solutions;

import java.io.Closeable;
import java.io.IOException;

/**
 * Solutions for suppressed exception exercises.
 */
public class SuppressedExceptionsSolutions {

    public static void main(String[] args) {
        System.out.println("=== Exercise 1 Solution ===");
        solution1();
        System.out.println("\n=== Exercise 2 Solution ===");
        solution2();
        System.out.println("\n=== Exercise 3 Solution ===");
        solution3();
    }

    // --- Exercise 1 Solution ---

    static void solution1() {
        try {
            try (FailingCloseResource r = new FailingCloseResource()) {
                throw new IOException("primary error");
            }
        } catch (IOException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    // --- Exercise 2 Solution ---

    static void solution2() {
        RuntimeException primary = new RuntimeException("primary");
        primary.addSuppressed(new RuntimeException("suppressed A"));
        primary.addSuppressed(new RuntimeException("suppressed B"));

        System.out.println("Primary: " + primary.getMessage());
        for (Throwable s : primary.getSuppressed()) {
            System.out.println("  Suppressed: " + s.getMessage());
        }
    }

    // --- Exercise 3 Solution ---

    static void solution3() {
        RuntimeException primary = null;
        for (int i = 1; i <= 5; i++) {
            try {
                runTask(i);
            } catch (RuntimeException e) {
                if (primary == null) {
                    primary = e;
                } else {
                    primary.addSuppressed(e);
                }
            }
        }
        if (primary != null) {
            System.out.println("Primary: " + primary.getMessage());
            System.out.println("Suppressed count: " + primary.getSuppressed().length);
            for (Throwable s : primary.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        } else {
            System.out.println("All tasks succeeded");
        }
    }

    static void runTask(int id) {
        if (id == 2 || id == 4) {
            throw new RuntimeException("Task " + id + " failed");
        }
        System.out.println("Task " + id + " succeeded");
    }

    // --- Helper Types ---

    static class FailingCloseResource implements Closeable {
        @Override
        public void close() {
            throw new RuntimeException("close() failed");
        }
    }

    static class IOException extends Exception {
        IOException(String message) {
            super(message);
        }
    }
}
