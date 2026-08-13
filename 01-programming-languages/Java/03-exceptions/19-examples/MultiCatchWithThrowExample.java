package academy.javaengineering.exceptions.examples;

/**
 * Multi-catch with throw example combining multiple exception handling concepts.
 * Demonstrates multi-catch syntax with throwing custom exceptions.
 */
public class MultiCatchWithThrowExample {

    // Custom exception class
    static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Multi-Catch with Throw Example ===");
        
        // Example 1: Multi-catch with validation and throw
        testValidationWithMultiCatch();
        
        // Example 2: Chaining multi-catch with custom exception
        testChainingWithCustomException();
        
        System.out.println("=== End of Multi-Catch with Throw Example ===");
    }
    
    private static void testValidationWithMultiCatch() {
        System.out.println("\n--- Validation with Multi-Catch ---");
        
        try {
            validateAge(25); // Valid
            validateAge(-5); // Will throw ValidationException
            validateAge(150); // Will throw ValidationException
            
        } catch (ValidationException e) {
            System.out.println("Caught ValidationException: " + e.getMessage());
            
        } catch (NullPointerException | IllegalArgumentException e) {
            // Multi-catch for runtime exceptions
            System.out.println("Caught runtime exception: " + e.getClass().getSimpleName());
        }
    }
    
    private static void testChainingWithCustomException() {
        System.out.println("\n--- Chaining with Custom Exception ---");
        
        try {
            processData("invalid");
            
        } catch (ValidationException e) {
            System.out.println("Caught ValidationException: " + e.getMessage());
            System.out.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "None"));
        }
    }
    
    private static void validateAge(int age) throws ValidationException {
        if (age < 0 || age > 120) {
            throw new ValidationException("Invalid age: " + age);
        }
        System.out.println("Age " + age + " is valid");
    }
    
    private static void processData(String input) throws ValidationException {
        try {
            // Simulating processing that might throw different exceptions
            if (input == null) {
                throw new NullPointerException("Input cannot be null");
            }
            
            if (input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be empty");
            }
            
            // Simulate parsing
            Integer.parseInt(input);
            
        } catch (NullPointerException | IllegalArgumentException e) {
            // Convert runtime exceptions to checked exception
            throw new ValidationException("Data validation failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid number format: " + input);
        }
    }
}