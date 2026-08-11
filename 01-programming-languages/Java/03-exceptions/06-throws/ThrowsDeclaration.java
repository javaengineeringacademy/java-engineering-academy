package academy.javaengineering.exceptions.throwsdeclaration;

/**
 * Demonstrates the throws declaration — method contracts, checked vs unchecked
 * exceptions in throws clauses, exception translation, and production patterns.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers throws syntax, checked exception
 * contracts, unchecked exception declarations, exception translation across
 * layers, and common production patterns.</p>
 */
package academy.javaengineering.exceptions.throwsdeclaration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Demonstrates the throws declaration — method contracts, checked vs unchecked
 * exceptions, exception translation, and production patterns.
 */
public class ThrowsDeclaration {

    // ──────────────────────────────────────────────
    // 1. Checked exception — must declare throws
    // ──────────────────────────────────────────────

    /**
     * Demonstrates a method that must declare throws because it uses
     * a checked exception (IOException).
     */
    static void checkedExceptionDeclaration() {
        System.out.println("=== Checked exception — must declare throws ===");
        try {
            String content = readFirstLine("data.txt");
            System.out.println("Content: " + content);
        } catch (IOException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName() + " — " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * This method MUST declare throws IOException — the compiler requires it.
     */
    static String readFirstLine(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    // ──────────────────────────────────────────────
    // 2. Multiple checked exceptions
    // ──────────────────────────────────────────────

    /**
     * Demonstrates declaring multiple checked exceptions.
     */
    static void multipleCheckedExceptions() {
        System.out.println("=== Multiple checked exceptions ===");
        try {
            transferFunds("account-1", "account-2", 500.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient funds: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Network error: " + e.getMessage());
        }
        System.out.println();
    }

    static void transferFunds(String from, String to, double amount)
            throws InsufficientFundsException, IOException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        // Simulate network call
        if (Math.random() > 0.5) {
            throw new IOException("Connection timeout");
        }
        System.out.println("Transferred $" + amount + " from " + from + " to " + to);
    }

    static class InsufficientFundsException extends Exception {
        InsufficientFundsException(String message) {
            super(message);
        }
    }

    // ──────────────────────────────────────────────
    // 3. Unchecked exception — throws is optional
    // ──────────────────────────────────────────────

    /**
     * Demonstrates that unchecked exceptions do not require throws declaration.
     */
    static void uncheckedExceptionDeclaration() {
        System.out.println("=== Unchecked exception — throws is optional ===");
        try {
            setAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * This method does NOT need to declare throws — IllegalArgumentException
     * is unchecked. The throws clause here is optional (for documentation).
     */
    static void setAge(int age) throws IllegalArgumentException {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    // ──────────────────────────────────────────────
    // 4. Exception translation pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates exception translation — catching low-level exceptions
     * and rethrowing as domain exceptions.
     */
    static void exceptionTranslation() {
        System.out.println("=== Exception translation ===");
        try {
            OrderDTO order = getOrderDetails(42);
            System.out.println("Order: " + order);
        } catch (OrderNotFoundException e) {
            System.out.println("Not found: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Data error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
        System.out.println();
    }

    static OrderDTO getOrderDetails(long orderId) {
        try {
            // Low-level data access — throws checked exception
            return queryDatabase(orderId);
        } catch (SQLException e) {
            // Translate to domain exception
            throw new DataAccessException("Failed to query order: " + orderId, e);
        }
    }

    static OrderDTO queryDatabase(long orderId) throws SQLException {
        // Simulate database error
        throw new SQLException("Table 'orders' not found");
    }

    static class OrderDTO {
        private final long id;
        OrderDTO(long id) { this.id = id; }
        @Override
        public String toString() { return "OrderDTO{id=" + id + "}"; }
    }

    static class OrderNotFoundException extends RuntimeException {
        OrderNotFoundException(long id) {
            super("Order not found: " + id);
        }
    }

    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 5. throws with catch-and-handle pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates handling an exception internally without propagating.
     */
    static void catchAndHandle() {
        System.out.println("=== Catch and handle internally ===");
        sendNotification("Hello!");
        sendNotification("Meeting at 3pm");
        System.out.println();
    }

    static void sendNotification(String message) {
        try {
            emailServiceSend(message);
            System.out.println("Email sent: " + message);
        } catch (IOException e) {
            // Handle internally — fallback to SMS
            System.out.println("Email failed, using SMS fallback for: " + message);
            smsServiceSend(message);
        }
    }

    static void emailServiceSend(String message) throws IOException {
        if (Math.random() > 0.5) {
            throw new IOException("SMTP server unavailable");
        }
    }

    static void smsServiceSend(String message) {
        System.out.println("SMS sent: " + message);
    }

    // ──────────────────────────────────────────────
    // 6. throws in interface/abstract methods
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throws in interface and abstract method declarations.
     */
    static void interfaceDeclaration() {
        System.out.println("=== Interface/abstract throws ===");
        DataSource source = new FileDataSource("config.txt");
        try {
            String data = source.read();
            System.out.println("Data: " + data);
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }
        System.out.println();
    }

    interface DataSource {
        String read() throws IOException;
    }

    static class FileDataSource implements DataSource {
        private final String path;
        FileDataSource(String path) { this.path = path; }

        @Override
        public String read() throws IOException {
            // Must declare throws — interface requires it
            throw new IOException("File not found: " + path);
        }
    }

    // ──────────────────────────────────────────────
    // 7. Exception hierarchy in throws
    // ──────────────────────────────────────────────

    /**
     * Demonstrates declaring only the base type in throws.
     */
    static void exceptionHierarchy() {
        System.out.println("=== Exception hierarchy in throws ===");
        try {
            processAll();
        } catch (IOException e) {
            // Catches FileNotFoundException, SocketException, etc.
            System.out.println("IO error: " + e.getClass().getSimpleName() + " — " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Declares IOException — covers all IO subtypes.
     * Callers do not need to know whether it was FileNotFoundException
     * or SocketException.
     */
    static void processAll() throws IOException {
        if (Math.random() > 0.5) {
            throw new IOException("General IO failure");
        }
        System.out.println("All IO operations completed");
    }

    // ──────────────────────────────────────────────
    // 8. Throws with generic methods
    // ──────────────────────────────────────────────

    /**
     * Demonstrates throws with generic methods.
     */
    static void genericMethodWithThrows() {
        System.out.println("=== Generic method with throws ===");
        try {
            Integer result = executeTask(() -> {
                if (Math.random() > 0.5) {
                    throw new RuntimeException("Task failed");
                }
                return 42;
            });
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    @FunctionalInterface
    interface Task<T> {
        T call() throws Exception;
    }

    static <T> T executeTask(Task<T> task) throws Exception {
        return task.call();
    }

    // ──────────────────────────────────────────────
    // 9. The "throws Exception" anti-pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates why throws Exception is an anti-pattern.
     */
    static void throwsExceptionAntiPattern() {
        System.out.println("=== throws Exception anti-pattern ===");

        // BAD — caller has no idea what to handle
        badMethod("input");

        // BETTER — specific exceptions
        goodMethod("input");
        System.out.println();
    }

    static void badMethod(String input) throws Exception {
        // Caller must catch Exception — loses all type safety
        System.out.println("badMethod: caller does not know failure modes");
    }

    static void goodMethod(String input) throws IOException, IllegalArgumentException {
        // Caller knows exactly what to handle
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        System.out.println("goodMethod: specific failure modes declared");
    }

    // ──────────────────────────────────────────────
    // 10. Restoring interrupt status pattern
    // ──────────────────────────────────────────────

    /**
     * Demonstrates proper handling of InterruptedException.
     */
    static void interruptedExceptionPattern() {
        System.out.println("=== InterruptedException handling ===");
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // BAD: ignoring — loses interrupt status
                // GOOD: restore it
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted, status restored");
            }
        });
        worker.start();
        worker.interrupt();
        try {
            worker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // main
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        checkedExceptionDeclaration();
        multipleCheckedExceptions();
        uncheckedExceptionDeclaration();
        exceptionTranslation();
        catchAndHandle();
        interfaceDeclaration();
        exceptionHierarchy();
        genericMethodWithThrows();
        throwsExceptionAntiPattern();
        interruptedExceptionPattern();
    }
}
