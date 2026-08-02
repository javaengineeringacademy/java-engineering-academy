package academy.javaengineering.exceptionhandling;

/**
 * Demonstrates exception propagation with throw and throws keywords.
 *
 * <p>This class shows how exceptions propagate up the call stack and the
 * difference between throw and throws keywords.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>throw keyword for throwing exceptions</li>
 *   <li>throws keyword for declaring exceptions</li>
 *   <li>Exception propagation up the call stack</li>
 *   <li>Chaining exceptions</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ExceptionPropagation {

    /**
     * Demonstrates exception propagation patterns.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Exception propagation demo
        propagationDemo();

        // Throw vs throws
        throwVsThrows();

        // Exception chaining
        exceptionChaining();
    }

    /**
     * Demonstrates exception propagation up the call stack.
     */
    public static void propagationDemo() {
        System.out.println("=== Exception Propagation ===");
        try {
            methodA();
        } catch (Exception e) {
            System.out.println("Caught in main: " + e.getMessage());
        }
        // Expected output:
        // === Exception Propagation ===
        // methodA: Calling methodB
        // methodB: Calling methodC
        // methodC: Throwing exception
        // Caught in main: Error in methodC
    }

    /**
     * Method A - calls method B.
     */
    public static void methodA() {
        System.out.println("methodA: Calling methodB");
        methodB();
    }

    /**
     * Method B - calls method C.
     */
    public static void methodB() {
        System.out.println("methodB: Calling methodC");
        methodC();
    }

    /**
     * Method C - throws exception.
     */
    public static void methodC() {
        System.out.println("methodC: Throwing exception");
        throw new RuntimeException("Error in methodC");
    }

    /**
     * Demonstrates difference between throw and throws.
     */
    public static void throwVsThrows() {
        System.out.println("\n=== Throw vs Throws ===");

        // throw: Used to actually throw an exception
        try {
            throwException();
        } catch (IllegalArgumentException e) {
            System.out.println("throw keyword caught: " + e.getMessage());
        }

        // throws: Used to declare exceptions in method signature
        try {
            declareException();
        } catch (CustomCheckedException e) {
            System.out.println("throws keyword caught: " + e.getMessage());
        }
        // Expected output:
        // === Throw vs Throws ===
        // throw keyword caught: Invalid argument
        // throws keyword caught: Declared exception
    }

    /**
     * Demonstrates throw keyword usage.
     */
    public static void throwException() {
        throw new IllegalArgumentException("Invalid argument");
    }

    /**
     * Demonstrates throws keyword usage.
     *
     * @throws CustomCheckedException if something goes wrong
     */
    public static void declareException() throws CustomCheckedException {
        throw new CustomCheckedException("Declared exception");
    }

    /**
     * Demonstrates exception chaining for better debugging.
     */
    public static void exceptionChaining() {
        System.out.println("\n=== Exception Chaining ===");
        try {
            processData();
        } catch (DataProcessingException e) {
            System.out.println("Chained exception:");
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Cause: " + e.getCause().getMessage());
            System.out.println("  Cause class: " + e.getCause().getClass().getSimpleName());
        }
        // Expected output:
        // === Exception Chaining ===
        // Chained exception:
        //   Message: Failed to process data
        //   Cause: Invalid data format
        //   Cause class: IllegalArgumentException
    }

    /**
     * Processes data and chains exceptions.
     *
     * @throws DataProcessingException if processing fails
     */
    public static void processData() throws DataProcessingException {
        try {
            validateData(null);
        } catch (IllegalArgumentException e) {
            throw new DataProcessingException("Failed to process data", e);
        }
    }

    /**
     * Validates data and throws exception.
     *
     * @param data the data to validate
     */
    public static void validateData(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Invalid data format");
        }
    }

    /**
     * Custom checked exception for chaining examples.
     */
    public static class CustomCheckedException extends Exception {

        /**
         * Constructs a CustomCheckedException.
         *
         * @param message the error message
         */
        public CustomCheckedException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception for data processing errors.
     */
    public static class DataProcessingException extends Exception {

        /**
         * Constructs a DataProcessingException with chaining.
         *
         * @param message the error message
         * @param cause the underlying cause
         */
        public DataProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
