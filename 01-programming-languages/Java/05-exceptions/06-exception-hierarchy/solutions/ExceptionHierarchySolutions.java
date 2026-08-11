package academy.javaengineering.exceptions.hierarchy;

import java.io.IOException;

/**
 * Solutions for the exception hierarchy exercises.
 */
public class ExceptionHierarchySolutions {

    // ========================================
    // Exercise 1: Custom Exception Hierarchy
    // ========================================

    /**
     * Base checked exception for the banking application.
     */
    public static class BankException extends Exception {
        public BankException(String message) {
            super(message);
        }
        public BankException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when there are insufficient funds.
     */
    public static class InsufficientFundsException extends BankException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when an account is invalid.
     */
    public static class InvalidAccountException extends BankException {
        public InvalidAccountException(String message) {
            super(message);
        }
    }

    /**
     * Base unchecked exception for the banking application.
     */
    public static class BankRuntimeException extends RuntimeException {
        public BankRuntimeException(String message) {
            super(message);
        }
        public BankRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Thrown when an account is locked.
     */
    public static class AccountLockedException extends BankRuntimeException {
        public AccountLockedException(String message) {
            super(message);
        }
    }

    // ========================================
    // Exercise 2: Method with Different Exceptions
    // ========================================

    /**
     * Returns the input string or throws an exception based on input.
     *
     * @param input the input string
     * @return the input string if valid
     * @throws IOException if input equals "error"
     * @throws NullPointerException if input is null
     * @throws IllegalArgumentException if input is empty
     */
    public static String exercise2(String input) throws IOException {
        if (input == null) {
            throw new NullPointerException("Input cannot be null");
        }
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        if (input.equals("error")) {
            throw new IOException("Error occurred");
        }
        return input;
    }

    // ========================================
    // Exercise 3: Exception Chaining
    // ========================================

    /**
     * Demonstrates exception chaining.
     */
    public static void exercise3() {
        try {
            try {
                throw new IOException("Original IO error");
            } catch (IOException e) {
                throw new DatabaseException("Database operation failed", e);
            }
        } catch (DatabaseException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }

    // ========================================
    // Exercise 4: Correct Order of Catching
    // ========================================

    /**
     * Demonstrates correct exception catching order.
     */
    public static void exercise4() {
        try {
            throw new NullPointerException("Null pointer");
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException (most specific)");
        } catch (RuntimeException e) {
            System.out.println("Caught RuntimeException");
        } catch (Exception e) {
            System.out.println("Caught Exception");
        }
    }

    // ========================================
    // Exercise 5: instanceof Checks
    // ========================================

    /**
     * Demonstrates instanceof checks for exception hierarchy.
     */
    public static void exercise5() {
        Throwable t = new IllegalArgumentException("Bad argument");

        if (t instanceof Error) {
            System.out.println("Caught Error: " + t.getMessage());
        } else if (t instanceof RuntimeException) {
            System.out.println("Caught RuntimeException: " + t.getMessage());
        } else if (t instanceof Exception) {
            System.out.println("Caught Exception: " + t.getMessage());
        } else {
            System.out.println("Caught Throwable: " + t.getMessage());
        }
    }

    // ========================================
    // Custom Exception Classes
    // ========================================

    public static class DatabaseException extends BankException {
        public DatabaseException(String message) {
            super(message);
        }
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ========================================
    // Main Method
    // ========================================

    public static void main(String[] args) {
        System.out.println("=== Exception Hierarchy Solutions ===\n");

        // Exercise 1: Custom Exception Hierarchy
        System.out.println("--- Exercise 1: Custom Exception Hierarchy ---");
        System.out.println("BankException -> InsufficientFundsException");
        System.out.println("BankException -> InvalidAccountException");
        System.out.println("BankRuntimeException -> AccountLockedException");
        System.out.println();

        // Exercise 2: Method with Different Exceptions
        System.out.println("--- Exercise 2: Method with Different Exceptions ---");
        try {
            exercise2("Hello");
            System.out.println("Valid input: Hello");
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }

        try {
            exercise2("error");
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }

        try {
            exercise2("");
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
        System.out.println();

        // Exercise 3: Exception Chaining
        System.out.println("--- Exercise 3: Exception Chaining ---");
        exercise3();
        System.out.println();

        // Exercise 4: Correct Order of Catching
        System.out.println("--- Exercise 4: Correct Order of Catching ---");
        exercise4();
        System.out.println();

        // Exercise 5: instanceof Checks
        System.out.println("--- Exercise 5: instanceof Checks ---");
        exercise5();
    }
}
