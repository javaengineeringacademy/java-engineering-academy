package trywithresources;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Try-With-Resources Demo
 * 
 * Covers multiple resources, custom AutoCloseable, exception suppression,
 * and complex resource management patterns.
 */
public class TryWithResourcesAdvanced {

    // ==========================================
    // SECTION 1: Basic Try-With-Resources
    // ==========================================
    static class BasicExamples {

        // Single resource
        static void singleResource() {
            System.out.println("=== Single Resource ===\n");
            try (BufferedReader reader = new BufferedReader(new StringReader("Hello\nWorld"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("  Read: " + line);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Multiple resources - auto-closed in reverse order
        static void multipleResources() {
            System.out.println("\n=== Multiple Resources ===\n");
            try (
                    StringWriter writer = new StringWriter();
                    BufferedWriter buffered = new BufferedWriter(writer);
                    PrintWriter print = new PrintWriter(buffered)
            ) {
                print.println("First line");
                print.println("Second line");
                print.flush();
                System.out.println("  Written: " + writer.toString().trim());
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("  Resources closed in reverse order");
        }

        // Resource with initialization that can throw
        static void resourceWithInit() {
            System.out.println("\n=== Resource with Initialization ===\n");
            try (
                    FileInputStream fis = new FileInputStream("/dev/null");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
            ) {
                System.out.println("  Resource initialized and ready");
            } catch (FileNotFoundException e) {
                System.out.println("  File not found (expected in demo)");
            } catch (IOException e) {
                System.out.println("  IO Error: " + e.getMessage());
            }
        }
    }

    // ==========================================
    // SECTION 2: Custom AutoCloseable
    // ==========================================
    static class CustomAutoCloseable {

        // Simple custom AutoCloseable
        static class DatabaseConnection implements AutoCloseable {
            private final String url;
            private boolean closed = false;
            private final List<String> operations = new ArrayList<>();

            public DatabaseConnection(String url) {
                this.url = url;
                System.out.println("    [DB] Connected to: " + url);
            }

            public void execute(String sql) {
                if (closed) throw new IllegalStateException("Connection is closed");
                operations.add(sql);
                System.out.println("    [DB] Executed: " + sql);
            }

            @Override
            public void close() {
                if (!closed) {
                    closed = true;
                    System.out.println("    [DB] Connection closed (" + operations.size() + " operations)");
                }
            }
        }

        // AutoCloseable with exception in close()
        static class RiskyResource implements AutoCloseable {
            private final String name;
            private boolean shouldFailOnClose;

            public RiskyResource(String name, boolean shouldFailOnClose) {
                this.name = name;
                this.shouldFailOnClose = shouldFailOnClose;
                System.out.println("    [Resource] " + name + " opened");
            }

            public void doWork() {
                System.out.println("    [Resource] " + name + " doing work");
            }

            @Override
            public void close() {
                System.out.println("    [Resource] " + name + " closing...");
                if (shouldFailOnClose) {
                    throw new RuntimeException("Error closing " + name);
                }
            }
        }

        // AutoCloseable with try-with-resources
        static void demonstrateCustomAutoCloseable() {
            System.out.println("\n=== Custom AutoCloseable ===\n");

            try (DatabaseConnection db = new DatabaseConnection("jdbc:mysql://localhost/mydb")) {
                db.execute("SELECT * FROM users");
                db.execute("INSERT INTO logs VALUES (1, 'test')");
            }
        }

        static void demonstrateCloseException() {
            System.out.println("\n=== Close Exception Handling ===\n");

            try (RiskyResource resource = new RiskyResource("risky", true)) {
                resource.doWork();
            } catch (Exception e) {
                System.out.println("    Caught: " + e.getMessage());
                System.out.println("    Suppressed exceptions:");
                for (Throwable suppressed : e.getSuppressed()) {
                    System.out.println("      - " + suppressed.getMessage());
                }
            }
        }
    }

    // ==========================================
    // SECTION 3: Exception Suppression
    // ==========================================
    static class ExceptionSuppression {

        // Demonstrates suppressed exceptions
        static class FailingCloseResource implements AutoCloseable {
            private final String name;

            public FailingCloseResource(String name) {
                this.name = name;
            }

            public void work() {
                System.out.println("    [" + name + "] Working...");
            }

            @Override
            public void close() {
                throw new RuntimeException("Close failed for " + name);
            }
        }

        static void demonstrateSuppression() {
            System.out.println("\n=== Exception Suppression ===\n");

            Exception caughtException = null;
            try {
                try (FailingCloseResource r1 = new FailingCloseResource("A");
                     FailingCloseResource r2 = new FailingCloseResource("B")) {
                    r1.work();
                    r2.work();
                    throw new RuntimeException("Primary exception");
                }
            } catch (Exception e) {
                caughtException = e;
                System.out.println("    Primary: " + e.getMessage());
                System.out.println("    Suppressed (" + e.getSuppressed().length + "):");
                for (Throwable t : e.getSuppressed()) {
                    System.out.println("      - " + t.getMessage());
                }
            }
        }

        // Manually adding suppressed exceptions
        static void demonstrateManualSuppression() {
            System.out.println("\n=== Manual Suppression ===\n");

            Exception primary = new RuntimeException("Primary error");
            Exception suppressed1 = new RuntimeException("Suppressed error 1");
            Exception suppressed2 = new RuntimeException("Suppressed error 2");

            primary.addSuppressed(suppressed1);
            primary.addSuppressed(suppressed2);

            System.out.println("    Primary: " + primary.getMessage());
            System.out.println("    Suppressed:");
            for (Throwable t : primary.getSuppressed()) {
                System.out.println("      - " + t.getMessage());
            }
        }

        // Accessing suppressed exceptions
        static void demonstrateAccessingSuppressed() {
            System.out.println("\n=== Accessing Suppressed Exceptions ===\n");

            try {
                try (FailingCloseResource r = new FailingCloseResource("C")) {
                    r.work();
                    throw new IOException("IO error");
                }
            } catch (Exception e) {
                System.out.println("    Main: " + e.getClass().getSimpleName() + " - " + e.getMessage());

                // Filter suppressed by type
                for (Throwable t : e.getSuppressed()) {
                    if (t instanceof RuntimeException) {
                        System.out.println("    RuntimeException suppressed: " + t.getMessage());
                    }
                }
            }
        }
    }

    // ==========================================
    // SECTION 4: Advanced Patterns
    // ==========================================
    static class AdvancedPatterns {

        // Resource manager with cleanup tracking
        static class TrackedResource implements AutoCloseable {
            private final String id;
            private final List<String> cleanupLog;
            private boolean active = true;

            public TrackedResource(String id, List<String> cleanupLog) {
                this.id = id;
                this.cleanupLog = cleanupLog;
                cleanupLog.add("Opened: " + id);
            }

            public void use() {
                if (!active) throw new IllegalStateException(id + " is closed");
                cleanupLog.add("Used: " + id);
            }

            @Override
            public void close() {
                if (active) {
                    active = false;
                    cleanupLog.add("Closed: " + id);
                }
            }
        }

        static void demonstrateCleanupTracking() {
            System.out.println("\n=== Cleanup Tracking ===\n");

            List<String> log = new ArrayList<>();
            try (TrackedResource r1 = new TrackedResource("R1", log);
                 TrackedResource r2 = new TrackedResource("R2", log);
                 TrackedResource r3 = new TrackedResource("R3", log)) {

                r1.use();
                r2.use();
                r3.use();
            }

            System.out.println("    Cleanup order:");
            for (String entry : log) {
                System.out.println("      " + entry);
            }
        }

        // Conditional resource management
        static class ConditionalResource implements AutoCloseable {
            private final String name;
            private final boolean autoClose;

            public ConditionalResource(String name, boolean autoClose) {
                this.name = name;
                this.autoClose = autoClose;
                System.out.println("    [" + name + "] Created (autoClose=" + autoClose + ")");
            }

            @Override
            public void close() {
                if (autoClose) {
                    System.out.println("    [" + name + "] Auto-closed");
                } else {
                    System.out.println("    [" + name + "] NOT auto-closed (manual cleanup needed)");
                }
            }
        }

        static void demonstrateConditional() {
            System.out.println("\n=== Conditional Resource Management ===\n");

            try (ConditionalResource auto = new ConditionalResource("Auto", true);
                 ConditionalResource manual = new ConditionalResource("Manual", false)) {
                System.out.println("    Using resources...");
            }
            System.out.println("    Note: 'Manual' resource still needs cleanup in real code");
        }

        // Nested try-with-resources
        static void demonstrateNestedTry() {
            System.out.println("\n=== Nested Try-With-Resources ===\n");

            try (TrackedResource outer = new TrackedResource("Outer", new ArrayList<>())) {
                outer.use();
                try (TrackedResource inner = new TrackedResource("Inner", new ArrayList<>())) {
                    inner.use();
                    System.out.println("    Inner scope active");
                }
                System.out.println("    Back to outer scope");
            }
        }

        // Resource pool pattern
        static class ResourcePool<T extends AutoCloseable> implements AutoCloseable {
            private final List<T> available = new ArrayList<>();
            private final List<T> inUse = new ArrayList<>();

            public void addResource(T resource) {
                available.add(resource);
            }

            public T acquire() {
                if (available.isEmpty()) {
                    throw new RuntimeException("No resources available");
                }
                T resource = available.remove(available.size() - 1);
                inUse.add(resource);
                return resource;
            }

            public void release(T resource) {
                inUse.remove(resource);
                available.add(resource);
            }

            @Override
            public void close() {
                System.out.println("    [Pool] Closing " + (available.size() + inUse.size()) + " resources");
                for (T r : available) {
                    try {
                        r.close();
                    } catch (Exception e) {
                        System.out.println("    [Pool] Error closing resource: " + e.getMessage());
                    }
                }
            }
        }

        static void demonstrateResourcePool() {
            System.out.println("\n=== Resource Pool Pattern ===\n");

            try (ResourcePool<FailingCloseResource> pool = new ResourcePool<>()) {
                pool.addResource(new FailingCloseResource("Pool-A"));
                pool.addResource(new FailingCloseResource("Pool-B"));

                FailingCloseResource acquired = pool.acquire();
                acquired.work();
                pool.release(acquired);
            }
        }
    }

    // ==========================================
    // SECTION 5: Real-World Examples
    // ==========================================
    static class RealWorldExamples {

        // File processing with multiple resources
        static void processFile(String inputFile, String outputFile) {
            System.out.println("\n=== File Processing Example ===\n");
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
            ) {
                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    writer.write(lineNum + ": " + line.toUpperCase());
                    writer.newLine();
                }
                System.out.println("    Processed " + lineNum + " lines");
            } catch (IOException e) {
                System.out.println("    File processing error: " + e.getMessage());
            }
        }

        // Transaction-like pattern
        static class Transaction implements AutoCloseable {
            private boolean committed = false;
            private final List<String> operations = new ArrayList<>();

            public void addOperation(String op) {
                operations.add(op);
                System.out.println("    [Tx] Added: " + op);
            }

            public void commit() {
                committed = true;
                System.out.println("    [Tx] Committed " + operations.size() + " operations");
            }

            @Override
            public void close() {
                if (!committed && !operations.isEmpty()) {
                    System.out.println("    [Tx] Rolling back " + operations.size() + " operations");
                }
            }
        }

        static void demonstrateTransaction() {
            System.out.println("\n=== Transaction Pattern ===\n");

            // Successful transaction
            System.out.println("  Successful:");
            try (Transaction tx = new Transaction()) {
                tx.addOperation("INSERT INTO users VALUES (1, 'Alice')");
                tx.addOperation("INSERT INTO logs VALUES (1, 'Created user')");
                tx.commit();
            }

            // Failed transaction (rollback)
            System.out.println("\n  Failed (auto-rollback):");
            try (Transaction tx = new Transaction()) {
                tx.addOperation("INSERT INTO users VALUES (2, 'Bob')");
                throw new RuntimeException("Simulated failure");
            } catch (Exception e) {
                System.out.println("    Error: " + e.getMessage());
            }
        }

        static void demonstrateAll() {
            // Create temp files for demo
            try {
                java.io.File tempIn = java.io.File.createTempFile("demo-in", ".txt");
                java.io.File tempOut = java.io.File.createTempFile("demo-out", ".txt");
                tempIn.deleteOnExit();
                tempOut.deleteOnExit();

                try (FileWriter fw = new FileWriter(tempIn)) {
                    fw.write("Hello World\nJava Programming\nTry-With-Resources");
                }

                processFile(tempIn.getAbsolutePath(), tempOut.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("    Setup error: " + e.getMessage());
            }

            demonstrateTransaction();
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  TRY-WITH-RESOURCES ADVANCED DEMO       ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        BasicExamples.singleResource();
        BasicExamples.multipleResources();
        BasicExamples.resourceWithInit();

        CustomAutoCloseable.demonstrateCustomAutoCloseable();
        CustomAutoCloseable.demonstrateCloseException();

        ExceptionSuppression.demonstrateSuppression();
        ExceptionSuppression.demonstrateManualSuppression();
        ExceptionSuppression.demonstrateAccessingSuppressed();

        AdvancedPatterns.demonstrateCleanupTracking();
        AdvancedPatterns.demonstrateConditional();
        AdvancedPatterns.demonstrateNestedTry();
        AdvancedPatterns.demonstrateResourcePool();

        RealWorldExamples.demonstrateAll();

        System.out.println("\nAll try-with-resources demos complete!");
    }
}
