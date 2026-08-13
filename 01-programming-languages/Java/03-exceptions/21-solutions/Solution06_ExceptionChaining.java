package academy.javaengineering.exceptions.solutions;

/**
 * Solution 6: Exception chaining
 *
 * Catch low-level exception and wrap in domain-specific exception, preserving cause.
 */
public class Solution06_ExceptionChaining {

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
        try {
            if (id == null) {
                throw new DatabaseException("Connection refused");
            }
            System.out.println("Loaded data for: " + id);
        } catch (DatabaseException e) {
            throw new DataLoadException("Failed to load data for id: " + id, e);
        }
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
