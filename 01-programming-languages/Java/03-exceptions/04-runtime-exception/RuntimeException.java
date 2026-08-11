/**
 * Demonstrates creating, throwing, and catching RuntimeExceptions.
 * Shows common subtypes including NullPointerException, IllegalArgumentException,
 * ArithmeticException, ClassCastException, NumberFormatException, and custom
 * unchecked exceptions with chaining.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers unchecked exception patterns,
 * precondition validation, and exception chaining.</p>
 */
package academy.javaengineering.exceptions.runtimeexception;

/**
 * Demonstrates creating, throwing, and catching RuntimeExceptions.
 * Shows common subtypes and their usage patterns.
 */
public class RuntimeExceptionDemo {

    /**
     * Demonstrates NullPointerException.
     */
    public static void nullPointerDemo() {
        System.out.println("=== NullPointerException ===");
        String text = null;
        try {
            text.length();
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates ArrayIndexOutOfBoundsException.
     */
    public static void arrayIndexDemo() {
        System.out.println("\n=== ArrayIndexOutOfBoundsException ===");
        int[] numbers = {1, 2, 3};
        try {
            int value = numbers[5];
            System.out.println("Value: " + value);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates IllegalArgumentException with precondition validation.
     */
    public static void illegalArgumentDemo() {
        System.out.println("\n=== IllegalArgumentException ===");
        try {
            setAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            setAge(200);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150, got: " + age);
        }
        System.out.println("Age set to: " + age);
    }

    /**
     * Demonstrates IllegalStateException.
     */
    public static void illegalStateDemo() {
        System.out.println("\n=== IllegalStateException ===");
        Connection conn = new Connection();
        conn.open();
        conn.open(); // second open should fail
        conn.close();
        conn.close(); // second close should fail
    }

    /**
     * Demonstrates ArithmeticException.
     */
    public static void arithmeticDemo() {
        System.out.println("\n=== ArithmeticException ===");
        try {
            int result = 10 / 0;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates ClassCastException.
     */
    public static void classCastDemo() {
        System.out.println("\n=== ClassCastException ===");
        Object obj = "Hello, World!";
        try {
            Integer number = (Integer) obj;
            System.out.println("Number: " + number);
        } catch (ClassCastException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates NumberFormatException.
     */
    public static void numberFormatDemo() {
        System.out.println("\n=== NumberFormatException ===");
        try {
            int value = Integer.parseInt("not a number");
            System.out.println("Value: " + value);
        } catch (NumberFormatException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates UnsupportedOperationException.
     */
    public static void unsupportedOperationDemo() {
        System.out.println("\n=== UnsupportedOperationException ===");
        java.util.List<String> list = java.util.List.of("a", "b", "c");
        try {
            list.add("d");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates custom RuntimeException subclass.
     */
    public static void customExceptionDemo() {
        System.out.println("\n=== Custom RuntimeException ===");
        try {
            processOrder(null);
        } catch (InvalidOrderException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void processOrder(String orderId) {
        if (orderId == null) {
            throw new InvalidOrderException("Order ID must not be null");
        }
        System.out.println("Processing order: " + orderId);
    }

    /**
     * Demonstrates exception chaining with RuntimeException.
     */
    public static void chainingDemo() {
        System.out.println("\n=== Exception Chaining ===");
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }

    private static void riskyOperation() {
        try {
            throw new java.io.IOException("File not found");
        } catch (java.io.IOException e) {
            throw new RuntimeException("Operation failed", e);
        }
    }

    /**
     * Demonstrates that RuntimeException is unchecked — no throws clause required.
     */
    public static void uncheckedDemo() {
        System.out.println("\n=== Unchecked — No throws clause needed ===");
        // This method does not declare "throws RuntimeException"
        // because the compiler does not require it for unchecked exceptions.
        String result = nullSafeTrim(null);
        System.out.println("Result: '" + result + "'");
    }

    private static String nullSafeTrim(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    public static void main(String[] args) {
        nullPointerDemo();
        arrayIndexDemo();
        illegalArgumentDemo();
        illegalStateDemo();
        arithmeticDemo();
        classCastDemo();
        numberFormatDemo();
        unsupportedOperationDemo();
        customExceptionDemo();
        chainingDemo();
        uncheckedDemo();
    }
}

/**
 * Custom unchecked exception for invalid order operations.
 */
class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}

/**
 * Simple connection class to demonstrate IllegalStateException.
 */
class Connection {
    private boolean open = false;

    public void open() {
        if (open) {
            throw new IllegalStateException("Connection is already open");
        }
        open = true;
        System.out.println("Connection opened");
    }

    public void close() {
        if (!open) {
            throw new IllegalStateException("Connection is already closed");
        }
        open = false;
        System.out.println("Connection closed");
    }
}
