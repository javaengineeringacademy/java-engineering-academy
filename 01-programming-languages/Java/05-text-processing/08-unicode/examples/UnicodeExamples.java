package academy.javaengineering.text.examples;

/**
 * Unicode Examples - Practical demonstrations of Unicode usage.
 * 
 * WHY UNICODE IS IMPORTANT:
 * - Supports all world languages
 * - Consistent encoding across platforms
 * - Enables internationalization
 * 
 * KEY CONCEPTS:
 * - Code Point: Unique number for each character
 * - UTF-16: Java's internal encoding
 * - Surrogate Pairs: For characters outside BMP
 * - BMP: Basic Multilingual Plane (first 65,536 characters)
 */
public class UnicodeExamples {

    public static void main(String[] args) {
        System.out.println("=== Unicode Examples ===\n");

        // Example 1: Code Points
        example1_CodePoints();

        // Example 2: Surrogate Pairs
        example2_SurrogatePairs();

        // Example 3: Unicode Operations
        example3_UnicodeOperations();

        // Example 4: Unicode in Strings
        example4_UnicodeInStrings();
    }

    /**
     * WHY: Code points represent characters as numbers.
     * 
     * INTERNAL: Java uses UTF-16 internally. Code points > 0xFFFF use surrogate pairs.
     */
    private static void example1_CodePoints() {
        System.out.println("--- Example 1: Code Points ---");

        String text = "Hello";

        System.out.println("Text: " + text);
        System.out.println("Length (chars): " + text.length());
        System.out.println("Length (code points): " + text.codePointCount(0, text.length()));

        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);
            System.out.println("Char " + i + ": " + text.charAt(i) + " (code point: " + codePoint + ")");
        }
    }

    /**
     * WHY: Emoji and some characters need surrogate pairs.
     * 
     * ENGINEERING: Always use codePointCount() for accurate length.
     */
    private static void example2_SurrogatePairs() {
        System.out.println("\n--- Example 2: Surrogate Pairs ---");

        String emoji = "Hello 🌍";
        String smile = "Hello 😊";

        System.out.println("Emoji: " + emoji);
        System.out.println("Length (chars): " + emoji.length());
        System.out.println("Length (code points): " + emoji.codePointCount(0, emoji.length()));

        System.out.println("\nSmile: " + smile);
        System.out.println("Length (chars): " + smile.length());
        System.out.println("Length (code points): " + smile.codePointCount(0, smile.length()));
    }

    /**
     * WHY: Unicode operations are needed for text processing.
     * 
     * ENGINEERING DECISION: Use code point methods for accurate text manipulation.
     */
    private static void example3_UnicodeOperations() {
        System.out.println("\n--- Example 3: Unicode Operations ---");

        String text = "Hello World";

        System.out.println("Text: " + text);
        System.out.println("Char at 0: " + text.charAt(0));
        System.out.println("Code point at 0: " + text.codePointAt(0));

        // Convert code point to char
        int codePoint = 65; // 'A'
        char[] chars = Character.toChars(codePoint);
        System.out.println("Code point 65: " + new String(chars));
    }

    /**
     * WHY: Unicode affects string comparison and sorting.
     * 
     * ENGINEERING DECISION: Use Collator for locale-aware sorting.
     */
    private static void example4_UnicodeInStrings() {
        System.out.println("\n--- Example 4: Unicode in Strings ---");

        String text = "Café";
        System.out.println("Text: " + text);
        System.out.println("Length (chars): " + text.length());
        System.out.println("Length (code points): " + text.codePointCount(0, text.length()));

        // Unicode normalization
        String normalized = text.normalize();
        System.out.println("Normalized: " + normalized);
        System.out.println("Equals: " + text.equals(normalized));
    }
}
