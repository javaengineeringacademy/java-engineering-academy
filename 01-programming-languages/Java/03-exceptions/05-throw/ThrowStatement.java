/**
 * Demonstrates the throw keyword — raising exceptions, rethrowing,
 * exception chaining, and common pitfalls in Java.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers throw syntax, checked vs unchecked
 * throws, rethrowing patterns, exception chaining, and dangerous patterns
 * like throwing in finally or throwing null.</p>
 */
package academy.javaengineering.exceptions.throwstatement;

import java.io.IOException;

/**
 * Demonstrates the throw keyword — raising exceptions, rethrowing,
 * exception chaining, and common pitfalls.
 */
public class ThrowStatement {

    // ──────────────────────────────────────────────
    // 1. Basic throw — unchecked exceptions
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throwing an IllegalArgumentException for invalid input.
     */
    static void basicUncheckedThrow() {
        System.out.println("=== Basic unchecked throw ===");
        try {
            setAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    static void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    // ──────────────────────────────────────────────
    // 2. Basic throw — checked exceptions
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throwing a checked exception (must declare throws).
     */
    static void basicCheckedThrow() {
        System.out.println("=== Basic checked throw ===");
        try {
            readFile("/nonexistent/file.txt");
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    static void readFile(String path) throws IOException {
        if (path == null) {
            throw new IOException("Path cannot be null");
        }
        if (!path.endsWith(".txt")) {
            throw new IOException("Only .txt files supported: " + path);
        }
        System.out.println("Reading: " + path);
    }

    // ──────────────────────────────────────────────
    // 3. throw with multiple conditions
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throwing different exceptions based on validation.
     */
    static void multipleValidation() {
        System.out.println("=== Multiple validation throws ===");
        String[] inputs = {"", null, "ab", "toolonginputvalue"};
        for (String input : inputs) {
            try {
                validateUsername(input);
            } catch (IllegalArgumentException | NullPointerException e) {
                System.out.println("Invalid: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            }
        }
        System.out.println();
    }

    static void validateUsername(String username) {
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username too short: " + username.length());
        }
        if (username.length() > 10) {
            throw new IllegalArgumentException("Username too long: " + username.length());
        }
    }

    // ──────────────────────────────────────────────
    // 4. Rethrowing exceptions
    // ──────────────────────────────────────────────

    /**
     * Demonstrates rethrowing the same exception after logging.
     */
    static void rethrowSame() {
        System.out.println("=== Rethrow same exception ===");
        try {
            processOrder(0);
        } catch (IllegalArgumentException e) {
            System.out.println("OrderService caught: " + e.getMessage());
        }
        System.out.println();
    }

    static void processOrder(int orderId) {
        try {
            if (orderId <= 0) {
                throw new IllegalArgumentException("Invalid order ID: " + orderId);
            }
            System.out.println("Processing order: " + orderId);
        } catch (IllegalArgumentException e) {
            System.out.println("Logging before rethrow: " + e.getMessage());
            throw e; // rethrow
        }
    }

    /**
     * Demonstrates wrapping and rethrowing as a different exception.
     */
    static void wrappedRethrow() {
        System.out.println("=== Wrapped rethrow (exception translation) ===");
        try {
            getUserFromDatabase(999);
        } catch (DataAccessException e) {
            System.out.println("Caught wrapped: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
        System.out.println();
    }

    static void getUserFromDatabase(long id) {
        try {
            simulateDatabaseError(id);
        } catch (IOException e) {
            // Translate low-level exception to domain exception
            throw new DataAccessException("Failed to fetch user: " + id, e);
        }
    }

    static void simulateDatabaseError(long id) throws IOException {
        throw new IOException("Connection refused to database for user: " + id);
    }

    // Custom domain exception
    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 5. Exception chaining
    // ──────────────────────────────────────────────

    /**
     * Demonstrates exception chaining — preserving the root cause.
     */
    static void exceptionChaining() {
        System.out.println("=== Exception chaining ===");
        try {
            executeWithChaining();
        } catch (ServiceException e) {
            System.out.println("Top-level: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
            Throwable causeOfCause = e.getCause().getCause();
            if (causeOfCause != null) {
                System.out.println("Root cause: " + causeOfCause.getMessage());
            }
        }
        System.out.println();
    }

    static void executeWithChaining() {
        try {
            stepOne();
        } catch (IOException e) {
            throw new ServiceException("Step one failed", e);
        }
    }

    static void stepOne() throws IOException {
        try {
            stepTwo();
        } catch (NumberFormatException e) {
            throw new IOException("Could not parse config", e);
        }
    }

    static void stepTwo() {
        throw new NumberFormatException("Invalid config value: abc");
    }

    static class ServiceException extends RuntimeException {
        ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 6. Dangerous: throwing in finally
    // ──────────────────────────────────────────────

    /**
     * WARNING: Demonstrates exception masking when throwing in finally.
     */
    static void throwInFinally() {
        System.out.println("=== DANGEROUS: throw in finally (exception masking) ===");
        try {
            System.out.println("try: throwing original");
            throw new RuntimeException("original");
        } finally {
            System.out.println("finally: throwing replacement");
            // This REPLACES the original exception — original is LOST
            throw new RuntimeException("finally exception — original lost!");
        }
    }

    // ──────────────────────────────────────────────
    // 7. Dangerous: throwing null
    // ──────────────────────────────────────────────

    /**
     * WARNING: Demonstrates throwing null produces NullPointerException.
     */
    static void throwNull() {
        System.out.println("=== DANGEROUS: throw null ===");
        try {
            RuntimeException e = null;
            throw e;
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException: " + e.getMessage());
            System.out.println("Stack trace shows: at ThrowStatement.throwNull");
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 8. Parameter validation pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates the defensive parameter validation pattern.
     */
    static void parameterValidation() {
        System.out.println("=== Parameter validation pattern ===");

        try {
            createOrder(null, 1, 10.0);
        } catch (NullPointerException e) {
            System.out.println("Null product: " + e.getMessage());
        }

        try {
            createOrder("Widget", -1, 10.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad quantity: " + e.getMessage());
        }

        try {
            createOrder("Widget", 1, -5.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad price: " + e.getMessage());
        }
        System.out.println();
    }

    static void createOrder(String product, int quantity, double price) {
        if (product == null) {
            throw new NullPointerException("Product name must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price must be non-negative: " + price);
        }
        System.out.println("Order created: " + product + " x" + quantity + " @ $" + price);
    }

    // ──────────────────────────────────────────────
    // 9. Builder validation pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throwing in a builder's build() method.
     */
    static void builderPattern() {
        System.out.println("=== Builder validation pattern ===");

        try {
            new Request.Builder().build(); // no fields set
        } catch (IllegalStateException e) {
            System.out.println("Build failed: " + e.getMessage());
        }

        Request req = new Request.Builder()
                .method("GET")
                .url("https://example.com")
                .build();
        System.out.println("Built request: " + req);
        System.out.println();
    }

    static class Request {
        private final String method;
        private final String url;

        private Request(String method, String url) {
            this.method = method;
            this.url = url;
        }

        @Override
        public String toString() {
            return method + " " + url;
        }

        static class Builder {
            private String method;
            private String url;

            Builder method(String method) {
                this.method = method;
                return this;
            }

            Builder url(String url) {
                this.url = url;
                return this;
            }

            Request build() {
                if (method == null) {
                    throw new IllegalStateException("HTTP method is required");
                }
                if (url == null) {
                    throw new IllegalStateException("URL is required");
                }
                return new Request(method, url);
            }
        }
    }

    // ──────────────────────────────────────────────
    // 10. Catching and rethrowing with preserved stack
    // ──────────────────────────────────────────────

    /**
     * Demonstrates fillInStackTrace for preserving original trace.
     */
    static void preserveStackTrace() {
        System.out.println("=== Preserve stack trace on rethrow ===");
        try {
            doWork();
        } catch (RuntimeException e) {
            System.out.println("Exception class: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            StackTraceElement[] trace = e.getStackTrace();
            System.out.println("Top of stack: " + trace[0].getMethodName());
        }
        System.out.println();
    }

    static void doWork() {
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            // fillInStackTrace records current stack, not original
            throw (RuntimeException) e.fillInStackTrace();
        }
    }

    static void riskyOperation() {
        throw new RuntimeException("Something went wrong");
    }

    // ──────────────────────────────────────────────
    // main
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        basicUncheckedThrow();
        basicCheckedThrow();
        multipleValidation();
        rethrowSame();
        wrappedRethrow();
        exceptionChaining();
        parameterValidation();
        builderPattern();
        preserveStackTrace();

        // Uncomment to see dangerous patterns:
        // throwInFinally();
        // throwNull();
    }
}
