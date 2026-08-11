package academy.javaengineering.exceptions.bestpractices.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Solutions to the exception best practices exercises.
 *
 * <p>Each method demonstrates the corrected version following the
 * applicable best practice rule.
 */
public class BestPracticesSolutions {

    // =====================================================================
    // Solution 1: Catch specific types (Rule 1)
    // =====================================================================

    public String readFile(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new FileReadException("Failed to read file: " + path, e);
        }
    }

    // =====================================================================
    // Solution 2: Don't swallow (Rule 2)
    // =====================================================================

    public void processItem(String item) {
        try {
            validate(item);
            transform(item);
        } catch (ValidationException | TransformException e) {
            System.err.printf("Processing failed for item '%s': %s%n",
                item, e.getMessage());
            throw new ItemProcessingException(
                "Failed to process item: " + item, e);
        }
    }

    // =====================================================================
    // Solution 3: Chain exceptions (Rule 3)
    // =====================================================================

    public User loadUser(String userId) {
        try {
            return fetchFromDatabase(userId);
        } catch (IOException e) {
            throw new UserException("User load failed: " + userId, e);
        }
    }

    // =====================================================================
    // Solution 4: try-with-resources (Rule 4)
    // =====================================================================

    public String firstLine(String path) throws IOException {
        try (var reader = Files.newBufferedReader(Path.of(path))) {
            return reader.readLine();
        }
    }

    // =====================================================================
    // Solution 5: Don't use for control flow (Rule 6)
    // =====================================================================

    public String getConfigValue(Map<String, String> config, String key) {
        return config.getOrDefault(key, "default-value");
    }

    // =====================================================================
    // Solution 6: Include context in messages (Rule 7)
    // =====================================================================

    public void withdraw(String accountId, double balance, double amount) {
        if (amount > balance) {
            throw new InsufficientFundsException(
                String.format(
                    "Insufficient funds in account %s: balance=%.2f, requested=%.2f",
                    accountId, balance, amount));
        }
        System.out.printf("Withdrew %.2f from %s%n", amount, accountId);
    }

    // =====================================================================
    // Solution 7: Batch processing (Best Practice Pattern)
    // =====================================================================

    public List<String> processAll(List<String> items) {
        List<String> succeeded = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (String item : items) {
            try {
                processItem(item);
                succeeded.add(item);
            } catch (Exception e) {
                System.err.printf("Item '%s' failed: %s%n",
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

    // =====================================================================
    // Stub helpers
    // =====================================================================

    private void validate(String item) { /* stub */ }

    private void transform(String item) { /* stub */ }

    private User fetchFromDatabase(String userId) throws IOException {
        return new User(userId, "user@example.com");
    }

    // =====================================================================
    // Inner types
    // =====================================================================

    public record User(String id, String email) {}

    public static class FileReadException extends RuntimeException {
        public FileReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserException extends RuntimeException {
        public UserException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InsufficientFundsException extends RuntimeException {
        private final String errorCode;

        public InsufficientFundsException(String message) {
            super(message);
            this.errorCode = "ERR_INSUFFICIENT_FUNDS";
        }

        public String getErrorCode() { return errorCode; }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) { super(message); }
    }

    public static class TransformException extends RuntimeException {
        public TransformException(String message) { super(message); }
    }

    public static class ItemProcessingException extends RuntimeException {
        public ItemProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BatchProcessingException extends RuntimeException {
        private final List<String> failedItems;

        public BatchProcessingException(String message, List<String> failedItems) {
            super(message);
            this.failedItems = List.copyOf(failedItems);
        }

        public List<String> getFailedItems() { return failedItems; }
    }
}
