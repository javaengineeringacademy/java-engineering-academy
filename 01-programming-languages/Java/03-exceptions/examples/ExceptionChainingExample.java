package academy.javaengineering.exceptions.examples;

/**
 * Exception chaining example demonstrating wrapping exceptions with cause chain.
 * Shows how to maintain exception context while providing meaningful abstractions.
 */
public class ExceptionChainingExample {

    // Custom exceptions with cause chaining
    static class ServiceException extends Exception {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class RepositoryException extends Exception {
        public RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
        
        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Exception Chaining Example ===");
        
        // Example 1: Basic cause chaining
        testBasicChaining();
        
        // Example 2: Multi-layer exception chaining
        testMultiLayerChaining();
        
        // Example 3: Exception chaining with custom messages
        testCustomMessageChaining();
        
        System.out.println("=== End of Exception Chaining Example ===");
    }
    
    private static void testBasicChaining() {
        System.out.println("\n--- Basic Cause Chaining ---");
        
        try {
            // Simulate exception that wraps another
            throw new ServiceException("Service failed", 
                new RuntimeException("Root cause"));
                
        } catch (ServiceException e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
            
            // Print full stack trace to see cause chain
            System.out.println("Full stack trace:");
            e.printStackTrace(System.out);
        }
    }
    
    private static void testMultiLayerChaining() {
        System.out.println("\n--- Multi-Layer Chaining ---");
        
        try {
            processData();
            
        } catch (ServiceException e) {
            System.out.println("Top-level exception: " + e.getMessage());
            
            // Walk the cause chain
            Throwable cause = e.getCause();
            int level = 1;
            while (cause != null) {
                System.out.println("Cause level " + level + ": " + 
                    cause.getClass().getSimpleName() + " - " + cause.getMessage());
                cause = cause.getCause();
                level++;
            }
        }
    }
    
    private static void testCustomMessageChaining() {
        System.out.println("\n--- Custom Message Chaining ---");
        
        try {
            validateAndProcess("invalid");
            
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Root cause: " + e.getCause().getMessage());
            }
        }
    }
    
    private static void processData() throws ServiceException {
        try {
            // Simulate repository layer exception
            throw new RepositoryException("Database connection failed",
                new RuntimeException("Connection timeout"));
                
        } catch (RepositoryException e) {
            // Translate to service layer exception
            throw new ServiceException("Service operation failed", e);
        }
    }
    
    private static void validateAndProcess(String input) throws ValidationException {
        try {
            // Simulate validation that throws runtime exception
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be empty");
            }
            
            // Simulate processing
            Integer.parseInt(input);
            
        } catch (IllegalArgumentException e) {
            // Chain with custom message
            throw new ValidationException("Validation failed for input: " + input, e);
            
        } catch (NumberFormatException e) {
            // Different exception type with different message
            throw new ValidationException("Invalid number format: " + input, e);
        }
    }
}