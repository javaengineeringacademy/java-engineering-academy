package academy.javaengineering.exceptions.hierarchy;

/**
 * Exercises for understanding the Java exception hierarchy.
 */
public class ExceptionHierarchyExercises {

    /**
     * Exercise 1: Create a custom exception hierarchy for a banking application.
     * Create:
     *   - BankException (checked)
     *   - InsufficientFundsException (checked, extends BankException)
     *   - InvalidAccountException (checked, extends BankException)
     *   - BankRuntimeException (unchecked)
     *   - AccountLockedException (unchecked, extends BankRuntimeException)
     */
    // TODO: Implement the exception hierarchy above

    /**
     * Exercise 2: Write a method that throws different exceptions based on input
     * and demonstrates catching at different levels.
     */
    public static void exercise2() {
        // TODO: Implement this method
        // - If input is null, throw NullPointerException
        // - If input is empty, throw IllegalArgumentException
        // - If input equals "error", throw IOException
        // - Otherwise, return the input string
    }

    /**
     * Exercise 3: Write a try-catch block that demonstrates exception chaining.
     * Create a chain: IOException -> DatabaseException -> ApplicationException
     */
    public static void exercise3() {
        // TODO: Implement this method
        // - Create an IOException
        // - Wrap it in a DatabaseException
        // - Wrap that in an ApplicationException
        // - Catch the ApplicationException and print the full chain
    }

    /**
     * Exercise 4: Write a method that demonstrates the correct order of catching
     * exceptions (specific first, general last).
     */
    public static void exercise4() {
        // TODO: Implement this method
        // - Create a NullPointerException
        // - Catch it with a NullPointerException handler first
        // - Then catch RuntimeException
        // - Then catch Exception
        // - Print which handler caught the exception
    }

    /**
     * Exercise 5: Write a method that uses instanceof to determine the type
     * of an exception and handle it accordingly.
     */
    public static void exercise5() {
        // TODO: Implement this method
        // - Create an Exception object
        // - Use instanceof to check if it's an Error, Exception, or RuntimeException
        // - Handle each case appropriately
    }

    /**
     * Main method to run exercises.
     */
    public static void main(String[] args) {
        System.out.println("=== Exception Hierarchy Exercises ===");
        System.out.println("Implement the methods above to complete the exercises.\n");

        // Uncomment to test your implementations:
        // exercise2();
        // exercise3();
        // exercise4();
        // exercise5();
    }
}
