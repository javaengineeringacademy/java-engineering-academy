package academy.javaengineering.exceptions.checkedexception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Practical examples of checked exception handling patterns.
 */
public class CheckedExceptionExample {

    /**
     * Example 1: Reading a file with proper resource management.
     * Demonstrates try-with-resources and catching IOException.
     */
    public static String readFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString().trim();
        }
    }

    /**
     * Example 2: Method that declares exceptions to let the caller decide.
     * This is the "throws" approach to the catch-or-specify requirement.
     */
    public static int countLines(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            int count = 0;
            while (reader.readLine() != null) {
                count++;
            }
            return count;
        }
    }

    /**
     * Example 3: Exception translation — wrapping a checked exception
     * in a domain-specific unchecked exception.
     */
    public static class FileProcessingException extends RuntimeException {
        public FileProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String readAndProcess(String path) {
        try {
            return readFile(path);
        } catch (IOException e) {
            throw new FileProcessingException(
                "Failed to read file: " + path, e);
        }
    }

    /**
     * Example 4: Retry pattern with checked exceptions.
     * Retries a failing I/O operation up to a maximum number of times.
     */
    public static String readWithRetry(String path, int maxRetries)
            throws IOException {
        IOException lastException = null;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return readFile(path);
            } catch (IOException e) {
                lastException = e;
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
            }
        }
        throw lastException;
    }

    /**
     * Example 5: Partial failure handling — continue processing
     * even when some operations fail.
     */
    public static void processFiles(String[] paths) {
        int successCount = 0;
        int failCount = 0;

        for (String path : paths) {
            try {
                String content = readFile(path);
                System.out.println("OK: " + path + " (" + content.length() + " chars)");
                successCount++;
            } catch (IOException e) {
                System.out.println("FAIL: " + path + " — " + e.getMessage());
                failCount++;
            }
        }

        System.out.println("Results: " + successCount + " succeeded, "
            + failCount + " failed");
    }

    /**
     * Example 6: Catching and rethrowing with additional context.
     */
    public static byte[] readBytes(String path) throws IOException {
        try {
            return java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(path));
        } catch (IOException e) {
            throw new IOException(
                "Unable to read bytes from " + path, e);
        }
    }

    /**
     * Example 7: Demonstrates how InterruptedException requires
     * special handling — restore the interrupt status.
     */
    public static void runWithTimeout(Runnable task, long timeoutMs)
            throws InterruptedException {
        Thread worker = new Thread(task);
        worker.start();
        worker.join(timeoutMs);
        if (worker.isAlive()) {
            worker.interrupt();
            throw new InterruptedException("Task timed out after " + timeoutMs + "ms");
        }
    }

    /**
     * Example 8: Custom checked exception for domain validation.
     */
    public static class InvalidEmailException extends Exception {
        public InvalidEmailException(String email) {
            super("Invalid email address: " + email);
        }
    }

    public static void validateEmail(String email) throws InvalidEmailException {
        if (email == null || !email.contains("@")) {
            throw new InvalidEmailException(email);
        }
    }

    public static void main(String[] args) {
        // Example 1: Basic file reading
        try {
            String content = readFile("/tmp/example.txt");
            System.out.println("File content:\n" + content);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Example 2: Counting lines
        try {
            int lines = countLines("/tmp/example.txt");
            System.out.println("Line count: " + lines);
        } catch (IOException e) {
            System.out.println("Error counting lines: " + e.getMessage());
        }

        // Example 3: Exception translation
        String result = readAndProcess("/nonexistent.txt");
        System.out.println("Processed: " + result);

        // Example 5: Partial failure
        processFiles(new String[]{
            "/tmp/example.txt",
            "/nonexistent.txt",
            "/tmp/another.txt"
        });

        // Example 8: Custom checked exception
        try {
            validateEmail("user@example.com");
            System.out.println("Email is valid.");
        } catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
        }

        try {
            validateEmail("invalid-email");
        } catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
        }
    }
}
