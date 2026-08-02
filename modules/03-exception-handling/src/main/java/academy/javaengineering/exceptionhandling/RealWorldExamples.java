package academy.javaengineering.exceptionhandling;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Real-world enterprise exception handling examples.
 *
 * <p>Key concepts covered:
 * <ul>
 *   <li>Service layer exception handling</li>
 *   <li>Repository pattern exceptions</li>
 *   <li>Exception translation</li>
 *   <li>Global exception handling</li>
 *   <li>Result pattern</li>
 * </ul>
 */
public class RealWorldExamples {

    /**
     * Demonstrates enterprise exception handling patterns.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Service layer pattern
        serviceLayerPattern();

        // Repository pattern
        repositoryPattern();

        // Result pattern
        resultPattern();

        // Exception translation
        exceptionTranslation();
    }

    /**
     * Demonstrates service layer exception handling.
     */
    public static void serviceLayerPattern() {
        System.out.println("=== Service Layer Pattern ===");
        var service = new UserService();

        try {
            service.createUser(null, "test@example.com");
        } catch (ValidationException e) {
            System.out.println("Service validation: " + e.getMessage());
            System.out.println("Field: " + e.getFieldName());
        }

        try {
            service.createUser("John", "duplicate@example.com");
        } catch (DuplicateEmailException e) {
            System.out.println("Duplicate email: " + e.getMessage());
        }
        // Expected output:
        // === Service Layer Pattern ===
        // Service validation: Name is required
        // Field: name
        // Duplicate email: Email already exists: duplicate@example.com
    }

    /**
     * Demonstrates repository pattern exception handling.
     */
    public static void repositoryPattern() {
        System.out.println("\n=== Repository Pattern ===");
        var repository = new UserRepository();

        try {
            repository.findById(999L);
        } catch (EntityNotFoundException e) {
            System.out.println("Entity not found: " + e.getMessage());
        }

        try {
            repository.save(null);
        } catch (DataAccessException e) {
            System.out.println("Data access error: " + e.getMessage());
        }
        // Expected output:
        // === Repository Pattern ===
        // Entity not found: User not found with id: 999
        // Data access error: Cannot save null entity
    }

    /**
     * Demonstrates Result pattern for expected failures.
     */
    public static void resultPattern() {
        System.out.println("\n=== Result Pattern ===");
        var calculator = new SafeCalculator();

        Result<Integer> divisionResult = calculator.divide(10, 0);
        divisionResult.ifPresent(value -> System.out.println("Result: " + value));
        divisionResult.ifError(error -> System.out.println("Error: " + error));

        Result<Integer> successResult = calculator.divide(10, 2);
        successResult.ifPresent(value -> System.out.println("Result: " + value));
        successResult.ifError(error -> System.out.println("Error: " + error));
        // Expected output:
        // === Result Pattern ===
        // Error: Division by zero
        // Result: 5
    }

    /**
     * Demonstrates exception translation in layered architecture.
     */
    public static void exceptionTranslation() {
        System.out.println("\n=== Exception Translation ===");
        var service = new OrderService();

        try {
            service.processOrder(1L);
        } catch (OrderNotFoundException e) {
            System.out.println("Order not found: " + e.getMessage());
        } catch (PaymentException e) {
            System.out.println("Payment failed: " + e.getMessage());
            System.out.println("Order ID: " + e.getOrderId());
        }
        // Expected output:
        // === Exception Translation ===
        // Payment failed: Insufficient funds
        // Order ID: 1
    }

    // ==================== Service Layer Classes ====================

    /**
     * User service with proper exception handling.
     */
    public static class UserService {

        private final Map<String, String> emailRegistry = new ConcurrentHashMap<>();

        /**
         * Creates a new user.
         *
         * @param name the user name
         * @param email the user email
         * @throws ValidationException if validation fails
         * @throws DuplicateEmailException if email already exists
         */
        public void createUser(String name, String email) throws ValidationException, DuplicateEmailException {
            if (name == null || name.isBlank()) {
                throw new ValidationException("name", "Name is required");
            }
            if (email == null || !email.contains("@")) {
                throw new ValidationException("email", "Invalid email format");
            }
            if (emailRegistry.containsKey(email)) {
                throw new DuplicateEmailException("Email already exists: " + email);
            }
            emailRegistry.put(email, name);
        }
    }

    // ==================== Repository Classes ====================

    /**
     * User repository with exception handling.
     */
    public static class UserRepository {

        /**
         * Finds a user by ID.
         *
         * @param id the user ID
         * @return the user name
         * @throws EntityNotFoundException if user not found
         */
        public String findById(Long id) throws EntityNotFoundException {
            if (id == 999L) {
                throw new EntityNotFoundException("User not found with id: " + id);
            }
            return "User-" + id;
        }

        /**
         * Saves a user.
         *
         * @param user the user to save
         * @throws DataAccessException if save fails
         */
        public void save(String user) throws DataAccessException {
            if (user == null) {
                throw new DataAccessException("Cannot save null entity");
            }
        }
    }

    // ==================== Result Pattern ====================

    /**
     * Result type for handling expected failures without exceptions.
     *
     * @param <T> the success type
     */
    public static class Result<T> {

        private final T value;
        private final String error;

        private Result(T value, String error) {
            this.value = value;
            this.error = error;
        }

        /**
         * Creates a success result.
         *
         * @param value the success value
         * @return success Result
         */
        public static <T> Result<T> success(T value) {
            return new Result<>(value, null);
        }

        /**
         * Creates an error result.
         *
         * @param error the error message
         * @return error Result
         */
        public static <T> Result<T> error(String error) {
            return new Result<>(null, error);
        }

        /**
         * Executes action if result is success.
         *
         * @param action the action to execute
         */
        public void ifPresent(java.util.function.Consumer<T> action) {
            if (value != null) {
                action.accept(value);
            }
        }

        /**
         * Executes action if result is error.
         *
         * @param action the action to execute
         */
        public void ifError(java.util.function.Consumer<String> action) {
            if (error != null) {
                action.accept(error);
            }
        }
    }

    /**
     * Safe calculator using Result pattern.
     */
    public static class SafeCalculator {

        /**
         * Divides two numbers safely.
         *
         * @param a the dividend
         * @param b the divisor
         * @return Result containing the quotient or error
         */
        public Result<Integer> divide(int a, int b) {
            if (b == 0) {
                return Result.error("Division by zero");
            }
            return Result.success(a / b);
        }
    }

    // ==================== Order Service ====================

    /**
     * Order service with exception translation.
     */
    public static class OrderService {

        /**
         * Processes an order.
         *
         * @param orderId the order ID
         * @throws OrderNotFoundException if order not found
         * @throws PaymentException if payment fails
         */
        public void processOrder(Long orderId) throws OrderNotFoundException, PaymentException {
            // Simulate order lookup
            if (orderId == null) {
                throw new OrderNotFoundException("Order not found with id: null");
            }
            // Simulate payment failure
            throw new PaymentException("Insufficient funds", orderId);
        }
    }

    // ==================== Exception Classes ====================

    /**
     * Validation exception with field information.
     */
    public static class ValidationException extends Exception {

        private final String fieldName;

        /**
         * Constructs a ValidationException.
         *
         * @param fieldName the field name
         * @param message the error message
         */
        public ValidationException(String fieldName, String message) {
            super(message);
            this.fieldName = fieldName;
        }

        /**
         * Gets the field name.
         *
         * @return the field name
         */
        public String getFieldName() {
            return fieldName;
        }
    }

    /**
     * Exception for duplicate email.
     */
    public static class DuplicateEmailException extends Exception {

        /**
         * Constructs a DuplicateEmailException.
         *
         * @param message the error message
         */
        public DuplicateEmailException(String message) {
            super(message);
        }
    }

    /**
     * Exception for entity not found.
     */
    public static class EntityNotFoundException extends Exception {

        /**
         * Constructs an EntityNotFoundException.
         *
         * @param message the error message
         */
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception for data access errors.
     */
    public static class DataAccessException extends RuntimeException {

        /**
         * Constructs a DataAccessException.
         *
         * @param message the error message
         */
        public DataAccessException(String message) {
            super(message);
        }
    }

    /**
     * Exception for order not found.
     */
    public static class OrderNotFoundException extends Exception {

        /**
         * Constructs an OrderNotFoundException.
         *
         * @param message the error message
         */
        public OrderNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception for payment failures.
     */
    public static class PaymentException extends Exception {

        private final Long orderId;

        /**
         * Constructs a PaymentException.
         *
         * @param message the error message
         * @param orderId the order ID
         */
        public PaymentException(String message, Long orderId) {
            super(message);
            this.orderId = orderId;
        }

        /**
         * Gets the order ID.
         *
         * @return the order ID
         */
        public Long getOrderId() {
            return orderId;
        }
    }
}
