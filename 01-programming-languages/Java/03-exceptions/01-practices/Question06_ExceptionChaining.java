package academy.javaengineering.exceptions.questions;

/**
 * Question 6: Exception chaining
 *
 * Task: Complete the method to catch a low-level exception and wrap it
 * in a domain-specific exception, preserving the original cause.
 */
public class Question06_ExceptionChaining {

    public static class DataLoadException extends Exception {
        public DataLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }

    public static void loadData(String id) throws DataLoadException {
        // TODO: Simulate a database query that throws DatabaseException
        // Catch DatabaseException and wrap it in DataLoadException
        // Preserve the original cause
    }

    public static void main(String[] args) {
        try {
            loadData("123");
        } catch (DataLoadException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
        }
    }
}
