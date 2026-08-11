package academy.javaengineering.exceptions.throwsdeclaration;

/**
 * Exercises for throws declaration patterns. Each method is a stub with a TODO
 * comment describing what to implement. Complete each exercise before checking
 * the solutions.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers throws declaration with checked exceptions,
 * exception translation across layers, method chain propagation, service layer
 * design, and cross-layer exception handling.</p>
 */
package academy.javaengineering.exceptions.throwsdeclaration.exercises;

import java.io.IOException;
import java.sql.SQLException;

public class ThrowsExercises {

    // ──────────────────────────────────────────────
    // Exercise 1: Add throws declaration
    // ──────────────────────────────────────────────

    /**
     * TODO: This method reads a file and returns its content.
     * The method uses FileReader which throws a checked IOException.
     * Add the appropriate throws declaration to this method.
     *
     * Expected: The method should compile only after adding the throws clause.
     */
    static String readConfigFile(String path) {
        // TODO: Add throws declaration for the checked exception
        //       used in this method body
        java.io.FileReader fr = new java.io.FileReader(path);
        java.io.BufferedReader br = new java.io.BufferedReader(fr);
        try {
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    // ──────────────────────────────────────────────
    // Exercise 2: Exception translation
    // ──────────────────────────────────────────────

    /**
     * TODO: Implement exception translation. This method simulates a data access
     * operation that throws a checked SQLException. Translate it to an
     * unchecked DataAccessException that preserves the root cause.
     *
     * Expected: The caller should catch DataAccessException, not SQLException.
     */
    static void exercise2_ExceptionTranslation() {
        System.out.println("=== Exercise 2: Exception translation ===");
        try {
            String result = fetchData(100);
            System.out.println("Data: " + result);
        } catch (DataAccessException e) {
            System.out.println("Caught translated exception: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause());
        }
        System.out.println();
    }

    static String fetchData(int id) {
        // TODO: Call queryDatabase(id) inside a try-catch,
        //       catch SQLException, and rethrow as DataAccessException
        //       preserving the original cause
        return null;
    }

    static String queryDatabase(int id) throws SQLException {
        throw new SQLException("Connection refused for id=" + id);
    }

    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // Exercise 3: Method chain with throws
    // ──────────────────────────────────────────────

    /**
     * TODO: Complete the method chain so that exceptions propagate correctly
     * through all three layers. Each layer should translate exceptions
     * to its own domain-specific type while preserving the root cause.
     *
     * Expected: controller() catches ServiceException,
     *           service() catches PersistenceException,
     *           repository() throws IOException directly.
     */
    static void exercise3_MethodChainWithThrows() {
        System.out.println("=== Exercise 3: Method chain with throws ===");
        try {
            String result = controller("item-42");
            System.out.println("Result: " + result);
        } catch (ServiceException e) {
            System.out.println("Service error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
        System.out.println();
    }

    static String controller(String itemId) {
        // TODO: Call service(itemId), catch PersistenceException,
        //       translate to ServiceException preserving cause
        return null;
    }

    static String service(String itemId) {
        // TODO: Call repository(itemId), catch IOException,
        //       translate to PersistenceException preserving cause
        return null;
    }

    static String repository(String itemId) throws IOException {
        throw new IOException("File not found: " + itemId);
    }

    static class ServiceException extends RuntimeException {
        ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class PersistenceException extends RuntimeException {
        PersistenceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // Exercise 4: Service layer design
    // ──────────────────────────────────────────────

    /**
     * TODO: Design a service layer method that:
     * - Accepts a user ID and action
     * - Validates inputs (throws IllegalArgumentException for invalid)
     * - Performs the action (may throw checked IOException)
     * - Translates any IOException to a UserServiceException
     * - Returns a success message on success
     *
     * Expected: The public method should not expose IOException to callers.
     */
    static void exercise4_ServiceLayerDesign() {
        System.out.println("=== Exercise 4: Service layer design ===");
        try {
            String result = processAction("user-1", "approve");
            System.out.println("Success: " + result);
        } catch (UserServiceException e) {
            System.out.println("Service error: " + e.getMessage());
        }
        System.out.println();
    }

    static String processAction(String userId, String action) {
        // TODO: Implement the service layer method:
        // 1. Validate userId is not null or empty
        // 2. Validate action is not null or empty
        // 3. Call performAction(userId, action) inside try-catch
        // 4. Catch IOException and throw UserServiceException with cause
        // 5. Return success message
        return null;
    }

    static boolean performAction(String userId, String action) throws IOException {
        // Simulate an IO operation that may fail
        if (Math.random() > 0.5) {
            throw new IOException("Backend service unavailable");
        }
        return true;
    }

    static class UserServiceException extends RuntimeException {
        UserServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // Exercise 5: Cross-layer throws
    // ──────────────────────────────────────────────

    /**
     * TODO: Implement a full cross-layer exception handling scenario:
     * - Presentation layer: catches and formats errors for display
     * - Business layer: validates and orchestrates
     * - Persistence layer: performs database operations
     *
     * Each layer should translate exceptions appropriately. The presentation
     * layer should never see raw SQLException or IOException.
     *
     * Expected: Clean exception propagation with proper translation at each layer.
     */
    static void exercise5_CrossLayerThrows() {
        System.out.println("=== Exercise 5: Cross-layer throws ===");
        try {
            String display = presentationLayer("ORDER-100");
            System.out.println("UI Output: " + display);
        } catch (PresentationException e) {
            System.out.println("UI Error: " + e.getMessage());
        }
        System.out.println();
    }

    static String presentationLayer(String orderId) {
        // TODO: Call businessLayer(orderId),
        //       catch BusinessException and translate to PresentationException
        return null;
    }

    static String businessLayer(String orderId) {
        // TODO: Validate orderId, call persistenceLayer(orderId),
        //       catch PersistenceException and translate to BusinessException
        return null;
    }

    static String persistenceLayer(String orderId) throws SQLException {
        // Simulate database operation
        throw new SQLException("Deadlock detected for order: " + orderId);
    }

    static class PresentationException extends RuntimeException {
        PresentationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class BusinessException extends RuntimeException {
        BusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class PersistenceException extends RuntimeException {
        PersistenceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // main — runs all exercises
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        exercise2_ExceptionTranslation();
        exercise3_MethodChainWithThrows();
        exercise4_ServiceLayerDesign();
        exercise5_CrossLayerThrows();
    }
}
