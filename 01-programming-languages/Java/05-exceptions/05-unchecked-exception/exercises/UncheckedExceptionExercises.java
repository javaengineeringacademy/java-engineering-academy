package academy.javaengineering.exceptions.uncheckedexception.exercises;

/**
 * Exercises for unchecked exceptions.
 *
 * <p>Complete each exercise by filling in the TODO sections. Run the main
 * method to verify your answers. Solutions are in the solutions package.</p>
 */
public final class UncheckedExceptionExercises {

    private UncheckedExceptionExercises() {}

    // -----------------------------------------------------------------------
    // Exercise 1: NullPointerException Prevention
    // -----------------------------------------------------------------------

    /**
     * Returns the length of the given string.
     *
     * <p>TODO: If the string is null, throw a NullPointerException with the
     * message "String must not be null".</p>
     *
     * @param text the input string
     * @return the length of the string
     */
    public static int getStringLength(String text) {
        // TODO: Implement null check and throw NullPointerException
        return text.length();
    }

    // -----------------------------------------------------------------------
    // Exercise 2: IllegalArgumentException Validation
    // -----------------------------------------------------------------------

    /**
     * Sets a percentage value, which must be between 0 and 100 (inclusive).
     *
     * <p>TODO: If the percentage is outside this range, throw an
     * IllegalArgumentException with the message "Percentage must be 0-100: "
     * followed by the value.</p>
     *
     * @param percentage the percentage value
     */
    public static void validatePercentage(double percentage) {
        // TODO: Validate percentage range
        System.out.println("Percentage set to: " + percentage);
    }

    // -----------------------------------------------------------------------
    // Exercise 3: IndexOutOfBoundsException Prevention
    // -----------------------------------------------------------------------

    /**
     * Returns the element at the given index in the array.
     *
     * <p>TODO: If the index is out of bounds (less than 0 or greater than or
     * equal to the array length), throw an IndexOutOfBoundsException with a
     * descriptive message.</p>
     *
     * @param array the input array
     * @param index the index to access
     * @return the element at the index
     */
    public static int getElement(int[] array, int index) {
        // TODO: Validate index and return element
        return array[index];
    }

    // -----------------------------------------------------------------------
    // Exercise 4: Multiple Validations
    // -----------------------------------------------------------------------

    /**
     * Creates a user with the given name and age.
     *
     * <p>TODO: Implement the following validations:</p>
     * <ul>
     *   <li>If name is null, throw NullPointerException</li>
     *   <li>If name is empty, throw IllegalArgumentException</li>
     *   <li>If age is negative, throw IllegalArgumentException</li>
     *   <li>If age is greater than 150, throw IllegalArgumentException</li>
     * </ul>
     *
     * @param name the user's name
     * @param age the user's age
     * @return a formatted user string
     */
    public static String createUser(String name, int age) {
        // TODO: Implement validations
        return name + " (age " + age + ")";
    }

    // -----------------------------------------------------------------------
    // Exercise 5: Custom Unchecked Exception
    // -----------------------------------------------------------------------

    /**
     * Custom unchecked exception for insufficient funds.
     *
     * <p>TODO: Create a static inner class called InsufficientFundsException
     * that extends RuntimeException. It should have:</p>
     * <ul>
     *   <li>A constructor taking (double balance, double amount)</li>
     *   <li>A getBalance() method</li>
     *   <li>A getAmount() method</li>
     * </ul>
     */
    // TODO: Declare InsufficientFundsException as a static inner class

    /**
     * Withdraws money from the given balance.
     *
     * <p>TODO: If the amount is greater than the balance, throw
     * InsufficientFundsException.</p>
     *
     * @param balance the current balance
     * @param amount the amount to withdraw
     * @return the new balance after withdrawal
     */
    public static double withdraw(double balance, double amount) {
        // TODO: Validate and perform withdrawal
        return balance - amount;
    }

    // -----------------------------------------------------------------------
    // Exercise 6: Exception Handling Pattern
    // -----------------------------------------------------------------------

    /**
     * Parses an integer from a string. Returns the default value if parsing
     * fails.
     *
     * <p>TODO: Use a try-catch block to catch NumberFormatException and return
     * the defaultValue instead.</p>
     *
     * @param input the string to parse
     * @param defaultValue the value to return on failure
     * @return the parsed integer or the default value
     */
    public static int parseOrDefault(String input, int defaultValue) {
        // TODO: Implement with exception handling
        return Integer.parseInt(input);
    }

    // -----------------------------------------------------------------------
    // Main — Run to test your solutions
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Unchecked Exception Exercises ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: NullPointerException Prevention ---");
        try {
            System.out.println("Length of 'Hello': " + getStringLength("Hello"));
            System.out.println("Length of null: " + getStringLength(null));
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // Exercise 2
        System.out.println("--- Exercise 2: IllegalArgumentException Validation ---");
        try {
            validatePercentage(50);
            validatePercentage(-10);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // Exercise 3
        System.out.println("--- Exercise 3: IndexOutOfBoundsException Prevention ---");
        int[] nums = {1, 2, 3, 4, 5};
        try {
            System.out.println("Element at 2: " + getElement(nums, 2));
            System.out.println("Element at 10: " + getElement(nums, 10));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // Exercise 4
        System.out.println("--- Exercise 4: Multiple Validations ---");
        try {
            System.out.println("User: " + createUser("Alice", 30));
            System.out.println("User: " + createUser(null, 25));
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // Exercise 5
        System.out.println("--- Exercise 5: Custom Exception ---");
        try {
            System.out.println("New balance: " + withdraw(100, 75));
            System.out.println("New balance: " + withdraw(25, 50));
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        System.out.println();

        // Exercise 6
        System.out.println("--- Exercise 6: Exception Handling Pattern ---");
        System.out.println("Parsed '42': " + parseOrDefault("42", 0));
        System.out.println("Parsed 'abc': " + parseOrDefault("abc", -1));
        System.out.println();

        System.out.println("=== All Exercises Complete ===");
    }
}
