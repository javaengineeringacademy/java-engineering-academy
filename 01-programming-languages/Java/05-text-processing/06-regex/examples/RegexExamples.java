package academy.javaengineering.text.examples;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regex Examples - Practical demonstrations of Regex usage.
 * 
 * WHY JAVA USES NFA ENGINE:
 * - Deterministic: Same input always produces same output
 * - Predictable: No backtracking explosions
 * - Safe: Resistant to ReDoS attacks
 * 
 * PERFORMANCE TIPS:
 * - Compile patterns once, reuse them
 * - Use non-capturing groups (?:...) when you don't need the match
 * - Avoid .* at the start of patterns
 */
public class RegexExamples {

    public static void main(String[] args) {
        System.out.println("=== Regex Examples ===\n");

        // Example 1: Basic Pattern Matching
        example1_BasicMatching();

        // Example 2: Groups and Capturing
        example2_Groups();

        // Example 3: Common Patterns
        example3_CommonPatterns();

        // Example 4: Performance Considerations
        example4_Performance();
    }

    /**
     * WHY: Pattern.compile() pre-compiles regex for reuse.
     * 
     * PERFORMANCE: Compiling regex is expensive. Compile once, use many times.
     */
    private static void example1_BasicMatching() {
        System.out.println("--- Example 1: Basic Pattern Matching ---");

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher("abc123def456");

        System.out.println("Input: abc123def456");
        System.out.println("Pattern: \\d+");

        while (matcher.find()) {
            System.out.println("Found: " + matcher.group() + " at index " + matcher.start());
        }
    }

    /**
     * WHY: Groups allow extracting specific parts of a match.
     * 
     * ENGINEERING DECISION: Use named groups for complex patterns.
     */
    private static void example2_Groups() {
        System.out.println("\n--- Example 2: Groups and Capturing ---");

        Pattern pattern = Pattern.compile("(\\d{3})-(\\d{3})-(\\d{4})");
        Matcher matcher = pattern.matcher("Phone: 123-456-7890");

        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            System.out.println("Area code: " + matcher.group(1));
            System.out.println("Prefix: " + matcher.group(2));
            System.out.println("Line number: " + matcher.group(3));
        }
    }

    /**
     * WHY: Common patterns are useful for validation.
     * 
     * ENGINEERING DECISION: Use well-tested patterns for common validations.
     */
    private static void example3_CommonPatterns() {
        System.out.println("\n--- Example 3: Common Patterns ---");

        String[] emails = {"user@example.com", "invalid@", "@invalid.com", "valid@email.org"};

        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

        for (String email : emails) {
            boolean matches = emailPattern.matcher(email).matches();
            System.out.println(email + ": " + (matches ? "Valid" : "Invalid"));
        }
    }

    /**
     * WHY: Regex performance matters for large inputs.
     * 
     * PERFORMANCE: Pre-compile patterns and avoid catastrophic backtracking.
     */
    private static void example4_Performance() {
        System.out.println("\n--- Example 4: Performance Considerations ---");

        // Good: Pre-compiled pattern
        Pattern goodPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

        // Bad: Compiling in loop
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        }
        long compileTime = System.currentTimeMillis() - start;

        // Good: Reusing compiled pattern
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            goodPattern.matcher("2024-01-15").matches();
        }
        long matchTime = System.currentTimeMillis() - start;

        System.out.println("Compile time (10000x): " + compileTime + " ms");
        System.out.println("Match time (10000x): " + matchTime + " ms");
    }
}
