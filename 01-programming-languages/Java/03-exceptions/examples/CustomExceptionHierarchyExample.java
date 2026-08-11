package academy.javaengineering.exceptions.examples;

/**
 * Custom exception hierarchy example demonstrating checked and unchecked exceptions.
 * Shows how to design exception hierarchies for different use cases.
 */
public class CustomExceptionHierarchyExample {

    // Base custom exception (checked)
    static class ApplicationException extends Exception {
        public ApplicationException(String message) {
            super(message);
        }
        
        public ApplicationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    // Checked exception for recoverable errors
    static class RecoverableException extends ApplicationException {
        private final String recoveryAction;
        
        public RecoverableException(String message, String recoveryAction) {
            super(message);
            this.recoveryAction = recoveryAction;
        }
        
        public String getRecoveryAction() {
            return recoveryAction;
        }
    }
    
    // Unchecked exception for programming errors
    static class ProgrammingException extends RuntimeException {
        public ProgrammingException(String message) {
            super(message);
        }
        
        public ProgrammingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    // Unchecked exception for system failures
    static class SystemException extends RuntimeException {
        private final int errorCode;
        
        public SystemException(String message, int errorCode) {
            super(message);
            this.errorCode = errorCode;
        }
        
        public int getErrorCode() {
            return errorCode;
        }
    }
    
    // Exception for specific domain
    static class UserException extends ApplicationException {
        public UserException(String message) {
            super(message);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Custom Exception Hierarchy Example ===");
        
        // Example 1: Checked exceptions
        testCheckedExceptions();
        
        // Example 2: Unchecked exceptions
        testUncheckedExceptions();
        
        // Example 3: Exception hierarchy in practice
        testExceptionHierarchyPractice();
        
        System.out.println("=== End of Custom Exception Hierarchy Example ===");
    }
    
    private static void testCheckedExceptions() {
        System.out.println("\n--- Checked Exceptions ---");
        
        try {
            processRecoverableOperation();
            
        } catch (RecoverableException e) {
            System.out.println("Recoverable error: " + e.getMessage());
            System.out.println("Recovery action: " + e.getRecoveryAction());
            
        } catch (ApplicationException e) {
            System.out.println("Application error: " + e.getMessage());
        }
    }
    
    private static void testUncheckedExceptions() {
        System.out.println("\n--- Unchecked Exceptions ---");
        
        try {
            processProgrammingOperation();
            
        } catch (ProgrammingException e) {
            System.out.println("Programming error: " + e.getMessage());
            System.out.println("This is a bug that should be fixed in code");
            
        } catch (SystemException e) {
            System.out.println("System error (code " + e.getErrorCode() + "): " + 
                e.getMessage());
        }
    }
    
    private static void testExceptionHierarchyPractice() {
        System.out.println("\n--- Exception Hierarchy in Practice ---");
        
        // Different catch strategies
        try {
            performUserOperation();
            
        } catch (UserException e) {
            // Handle user-specific errors
            System.out.println("User error: " + e.getMessage());
            
        } catch (RecoverableException e) {
            // Handle recoverable errors
            System.out.println("Recoverable: " + e.getMessage());
            
        } catch (ApplicationException e) {
            // Handle other application errors
            System.out.println("Application error: " + e.getMessage());
            
        } catch (RuntimeException e) {
            // Handle unexpected runtime errors
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    
    private static void processRecoverableOperation() throws RecoverableException {
        // Simulate recoverable error
        if (Math.random() > 0.5) {
            throw new RecoverableException("Service temporarily unavailable", 
                "Try again in 30 seconds");
        }
        System.out.println("Recoverable operation completed successfully");
    }
    
    private static void processProgrammingOperation() {
        // Simulate programming error
        String[] array = null;
        try {
            // This would throw NullPointerException
            array.length;
        } catch (NullPointerException e) {
            throw new ProgrammingException("Array cannot be null", e);
        }
    }
    
    private static void performUserOperation() throws UserException {
        // Simulate user error
        if (Math.random() > 0.5) {
            throw new UserException("Invalid user input");
        }
        System.out.println("User operation completed successfully");
    }
}