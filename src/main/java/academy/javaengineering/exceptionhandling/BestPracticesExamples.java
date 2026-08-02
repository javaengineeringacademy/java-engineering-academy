package academy.javaengineering.exceptionhandling;

import java.util.logging.Logger;

/**
 * Best Practices Examples
 * 
 * Demonstrates exception handling best practices.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class BestPracticesExamples {

    private static final Logger logger = Logger.getLogger(BestPracticesExamples.class.getName());

    /**
     * Demonstrates proper exception handling.
     */
    public static void properExceptionHandling() {
        System.out.println("=== Proper Exception Handling ===");
        
        // Bad practice
        try {
            riskyOperation();
        } catch (Exception e) {
            // Swallowed exception - BAD!
            System.out.println("Bad practice: Exception swallowed");
        }
        
        // Good practice
        try {
            riskyOperation();
        } catch (SpecificException e) {
            logger.warning("Specific error: " + e.getMessage());
            throw new ApplicationException("Operation failed", e);
        }
        
        System.out.println();
    }

    static void riskyOperation() throws SpecificException {
        throw new SpecificException("Something went wrong");
    }

    /**
     * Demonstrates pre-validation.
     */
    public static void preValidation() {
        System.out.println("=== Pre-Validation ===");
        
        // Bad practice - exception-based control flow
        try {
            Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("Bad practice: Using exceptions for control flow");
        }
        
        // Good practice - pre-validation
        String input = "abc";
        if (input != null && input.matches("-?\\d+")) {
            int number = Integer.parseInt(input);
            System.out.println("Parsed: " + number);
        } else {
            System.out.println("Good practice: Pre-validated input");
        }
        
        System.out.println();
    }

    /**
     * Demonstrates try-with-resources.
     */
    public static void tryWithResources() {
        System.out.println("=== Try-With-Resources ===");
        
        // Bad practice
        java.io.BufferedReader reader = null;
        try {
            reader = new java.io.BufferedReader(new java.io.StringReader("Data"));
            String line = reader.readLine();
            System.out.println("Read: " + line);
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (java.io.IOException e) {
                System.out.println("Close error: " + e.getMessage());
            }
        }
        
        // Good practice - try-with-resources
        try (java.io.BufferedReader autoReader = new java.io.BufferedReader(
                new java.io.StringReader("Data"))) {
            String line = autoReader.readLine();
            System.out.println("Read with auto-close: " + line);
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception chaining.
     */
    public static void exceptionChaining() {
        System.out.println("=== Exception Chaining ===");
        
        // Bad practice - losing original exception
        try {
            processData();
        } catch (Exception e) {
            System.out.println("Bad practice: Original exception lost");
        }
        
        // Good practice - preserving original exception
        try {
            processDataGood();
        } catch (DataException e) {
            System.out.println("Good practice: Original exception preserved");
            System.out.println("Cause: " + e.getCause().getMessage());
        }
        
        System.out.println();
    }

    static void processData() throws Exception {
        try {
            throw new java.io.IOException("IO error");
        } catch (Exception e) {
            throw new Exception("Failed"); // Original lost!
        }
    }

    static void processDataGood() throws DataException {
        try {
            throw new java.io.IOException("IO error");
        } catch (Exception e) {
            throw new DataException("Failed", e); // Original preserved
        }
    }

    /**
     * Demonstrates specific exception catching.
     */
    public static void specificExceptionCatching() {
        System.out.println("=== Specific Exception Catching ===");
        
        // Bad practice
        try {
            riskyOperation();
        } catch (Exception e) {
            System.out.println("Bad practice: Catching generic Exception");
        }
        
        // Good practice
        try {
            riskyOperation();
        } catch (SpecificException e) {
            System.out.println("Good practice: Catching specific exception");
        } catch (Exception e) {
            System.out.println("Good practice: Catching generic as fallback");
        }
        
        System.out.println();
    }

    /**
     * Demonstrates proper logging.
     */
    public static void properLogging() {
        System.out.println("=== Proper Logging ===");
        
        try {
            riskyOperation();
        } catch (Exception e) {
            // Bad practice
            System.out.println("Error occurred"); // No context
            
            // Good practice
            logger.severe(String.format("Operation failed: %s, Type: %s", 
                e.getMessage(), e.getClass().getSimpleName()));
        }
        
        System.out.println();
    }

    /**
     * Demonstrates empty catch blocks.
     */
    public static void emptyCatchBlocks() {
        System.out.println("=== Empty Catch Blocks ===");
        
        // Bad practice
        try {
            riskyOperation();
        } catch (Exception e) {
            // Empty catch block - BAD!
            System.out.println("Bad practice: Empty catch block");
        }
        
        // Good practice
        try {
            riskyOperation();
        } catch (Exception e) {
            // At least log the exception
            logger.warning("Operation failed: " + e.getMessage());
            System.out.println("Good practice: Exception logged");
        }
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        properExceptionHandling();
        preValidation();
        tryWithResources();
        exceptionChaining();
        specificExceptionCatching();
        properLogging();
        emptyCatchBlocks();
    }

    // Supporting classes

    static class SpecificException extends Exception {
        SpecificException(String message) {
            super(message);
        }
    }

    static class ApplicationException extends Exception {
        ApplicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class DataException extends Exception {
        DataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
