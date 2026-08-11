package academy.javaengineering.exceptions.examples;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Try-catch-finally example demonstrating cleanup operations.
 * Shows how finally block is used for resource cleanup regardless of exceptions.
 */
public class FinallyCleanupExample {

    public static void main(String[] args) {
        System.out.println("=== Finally Cleanup Example ===");
        
        // Example 1: Basic finally block
        testBasicFinally();
        
        // Example 2: File cleanup with finally
        testFileCleanup();
        
        // Example 3: Multiple resources cleanup
        testMultipleResourcesCleanup();
        
        System.out.println("=== End of Finally Cleanup Example ===");
    }
    
    private static void testBasicFinally() {
        System.out.println("\n--- Basic Finally Block ---");
        
        try {
            System.out.println("In try block");
            int result = 10 / 0; // ArithmeticException
            System.out.println("This won't execute: " + result);
            
        } catch (ArithmeticException e) {
            System.out.println("Caught exception: " + e.getMessage());
            
        } finally {
            // This always executes, whether exception occurs or not
            System.out.println("Finally block executed");
        }
        
        System.out.println("Program continues after try-catch-finally");
    }
    
    private static void testFileCleanup() {
        System.out.println("\n--- File Cleanup with Finally ---");
        
        PrintWriter writer = null;
        
        try {
            writer = new PrintWriter(new FileWriter("cleanup-test.txt"));
            writer.println("Writing to file");
            writer.println("Another line");
            
            // Simulate an exception during file operations
            if (Math.random() > 0.5) {
                throw new IOException("Simulated IO error");
            }
            
            System.out.println("File written successfully");
            
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
            
        } finally {
            // Always close the resource, regardless of exception
            if (writer != null) {
                writer.close();
                System.out.println("File writer closed in finally block");
            }
        }
    }
    
    private static void testMultipleResourcesCleanup() {
        System.out.println("\n--- Multiple Resources Cleanup ---");
        
        PrintWriter writer1 = null;
        PrintWriter writer2 = null;
        
        try {
            writer1 = new PrintWriter(new FileWriter("cleanup1.txt"));
            writer2 = new PrintWriter(new FileWriter("cleanup2.txt"));
            
            writer1.println("File 1 content");
            writer2.println("File 2 content");
            
            System.out.println("Both files written successfully");
            
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
            
        } finally {
            // Cleanup order matters - close in reverse order
            if (writer2 != null) {
                writer2.close();
                System.out.println("Writer 2 closed");
            }
            if (writer1 != null) {
                writer1.close();
                System.out.println("Writer 1 closed");
            }
        }
    }
}