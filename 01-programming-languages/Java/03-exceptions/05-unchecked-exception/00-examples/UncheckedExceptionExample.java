package academy.javaengineering.exceptions.uncheckedexception.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Examples demonstrating unchecked exceptions in practice.
 *
 * <p>Run this class to see each unchecked exception type in action.</p>
 */
public final class UncheckedExceptionExample {

    private UncheckedExceptionExample() {}

    // -----------------------------------------------------------------------
    // Example 1: NullPointerException
    // -----------------------------------------------------------------------

    /** Demonstrates how NullPointerException is thrown. */
    public static void nullPointerExample() {
        System.out.println("=== NullPointerException ===");
        String name = null;

        // Uncomment the line below to see the exception:
        // name.length(); // NullPointerException

        // Safe alternative:
        if (name != null) {
            System.out.println("Name length: " + name.length());
        } else {
            System.out.println("Name is null — skipping");
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 2: IllegalArgumentException
    // -----------------------------------------------------------------------

    /** Method with input validation using IllegalArgumentException. */
    public static void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    public static void illegalArgumentExample() {
        System.out.println("=== IllegalArgumentException ===");
        try {
            setAge(25);  // OK
            setAge(-5);  // Throws IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 3: IndexOutOfBoundsException
    // -----------------------------------------------------------------------

    /** Accesses a list element with validation. */
    public static String safeGet(List<String> list, int index) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size " + list.size());
        }
        return list.get(index);
    }

    public static void indexOutOfBoundsExample() {
        System.out.println("=== IndexOutOfBoundsException ===");
        List<String> colors = List.of("Red", "Green", "Blue");

        System.out.println("Element at 1: " + safeGet(colors, 1));
        try {
            safeGet(colors, 10);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 4: NumberFormatException
    // -----------------------------------------------------------------------

    /** Parses a string to an integer with error handling. */
    public static int parseAge(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("\"" + input + "\" is not a valid number");
        }
    }

    public static void numberFormatExample() {
        System.out.println("=== NumberFormatException ===");
        try {
            System.out.println("Parsed age: " + parseAge("30"));
            parseAge("abc");
        } catch (NumberFormatException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 5: ArithmeticException
    // -----------------------------------------------------------------------

    /** Divides two numbers with zero-check. */
    public static double safeDivide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide " + a + " by zero");
        }
        return (double) a / b;
    }

    public static void arithmeticExample() {
        System.out.println("=== ArithmeticException ===");
        try {
            System.out.println("10 / 3 = " + safeDivide(10, 3));
            System.out.println("10 / 0 = " + safeDivide(10, 0));
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 6: ClassCastException
    // -----------------------------------------------------------------------

    /** Demonstrates ClassCastException with unsafe cast. */
    public static void classCastExample() {
        System.out.println("=== ClassCastException ===");
        Object obj = "Hello";

        try {
            Integer num = (Integer) obj; // ClassCastException
        } catch (ClassCastException e) {
            System.out.println("Caught: cannot cast String to Integer");
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 7: IllegalStateException
    // -----------------------------------------------------------------------

    /** Demonstrates IllegalStateException for wrong object state. */
    static class Connection {
        private boolean open = false;

        public void open() {
            this.open = true;
            System.out.println("Connection opened");
        }

        public void close() {
            this.open = false;
            System.out.println("Connection closed");
        }

        public void execute(String query) {
            if (!open) {
                throw new IllegalStateException("Connection is not open");
            }
            System.out.println("Executing: " + query);
        }
    }

    public static void illegalStateExample() {
        System.out.println("=== IllegalStateException ===");
        Connection conn = new Connection();

        try {
            conn.execute("SELECT * FROM users"); // Not open yet
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        conn.open();
        conn.execute("SELECT * FROM users");
        conn.close();
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 8: Defensive Validation Pattern
    // -----------------------------------------------------------------------

    /** Demonstrates defensive validation at method boundaries. */
    public static String createOrder(String productId, int quantity, double price) {
        Objects.requireNonNull(productId, "productId must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative: " + price);
        }
        double total = quantity * price;
        return String.format("Order: %s x%d @ $%.2f = $%.2f",
            productId, quantity, price, total);
    }

    public static void defensiveValidationExample() {
        System.out.println("=== Defensive Validation Pattern ===");
        System.out.println(createOrder("WIDGET", 3, 9.99));

        try {
            createOrder(null, 1, 5.0);
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            createOrder("GADGET", -2, 10.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Example 9: Global Uncaught Exception Handler
    // -----------------------------------------------------------------------

    /** Demonstrates setting a global uncaught exception handler. */
    public static void globalHandlerExample() {
        System.out.println("=== Global Uncaught Exception Handler ===");
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.printf("Uncaught exception in thread '%s': %s%n",
                thread.getName(), throwable.getMessage());
        });

        Thread t = new Thread(() -> {
            throw new RuntimeException("Something went wrong in background thread");
        }, "BackgroundThread");

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    // -----------------------------------------------------------------------
    // Main
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Unchecked Exception Examples ===\n");

        nullPointerExample();
        illegalArgumentExample();
        indexOutOfBoundsExample();
        numberFormatExample();
        arithmeticExample();
        classCastExample();
        illegalStateExample();
        defensiveValidationExample();
        globalHandlerExample();

        System.out.println("=== All Examples Complete ===");
    }
}
