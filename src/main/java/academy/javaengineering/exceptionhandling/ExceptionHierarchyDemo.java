package academy.javaengineering.exceptionhandling;

/**
 * Exception Hierarchy Demo
 * 
 * Demonstrates the Java exception hierarchy and different exception types.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ExceptionHierarchyDemo {

    /**
     * Demonstrates the exception hierarchy.
     */
    public static void demonstrateHierarchy() {
        System.out.println("=== Exception Hierarchy ===\n");
        
        // Throwable
        System.out.println("Throwable: Base class for all errors and exceptions");
        
        // Error
        System.out.println("\nErrors (should not be caught):");
        System.out.println("- OutOfMemoryError");
        System.out.println("- StackOverflowError");
        System.out.println("- NoClassDefFoundError");
        
        // Exception
        System.out.println("\nExceptions (can be caught and handled):");
        
        // Checked Exceptions
        System.out.println("\nChecked Exceptions (must be declared or caught):");
        System.out.println("- IOException");
        System.out.println("- SQLException");
        System.out.println("- ClassNotFoundException");
        
        // Unchecked Exceptions
        System.out.println("\nUnchecked Exceptions (don't need to be declared):");
        System.out.println("- RuntimeException");
        System.out.println("  - NullPointerException");
        System.out.println("  - ArrayIndexOutOfBoundsException");
        System.out.println("  - ArithmeticException");
        System.out.println("  - IllegalArgumentException");
        System.out.println("  - IllegalStateException");
    }

    /**
     * Demonstrates checked vs unchecked exceptions.
     */
    public static void demonstrateCheckedVsUnchecked() {
        System.out.println("\n=== Checked vs Unchecked Exceptions ===\n");
        
        // Checked exception - must be caught or declared
        try {
            java.io.FileReader file = new java.io.FileReader("nonexistent.txt");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Checked Exception (FileNotFoundException): " + e.getMessage());
        }
        
        // Unchecked exception - don't need to be caught
        try {
            String text = null;
            int length = text.length();
        } catch (NullPointerException e) {
            System.out.println("Unchecked Exception (NullPointerException): " + e.getMessage());
        }
    }

    /**
     * Demonstrates exception propagation.
     */
    public static void demonstratePropagation() {
        System.out.println("\n=== Exception Propagation ===\n");
        
        try {
            method1();
        } catch (Exception e) {
            System.out.println("Caught in main: " + e.getMessage());
            System.out.println("Stack trace:");
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println("\tat " + element);
            }
        }
    }

    static void method1() throws Exception {
        try {
            method2();
        } catch (Exception e) {
            System.out.println("Caught in method1: " + e.getMessage());
            throw e; // Rethrow
        }
    }

    static void method2() throws Exception {
        try {
            method3();
        } catch (Exception e) {
            System.out.println("Caught in method2: " + e.getMessage());
            throw e; // Rethrow
        }
    }

    static void method3() throws Exception {
        throw new Exception("Exception from method3");
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        demonstrateHierarchy();
        demonstrateCheckedVsUnchecked();
        demonstratePropagation();
    }
}
