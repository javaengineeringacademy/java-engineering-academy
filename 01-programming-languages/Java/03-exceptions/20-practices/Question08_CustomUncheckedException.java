package academy.javaengineering.exceptions.questions;

/**
 * Question 8: Custom unchecked exception
 *
 * Task: Create a custom unchecked exception called ValidationException.
 * It should carry a field name that identifies which field failed validation.
 */
public class Question08_CustomUncheckedException {

    // TODO: Create ValidationException that extends RuntimeException
    // It should have:
    // - A private final String fieldName
    // - A constructor that takes fieldName and message
    // - A getFieldName() method

    public static void validateAge(int age) {
        // TODO: If age < 0 or age > 150, throw ValidationException
        // with fieldName = "age"
    }

    public static void validateEmail(String email) {
        // TODO: If email is null or doesn't contain @, throw ValidationException
        // with fieldName = "email"
    }

    public static void main(String[] args) {
        try {
            validateAge(-5);
        } catch (Exception e) {
            System.out.println("Age error: " + e.getMessage());
        }

        try {
            validateEmail("invalid");
        } catch (Exception e) {
            System.out.println("Email error: " + e.getMessage());
        }
    }
}
