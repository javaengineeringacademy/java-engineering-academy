package academy.javaengineering.exceptions.suppressed;

/**
 * Demonstrates suppressed exception handling including TWR behavior,
 * manual addSuppressed usage, and exception chaining patterns.
 */
public class SuppressedExceptions {

    public static void main(String[] args) {
        demoTwrSuppressed();
        System.out.println("---");
        demoManualSuppressed();
        System.out.println("---");
        demoMultipleSuppressed();
        System.out.println("---");
        demoChainingVsSuppressed();
    }

    // --- TWR Demo ---

    static void demoTwrSuppressed() {
        System.out.println("== TWR Suppressed Exceptions ==");
        try {
            try (FailingResource r = new FailingResource("primary")) {
                throw new RuntimeException("primary error");
            }
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    // --- Manual Demo ---

    static void demoManualSuppressed() {
        System.out.println("== Manual addSuppressed ==");
        RuntimeException primary = new RuntimeException("primary");
        RuntimeException suppressed = new RuntimeException("cleanup failed");
        primary.addSuppressed(suppressed);

        System.out.println("Primary: " + primary.getMessage());
        for (Throwable s : primary.getSuppressed()) {
            System.out.println("  Suppressed: " + s.getMessage());
        }
    }

    // --- Multiple Suppressed ---

    static void demoMultipleSuppressed() {
        System.out.println("== Multiple Suppressed Exceptions ==");
        RuntimeException primary = new RuntimeException("primary");
        primary.addSuppressed(new RuntimeException("cleanup A"));
        primary.addSuppressed(new RuntimeException("cleanup B"));
        primary.addSuppressed(new RuntimeException("cleanup C"));

        System.out.println("Primary: " + primary.getMessage());
        System.out.println("Suppressed count: " + primary.getSuppressed().length);
        for (Throwable s : primary.getSuppressed()) {
            System.out.println("  Suppressed: " + s.getMessage());
        }
    }

    // --- Chaining vs Suppressed ---

    static void demoChainingVsSuppressed() {
        System.out.println("== Cause Chaining vs Suppressed ==");
        IOException cause = new IOException("root cause");
        RuntimeException wrapper = new RuntimeException("wrapper", cause);
        wrapper.addSuppressed(new RuntimeException("cleanup issue"));

        System.out.println("Exception: " + wrapper.getMessage());
        System.out.println("Cause: " + wrapper.getCause().getMessage());
        System.out.println("Suppressed count: " + wrapper.getSuppressed().length);
        for (Throwable s : wrapper.getSuppressed()) {
            System.out.println("  Suppressed: " + s.getMessage());
        }
    }

    // --- Helper Types ---

    static class FailingResource implements AutoCloseable {
        private final String name;

        FailingResource(String name) {
            this.name = name;
        }

        @Override
        public void close() {
            throw new RuntimeException(name + " close() failed");
        }
    }

    static class IOException extends Exception {
        IOException(String message) {
            super(message);
        }
    }
}
