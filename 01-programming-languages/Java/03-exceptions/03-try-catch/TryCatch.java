/**
 * TryCatch.java
 *
 * Demonstrates all aspects of try-catch exception handling in Java:
 * - Basic try-catch
 * - Single catch block
 * - Multiple catch blocks
 * - Multi-catch (Java 7+)
 * - Nested try-catch
 * - Rethrowing exceptions
 * - Common patterns and anti-patterns
 */
public class TryCatch {

    public static void main(String[] args) {
        System.out.println("=== 03 - Try-Catch Exception Handling ===\n");

        demoBasicTryCatch();
        demoSingleCatch();
        demoMultipleCatch();
        demoMultiCatch();
        demoNestedTryCatch();
        demoRethrowing();
        demoExceptionChaining();
        demoFinallyWithTryCatch();
        demoCommonPatterns();
    }

    // -----------------------------------------------------------------------
    // 1. Basic try-catch
    // -----------------------------------------------------------------------
    static void demoBasicTryCatch() {
        System.out.println("--- 1. Basic try-catch ---");

        // Without try-catch: ArithmeticException would crash the program
        try {
            int a = 10;
            int b = 0;
            int result = a / b;
            System.out.println("Result: " + result);  // never reached
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("Program continues after catch\n");
    }

    // -----------------------------------------------------------------------
    // 2. Single catch block
    // -----------------------------------------------------------------------
    static void demoSingleCatch() {
        System.out.println("--- 2. Single catch block ---");

        try {
            String text = null;
            System.out.println("Length: " + text.length());
        } catch (NullPointerException e) {
            System.out.println("Caught NPE: " + e.getMessage());
        }

        System.out.println();
    }

    // -----------------------------------------------------------------------
    // 3. Multiple catch blocks
    // -----------------------------------------------------------------------
    static void demoMultipleCatch() {
        System.out.println("--- 3. Multiple catch blocks ---");

        String[] inputs = {"42", "abc", "100"};

        for (String input : inputs) {
            try {
                int number = Integer.parseInt(input);
                int[] array = new int[5];
                array[number] = number;
                System.out.println("Set index " + number + " = " + number);
            } catch (NumberFormatException e) {
                System.out.println("Bad number: \"" + input + "\" - " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Index out of range: " + e.getMessage());
            }
        }

        System.out.println();
    }

    // -----------------------------------------------------------------------
    // 4. Multi-catch (Java 7+)
    // -----------------------------------------------------------------------
    static void demoMultiCatch() {
        System.out.println("--- 4. Multi-catch (Java 7+) ---");

        String[] testCases = {"file.txt", null, ""};

        for (String fileName : testCases) {
            try {
                // This throws FileNotFoundException or SecurityException
                // or IllegalArgumentException
                processFileMultiCatch(fileName);
            } catch (FileNotFoundException | SecurityException | IllegalArgumentException e) {
                System.out.println("Handled [" + e.getClass().getSimpleName() + "]: " + e.getMessage());
            }
        }

        System.out.println();
    }

    static void processFileMultiCatch(String fileName) throws FileNotFoundException, SecurityException {
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        if (fileName.isEmpty()) {
            throw new FileNotFoundException("File name is empty");
        }
        // In real code, would actually try to open the file
        throw new SecurityException("Simulated access denied for: " + fileName);
    }

    // -----------------------------------------------------------------------
    // 5. Nested try-catch
    // -----------------------------------------------------------------------
    static void demoNestedTryCatch() {
        System.out.println("--- 5. Nested try-catch ---");

        try {
            System.out.println("Outer try: starting");
            String data = "not_a_number";

            try {
                System.out.println("  Inner try: parsing");
                int value = Integer.parseInt(data);
                int result = 100 / value;
                System.out.println("  Inner result: " + result);
            } catch (NumberFormatException e) {
                System.out.println("  Inner catch: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println("  Inner catch: " + e.getMessage());
            }

            System.out.println("Outer try: continues after inner block");
        } catch (Exception e) {
            System.out.println("Outer catch: " + e.getMessage());
        }

        System.out.println();
    }

    // -----------------------------------------------------------------------
    // 6. Rethrowing exceptions
    // -----------------------------------------------------------------------
    static void demoRethrowing() {
        System.out.println("--- 6. Rethrowing exceptions ---");

        try {
            try {
                riskyOperation();
            } catch (Exception e) {
                System.out.println("  Caught and rethrowing: " + e.getMessage());
                throw e;  // rethrow
            }
        } catch (Exception e) {
            System.out.println("  Outer caught rethrown: " + e.getMessage());
        }

        System.out.println();
    }

    static void riskyOperation() throws Exception {
        throw new Exception("Something went wrong in riskyOperation");
    }

    // -----------------------------------------------------------------------
    // 7. Exception chaining
    // -----------------------------------------------------------------------
    static void demoExceptionChaining() {
        System.out.println("--- 7. Exception chaining ---");

        try {
            try {
                int[] arr = new int[3];
                arr[10] = 42;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("Failed to process data", e);
            }
        } catch (RuntimeException e) {
            System.out.println("Chained exception: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }

        System.out.println();
    }

    // -----------------------------------------------------------------------
    // 8. finally with try-catch
    // -----------------------------------------------------------------------
    static void demoFinallyWithTryCatch() {
        System.out.println("--- 8. finally with try-catch ---");

        // finally runs even when exception is thrown
        try {
            System.out.println("Try block: before exception");
            int result = 10 / 0;
            System.out.println("Try block: after exception (never reached)");
        } catch (ArithmeticException e) {
            System.out.println("Catch block: " + e.getMessage());
        } finally {
            System.out.println("Finally block: always runs");
        }

        System.out.println("After try-catch-finally\n");
    }

    // -----------------------------------------------------------------------
    // 9. Common patterns
    // -----------------------------------------------------------------------
    static void demoCommonPatterns() {
        System.out.println("--- 9. Common patterns ---");

        // Pattern: Retry with backoff
        System.out.println("Retry pattern:");
        int result = retryWithBackoff(() -> {
            if (Math.random() < 0.7) {
                throw new RuntimeException("Simulated failure");
            }
            return 42;
        }, 3);
        System.out.println("Got result: " + result);

        // Pattern: Safe default
        System.out.println("\nSafe default pattern:");
        String value = getOrDefault("invalid", "fallback");
        System.out.println("Value: " + value);

        System.out.println();
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    static <T> T retryWithBackoff(ThrowingSupplier<T> operation, int maxAttempts) {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                System.out.println("  Attempt " + attempt + " failed: " + e.getMessage());
                try {
                    Thread.sleep((long) Math.pow(2, attempt) * 100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        throw new RuntimeException("Failed after " + maxAttempts + " attempts", lastException);
    }

    static String getOrDefault(String input, String defaultVal) {
        try {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input is empty");
            }
            return input.toUpperCase();
        } catch (IllegalArgumentException e) {
            return defaultVal;
        }
    }

    // -----------------------------------------------------------------------
    // Import for demo
    // -----------------------------------------------------------------------
    static class FileNotFoundException extends Exception {
        public FileNotFoundException(String message) {
            super(message);
        }
    }

    static class SecurityException extends Exception {
        public SecurityException(String message) {
            super(message);
        }
    }
}
