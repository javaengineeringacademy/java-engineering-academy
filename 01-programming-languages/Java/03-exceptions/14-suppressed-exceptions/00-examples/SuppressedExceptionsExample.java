package academy.javaengineering.exceptions.suppressed.examples;

import java.io.Closeable;
import java.io.IOException;

/**
 * Examples of suppressed exception handling in production scenarios.
 */
public class SuppressedExceptionsExample {

    public static void main(String[] args) {
        exampleTwrWithLogging();
        System.out.println("---");
        exampleManualResourceManagement();
        System.out.println("---");
        exampleParallelAggregation();
        System.out.println("---");
        exampleCustomExceptionWithSuppressed();
    }

    // --- TWR with Logging ---

    static void exampleTwrWithLogging() {
        System.out.println("== TWR with Logging ==");
        try {
            try (LogFileWriter writer = new LogFileWriter()) {
                writer.write("data");
                throw new IOException("write failed");
            }
        } catch (IOException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    // --- Manual Resource Management ---

    static void exampleManualResourceManagement() {
        System.out.println("== Manual Resource Management ==");
        DatabaseConnection conn = null;
        IOException primary = null;
        try {
            conn = new DatabaseConnection();
            conn.execute("SELECT * FROM users");
            throw new IOException("query timeout");
        } catch (IOException e) {
            primary = e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException closeEx) {
                    if (primary != null) {
                        primary.addSuppressed(closeEx);
                    } else {
                        primary = closeEx;
                    }
                }
            }
        }
        if (primary != null) {
            System.out.println("Primary: " + primary.getMessage());
            for (Throwable s : primary.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    // --- Parallel Aggregation ---

    static void exampleParallelAggregation() {
        System.out.println("== Parallel Aggregation ==");
        RuntimeException primary = null;
        for (int i = 1; i <= 3; i++) {
            try {
                processTask(i);
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
        }
    }

    static void processTask(int id) {
        if (id % 2 == 0) {
            throw new RuntimeException("Task " + id + " failed");
        }
    }

    // --- Custom Exception with Suppressed ---

    static void exampleCustomExceptionWithSuppressed() {
        System.out.println("== Custom Exception with Suppressed ==");
        PipelineException primary = new PipelineException("pipeline failed");
        primary.addSuppressed(new RuntimeException("stage 1 cleanup"));
        primary.addSuppressed(new RuntimeException("stage 2 cleanup"));

        System.out.println("Primary: " + primary.getMessage());
        for (Throwable s : primary.getSuppressed()) {
            System.out.println("  Suppressed: " + s.getMessage());
        }
    }

    // --- Helper Types ---

    static class LogFileWriter implements Closeable {
        void write(String data) {
            System.out.println("Writing: " + data);
        }

        @Override
        public void close() {
            throw new RuntimeException("LogFileWriter close() failed");
        }
    }

    static class DatabaseConnection implements Closeable {
        void execute(String sql) {
            System.out.println("Executing: " + sql);
        }

        @Override
        public void close() {
            throw new IOException("DatabaseConnection close() failed");
        }
    }

    static class PipelineException extends Exception {
        PipelineException(String message) {
            super(message);
        }
    }
}
