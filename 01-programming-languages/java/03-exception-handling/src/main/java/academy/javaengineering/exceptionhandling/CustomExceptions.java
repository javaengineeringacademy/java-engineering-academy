package academy.javaengineering.exceptionhandling;

/**
 * Demonstrates creating and using custom exception classes.
 *
 * <p>This class shows how to create custom checked and unchecked exceptions,
 * including exception chaining and proper constructor patterns.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Custom checked exceptions</li>
 *   <li>Custom unchecked exceptions</li>
 *   <li>Exception chaining</li>
 *   <li>Exception constructors</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class CustomExceptions {

    /**
     * Custom checked exception for business validation.
     */
    public static class ValidationException extends Exception {

        private final String fieldName;

        /**
         * Constructs a ValidationException with a message.
         *
         * @param message the error message
         */
        public ValidationException(String message) {
            super(message);
            this.fieldName = null;
        }

        /**
         * Constructs a ValidationException with field name and message.
         *
         * @param fieldName the name of the invalid field
         * @param message the error message
         */
        public ValidationException(String fieldName, String message) {
            super(message);
            this.fieldName = fieldName;
        }

        /**
         * Gets the name of the invalid field.
         *
         * @return the field name
         */
        public String getFieldName() {
            return fieldName;
        }
    }

    /**
     * Custom unchecked exception for invalid state.
     */
    public static class IllegalStateException extends RuntimeException {

        private final String state;

        /**
         * Constructs an IllegalStateException.
         *
         * @param message the error message
         * @param state the invalid state
         */
        public IllegalStateException(String message, String state) {
            super(message);
            this.state = state;
        }

        /**
         * Gets the invalid state.
         *
         * @return the state
         */
        public String getState() {
            return state;
        }
    }

    /**
     * Custom exception with exception chaining.
     */
    public static class DatabaseException extends Exception {

        private final int errorCode;

        /**
         * Constructs a DatabaseException with chaining.
         *
         * @param message the error message
         * @param cause the underlying cause
         * @param errorCode the database error code
         */
        public DatabaseException(String message, Throwable cause, int errorCode) {
            super(message, cause);
            this.errorCode = errorCode;
        }

        /**
         * Gets the error code.
         *
         * @return the error code
         */
        public int getErrorCode() {
            return errorCode;
        }
    }

    /**
     * Demonstrates custom exception usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Custom checked exception
        try {
            validateAge(-5);
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
            if (e.getFieldName() != null) {
                System.out.println("Field: " + e.getFieldName());
            }
        }

        // Custom unchecked exception
        try {
            setState("INACTIVE");
        } catch (IllegalStateException e) {
            System.out.println("Invalid state: " + e.getState());
        }

        // Exception chaining
        try {
            connectToDatabase();
        } catch (DatabaseException e) {
            System.out.println("Database error " + e.getErrorCode() + ": " + e.getMessage());
            System.out.println("Caused by: " + e.getCause().getMessage());
        }
    }

    /**
     * Validates age using custom checked exception.
     *
     * @param age the age to validate
     * @throws ValidationException if age is negative
     */
    public static void validateAge(int age) throws ValidationException {
        if (age < 0) {
            throw new ValidationException("age", "Age cannot be negative: " + age);
        }
        System.out.println("Age is valid: " + age);
    }

    /**
     * Sets state using custom unchecked exception.
     *
     * @param newState the new state
     */
    public static void setState(String newState) {
        if ("INACTIVE".equals(newState)) {
            throw new IllegalStateException("Cannot set INACTIVE state", newState);
        }
        System.out.println("State set to: " + newState);
    }

    /**
     * Simulates database connection with exception chaining.
     *
     * @throws DatabaseException if connection fails
     */
    public static void connectToDatabase() throws DatabaseException {
        try {
            throw new java.sql.SQLException("Connection refused");
        } catch (java.sql.SQLException e) {
            throw new DatabaseException("Failed to connect to database", e, 1001);
        }
    }
}
