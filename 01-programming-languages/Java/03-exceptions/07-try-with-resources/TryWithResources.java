package academy.javaengineering.exceptions.trywithresources;

/**
 * Demonstrates try-with-resources (TWR) with various Closeable resources,
 * including exception suppression, multiple resources, nested TWR,
 * resource lifecycle tracking, and custom close exception handling.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers TWR syntax, exception suppression,
 * resource close ordering, and integration with try-catch-finally.</p>
 */
package academy.javaengineering.exceptions.trywithresources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates try-with-resources (TWR) with various Closeable resources.
 */
public class TryWithResources {

    // Simple custom AutoCloseable resource
    static class SimpleResource implements AutoCloseable {
        private final String name;

        SimpleResource(String name) {
            this.name = name;
            System.out.println("Opened: " + name);
        }

        void doWork() {
            System.out.println("Working in: " + name);
        }

        @Override
        public void close() {
            System.out.println("Closed: " + name);
        }
    }

    // Resource that throws on close
    static class BadCloseResource implements AutoCloseable {
        @Override
        public void close() throws Exception {
            throw new IOException("Close failed!");
        }

        void doWork() {
            System.out.println("Working in BadCloseResource");
        }
    }

    // Resource that tracks if close was called
    static class TrackableResource implements AutoCloseable {
        private final String name;
        private boolean closed = false;

        TrackableResource(String name) {
            this.name = name;
        }

        boolean isClosed() {
            return closed;
        }

        @Override
        public void close() {
            closed = true;
            System.out.println("Closed: " + name);
        }
    }

    /**
     * Demonstrates basic TWR with a single resource.
     */
    static void basicTWR() {
        System.out.println("=== Basic TWR ===");
        try (SimpleResource resource = new SimpleResource("basic")) {
            resource.doWork();
        }
        System.out.println();
    }

    /**
     * Demonstrates TWR with multiple resources.
     */
    static void multipleResources() {
        System.out.println("=== Multiple Resources ===");
        try (SimpleResource a = new SimpleResource("A");
             SimpleResource b = new SimpleResource("B");
             SimpleResource c = new SimpleResource("C")) {
            a.doWork();
            b.doWork();
            c.doWork();
        }
        System.out.println("Note: Closed in reverse order (C, B, A)\n");
    }

    /**
     * Demonstrates exception suppression.
     */
    static void exceptionSuppression() {
        System.out.println("=== Exception Suppression ===");
        try (BadCloseResource resource = new BadCloseResource()) {
            throw new RuntimeException("Body exception");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Suppressed exceptions:");
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("  - " + suppressed);
            }
        }
        System.out.println();
    }

    /**
     * Demonstrates TWR with BufferedReader.
     */
    static void bufferedReaderExample() {
        System.out.println("=== BufferedReader TWR ===");
        String text = "Line 1\nLine 2\nLine 3";
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Demonstrates TWR with try-catch-finally.
     */
    static void tryCatchFinally() {
        System.out.println("=== TWR with try-catch-finally ===");
        try (SimpleResource resource = new TryWithResources.SimpleResource("full")) {
            resource.doWork();
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("Finally block executed");
        }
        System.out.println();
    }

    /**
     * Demonstrates custom close exception handling.
     */
    static void customCloseHandling() {
        System.out.println("=== Custom Close Handling ===");
        try (BadCloseResource resource = new BadCloseResource()) {
            System.out.println("Body executing");
        } catch (Exception e) {
            System.out.println("Handling exception: " + e.getMessage());
            System.out.println("Suppressed count: " + e.getSuppressed().length);
        }
        System.out.println();
    }

    /**
     * Demonstrates resource lifecycle tracking.
     */
    static void resourceLifecycle() {
        System.out.println("=== Resource Lifecycle ===");
        TrackableResource r1 = new TrackableResource("tracked-1");
        TrackableResource r2 = new TrackableResource("tracked-2");

        try (r1; r2) {
            System.out.println("r1 closed? " + r1.isClosed());
            System.out.println("r2 closed? " + r2.isClosed());
        }

        System.out.println("After TWR:");
        System.out.println("r1 closed? " + r1.isClosed());
        System.out.println("r2 closed? " + r2.isClosed());
        System.out.println();
    }

    /**
     * Demonstrates TWR with nested try blocks.
     */
    static void nestedTWR() {
        System.out.println("=== Nested TWR ===");
        try (SimpleResource outer = new SimpleResource("outer")) {
            try (SimpleResource inner = new SimpleResource("inner")) {
                System.out.println("Both resources open");
            }
            System.out.println("Only outer open");
        }
        System.out.println();
    }

    /**
     * Demonstrates empty TWR (legal but pointless).
     */
    static void emptyTWR() {
        System.out.println("=== Empty TWR (pointless) ===");
        try {
            System.out.println("No resources declared");
        }
        System.out.println();
    }

    /**
     * Demonstrates TWR with exception in body.
     */
    static void exceptionInBody() {
        System.out.println("=== Exception in Body ===");
        try (SimpleResource resource = new SimpleResource("exception-body")) {
            System.out.println("About to throw...");
            throw new IllegalStateException("Body error");
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        basicTWR();
        multipleResources();
        exceptionSuppression();
        bufferedReaderExample();
        tryCatchFinally();
        customCloseHandling();
        resourceLifecycle();
        nestedTWR();
        emptyTWR();
        exceptionInBody();
    }
}
