package academy.javaengineering.exceptionhandling;

import java.io.*;

/**
 * Finally Block Examples
 * 
 * Demonstrates the usage and behavior of the finally block.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class FinallyExamples {

    /**
     * Demonstrates basic finally block usage.
     */
    public static void basicFinally() {
        System.out.println("=== Basic Finally Block ===");
        
        try {
            System.out.println("In try block");
            int result = 10 / 0;
            System.out.println("This won't print");
        } catch (ArithmeticException e) {
            System.out.println("In catch block: " + e.getMessage());
        } finally {
            System.out.println("In finally block - always executes");
        }
        
        System.out.println();
    }

    /**
     * Demonstrates finally with return statement.
     */
    public static int finallyWithReturn() {
        System.out.println("=== Finally With Return ===");
        
        try {
            System.out.println("In try block");
            return 1;
        } finally {
            System.out.println("In finally block");
            // Note: finally executes even with return
        }
    }

    /**
     * Demonstrates resource cleanup pattern.
     */
    public static void resourceCleanup() {
        System.out.println("=== Resource Cleanup Pattern ===");
        
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new StringReader("Hello\nWorld"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                    System.out.println("Reader closed in finally");
                }
            } catch (IOException e) {
                System.out.println("Error closing: " + e.getMessage());
            }
        }
        
        System.out.println();
    }

    /**
     * Demonstrates try-with-resources (Java 7+).
     */
    public static void tryWithResources() {
        System.out.println("=== Try-With-Resources ===");
        
        try (BufferedReader reader = new BufferedReader(new StringReader("Hello\nWorld"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // reader automatically closed - no finally needed
        
        System.out.println();
    }

    /**
     * Demonstrates multiple resources cleanup.
     */
    public static void multipleResources() {
        System.out.println("=== Multiple Resources Cleanup ===");
        
        try (BufferedReader reader = new BufferedReader(new StringReader("Data"));
             StringWriter writer = new StringWriter()) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
            
            System.out.println("Written: " + writer.toString().trim());
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates finally exception handling.
     */
    public static void finallyExceptionHandling() {
        System.out.println("=== Finally Exception Handling ===");
        
        Exception originalException = null;
        
        try {
            System.out.println("In try block");
            throw new RuntimeException("Original exception");
        } catch (RuntimeException e) {
            System.out.println("In catch block: " + e.getMessage());
            originalException = e;
        } finally {
            System.out.println("In finally block");
            
            try {
                // Simulate cleanup that might throw
                if (true) {
                    throw new RuntimeException("Exception in finally");
                }
            } catch (RuntimeException cleanupException) {
                System.out.println("Cleanup exception: " + cleanupException.getMessage());
                
                // Preserve original exception
                if (originalException != null) {
                    originalException.addSuppressed(cleanupException);
                }
            }
        }
        
        if (originalException != null) {
            System.out.println("Original exception: " + originalException.getMessage());
            if (originalException.getSuppressed().length > 0) {
                System.out.println("Suppressed exceptions: " + 
                    java.util.Arrays.toString(originalException.getSuppressed()));
            }
        }
        
        System.out.println();
    }

    /**
     * Demonstrates finally without catch.
     */
    public static void finallyWithoutCatch() throws Exception {
        System.out.println("=== Finally Without Catch ===");
        
        try {
            System.out.println("In try block");
            throw new Exception("Exception in try");
        } finally {
            System.out.println("In finally block - executes even without catch");
        }
        // Exception propagates after finally
    }

    /**
     * Demonstrates finally block execution order.
     */
    public static void executionOrder() {
        System.out.println("=== Execution Order ===");
        
        try {
            System.out.println("1. Try block");
            int result = 10 / 0;
            System.out.println("2. After division (won't print)");
        } catch (ArithmeticException e) {
            System.out.println("3. Catch block");
        } finally {
            System.out.println("4. Finally block");
        }
        
        System.out.println("5. After try-catch-finally");
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        basicFinally();
        
        int returnResult = finallyWithReturn();
        System.out.println("Return value: " + returnResult);
        System.out.println();
        
        resourceCleanup();
        tryWithResources();
        multipleResources();
        finallyExceptionHandling();
        executionOrder();
        
        try {
            finallyWithoutCatch();
        } catch (Exception e) {
            System.out.println("Caught propagated exception: " + e.getMessage());
        }
    }
}
