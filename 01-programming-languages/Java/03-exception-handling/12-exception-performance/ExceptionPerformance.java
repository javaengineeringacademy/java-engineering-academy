package exceptionperformance;

import java.util.Optional;
import java.util.function.Function;

/**
 * Exception Performance Demo
 * 
 * Covers performance costs of exceptions, alternative patterns,
 * and when to use exceptions vs return values.
 */
public class ExceptionPerformance {

    // ==========================================
    // SECTION 1: Performance Cost of Exceptions
    // ==========================================
    static class PerformanceCost {

        // Method that throws exceptions frequently
        static int divideByException(int a, int b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }

        // Method using conditional check instead
        static int divideByCheck(int a, int b) {
            if (b == 0) return 0; // or some default
            return a / b;
        }

        // Optional-based approach
        static Optional<Integer> divideByOptional(int a, int b) {
            if (b == 0) return Optional.empty();
            return Optional.of(a / b);
        }

        static void benchmarkExceptionCreation() {
            System.out.println("=== Exception Creation Cost ===\n");

            int iterations = 1_000_000;

            // Warm up
            for (int i = 0; i < 1000; i++) {
                try {
                    divideByException(10, 0);
                } catch (ArithmeticException e) {
                    // expected
                }
            }

            // Benchmark: Exception path
            long start = System.nanoTime();
            int exCount = 0;
            for (int i = 0; i < iterations; i++) {
                try {
                    divideByException(10, 0);
                } catch (ArithmeticException e) {
                    exCount++;
                }
            }
            long exceptionTime = System.nanoTime() - start;

            // Benchmark: Check path
            start = System.nanoTime();
            int checkCount = 0;
            for (int i = 0; i < iterations; i++) {
                int result = divideByCheck(10, 0);
                if (result == 0) checkCount++;
            }
            long checkTime = System.nanoTime() - start;

            // Benchmark: Optional path
            start = System.nanoTime();
            int optCount = 0;
            for (int i = 0; i < iterations; i++) {
                Optional<Integer> result = divideByOptional(10, 0);
                if (!result.isPresent()) optCount++;
            }
            long optionalTime = System.nanoTime() - start;

            System.out.printf("  Exception approach: %,d ns (%,d calls)%n", exceptionTime, exCount);
            System.out.printf("  Check approach:     %,d ns (%,d calls)%n", checkTime, checkCount);
            System.out.printf("  Optional approach:  %,d ns (%,d calls)%n", optionalTime, optCount);
            System.out.printf("  Exception is ~%.1fx slower than check%n",
                    (double) exceptionTime / checkTime);
            System.out.printf("  Optional is ~%.1fx slower than check%n",
                    (double) optionalTime / checkTime);
        }

        static void benchmarkStackTraceFill() {
            System.out.println("\n=== Stack Trace Fill Cost ===\n");

            int iterations = 100_000;

            // With stack trace (default)
            long start = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                try {
                    throw new RuntimeException("test");
                } catch (RuntimeException e) {
                    // just catch
                }
            }
            long withTrace = System.nanoTime() - start;

            // FillInStackTrace disabled
            start = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                try {
                    throw new NoStackException("test");
                } catch (NoStackException e) {
                    // just catch
                }
            }
            long noTrace = System.nanoTime() - start;

            System.out.printf("  With stack trace: %,d ns%n", withTrace);
            System.out.printf("  Without stack trace: %,d ns%n", noTrace);
            System.out.printf("  Stack trace adds ~%.1fx cost%n",
                    (double) withTrace / noTrace);
        }

        // Custom exception that doesn't fill stack trace
        static class NoStackException extends RuntimeException {
            public NoStackException(String message) {
                super(message);
            }

            @Override
            public synchronized Throwable fillInStackTrace() {
                return this; // Skip expensive stack trace filling
            }
        }
    }

    // ==========================================
    // SECTION 2: Alternative Patterns
    // ==========================================
    static class AlternativePatterns {

        // Result type pattern
        static class Result<T> {
            private final T value;
            private final Exception error;
            private final boolean success;

            private Result(T value, Exception error, boolean success) {
                this.value = value;
                this.error = error;
                this.success = success;
            }

            public static <T> Result<T> success(T value) {
                return new Result<>(value, null, true);
            }

            public static <T> Result<T> failure(Exception error) {
                return new Result<>(null, error, false);
            }

            public boolean isSuccess() { return success; }
            public boolean isFailure() { return !success; }
            public T getValue() { return value; }
            public Exception getError() { return error; }

            public T orElse(T defaultValue) {
                return success ? value : defaultValue;
            }

            public <R> Result<R> map(Function<T, R> mapper) {
                if (success) {
                    try {
                        return Result.success(mapper.apply(value));
                    } catch (Exception e) {
                        return Result.failure(e);
                    }
                }
                return Result.failure(error);
            }
        }

        // Parser using Result type
        static Result<Integer> parseIntSafe(String input) {
            if (input == null || input.isEmpty()) {
                return Result.failure(new IllegalArgumentException("Input is null or empty"));
            }
            try {
                return Result.success(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                return Result.failure(e);
            }
        }

        static void demonstrateResultPattern() {
            System.out.println("\n=== Result Type Pattern ===\n");

            String[] inputs = {"42", "abc", "", null, "100"};

            for (String input : inputs) {
                Result<Integer> result = parseIntSafe(input);
                if (result.isSuccess()) {
                    System.out.println("  '" + input + "' -> " + result.getValue());
                } else {
                    System.out.println("  '" + input + "' -> Error: " + result.getError().getMessage());
                }
            }

            // Chaining with map
            System.out.println("\n  Chaining:");
            Result<Integer> chained = parseIntSafe("42")
                    .map(n -> n * 2)
                    .map(n -> n + 10);
            System.out.println("    '42' -> *2 -> +10 = " + chained.orElse(0));
        }

        // Optional chaining pattern
        static Optional<Integer> parseAndValidate(String input) {
            return Optional.ofNullable(input)
                    .filter(s -> !s.isEmpty())
                    .flatMap(s -> {
                        try {
                            return Optional.of(Integer.parseInt(s));
                        } catch (NumberFormatException e) {
                            return Optional.empty();
                        }
                    })
                    .filter(n -> n > 0 && n < 1000);
        }

        static void demonstrateOptionalChaining() {
            System.out.println("\n=== Optional Chaining Pattern ===\n");

            String[] inputs = {"42", "abc", "", null, "100", "-5", "999", "1000"};

            for (String input : inputs) {
                Optional<Integer> result = parseAndValidate(input);
                System.out.printf("  '%s' -> %s%n", input,
                        result.map(String::valueOf).orElse("invalid"));
            }
        }

        // Null Object pattern
        static class NullUser {
            private final String name;

            NullUser(String name) { this.name = name; }

            String getName() { return name; }

            static final NullUser INSTANCE = new NullUser("anonymous");

            static NullUser of(String name) {
                return name != null ? new NullUser(name) : INSTANCE;
            }
        }

        static void demonstrateNullObject() {
            System.out.println("\n=== Null Object Pattern ===\n");

            String[] names = {"Alice", null, "Bob", ""};

            for (String name : names) {
                NullUser user = NullUser.of(name);
                System.out.println("  Name: " + user.getName());
            }
        }
    }

    // ==========================================
    // SECTION 3: When to Use Exceptions
    // ==========================================
    static class WhenToUseExceptions {

        // GOOD: Exception for truly exceptional conditions
        static int[] divideArray(int[] numerator, int denominator) {
            if (denominator == 0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            int[] result = new int[numerator.length];
            for (int i = 0; i < numerator.length; i++) {
                result[i] = numerator[i] / denominator;
            }
            return result;
        }

        // BAD: Using exceptions for control flow
        static int findIndexBad(int[] array, int target) {
            // Don't do this!
            try {
                for (int i = 0; i < array.length; i++) {
                    if (array[i] == target) return i;
                }
                throw new RuntimeException("Not found"); // Bad practice!
            } catch (RuntimeException e) {
                return -1;
            }
        }

        // GOOD: Using return value for expected cases
        static int findIndexGood(int[] array, int target) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == target) return i;
            }
            return -1; // Normal return value
        }

        static void demonstrateWhenToUse() {
            System.out.println("\n=== When to Use Exceptions ===\n");

            System.out.println("  USE exceptions for:");
            System.out.println("    - Truly unexpected errors (IO, network, DB)");
            System.out.println("    - Contract violations (null args, illegal state)");
            System.out.println("    - Resource cleanup failures");
            System.out.println("    - API boundaries where caller can't handle locally");

            System.out.println("\n  DON'T use exceptions for:");
            System.out.println("    - Normal control flow (loop termination)");
            System.out.println("    - Expected conditions (null checks, not found)");
            System.out.println("    - Alternative return values exist (Optional, Result)");
            System.out.println("    - Performance-critical code paths");

            System.out.println("\n  Comparison:");

            int[] data = {1, 2, 3, 4, 5};

            long start = System.nanoTime();
            for (int i = 0; i < 1_000_000; i++) {
                findIndexBad(data, 3);
            }
            long badTime = System.nanoTime() - start;

            start = System.nanoTime();
            for (int i = 0; i < 1_000_000; i++) {
                findIndexGood(data, 3);
            }
            long goodTime = System.nanoTime() - start;

            System.out.printf("    Exception-based search: %,d ns%n", badTime);
            System.out.printf("    Return-value search:    %,d ns%n", goodTime);
            System.out.printf("    Return value is ~%.1fx faster%n", (double) badTime / goodTime);
        }

        // Error code vs exception comparison
        static class ErrorCodes {

            // Error code approach
            static final int SUCCESS = 0;
            static final int ERROR_NULL_INPUT = 1;
            static final int ERROR_INVALID_FORMAT = 2;
            static final int ERROR_OUT_OF_RANGE = 3;

            static int parseWithCodes(String input, int[] output) {
                if (input == null || input.isEmpty()) return ERROR_NULL_INPUT;
                try {
                    output[0] = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    return ERROR_INVALID_FORMAT;
                }
                if (output[0] < 0 || output[0] > 100) return ERROR_OUT_OF_RANGE;
                return SUCCESS;
            }

            // Exception approach
            static int parseWithException(String input) {
                if (input == null || input.isEmpty()) {
                    throw new IllegalArgumentException("Input is null or empty");
                }
                int value = Integer.parseInt(input);
                if (value < 0 || value > 100) {
                    throw new IllegalArgumentException("Value out of range: " + value);
                }
                return value;
            }

            static void demonstrateComparison() {
                System.out.println("\n=== Error Codes vs Exceptions ===\n");

                String[] inputs = {"42", null, "abc", "150"};

                System.out.println("  Error Code approach:");
                for (String input : inputs) {
                    int[] result = new int[1];
                    int code = parseWithCodes(input, result);
                    switch (code) {
                        case SUCCESS: System.out.println("    '" + input + "' -> " + result[0]); break;
                        case ERROR_NULL_INPUT: System.out.println("    '" + input + "' -> null input"); break;
                        case ERROR_INVALID_FORMAT: System.out.println("    '" + input + "' -> invalid format"); break;
                        case ERROR_OUT_OF_RANGE: System.out.println("    '" + input + "' -> out of range"); break;
                    }
                }

                System.out.println("\n  Exception approach:");
                for (String input : inputs) {
                    try {
                        int value = parseWithException(input);
                        System.out.println("    '" + input + "' -> " + value);
                    } catch (Exception e) {
                        System.out.println("    '" + input + "' -> " + e.getMessage());
                    }
                }

                System.out.println("\n  Trade-offs:");
                System.out.println("    Error codes: No overhead, but easy to ignore");
                System.out.println("    Exceptions: Guaranteed handling, but slower");
            }
        }
    }

    // ==========================================
    // SECTION 4: Performance Best Practices
    // ==========================================
    static class PerformanceBestPractices {

        // Lazy exception message construction
        static void processWithLazyMessage(int value) {
            if (value < 0) {
                // Only construct message if exception will be thrown
                throw new IllegalArgumentException("Negative value: " + value);
            }
            // Normal processing continues
        }

        // Pre-validate to avoid exception
        static int safeDivide(int a, int b) {
            // Check BEFORE the operation
            if (b == 0) {
                // Handle gracefully or throw meaningful exception
                return 0;
            }
            return a / b;
        }

        // Exception-safe iteration
        static <T> boolean contains(java.util.Collection<T> collection, T target) {
            // Use return value instead of catching exception
            return collection.stream().anyMatch(item ->
                    item == null ? target == null : item.equals(target));
        }

        static void demonstrateBestPractices() {
            System.out.println("\n=== Performance Best Practices ===\n");

            System.out.println("  1. Validate early - check conditions before operations");
            System.out.println("  2. Use return values for expected cases");
            System.out.println("  3. Lazy message construction - build messages only when needed");
            System.out.println("  4. Cache exception messages for repeated scenarios");
            System.out.println("  5. Consider fillInStackTrace() override for hot paths");
            System.out.println("  6. Use try-catch outside loops, not inside");
            System.out.println("  7. Profile before optimizing - measure actual bottlenecks");

            System.out.println("\n  Quick benchmark - try inside vs outside loop:");

            int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

            // Bad: try-catch inside loop
            long start = System.nanoTime();
            int sum1 = 0;
            for (int val : data) {
                try {
                    sum1 += safeDivide(100, val);
                } catch (Exception e) {
                    // handle
                }
            }
            long insideTime = System.nanoTime() - start;

            // Good: try-catch outside loop
            start = System.nanoTime();
            int sum2 = 0;
            try {
                for (int val : data) {
                    sum2 += safeDivide(100, val);
                }
            } catch (Exception e) {
                // handle
            }
            long outsideTime = System.nanoTime() - start;

            System.out.printf("    Inside loop:  %,d ns (sum=%d)%n", insideTime, sum1);
            System.out.printf("    Outside loop: %,d ns (sum=%d)%n", outsideTime, sum2);
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  EXCEPTION PERFORMANCE DEMO              ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        PerformanceCost.benchmarkExceptionCreation();
        PerformanceCost.benchmarkStackTraceFill();

        AlternativePatterns.demonstrateResultPattern();
        AlternativePatterns.demonstrateOptionalChaining();
        AlternativePatterns.demonstrateNullObject();

        WhenToUseExceptions.demonstrateWhenToUse();
        WhenToUseExceptions.ErrorCodes.demonstrateComparison();

        PerformanceBestPractices.demonstrateBestPractices();

        System.out.println("\nAll exception performance demos complete!");
    }
}
