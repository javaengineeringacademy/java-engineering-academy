package academy.javaengineering.exceptionhandling;

/**
 * Summary Examples
 * 
 * Provides a summary of all exception handling concepts.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SummaryExamples {

    /**
     * Provides a summary of exception handling concepts.
     */
    public static void provideSummary() {
        System.out.println("=== Exception Handling Summary ===\n");
        
        System.out.println("1. Exception Hierarchy:");
        System.out.println("   - Throwable → Error | Exception");
        System.out.println("   - Exception → Checked | RuntimeException (Unchecked)");
        System.out.println();
        
        System.out.println("2. Try-Catch-Finally:");
        System.out.println("   - try: Code that might throw exception");
        System.out.println("   - catch: Handle the exception");
        System.out.println("   - finally: Always execute (cleanup)");
        System.out.println();
        
        System.out.println("3. Throw vs Throws:");
        System.out.println("   - throw: Actually抛出 an exception");
        System.out.println("   - throws: Declare exceptions in method signature");
        System.out.println();
        
        System.out.println("4. Custom Exceptions:");
        System.out.println("   - Extend Exception or RuntimeException");
        System.out.println("   - Provide meaningful messages");
        System.out.println("   - Include contextual information");
        System.out.println();
        
        System.out.println("5. Best Practices:");
        System.out.println("   - Catch specific exceptions");
        System.out.println("   - Don't swallow exceptions");
        System.out.println("   - Use try-with-resources");
        System.out.println("   - Preserve exception causes");
        System.out.println("   - Log exceptions properly");
        System.out.println("   - Document exceptions");
        System.out.println();
        
        System.out.println("6. Patterns:");
        System.out.println("   - Retry: Attempt operation multiple times");
        System.out.println("   - Circuit Breaker: Stop calling failing service");
        System.out.println("   - Fallback: Use alternative approach");
        System.out.println("   - Recovery: Handle and continue");
        System.out.println();
    }

    /**
     * Demonstrates key concepts.
     */
    public static void demonstrateKeyConcepts() {
        System.out.println("=== Key Concepts Demonstration ===\n");
        
        // Basic exception handling
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        
        // Try-with-resources
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.StringReader("Data"))) {
            System.out.println("Read: " + reader.readLine());
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Exception chaining
        try {
            try {
                throw new java.io.IOException("IO error");
            } catch (java.io.IOException e) {
                throw new RuntimeException("Wrapped error", e);
            }
        } catch (RuntimeException e) {
            System.out.println("Chained: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
        
        // Custom exception
        try {
            throw new CustomException("Custom error", "CUSTOM_001");
        } catch (CustomException e) {
            System.out.println("Custom: " + e.getMessage());
            System.out.println("Code: " + e.getErrorCode());
        }
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        provideSummary();
        demonstrateKeyConcepts();
    }

    // Supporting class

    static class CustomException extends Exception {
        private final String errorCode;
        
        CustomException(String message, String errorCode) {
            super(message);
            this.errorCode = errorCode;
        }
        
        String getErrorCode() {
            return errorCode;
        }
    }
}
