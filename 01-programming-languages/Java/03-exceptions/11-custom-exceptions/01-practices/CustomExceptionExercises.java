package academy.javaengineering.exceptions.custom;

/**
 * Exercises for creating and using custom exceptions.
 *
 * <p>Complete each exercise by implementing the TODO sections.</p>
 */
public class CustomExceptionExercises {

    // ================================================================
    // Exercise 1: Create a Checked Exception
    // ================================================================

    /**
     * TODO: Create a checked exception called {@code FileProcessingException}
     * that:
     * 1. Extends Exception
     * 2. Has a field for the file path
     * 3. Provides constructors: (String), (String, Throwable),
     *    (String, String) where first String is file path and second is message
     */
    // public static class FileProcessingException extends Exception {
    //     // TODO: Implement
    // }

    // ================================================================
    // Exercise 2: Create an Unchecked Exception
    // ================================================================

    /**
     * TODO: Create an unchecked exception called {@code RateLimitExceededException}
     * that:
     * 1. Extends RuntimeException
     * 2. Has fields: retryAfterSeconds (int), clientId (String)
     * 3. Provides a constructor that builds a descriptive message
     */
    // public static class RateLimitExceededException extends RuntimeException {
    //     // TODO: Implement
    // }

    // ================================================================
    // Exercise 3: Exception with Error Code
    // ================================================================

    /**
     * TODO: Create an unchecked exception called {@code OrderException}
     * that:
     * 1. Extends RuntimeException
     * 2. Has fields: orderId (String), errorCode (String)
     * 3. Provides a static factory method:
     *    {@code static OrderException notFound(String orderId)}
     */
    // public static class OrderException extends RuntimeException {
    //     // TODO: Implement
    // }

    // ================================================================
    // Exercise 4: Domain Exception Hierarchy
    // ================================================================

    /**
     * TODO: Create a base exception called {@code ServiceException}
     * and two subclasses:
     * 1. ServiceException extends RuntimeException
     *    - Fields: service name, operation name
     * 2. TimeoutException extends ServiceException
     *    - Field: timeoutMillis
     * 3. ConnectionException extends ServiceException
     *    - Field: endpoint URL
     */
    // TODO: Implement ServiceException hierarchy

    // ================================================================
    // Exercise 5: Exception Builder
    // ================================================================

    /**
     * TODO: Create a builder for a complex exception called
     * {@code InventoryException} with these fields:
     * - productId (String)
     * - warehouseId (String)
     * - requestedQuantity (int)
     * - availableQuantity (int)
     * - errorCode (String)
     *
     * The builder should have fluent methods for each field.
     */
    // TODO: Implement InventoryException with builder

    // ================================================================
    // Test Methods
    // ================================================================

    public static void main(String[] args) {
        System.out.println("=== Custom Exception Exercises ===\n");

        // Uncomment each exercise as you complete it:

        // Exercise 1
        // try {
        //     throw new FileProcessingException("/data/file.csv",
        //         "File not found");
        // } catch (FileProcessingException e) {
        //     System.out.println("Exercise 1: " + e.getMessage());
        // }

        // Exercise 2
        // try {
        //     throw new RateLimitExceededException("client-123", 30);
        // } catch (RateLimitExceededException e) {
        //     System.out.println("Exercise 2: " + e.getMessage());
        // }

        // Exercise 3
        // try {
        //     throw OrderException.notFound("ORD-999");
        // } catch (OrderException e) {
        //     System.out.println("Exercise 3: " + e.getMessage());
        // }

        System.out.println("Implement exercises and uncomment test code.");
    }
}
