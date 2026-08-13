package academy.javaengineering.exceptions.uncheckedexception.solutions;

/**
 * Solutions for unchecked exception exercises.
 *
 * <p>This file contains completed implementations for all exercises in the
 * exercises package.</p>
 */
public final class UncheckedExceptionSolutions {

    private UncheckedExceptionSolutions() {}

    // -----------------------------------------------------------------------
    // Exercise 1: NullPointerException Prevention
    // -----------------------------------------------------------------------

    /**
     * Returns the length of the given string.
     *
     * @param text the input string
     * @return the length of the string
     * @throws NullPointerException if text is null
     */
    public static int getStringLength(String text) {
        if (text == null) {
            throw new NullPointerException("String must not be null");
        }
        return text.length();
    }

    // -----------------------------------------------------------------------
    // Exercise 2: IllegalArgumentException Validation
    // -----------------------------------------------------------------------

    /**
     * Sets a percentage value, which must be between 0 and 100 (inclusive).
     *
     * @param percentage the percentage value
     * @throws IllegalArgumentException if percentage is outside 0-100
     */
    public static void validatePercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be 0-100: " + percentage);
        }
        System.out.println("Percentage set to: " + percentage);
    }

    // -----------------------------------------------------------------------
    // Exercise 3: IndexOutOfBoundsException Prevention
    // -----------------------------------------------------------------------

    /**
     * Returns the element at the given index in the array.
     *
     * @param array the input array
     * @param index the index to access
     * @return the element at the index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public static int getElement(int[] array, int index) {
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for length " + array.length);
        }
        return array[index];
    }

    // -----------------------------------------------------------------------
    // Exercise 4: Multiple Validations
    // -----------------------------------------------------------------------

    /**
     * Creates a user with the given name and age.
     *
     * @param name the user's name
     * @param age the user's age
     * @return a formatted user string
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty or age is invalid
     */
    public static String createUser(String name, int age) {
        if (name == null) {
            throw new NullPointerException("Name must not be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age cannot exceed 150: " + age);
        }
        return name + " (age " + age + ")";
    }

    // -----------------------------------------------------------------------
    // Exercise 5: Custom Unchecked Exception
    // -----------------------------------------------------------------------

    /**
     * Custom unchecked exception for insufficient funds.
     */
    public static class InsufficientFundsException extends RuntimeException {

        private final double balance;
        private final double amount;

        public InsufficientFundsException(double balance, double amount) {
            super("Insufficient funds: balance " + balance + ", requested " + amount);
            this.balance = balance;
            this.amount = amount;
        }

        public double getBalance() {
            return balance;
        }

        public double getAmount() {
            return amount;
        }
    }

    /**
     * Withdraws money from the given balance.
     *
     * @param balance the current balance
     * @param amount the amount to withdraw
     * @return the new balance after withdrawal
     * @throws InsufficientFundsException if amount exceeds balance
     */
    public static double withdraw(double balance, double amount) {
        if (amount > balance) {
            throw new InsufficientFundsException(balance, amount);
        }
        return balance - amount;
    }

    // -----------------------------------------------------------------------
    // Exercise 6: Exception Handling Pattern
    // -----------------------------------------------------------------------

    /**
     * Parses an integer from a string. Returns the default value if parsing
     * fails.
     *
     * @param input the string to parse
     * @param defaultValue the value to return on failure
     * @return the parsed integer or the default value
     */
    public static int parseOrDefault(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // -----------------------------------------------------------------------
    // Main — Run to verify solutions
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=== Unchecked Exception Solutions ===\n");

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
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("  Balance: " + e.getBalance());
            System.out.println("  Requested: " + e.getAmount());
        }
        System.out.println();

        // Exercise 6
        System.out.println("--- Exercise 6: Exception Handling Pattern ---");
        System.out.println("Parsed '42': " + parseOrDefault("42", 0));
        System.out.println("Parsed 'abc': " + parseOrDefault("abc", -1));
        System.out.println();

        System.out.println("=== All Solutions Complete ===");
    }
}
