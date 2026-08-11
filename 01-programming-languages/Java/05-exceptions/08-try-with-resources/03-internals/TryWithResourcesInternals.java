package academy.javaengineering.exceptions.trywithresources.internals;

import java.io.Closeable;
import java.io.IOException;

/**
 * Demonstrates how try-with-resources is implemented in bytecode.
 * Run with javap -c to inspect the generated code.
 */
public class TryWithResourcesInternals {

    // Simple AutoCloseable resource
    static class SimpleResource implements AutoCloseable {
        private final String name;
        private boolean closed = false;

        SimpleResource(String name) {
            this.name = name;
            System.out.println("Opened: " + name);
        }

        void doWork() {
            System.out.println("Working in: " + name);
        }

        @Override
        public void close() {
            closed = true;
            System.out.println("Closed: " + name);
        }

        boolean isClosed() {
            return closed;
        }
    }

    // Resource that throws on close
    static class BadCloseResource implements AutoCloseable {
        @Override
        public void close() throws IOException {
            throw new IOException("Close failed!");
        }

        void doWork() {
            System.out.println("Working in BadCloseResource");
        }
    }

    // Two resources to show nesting
    static class ResourceA implements AutoCloseable {
        ResourceA() {
            System.out.println("Opened A");
        }

        @Override
        public void close() {
            System.out.println("Closed A");
        }
    }

    static class ResourceB implements AutoCloseable {
        ResourceB() {
            System.out.println("Opened B");
        }

        @Override
        public void close() {
            System.out.println("Closed B");
        }
    }

    /**
     * Single resource TWR.
     * Bytecode equivalent:
     *   MyResource r = new MyResource();
     *   try { r.doWork(); }
     *   finally { r.close(); }
     */
    static void singleResource() {
        System.out.println("=== Single Resource ===");
        try (SimpleResource r = new SimpleResource("single")) {
            r.doWork();
        }
        System.out.println();
    }

    /**
     * Multiple resources TWR.
     * Bytecode equivalent:
     *   ResourceA a = new ResourceA();
     *   try {
     *     ResourceB b = new ResourceB();
     *     try { // body }
     *     finally { b.close(); }
     *   } finally { a.close(); }
     */
    static void multipleResources() {
        System.out.println("=== Multiple Resources ===");
        try (ResourceA a = new ResourceA();
             ResourceB b = new ResourceB()) {
            System.out.println("Both resources open");
        }
        System.out.println();
    }

    /**
     * Demonstrates exception suppression.
     * Bytecode equivalent:
     *   try { throw bodyException; }
     *   finally {
     *     try { r.close(); }
     *     catch (Throwable t) { bodyException.addSuppressed(t); }
     *   }
     */
    static void exceptionSuppression() {
        System.out.println("=== Exception Suppression ===");
        try (BadCloseResource r = new BadCloseResource()) {
            throw new RuntimeException("Body exception");
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            System.out.println("Suppressed: " + e.getSuppressed().length);
            for (Throwable t : e.getSuppressed()) {
                System.out.println("  - " + t);
            }
        }
        System.out.println();
    }

    /**
     * Shows close exception handling when body completes normally.
     */
    static void closeExceptionNormalBody() {
        System.out.println("=== Close Exception (Normal Body) ===");
        try (BadCloseResource r = new BadCloseResource()) {
            System.out.println("Body completes normally");
        } catch (IOException e) {
            System.out.println("Caught close exception: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Demonstrates resource lifecycle tracking.
     */
    static void lifecycleTracking() {
        System.out.println("=== Lifecycle Tracking ===");
        SimpleResource r1 = new SimpleResource("tracked-1");
        SimpleResource r2 = new SimpleResource("tracked-2");

        System.out.println("Before TWR: r1.closed=" + r1.isClosed() + ", r2.closed=" + r2.isClosed());

        try (r1; r2) {
            System.out.println("Inside TWR: r1.closed=" + r1.isClosed() + ", r2.closed=" + r2.isClosed());
        }

        System.out.println("After TWR: r1.closed=" + r1.isClosed() + ", r2.closed=" + r2.isClosed());
        System.out.println();
    }

    /**
     * Nested TWR vs multiple resources.
     */
    static void nestedVsMultiple() {
        System.out.println("=== Nested TWR ===");
        try (ResourceA a = new ResourceA()) {
            try (ResourceB b = new ResourceB()) {
                System.out.println("Both open (nested)");
            }
            System.out.println("Only A open");
        }
        System.out.println();
    }

    /**
     * Demonstrates that TWR is syntactic sugar.
     * This is what the compiler generates.
     */
    static void manualEquivalent() {
        System.out.println("=== Manual Equivalent ===");
        SimpleResource r = new SimpleResource("manual");
        Throwable primaryException = null;
        try {
            r.doWork();
        } catch (Throwable t) {
            primaryException = t;
            throw t;
        } finally {
            if (r != null) {
                if (primaryException != null) {
                    try {
                        r.close();
                    } catch (Throwable closeEx) {
                        primaryException.addSuppressed(closeEx);
                    }
                } else {
                    r.close();
                }
            }
        }
    }

    public static void main(String[] args) {
        singleResource();
        multipleResources();
        exceptionSuppression();
        closeExceptionNormalBody();
        lifecycleTracking();
        nestedVsMultiple();

        System.out.println("=== Manual Equivalent (see code) ===");
        System.out.println("The manualEquivalent() method shows what the compiler generates.");
    }
}
