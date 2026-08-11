package academy.javaengineering.exceptions.examples;

/**
 * Multiple catch blocks example showing how to handle different exception types.
 * Demonstrates the importance of catch block ordering and specific exception handling.
 */
public class MultipleCatchExample {

    public static void main(String[] args) {
        System.out.println("=== Multiple Catch Example ===");
        
        // Example 1: Different exception types
        testDifferentExceptions();
        
        // Example 2: Multi-catch syntax (Java 7+)
        testMultiCatchSyntax();
        
        // Example 3: Exception hierarchy consideration
        testExceptionHierarchy();
        
        System.out.println("=== End of Multiple Catch Example ===");
    }
    
    private static void testDifferentExceptions() {
        System.out.println("\n--- Different Exception Types ---");
        
        try {
            String str = null;
            str.length(); // NullPointerException
            
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException: " + e.getMessage());
            
        } catch (RuntimeException e) {
            // This won't catch NullPointerException because it's caught above
            System.out.println("Caught RuntimeException: " + e.getMessage());
            
        } catch (Exception e) {
            // Generic catch for other exceptions
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }
    
    private static void testMultiCatchSyntax() {
        System.out.println("\n--- Multi-Catch Syntax (Java 7+) ---");
        
        try {
            // This could throw different types of exceptions
            int[] arr = new int[5];
            arr[10] = 100; // ArrayIndexOutOfBoundsException
            
        } catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
            // Multi-catch: same handler for multiple exception types
            System.out.println("Caught with multi-catch: " + e.getClass().getSimpleName());
            
        } catch (Exception e) {
            System.out.println("Caught general exception: " + e.getMessage());
        }
    }
    
    private static void testExceptionHierarchy() {
        System.out.println("\n--- Exception Hierarchy ---");
        
        try {
            // Simulating a NumberFormatException
            Integer.parseInt("abc");
            
        } catch (NumberFormatException e) {
            System.out.println("Caught NumberFormatException (specific): " + e.getMessage());
            
        } catch (IllegalArgumentException e) {
            // NumberFormatException extends IllegalArgumentException
            // This would catch it if the specific catch above wasn't there
            System.out.println("Caught IllegalArgumentException: " + e.getMessage());
            
        } catch (RuntimeException e) {
            System.out.println("Caught RuntimeException: " + e.getMessage());
        }
    }
}