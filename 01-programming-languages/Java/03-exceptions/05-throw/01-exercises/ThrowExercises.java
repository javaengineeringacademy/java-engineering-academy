package academy.javaengineering.exceptions.throwstatement;

/**
 * Practice exercises for the throw keyword in Java.
 *
 * <p>Complexity: O(1) per method — each exercise focuses on a specific throw pattern.</p>
 *
 * <p>Thread-Safety: Yes — all methods are stateless and independent.</p>
 *
 * <p>Key Characteristics:
 * <ul>
 *   <li>Each method is a stub with TODO comments — implement the required throw behavior</li>
 *   <li>Covers checked exceptions, unchecked exceptions, rethrowing, chaining, and custom exceptions</li>
 *   <li>Tests focus on correct exception type, message, and cause chain</li>
 * </ul>
 */
public class ThrowExercises {

    /**
     * Exercise 1: Throw a checked exception.
     *
     * <p>TODO: If the input string is null or empty, throw an IllegalArgumentException
     * with the message "Input must not be null or empty".
     * Otherwise, print the uppercased string.</p>
     *
     * @param input the string to validate
     */
    public static void exercise1_ThrowChecked(String input) {
        // TODO: Implement this method
        // Hint: throw new IllegalArgumentException("Input must not be null or empty");
    }

    /**
     * Exercise 2: Throw an unchecked exception.
     *
     * <p>TODO: If the array is null, throw a NullPointerException.
     * If the array is empty, throw an IllegalArgumentException with "Array is empty".
     * Otherwise, return the first element.</p>
     *
     * @param array the array to inspect
     * @return the first element
     */
    public static String exercise2_ThrowUnchecked(String[] array) {
        // TODO: Implement this method
        return null;
    }

    /**
     * Exercise 3: Rethrow with cause.
     *
     * <p>TODO: Parse the input string as an integer using Integer.parseInt().
     * If it fails, catch the NumberFormatException and rethrow it as a
     * RuntimeException with the message "Failed to parse integer" and preserve
     * the original exception as the cause.</p>
     *
     * @param input the string to parse
     * @return the parsed integer
     */
    public static int exercise3_RethrowWithCause(String input) {
        // TODO: Implement this method
        return 0;
    }

    /**
     * Exercise 4: Exception chaining with multiple levels.
     *
     * <p>TODO: Given a userId (String), try to look up a user. If the lookup fails
     * (simulated by checking if userId equals "missing"), catch a RuntimeException
     * and throw an IllegalStateException wrapping it. If userId is null,
     * throw an IllegalArgumentException directly.</p>
     *
     * @param userId the user identifier
     * @return the user identifier if valid
     */
    public static String exercise4_ExceptionChaining(String userId) {
        // TODO: Implement this method
        return null;
    }

    /**
     * Exercise 5: Throw a custom exception.
     *
     * <p>TODO: If the age is less than 0 or greater than 150, throw a custom
     * InvalidAgeException with the message "Invalid age: " + age.
     * Otherwise, return true.</p>
     *
     * @param age the age to validate
     * @return true if valid
     * @throws InvalidAgeException if age is out of range
     */
    public static boolean exercise5_CustomException(int age) throws InvalidAgeException {
        // TODO: Implement this method
        return false;
    }

    /**
     * Custom exception for exercise 5.
     */
    public static class InvalidAgeException extends Exception {
        public InvalidAgeException(String message) {
            super(message);
        }
    }
}
