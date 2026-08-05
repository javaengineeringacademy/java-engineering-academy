/**
 * Demonstrates basic try-catch-finally exception handling in Java.
 *
 * <p>This class covers fundamental exception handling concepts including
 * try-catch blocks, multiple catch blocks, and the finally block.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>try-catch blocks</li>
 *   <li>Multiple catch blocks</li>
 *   <li>finally block execution</li>
 *   <li>Exception message and stack trace</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ExceptionBasics {

    /**
     * Demonstrates basic try-catch-finally usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Basic try-catch
        basicTryCatch();

        // Multiple catch blocks
        multipleCatchBlocks();

        // Finally block
        finallyBlockDemo();

        // Nested try-catch
        nestedTryCatch();
    }

    /**
     * Basic try-catch example with ArithmeticException.
     */
    public static void basicTryCatch() {
        System.out.println("=== Basic Try-Catch ===");
        try {
            int result = 10 / 0;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println("Program continues after exception");
        // Expected output:
        // === Basic Try-Catch ===
        // Caught: / by zero
        // Program continues after exception
    }

    /**
     * Demonstrates multiple catch blocks with different exception types.
     */
    public static void multipleCatchBlocks() {
        System.out.println("\n=== Multiple Catch Blocks ===");
        try {
            String text = null;
            System.out.println(text.length());
        } catch (NullPointerException e) {
            System.out.println("NullPointer caught: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General exception: " + e.getMessage());
        }
        // Expected output:
        // === Multiple Catch Blocks ===
        // NullPointer caught: null
    }

    /**
     * Demonstrates finally block always executes.
     */
    public static void finallyBlockDemo() {
        System.out.println("\n=== Finally Block ===");
        try {
            int[] numbers = {1, 2, 3};
            System.out.println("Element: " + numbers[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index out of bounds: " + e.getMessage());
        } finally {
            System.out.println("Finally block always executes");
        }
        // Expected output:
        // === Finally Block ===
        // Index out of bounds: Index 5 out of bounds for length 3
        // Finally block always executes
    }

    /**
     * Demonstrates nested try-catch blocks.
     */
    public static void nestedTryCatch() {
        System.out.println("\n=== Nested Try-Catch ===");
        try {
            try {
                String str = null;
                str.length();
            } catch (NullPointerException e) {
                System.out.println("Inner catch: " + e.getMessage());
                throw new RuntimeException("Wrapped exception");
            }
        } catch (RuntimeException e) {
            System.out.println("Outer catch: " + e.getMessage());
        }
        // Expected output:
        // === Nested Try-Catch ===
        // Inner catch: null
        // Outer catch: Wrapped exception
    }
}
