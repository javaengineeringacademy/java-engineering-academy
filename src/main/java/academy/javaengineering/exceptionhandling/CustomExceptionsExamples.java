package academy.javaengineering.exceptionhandling;

import java.time.Instant;
import java.util.Map;

/**
 * Custom Exceptions Examples
 * 
 * Demonstrates creating and using custom exception classes.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CustomExceptionsExamples {

    /**
     * Demonstrates basic custom exception.
     */
    public static void basicCustomException() {
        System.out.println("=== Basic Custom Exception ===");
        
        try {
            BankAccount account = new BankAccount(100.0);
            account.withdraw(150.0);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
            System.out.printf("Balance: %.2f, Amount: %.2f, Deficit: %.2f%n",
                e.getBalance(), e.getAmount(), e.getDeficit());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates validation exception.
     */
    public static void validationException() {
        System.out.println("=== Validation Exception ===");
        
        try {
            UserValidator.validate(null, "John", 25);
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
            System.out.println("Field: " + e.getFieldName());
            System.out.println("Value: " + e.getInvalidValue());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception hierarchy.
     */
    public static void exceptionHierarchy() {
        System.out.println("=== Exception Hierarchy ===");
        
        try {
            processData("invalid");
        } catch (DataException e) {
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("Timestamp: " + e.getTimestamp());
            System.out.println("Recovery: " + e.getRecoverySuggestion());
        }
        
        System.out.println();
    }

    static void processData(String data) throws DataException {
        if (data == null) {
            throw new DataException("DATA_NULL", "Data cannot be null", null);
        }
        
        try {
            Integer.parseInt(data);
        } catch (NumberFormatException e) {
            throw new DataException("DATA_FORMAT", "Invalid data format: " + data, e);
        }
    }

    /**
     * Demonstrates exception with context.
     */
    public static void exceptionWithContext() {
        System.out.println("=== Exception With Context ===");
        
        try {
            OrderService.processOrder(null);
        } catch (OrderException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Context: " + e.getContext());
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception chaining.
     */
    public static void exceptionChaining() {
        System.out.println("=== Exception Chaining ===");
        
        try {
            ServiceLayer.processData();
        } catch (ServiceException e) {
            System.out.println("Service error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
            System.out.println("Root cause: " + 
                (e.getCause().getCause() != null ? 
                 e.getCause().getCause().getMessage() : "None"));
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception factory.
     */
    public static void exceptionFactory() {
        System.out.println("=== Exception Factory ===");
        
        try {
            throw ExceptionFactory.validationError("email", "invalid", "must contain @");
        } catch (ValidationException e) {
            System.out.println("Factory created: " + e.getMessage());
        }
        
        try {
            throw ExceptionFactory.notFound("User", 123L);
        } catch (ResourceNotFoundException e) {
            System.out.println("Factory created: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        basicCustomException();
        validationException();
        exceptionHierarchy();
        exceptionWithContext();
        exceptionChaining();
        exceptionFactory();
    }

    // Supporting classes

    static class BankAccount {
        private double balance;
        
        BankAccount(double balance) {
            this.balance = balance;
        }
        
        void withdraw(double amount) throws InsufficientFundsException {
            if (amount > balance) {
                throw new InsufficientFundsException(balance, amount);
            }
            balance -= amount;
            System.out.printf("Withdrew %.2f, new balance: %.2f%n", amount, balance);
        }
    }

    static class InsufficientFundsException extends Exception {
        private final double balance;
        private final double amount;
        
        InsufficientFundsException(double balance, double amount) {
            super(String.format("Insufficient funds: balance=%.2f, amount=%.2f", 
                balance, amount));
            this.balance = balance;
            this.amount = amount;
        }
        
        double getBalance() { return balance; }
        double getAmount() { return amount; }
        double getDeficit() { return amount - balance; }
    }

    static class UserValidator {
        static void validate(String email, String name, int age) throws ValidationException {
            if (email == null) {
                throw new ValidationException("email", null, "cannot be null");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new ValidationException("name", name, "cannot be empty");
            }
            if (age < 0 || age > 150) {
                throw new ValidationException("age", age, "must be between 0 and 150");
            }
            System.out.println("Valid user: " + name);
        }
    }

    static class ValidationException extends Exception {
        private final String fieldName;
        private final Object invalidValue;
        
        ValidationException(String fieldName, Object invalidValue, String message) {
            super(String.format("Validation failed for '%s': %s (value: %s)", 
                fieldName, message, invalidValue));
            this.fieldName = fieldName;
            this.invalidValue = invalidValue;
        }
        
        String getFieldName() { return fieldName; }
        Object getInvalidValue() { return invalidValue; }
    }

    static class DataException extends Exception {
        private final String errorCode;
        private final Instant timestamp;
        
        DataException(String errorCode, String message, Throwable cause) {
            super(message, cause);
            this.errorCode = errorCode;
            this.timestamp = Instant.now();
        }
        
        String getErrorCode() { return errorCode; }
        Instant getTimestamp() { return timestamp; }
        String getRecoverySuggestion() {
            if ("DATA_NULL".equals(errorCode)) {
                return "Provide non-null data";
            }
            return "Check data format";
        }
    }

    static class OrderException extends Exception {
        private final Map<String, Object> context;
        
        OrderException(String message, Map<String, Object> context) {
            super(message);
            this.context = context;
        }
        
        Map<String, Object> getContext() { return context; }
    }

    static class OrderService {
        static void processOrder(Object order) throws OrderException {
            if (order == null) {
                throw new OrderException("Order cannot be null", 
                    Map.of("operation", "processOrder", "timestamp", Instant.now()));
            }
            System.out.println("Order processed");
        }
    }

    static class ServiceException extends Exception {
        ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ServiceLayer {
        static void processData() throws ServiceException {
            try {
                RepositoryLayer.fetchData();
            } catch (RepositoryException e) {
                throw new ServiceException("Service layer failed", e);
            }
        }
    }

    static class RepositoryLayer {
        static void fetchData() throws RepositoryException {
            try {
                throw new java.io.IOException("Database connection failed");
            } catch (java.io.IOException e) {
                throw new RepositoryException("Repository layer failed", e);
            }
        }
    }

    static class RepositoryException extends Exception {
        RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ExceptionFactory {
        static ValidationException validationError(String field, String value, String reason) {
            return new ValidationException(field, value, reason);
        }
        
        static ResourceNotFoundException notFound(String resourceType, Object id) {
            return new ResourceNotFoundException(resourceType, id);
        }
    }

    static class ResourceNotFoundException extends Exception {
        ResourceNotFoundException(String resourceType, Object id) {
            super(String.format("%s not found with id: %s", resourceType, id));
        }
    }
}
