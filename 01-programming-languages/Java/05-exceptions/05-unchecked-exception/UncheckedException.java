package academy.javaengineering.exceptions.uncheckedexception;

/**
 * UncheckedException — comprehensive reference for unchecked exceptions in Java.
 *
 * <p>Unchecked exceptions extend {@link RuntimeException} (or {@link Error}) and
 * are not checked at compile time. They represent programming bugs, not
 * recoverable external failures.</p>
 *
 * <p>This class demonstrates the hierarchy, common subtypes, when to throw them,
 * and production patterns for handling them.</p>
 */
public final class UncheckedException {

    private UncheckedException() {}

    // -----------------------------------------------------------------------
    // 1. Hierarchy
    // -----------------------------------------------------------------------

    /**
     * The unchecked exception hierarchy:
     *
     * <pre>
     *   Throwable
     *   ├── Error  (unchecked — JVM failures)
     *   │     ├── OutOfMemoryError
     *   │     ├── StackOverflowError
     *   │     └── ...
     *   └── Exception
     *         ├── RuntimeException  (unchecked — programming bugs)
     *         │     ├── NullPointerException
     *         │     ├── IllegalArgumentException
     *         │     │     ├── NumberFormatException
     *         │     │     └── IllegalMonitorStateException
     *         │     ├── IllegalStateException
     *         │     ├── ArrayIndexOutOfBoundsException
     *         │     ├── StringIndexOutOfBoundsException
     *         │     ├── IndexOutOfBoundsException
     *         │     ├── ArithmeticException
     *         │     ├── ClassCastException
     *         │     ├── ConcurrentModificationException
     *         │     ├── UnsupportedOperationException
     *         │     ├── EmptyStackException
     *         │     └── NoSuchElementException
     *         └── IOException  (checked — external failures)
     *               ├── FileNotFoundException
     *               └── ...
     * </pre>
     */

    // -----------------------------------------------------------------------
    // 2. Common Subtypes with Examples
    // -----------------------------------------------------------------------

    /** Throws NullPointerException when a null reference is dereferenced. */
    public static void demonstrateNullPointerException() {
        String text = null;
        // This will throw NullPointerException:
        // text.length();
        System.out.println("NullPointerException: thrown when dereferencing null");
    }

    /** Throws IllegalArgumentException for invalid method arguments. */
    public static void demonstrateIllegalArgumentException(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    /** Throws IllegalStateException when the object is in the wrong state. */
    public static void demonstrateIllegalStateException(boolean isOpen) {
        if (!isOpen) {
            throw new IllegalStateException("Connection is closed");
        }
        System.out.println("Connection is open — proceeding");
    }

    /** Throws ArrayIndexOutOfBoundsException for invalid array access. */
    public static void demonstrateArrayIndexOutOfBoundsException(int[] array, int index) {
        if (index < 0 || index >= array.length) {
            throw new ArrayIndexOutOfBoundsException(
                "Index " + index + " out of bounds for length " + array.length);
        }
        System.out.println("Element: " + array[index]);
    }

    /** Throws NumberFormatException when parsing invalid strings. */
    public static void demonstrateNumberFormatException(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: \"" + input + "\" is not a valid integer");
        }
    }

    /** Throws ArithmeticException on division by zero. */
    public static void demonstrateArithmeticException(int numerator, int denominator) {
        try {
            int result = numerator / denominator;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException: " + e.getMessage());
        }
    }

    /** Throws ClassCastException for invalid type casts. */
    public static void demonstrateClassCastException() {
        Object obj = "Hello";
        try {
            Integer num = (Integer) obj; // ClassCastException
        } catch (ClassCastException e) {
            System.out.println("ClassCastException: cannot cast String to Integer");
        }
    }

    // -----------------------------------------------------------------------
    // 3. When to Throw Unchecked Exceptions
    // -----------------------------------------------------------------------

    /**
     * Validates a user object. Throws unchecked exceptions for programming
     * errors — a null user or empty name indicates a bug in the calling code.
     */
    public static String validateUser(String name, int age) {
        if (name == null) {
            throw new NullPointerException("name must not be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        return name + " (age " + age + ")";
    }

    /**
     * Demonstrates defensive validation at method boundaries.
     */
    public static double calculateDiscount(double price, double percentage) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be 0-100: " + percentage);
        }
        return price * (1 - percentage / 100.0);
    }

    // -----------------------------------------------------------------------
    // 4. Production Patterns
    // -----------------------------------------------------------------------

    /**
     * Pattern: Global Uncaught Exception Handler.
     * Set this once at application startup to catch exceptions from any thread.
     */
    public static void installGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught exception in thread '" + thread.getName() + "': "
                + throwable.getMessage());
            throwable.printStackTrace();
        });
    }

    /**
     * Pattern: Logging before re-throwing.
     * Useful in layered architectures where you want to record the error
     * without swallowing it.
     */
    public static void logAndRethrow(Runnable operation) {
        try {
            operation.run();
        } catch (RuntimeException e) {
            System.err.println("[LOG] Exception: " + e.getMessage());
            throw e; // Re-throw after logging
        }
    }

    /**
     * Pattern: Defensive programming with Objects.requireNonNull.
     * Fail fast with a clear message.
     */
    public static String processOrder(String orderId, java.util.List<String> items) {
        java.util.Objects.requireNonNull(orderId, "orderId must not be null");
        java.util.Objects.requireNonNull(items, "items must not be null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        return "Order " + orderId + " with " + items.size() + " item(s)";
    }

    // -----------------------------------------------------------------------
    // 5. Custom Unchecked Exception Hierarchy
    // -----------------------------------------------------------------------

    /**
     * Base class for domain-specific unchecked exceptions.
     */
    public static class DomainException extends RuntimeException {

        private final String errorCode;

        public DomainException(String errorCode, String message) {
            super(message);
            this.errorCode = errorCode;
        }

        public DomainException(String errorCode, String message, Throwable cause) {
            super(message, cause);
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

    /**
     * Thrown when a withdrawal exceeds the available balance.
     */
    public static class InsufficientFundsException extends DomainException {

        private final double balance;
        private final double requested;

        public InsufficientFundsException(double balance, double requested) {
            super("INSUFFICIENT_FUNDS",
                "Balance " + balance + " is less than requested " + requested);
            this.balance = balance;
            this.requested = requested;
        }

        public double getBalance() {
            return balance;
        }

        public double getRequested() {
            return requested;
        }
    }

    /**
     * Thrown when an account is not found.
     */
    public static class AccountNotFoundException extends DomainException {

        public AccountNotFoundException(String accountId) {
            super("ACCOUNT_NOT_FOUND", "Account not found: " + accountId);
        }
    }

    // -----------------------------------------------------------------------
    // 6. Demonstrations
    // -----------------------------------------------------------------------

    /**
     * Demonstrates using the custom exception hierarchy.
     */
    public static void transferFunds(double fromBalance, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (fromBalance < amount) {
            throw new InsufficientFundsException(fromBalance, amount);
        }
        System.out.println("Transfer of " + amount + " successful. Remaining: " + (fromBalance - amount));
    }

    /**
     * Main method — runs all demonstrations.
     */
    public static void main(String[] args) {
        System.out.println("=== Unchecked Exception Demonstrations ===\n");

        // 1. NullPointerException
        System.out.println("--- NullPointerException ---");
        demonstrateNullPointerException();
        System.out.println();

        // 2. IllegalArgumentException
        System.out.println("--- IllegalArgumentException ---");
        try {
            demonstrateIllegalArgumentException(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        demonstrateIllegalArgumentException(25);
        System.out.println();

        // 3. IllegalStateException
        System.out.println("--- IllegalStateException ---");
        try {
            demonstrateIllegalStateException(false);
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        demonstrateIllegalStateException(true);
        System.out.println();

        // 4. ArrayIndexOutOfBoundsException
        System.out.println("--- ArrayIndexOutOfBoundsException ---");
        int[] numbers = {10, 20, 30, 40, 50};
        demonstrateArrayIndexOutOfBoundsException(numbers, 2);
        try {
            demonstrateArrayIndexOutOfBoundsException(numbers, 10);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // 5. NumberFormatException
        System.out.println("--- NumberFormatException ---");
        demonstrateNumberFormatException("42");
        demonstrateNumberFormatException("abc");
        System.out.println();

        // 6. ArithmeticException
        System.out.println("--- ArithmeticException ---");
        demonstrateArithmeticException(10, 3);
        demonstrateArithmeticException(10, 0);
        System.out.println();

        // 7. ClassCastException
        System.out.println("--- ClassCastException ---");
        demonstrateClassCastException();
        System.out.println();

        // 8. Validation
        System.out.println("--- Input Validation ---");
        System.out.println(validateUser("Alice", 30));
        try {
            validateUser(null, 25);
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        try {
            validateUser("Bob", -1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // 9. Defensive validation
        System.out.println("--- Defensive Validation ---");
        System.out.println("Discounted price: " + calculateDiscount(100.0, 20));
        try {
            calculateDiscount(-10.0, 20);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // 10. Custom exception hierarchy
        System.out.println("--- Custom Domain Exception ---");
        try {
            transferFunds(100.0, 150.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("  Error code: " + e.getErrorCode());
            System.out.println("  Balance: " + e.getBalance());
            System.out.println("  Requested: " + e.getRequested());
        }
        System.out.println();

        // 11. Process order with defensive validation
        System.out.println("--- Process Order ---");
        System.out.println(processOrder("ORD-001", java.util.List.of("Item A", "Item B")));
        try {
            processOrder(null, java.util.List.of("Item A"));
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        try {
            processOrder("ORD-002", java.util.List.of());
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== All Demonstrations Complete ===");
    }
}
