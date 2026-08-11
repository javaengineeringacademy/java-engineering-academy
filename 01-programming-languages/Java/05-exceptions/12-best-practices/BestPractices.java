package academy.javaengineering.exceptions.bestpractices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates good vs bad exception handling practices side by side.
 *
 * <p>Each method pair shows a common anti-pattern and its corrected version.
 */
public class BestPractices {

    // ========================================================================
    // Rule 1: Catch specific types, not broad Exception
    // ========================================================================

    /** BAD: Catches everything, including programming bugs. */
    public String readConfigBad(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (Exception e) {
            throw new RuntimeException("config read failed", e);
        }
    }

    /** GOOD: Catches specific, expected exceptions. */
    public String readConfigGood(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new ConfigException("Failed to read config: " + path, e);
        }
    }

    // ========================================================================
    // Rule 2: Don't swallow exceptions
    // ========================================================================

    /** BAD: Silently ignores the failure. */
    public void sendNotificationBad(String userId, String message) {
        try {
            notificationService().send(userId, message);
        } catch (Exception e) {
            // TODO: handle later
        }
    }

    /** GOOD: Logs and rethrows with context. */
    public void sendNotificationGood(String userId, String message) {
        try {
            notificationService().send(userId, message);
        } catch (NotificationException e) {
            System.err.printf("Notification failed for user %s: %s%n",
                userId, e.getMessage());
            throw e;
        }
    }

    // ========================================================================
    // Rule 3: Chain exceptions
    // ========================================================================

    /** BAD: Loses the original cause. */
    public Order loadOrderBad(String orderId) {
        try {
            return orderRepository().findById(orderId);
        } catch (IOException e) {
            throw new OrderException("Cannot load order");
        }
    }

    /** GOOD: Preserves the cause. */
    public Order loadOrderGood(String orderId) {
        try {
            return orderRepository().findById(orderId);
        } catch (IOException e) {
            throw new OrderException("Cannot load order: " + orderId, e);
        }
    }

    // ========================================================================
    // Rule 4: Use try-with-resources
    // ========================================================================

    /** BAD: Manual resource management — leak-prone. */
    public String readFileBad(String path) throws IOException {
        var reader = Files.newBufferedReader(Path.of(path));
        String line = reader.readLine();
        reader.close();
        return line;
    }

    /** GOOD: try-with-resources guarantees cleanup. */
    public String readFileGood(String path) throws IOException {
        try (var reader = Files.newBufferedReader(Path.of(path))) {
            return reader.readLine();
        }
    }

    // ========================================================================
    // Rule 5: Document checked exceptions
    // ========================================================================

    /**
     * Validates that the given age is within the acceptable range.
     *
     * @param age the age to validate
     * @throws IllegalArgumentException if age is negative or exceeds 150
     */
    public void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException(
                "Age must be between 0 and 150, got: " + age);
        }
    }

    // ========================================================================
    // Rule 6: Don't use exceptions for control flow
    // ========================================================================

    /** BAD: Uses exception as a way to check if a key exists. */
    public String lookupBad(Map<String, String> map, String key) {
        try {
            return map.get(key);
        } catch (NullPointerException e) {
            return "default";
        }
    }

    /** GOOD: Uses standard map operations. */
    public String lookupGood(Map<String, String> map, String key) {
        return map.getOrDefault(key, "default");
    }

    // ========================================================================
    // Rule 7: Include context in messages
    // ========================================================================

    /** BAD: Generic, unhelpful message. */
    public void transferBad(String from, String to, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        // ...
    }

    /** GOOD: Descriptive message with relevant values. */
    public void transferGood(String from, String to, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                String.format("Transfer amount must be positive, got %.2f from %s to %s",
                    amount, from, to));
        }
        // ...
    }

    // ========================================================================
    // Rule 8: Avoid catching Exception/Throwable in business logic
    // ========================================================================

    /** BAD: Catching Throwable hides bugs. */
    public void processPaymentBad(Payment payment) {
        try {
            paymentGateway().charge(payment);
        } catch (Throwable t) {
            System.err.println("Payment error: " + t.getMessage());
        }
    }

    /** GOOD: Catches only the expected failure. */
    public void processPaymentGood(Payment payment) {
        try {
            paymentGateway().charge(payment);
        } catch (PaymentDeclinedException e) {
            System.err.printf("Payment declined for order %s: %s%n",
                payment.getOrderId(), e.getMessage());
            throw e;
        }
    }

    // ========================================================================
    // Rule 9: Use custom exceptions for domain errors
    // ========================================================================

    /** Shows a domain-specific exception hierarchy. */
    public static class InsufficientFundsException extends DomainException {
        private final double balance;
        private final double requested;

        public InsufficientFundsException(double balance, double requested) {
            super(String.format("Insufficient funds: balance=%.2f, requested=%.2f",
                balance, requested), "ERR_INSUFFICIENT_FUNDS");
            this.balance = balance;
            this.requested = requested;
        }

        public double getBalance() { return balance; }
        public double getRequested() { return requested; }
    }

    // ========================================================================
    // Rule 10: Prefer unchecked for programming bugs
    // ========================================================================

    /** GOOD: Unchecked for precondition violation. */
    public void setRetryCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException(
                "Retry count must be non-negative, got: " + count);
        }
        // ...
    }

    // ========================================================================
    // Batch Processing Pattern
    // ========================================================================

    /**
     * Processes items individually so that one failure does not
     * prevent other items from being processed.
     */
    public List<String> processBatch(List<String> items) {
        List<String> failed = new ArrayList<>();
        List<String> succeeded = new ArrayList<>();

        for (String item : items) {
            try {
                processItem(item);
                succeeded.add(item);
            } catch (ItemProcessingException e) {
                System.err.printf("Failed to process item '%s': %s%n",
                    item, e.getMessage());
                failed.add(item);
            }
        }

        if (!failed.isEmpty()) {
            throw new BatchProcessingException(
                failed.size() + " of " + items.size() + " items failed", failed);
        }

        return succeeded;
    }

    // ========================================================================
    // Helper stubs (in a real app these would be injected)
    // ========================================================================

    private void processItem(String item) {
        if (item == null) {
            throw new ItemProcessingException("Item is null");
        }
    }

    private NotificationService notificationService() {
        return new NotificationService();
    }

    private OrderRepository orderRepository() {
        return new OrderRepository();
    }

    private PaymentGateway paymentGateway() {
        return new PaymentGateway();
    }

    // ========================================================================
    // Inner types used by demonstrations
    // ========================================================================

    public record Payment(String orderId, double amount) {
        public String getOrderId() { return orderId; }
    }

    // ========================================================================
    // Exception types
    // ========================================================================

    public static class ConfigException extends RuntimeException {
        public ConfigException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class OrderException extends RuntimeException {
        public OrderException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static abstract class DomainException extends RuntimeException {
        private final String errorCode;

        protected DomainException(String message, String errorCode) {
            super(message);
            this.errorCode = errorCode;
        }

        protected DomainException(String message, String errorCode, Throwable cause) {
            super(message, cause);
            this.errorCode = errorCode;
        }

        public String getErrorCode() { return errorCode; }
    }

    public static class PaymentDeclinedException extends DomainException {
        public PaymentDeclinedException(String message) {
            super(message, "ERR_PAYMENT_DECLINED");
        }
    }

    public static class ItemProcessingException extends DomainException {
        public ItemProcessingException(String message) {
            super(message, "ERR_ITEM_PROCESSING");
        }
    }

    public static class BatchProcessingException extends DomainException {
        private final List<String> failedItems;

        public BatchProcessingException(String message, List<String> failedItems) {
            super(message, "ERR_BATCH_PROCESSING");
            this.failedItems = List.copyOf(failedItems);
        }

        public List<String> getFailedItems() { return failedItems; }
    }

    // ========================================================================
    // Service stubs
    // ========================================================================

    private static class NotificationService {
        void send(String userId, String message) throws NotificationException {
            // stub
        }
    }

    private static class NotificationException extends Exception {
        NotificationException(String msg) { super(msg); }
    }

    private static class OrderRepository {
        Order findById(String id) throws IOException {
            // stub
            return null;
        }
    }

    private static class Order {
    }

    private static class PaymentGateway {
        void charge(Payment payment) throws PaymentDeclinedException {
            // stub
        }
    }
}
