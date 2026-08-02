package academy.javaengineering.exceptionhandling;

/**
 * Common Mistakes Examples
 * 
 * Demonstrates common exception handling mistakes and how to fix them.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CommonMistakesExamples {

    /**
     * Demonstrates common exception handling mistakes.
     */
    public static void demonstrateMistakes() {
        System.out.println("=== Common Exception Handling Mistakes ===\n");
        
        // Mistake 1: Catching too broadly
        System.out.println("Mistake 1: Catching too broadly");
        try {
            riskyOperation();
        } catch (Exception e) {
            System.out.println("  BAD: Caught generic Exception");
        }
        
        // Fix 1: Catch specific exceptions
        System.out.println("\nFix 1: Catch specific exceptions");
        try {
            riskyOperation();
        } catch (SpecificException e) {
            System.out.println("  GOOD: Caught SpecificException");
        }
        
        // Mistake 2: Empty catch block
        System.out.println("\nMistake 2: Empty catch block");
        try {
            riskyOperation();
        } catch (Exception e) {
            // Empty catch block - BAD!
            System.out.println("  BAD: Empty catch block");
        }
        
        // Fix 2: Log or rethrow
        System.out.println("\nFix 2: Log or rethrow");
        try {
            riskyOperation();
        } catch (SpecificException e) {
            System.out.println("  GOOD: Logged and handling: " + e.getMessage());
        }
        
        // Mistake 3: Using exceptions for control flow
        System.out.println("\nMistake 3: Using exceptions for control flow");
        String input = "not_a_number";
        try {
            Integer.parseInt(input);
            System.out.println("  Valid number");
        } catch (NumberFormatException e) {
            System.out.println("  BAD: Using exception for control flow");
        }
        
        // Fix 3: Pre-validation
        System.out.println("\nFix 3: Pre-validation");
        if (input != null && input.matches("-?\\d+")) {
            System.out.println("  Valid number");
        } else {
            System.out.println("  GOOD: Pre-validated input");
        }
        
        // Mistake 4: Swallowing exceptions
        System.out.println("\nMistake 4: Swallowing exceptions");
        try {
            riskyOperation();
        } catch (Exception e) {
            // Swallowed - BAD!
            System.out.println("  BAD: Exception swallowed");
        }
        
        // Fix 4: Handle properly
        System.out.println("\nFix 4: Handle properly");
        try {
            riskyOperation();
        } catch (SpecificException e) {
            System.out.println("  GOOD: Exception handled: " + e.getMessage());
        }
        
        // Mistake 5: Not preserving cause
        System.out.println("\nMistake 5: Not preserving cause");
        try {
            processDataBad();
        } catch (Exception e) {
            System.out.println("  BAD: Original exception lost");
        }
        
        // Fix 5: Preserve cause
        System.out.println("\nFix 5: Preserve cause");
        try {
            processDataGood();
        } catch (DataException e) {
            System.out.println("  GOOD: Original exception preserved");
            System.out.println("  Cause: " + e.getCause().getMessage());
        }
        
        System.out.println();
    }

    static void riskyOperation() throws SpecificException {
        throw new SpecificException("Something went wrong");
    }

    static void processDataBad() throws Exception {
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
     * Demonstrates more common mistakes.
     */
    public static void moreMistakes() {
        System.out.println("=== More Common Mistakes ===\n");
        
        // Mistake 6: Catching Throwable
        System.out.println("Mistake 6: Catching Throwable");
        try {
            riskyOperation();
        } catch (Throwable t) {
            System.out.println("  BAD: Caught Throwable (includes Errors)");
        }
        
        // Fix 6: Catch Exception
        System.out.println("\nFix 6: Catch Exception");
        try {
            riskyOperation();
        } catch (Exception e) {
            System.out.println("  GOOD: Caught Exception");
        }
        
        // Mistake 7: Exception in finally block
        System.out.println("\nMistake 7: Exception in finally block");
        try {
            System.out.println("  Original exception");
            throw new RuntimeException("Original");
        } finally {
            System.out.println("  BAD: Finally block throws exception");
        }
        
        // Fix 7: Handle exceptions in finally
        System.out.println("\nFix 7: Handle exceptions in finally");
        try {
            System.out.println("  Original exception");
            throw new RuntimeException("Original");
        } finally {
            try {
                // Cleanup code
                System.out.println("  GOOD: Finally block handles exceptions");
            } catch (Exception e) {
                System.out.println("  Cleanup failed: " + e.getMessage());
            }
        }
        
        // Mistake 8: Not closing resources
        System.out.println("\nMistake 8: Not closing resources");
        java.io.BufferedReader reader = null;
        try {
            reader = new java.io.BufferedReader(new java.io.StringReader("Data"));
            String line = reader.readLine();
            System.out.println("  Read: " + line);
            // BAD: Resource not closed in finally
        } catch (java.io.IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        
        // Fix 8: Use try-with-resources
        System.out.println("\nFix 8: Use try-with-resources");
        try (java.io.BufferedReader autoReader = new java.io.BufferedReader(
                new java.io.StringReader("Data"))) {
            String line = autoReader.readLine();
            System.out.println("  Read: " + line);
        } catch (java.io.IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        demonstrateMistakes();
        moreMistakes();
    }

    // Supporting classes

    static class SpecificException extends Exception {
        SpecificException(String message) {
            super(message);
        }
    }

    static class DataException extends Exception {
        DataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
