package academy.javaengineering.exceptions.solutions;

/**
 * Solution 8: Custom unchecked exception
 *
 * Create ValidationException with fieldName field.
 */
public class Solution08_CustomUncheckedException {

    public static class ValidationException extends RuntimeException {
        private final String fieldName;

        public ValidationException(String fieldName, String message) {
            super(message);
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    public static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new ValidationException("age", "Age must be between 0 and 150, got: " + age);
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new ValidationException("email", "Invalid email format: " + email);
        }
    }

    public static void main(String[] args) {
        try {
            validateAge(-5);
        } catch (ValidationException e) {
            System.out.println("Age error [" + e.getFieldName() + "]: " + e.getMessage());
        }

        try {
            validateEmail("invalid");
        } catch (ValidationException e) {
            System.out.println("Email error [" + e.getFieldName() + "]: " + e.getMessage());
        }
    }
}
