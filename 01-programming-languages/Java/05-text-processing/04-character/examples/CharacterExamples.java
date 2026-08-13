package academy.javaengineering.text.examples;

/**
 * Character Examples - Practical demonstrations of Character class usage.
 * 
 * WHY CHARACTER CLASS EXISTS:
 * - Wraps primitive char in an object
 * - Provides utility methods for character operations
 * - Used in generics (Character instead of char)
 * 
 * KEY METHODS:
 * - isLetter(), isDigit(), isWhitespace()
 * - toUpperCase(), toLowerCase()
 * - isUpperCase(), isLowerCase()
 */
public class CharacterExamples {

    public static void main(String[] args) {
        System.out.println("=== Character Examples ===\n");

        // Example 1: Character Classification
        example1_Classification();

        // Example 2: Character Conversion
        example2_Conversion();

        // Example 3: Character Validation
        example3_Validation();

        // Example 4: Character in Strings
        example4_InStrings();
    }

    /**
     * WHY: Character classification helps validate user input.
     * 
     * ENGINEERING DECISION: Use Character methods instead of manual ASCII checks.
     */
    private static void example1_Classification() {
        System.out.println("--- Example 1: Character Classification ---");

        char[] chars = {'A', 'z', '5', ' ', '@', '\n'};

        for (char c : chars) {
            System.out.println("'" + c + "':");
            System.out.println("  isLetter(): " + Character.isLetter(c));
            System.out.println("  isDigit(): " + Character.isDigit(c));
            System.out.println("  isWhitespace(): " + Character.isWhitespace(c));
            System.out.println("  isUpperCase(): " + Character.isUpperCase(c));
            System.out.println("  isLowerCase(): " + Character.isLowerCase(c));
        }
    }

    /**
     * WHY: Character conversion is needed for case-insensitive operations.
     * 
     * PERFORMANCE: toLowerCase() and toUpperCase() are locale-sensitive.
     * Use toLowerCase(Locale) for consistent behavior.
     */
    private static void example2_Conversion() {
        System.out.println("\n--- Example 2: Character Conversion ---");

        char c = 'a';
        System.out.println("Original: " + c);
        System.out.println("toUpperCase(): " + Character.toUpperCase(c));
        System.out.println("toLowerCase(): " + Character.toLowerCase(Character.toUpperCase(c)));

        int codePoint = Character.codePointAt("Hello", 0);
        System.out.println("Code point of 'H': " + codePoint);
        System.out.println("Char from code point: " + Character.toChars(codePoint));
    }

    /**
     * WHY: Character validation is essential for input processing.
     * 
     * ENGINEERING DECISION: Always validate input characters before processing.
     */
    private static void example3_Validation() {
        System.out.println("\n--- Example 3: Character Validation ---");

        String email = "user@example.com";
        boolean allLetters = true;

        for (char c : email.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '@' && c != '.') {
                allLetters = false;
                break;
            }
        }

        System.out.println("Email: " + email);
        System.out.println("Valid characters: " + allLetters);
    }

    /**
     * WHY: Character operations are fundamental to string processing.
     * 
     * PERFORMANCE: Using char arrays is faster than String operations.
     */
    private static void example4_InStrings() {
        System.out.println("\n--- Example 4: Character in Strings ---");

        String text = "Hello World";
        int vowels = 0;
        int consonants = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                if ("aeiouAEIOU".indexOf(c) != -1) {
                    vowels++;
                } else {
                    consonants++;
                }
            }
        }

        System.out.println("Text: " + text);
        System.out.println("Vowels: " + vowels);
        System.out.println("Consonants: " + consonants);
    }
}
