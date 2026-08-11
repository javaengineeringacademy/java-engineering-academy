package academy.javaengineering.exceptions.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Try-with-resources and exception translation example.
 * Demonstrates TWR syntax and how to translate exceptions for better abstraction.
 */
public class TWRwithExceptionTranslationExample {

    // Custom exception for layer abstraction
    static class DataAccessException extends Exception {
        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== TWR with Exception Translation Example ===");
        
        // Example 1: Basic try-with-resources
        testBasicTWR();
        
        // Example 2: Exception translation pattern
        testExceptionTranslation();
        
        // Example 3: Multiple resources in TWR
        testMultipleResourcesTWR();
        
        System.out.println("=== End of TWR with Exception Translation Example ===");
    }
    
    private static void testBasicTWR() {
        System.out.println("\n--- Basic Try-With-Resources ---");
        
        // Resource implements AutoCloseable
        try (BufferedReader reader = new BufferedReader(new FileReader("test.txt"))) {
            String line = reader.readLine();
            System.out.println("First line: " + line);
            
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
        // reader is automatically closed here, even if exception occurs
    }
    
    private static void testExceptionTranslation() {
        System.out.println("\n--- Exception Translation Pattern ---");
        
        try {
            String data = readDataFromFile("data.txt");
            System.out.println("Data read: " + data);
            
        } catch (DataAccessException e) {
            System.out.println("Caught DataAccessException: " + e.getMessage());
            System.out.println("Original cause: " + e.getCause().getClass().getSimpleName());
        }
    }
    
    private static void testMultipleResourcesTWR() {
        System.out.println("\n--- Multiple Resources in TWR ---");
        
        // Multiple resources separated by semicolons
        try (BufferedReader reader1 = new BufferedReader(new FileReader("file1.txt"));
             BufferedReader reader2 = new BufferedReader(new FileReader("file2.txt"))) {
            
            String line1 = reader1.readLine();
            String line2 = reader2.readLine();
            System.out.println("Lines read: " + line1 + ", " + line2);
            
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
        // Both readers are automatically closed in reverse declaration order
    }
    
    private static String readDataFromFile(String filename) throws DataAccessException {
        // Translate low-level IOException to higher-level DataAccessException
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString().trim();
            
        } catch (IOException e) {
            // Translate exception - wrap low-level exception in high-level one
            throw new DataAccessException("Failed to read data from " + filename, e);
        }
    }
}