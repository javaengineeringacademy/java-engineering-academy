package academy.javaengineering.exceptions.bestpractices.exercises;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Practice exercises for exception best practices.
 *
 * <p>Fix each method according to the best practice rule indicated.
 * Comments mark what is wrong. Correct the code to follow the guideline.
 */
public class BestPracticesExercises {

    // =====================================================================
    // Exercise 1: Catch specific types (Rule 1)
    // =====================================================================
    // FIX: The catch block is too broad. Catch only the expected exception.

    public String readFile(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (Exception e) {   // <-- BUG: too broad
            throw new RuntimeException("read failed", e);
        }
    }

    // =====================================================================
    // Exercise 2: Don't swallow (Rule 2)
    // =====================================================================
    // FIX: The exception is swallowed. Log it and/or rethrow.

    public void processItem(String item) {
        try {
            validate(item);
            transform(item);
        } catch (Exception e) {
            // <-- BUG: silently swallowed
        }
    }

    // =====================================================================
    // Exercise 3: Chain exceptions (Rule 3)
    // =====================================================================
    // FIX: The cause is lost when creating the new exception.

    public User loadUser(String userId) {
        try {
            return fetchFromDatabase(userId);
        } catch (IOException e) {
            throw new UserException("User load failed");  // <-- BUG: no cause
        }
    }

    // =====================================================================
    // Exercise 4: try-with-resources (Rule 4)
    // =====================================================================
    // FIX: Resource is not managed with try-with-resources. Could leak.

    public String firstLine(String path) throws IOException {
        var reader = Files.newBufferedReader(Path.of(path));  // <-- BUG: manual close
        String line = reader.readLine();
        reader.close();
        return line;
    }

    // =====================================================================
    // Exercise 5: Don't use for control flow (Rule 6)
    // =====================================================================
    // FIX: Exception used as control flow. Use a conditional instead.

    public String getConfigValue(Map<String, String> config, String key) {
        try {
            return config.get(key);
        } catch (NullPointerException e) {
            return "default-value";  // <-- BUG: exception as control flow
        }
    }

    // =====================================================================
    // Exercise 6: Include context in messages (Rule 7)
    // =====================================================================
    // FIX: The exception message is too vague to be useful.

    public void withdraw(String accountId, double balance, double amount) {
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Insufficient funds");  // <-- BUG: no context
        }
        System.out.printf("Withdrew %.2f from %s%n", amount, accountId);
    }

    // =====================================================================
    // Exercise 7: Batch processing (Best Practice Pattern)
    // =====================================================================
    // FIX: One failure stops all processing. Process each item individually.

    public List<String> processAll(List<String> items) {
        List<String> results = new ArrayList<>();
        for (String item : items) {
            processItem(item);  // <-- BUG: first failure aborts entire batch
            results.add(item);
        }
        return results;
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

    public static class UserException extends RuntimeException {
        public UserException(String message) { super(message); }
        public UserException(String message, Throwable cause) { super(message, cause); }
    }

    public static class InsufficientFundsException extends RuntimeException {
        private final String errorCode;

        public InsufficientFundsException(String message) {
            super(message);
            this.errorCode = "ERR_INSUFFICIENT_FUNDS";
        }

        public InsufficientFundsException(String message, Throwable cause) {
            super(message, cause);
            this.errorCode = "ERR_INSUFFICIENT_FUNDS";
        }

        public String getErrorCode() { return errorCode; }
    }

    public static class ItemProcessingException extends RuntimeException {
        public ItemProcessingException(String message) { super(message); }
        public ItemProcessingException(String message, Throwable cause) { super(message, cause); }
    }
}
