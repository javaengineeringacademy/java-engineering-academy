package academy.javaengineering.exceptionhandling;

/**
 * Performance Examples
 * 
 * Demonstrates performance considerations in exception handling.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class PerformanceExamples {

    private static final int ITERATIONS = 1_000_000;

    /**
     * Benchmarks exception handling performance.
     */
    public static void benchmarkExceptionHandling() {
        System.out.println("=== Exception Handling Performance Benchmark ===\n");
        
        // Benchmark 1: Normal operation
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            normalOperation(i);
        }
        long normalTime = System.nanoTime() - startTime;
        
        // Benchmark 2: Exception-based control flow
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            try {
                exceptionOperation(i);
            } catch (Exception e) {
                // Expected
            }
        }
        long exceptionTime = System.nanoTime() - startTime;
        
        // Benchmark 3: Pre-validation
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            validatedOperation(i);
        }
        long validatedTime = System.nanoTime() - startTime;
        
        System.out.printf("Normal operation: %d ms%n", normalTime / 1_000_000);
        System.out.printf("Exception-based: %d ms%n", exceptionTime / 1_000_000);
        System.out.printf("Pre-validated: %d ms%n", validatedTime / 1_000_000);
        System.out.printf("Exception/Normal ratio: %.2f%n", (double) exceptionTime / normalTime);
        System.out.printf("Validated/Normal ratio: %.2f%n", (double) validatedTime / normalTime);
    }

    static void normalOperation(int i) {
        if (i % 100 == 0) {
            // Handle special case
        }
    }

    static void exceptionOperation(int i) throws Exception {
        if (i % 100 == 0) {
            throw new Exception("Special case");
        }
    }

    static void validatedOperation(int i) {
        if (i % 100 == 0) {
            // Handle special case
        }
    }

    /**
     * Demonstrates exception creation cost.
     */
    public static void exceptionCreationCost() {
        System.out.println("\n=== Exception Creation Cost ===\n");
        
        // Without stack trace
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            new RuntimeException("Message");
        }
        long withoutTrace = System.nanoTime() - startTime;
        
        // With stack trace (default)
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            RuntimeException e = new RuntimeException("Message");
            e.getStackTrace(); // Forces stack trace filling
        }
        long withTrace = System.nanoTime() - startTime;
        
        System.out.printf("Without stack trace: %d ms%n", withoutTrace / 1_000_000);
        System.out.printf("With stack trace: %d ms%n", withTrace / 1_000_000);
        System.out.printf("Ratio: %.2f%n", (double) withTrace / withoutTrace);
    }

    /**
     * Demonstrates performance best practices.
     */
    public static void bestPractices() {
        System.out.println("\n=== Performance Best Practices ===\n");
        
        // Bad: Using exceptions for control flow
        System.out.println("Bad: Using exceptions for control flow");
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS / 10; i++) {
            try {
                Integer.parseInt("not_a_number");
            } catch (NumberFormatException e) {
                // Expected
            }
        }
        long badTime = System.nanoTime() - start;
        
        // Good: Using pre-validation
        System.out.println("Good: Using pre-validation");
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS / 10; i++) {
            String input = "not_a_number";
            if (input != null && input.matches("-?\\d+")) {
                Integer.parseInt(input);
            }
        }
        long goodTime = System.nanoTime() - start;
        
        System.out.printf("Bad approach: %d ms%n", badTime / 1_000_000);
        System.out.printf("Good approach: %d ms%n", goodTime / 1_000_000);
        System.out.printf("Speedup: %.2fx%n", (double) badTime / goodTime);
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        benchmarkExceptionHandling();
        exceptionCreationCost();
        bestPractices();
    }
}
