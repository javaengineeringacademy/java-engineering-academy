package academy.javaengineering.exceptions.chaining.solutions;

import java.io.IOException;

/**
 * Solutions for exception chaining exercises.
 *
 * <p>Google Java Style: no comments, clean formatting.
 */
public class ExceptionChainingSolutions {

    // ──────────────────────────────────────────────
    // Exception definitions
    // ──────────────────────────────────────────────

    public static class ServiceException extends Exception {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DataException extends RuntimeException {
        public DataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class NetworkException extends RuntimeException {
        public NetworkException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // Solution 1: Chain exceptions
    // ──────────────────────────────────────────────

    public static void exercise1() throws ServiceException {
        try {
            riskyOperation();
        } catch (IOException e) {
            throw new ServiceException("Service failed", e);
        }
    }

    private static void riskyOperation() throws IOException {
        throw new IOException("Something went wrong");
    }

    // ──────────────────────────────────────────────
    // Solution 2: Exception translation
    // ──────────────────────────────────────────────

    public static void exercise2() {
        try {
            databaseOperation();
        } catch (IOException e) {
            throw new DataException("Database error", e);
        }
    }

    private static void databaseOperation() throws IOException {
        throw new IOException("Database connection failed");
    }

    // ──────────────────────────────────────────────
    // Solution 3: Root cause analysis
    // ──────────────────────────────────────────────

    public static Throwable getRootCause(Throwable e) {
        Throwable cause = e.getCause();
        while (cause != null) {
            Throwable next = cause.getCause();
            if (next == null) {
                return cause;
            }
            cause = next;
        }
        return e;
    }

    // ──────────────────────────────────────────────
    // Solution 4: Find specific cause in chain
    // ──────────────────────────────────────────────

    public static <T extends Throwable> T findCauseInChain(
            Throwable e, Class<T> type) {
        Throwable cause = e;
        while (cause != null) {
            if (type.isInstance(cause)) {
                return type.cast(cause);
            }
            cause = cause.getCause();
        }
        return null;
    }

    // ──────────────────────────────────────────────
    // Solution 5: Multi-layer exception translation
    // ──────────────────────────────────────────────

    public static void exercise5() {
        try {
            try {
                try {
                    throw new IOException("Low-level error");
                } catch (IOException e) {
                    throw new DataException("Data layer error", e);
                }
            } catch (DataException e) {
                throw new ServiceException("Service layer error", e);
            }
        } catch (ServiceException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Root cause: " + getRootCause(e).getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // Solution 6: Exception logging
    // ──────────────────────────────────────────────

    public static void logExceptionChain(Throwable e, String context) {
        System.out.println("[LOG] Context: " + context);
        Throwable current = e;
        int depth = 0;
        while (current != null) {
            System.out.printf("[LOG] Cause #%d: [%s] %s%n",
                    depth, current.getClass().getSimpleName(), current.getMessage());
            current = current.getCause();
            depth++;
        }
    }

    // ──────────────────────────────────────────────
    // Main
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Solution 1 ===");
        try {
            exercise1();
        } catch (ServiceException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        System.out.println("\n=== Solution 2 ===");
        try {
            exercise2();
        } catch (DataException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        System.out.println("\n=== Solution 3 ===");
        try {
            throw new IOException("IO error");
        } catch (IOException e) {
            DataException da = new DataException("Data error", e);
            System.out.println("Root cause: " + getRootCause(da).getMessage());
        }

        System.out.println("\n=== Solution 4 ===");
        try {
            throw new IOException("IO error");
        } catch (IOException e) {
            DataException da = new DataException("Data error", e);
            IOException io = findCauseInChain(da, IOException.class);
            System.out.println("Found IOException: " + io.getMessage());
        }

        System.out.println("\n=== Solution 5 ===");
        exercise5();

        System.out.println("\n=== Solution 6 ===");
        try {
            try {
                throw new IOException("IO error");
            } catch (IOException e) {
                throw new DataException("Data error", e);
            }
        } catch (DataException e) {
            logExceptionChain(e, "test-context");
        }
    }
}
