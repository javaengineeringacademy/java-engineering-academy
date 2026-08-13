package academy.javaengineering.exceptions.throwsexamples;

/**
 * Solutions for throws declaration exercises.
 *
 * <p><b>Complexity:</b> O(1) per method call.</p>
 * <p><b>Thread-safety:</b> Stateless methods, safe for concurrent use.</p>
 * <p><b>Key characteristics:</b> Demonstrates proper throws declarations,
 * exception translation, and cross-layer error propagation.</p>
 */
public class ThrowsSolutions {

    // =========================================================
    // Exercise 1: Add throws declaration to a method
    // =========================================================

    /**
     * Solution: Declare checked exception in method signature.
     * The caller must handle or propagate FileNotFoundException.
     */
    public static String readConfig(String path) throws java.io.FileNotFoundException {
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            throw new java.io.FileNotFoundException("Config not found: " + path);
        }
        return "config-data-from-" + path;
    }

    // =========================================================
    // Exercise 2: Exception translation
    // =========================================================

    /**
     * Solution: Catch low-level exception, wrap in domain exception.
     * The caller sees a meaningful UserRepositoryException, not a raw SQLException.
     */
    public static User findUser(int id) throws UserRepositoryException {
        try {
            return queryDatabase(id);
        } catch (java.sql.SQLException e) {
            throw new UserRepositoryException("Failed to find user: " + id, e);
        }
    }

    // =========================================================
    // Exercise 3: Method chain with throws
    // =========================================================

    /**
     * Solution: Each method in the chain declares what it can throw.
     * The top-level caller handles all checked exceptions.
     */
    public static Order processOrder(int orderId) throws ValidationException, PersistenceException {
        Order order = fetchOrder(orderId);
        validateOrder(order);
        saveOrder(order);
        return order;
    }

    // =========================================================
    // Exercise 4: Service layer design
    // =========================================================

    /**
     * Solution: Service layer translates infrastructure exceptions
     * into domain exceptions. Callers never see SQLException.
     */
    public static Invoice generateInvoice(int orderId) throws InvoiceException {
        try {
            Order order = processOrder(orderId);
            return createInvoice(order);
        } catch (ValidationException e) {
            throw new InvoiceException("Invalid order: " + orderId, e);
        } catch (PersistenceException e) {
            throw new InvoiceException("Database failure for order: " + orderId, e);
        }
    }

    // =========================================================
    // Exercise 5: Handle throws across layers
    // =========================================================

    /**
     * Solution: Presentation layer catches all checked exceptions
     * and returns a user-friendly result. No exceptions leak to the user.
     */
    public static String handleRequest(int orderId) {
        try {
            Invoice invoice = generateInvoice(orderId);
            return "Invoice generated: " + invoice.getId();
        } catch (InvoiceException e) {
            return "Error: " + e.getMessage();
        }
    }

    // =========================================================
    // Supporting classes and methods
    // =========================================================

    public static class User {
        private final int id;
        public User(int id) { this.id = id; }
        public int getId() { return id; }
    }

    public static class Order {
        private final int id;
        public Order(int id) { this.id = id; }
        public int getId() { return id; }
    }

    public static class Invoice {
        private final int id;
        public Invoice(int id) { this.id = id; }
        public int getId() { return id; }
    }

    public static class UserRepositoryException extends Exception {
        public UserRepositoryException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String msg) { super(msg); }
    }

    public static class PersistenceException extends Exception {
        public PersistenceException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static class InvoiceException extends Exception {
        public InvoiceException(String msg, Throwable cause) { super(msg, cause); }
    }

    private static User queryDatabase(int id) throws java.sql.SQLException {
        throw new java.sql.SQLException("Simulated DB failure");
    }

    private static Order fetchOrder(int id) throws PersistenceException {
        throw new PersistenceException("Cannot fetch order: " + id, null);
    }

    private static void validateOrder(Order order) throws ValidationException {
        if (order == null) throw new ValidationException("Order is null");
    }

    private static void saveOrder(Order order) throws PersistenceException {
        throw new PersistenceException("Cannot save order: " + order.getId(), null);
    }

    private static Invoice createInvoice(Order order) {
        return new Invoice(order.getId() * 100);
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Throws Declaration ===");
        try {
            String config = readConfig("app.properties");
            System.out.println("Loaded: " + config);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Handled: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 2: Exception Translation ===");
        try {
            User user = findUser(42);
            System.out.println("Found user: " + user.getId());
        } catch (UserRepositoryException e) {
            System.out.println("Domain error: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getClass().getSimpleName());
        }

        System.out.println("\n=== Exercise 5: Cross-Layer ===");
        String result1 = handleRequest(1);
        System.out.println("Result: " + result1);
        String result2 = handleRequest(999);
        System.out.println("Result: " + result2);
    }
}
