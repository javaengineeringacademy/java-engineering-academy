/**
 * UnicodeDemo.java
 *
 * Demonstrates Unicode basics and handling in Java.
 */
public class UnicodeDemo {

    public static void main(String[] args) {
        System.out.println("=== Unicode Demo ===\n");

        // 1. Unicode basics
        unicodeBasics();

        // 2. Code points and code units
        codePointsAndCodeUnits();

        // 3. Supplementary characters
        supplementaryCharacters();

        // 4. Unicode escape sequences
        unicodeEscapeSequences();

        // 5. Unicode string operations
        unicodeStringOperations();
    }

    private static void unicodeBasics() {
        System.out.println("--- 1. Unicode Basics ---");

        // Basic Unicode characters
        char latin = 'A';
        char chinese = '你';
        char arabic = 'ع';

        System.out.println("Latin 'A': " + (int) latin);
        System.out.println("Chinese '你': " + (int) chinese);
        System.out.println("Arabic 'ع': " + (int) arabic);

        // Unicode representation
        System.out.println("\nUnicode representations:");
        System.out.println("Latin 'A': U+" + String.format("%04X", (int) latin));
        System.out.println("Chinese '你': U+" + String.format("%04X", (int) chinese));
        System.out.println("Arabic 'ع': U+" + String.format("%04X", (int) arabic));

        // Unicode blocks
        System.out.println("\nUnicode blocks:");
        System.out.println("Latin 'A': " +
            java.lang.Character.UnicodeBlock.of(latin));
        System.out.println("Chinese '你': " +
            java.lang.Character.UnicodeBlock.of(chinese));

        System.out.println();
    }

    private static void codePointsAndCodeUnits() {
        System.out.println("--- 2. Code Points and Code Units ---");

        // String with mixed characters
        String text = "Hello, 世界! 𝕳𝖊𝖑𝖑𝖔";

        System.out.println("String: " + text);
        System.out.println("Length (code units): " + text.length());

        // Count code points
        int codePoints = text.codePointCount(0, text.length());
        System.out.println("Code points: " + codePoints);

        // Iterate by code points
        System.out.println("\nIterating by code points:");
        StringBuilder sb = new StringBuilder();
        text.codePoints().forEach(cp -> {
            sb.appendCodePoint(cp);
            System.out.print("U+" + String.format("%04X", cp) + " ");
        });
        System.out.println();

        // Code point at index
        System.out.println("\nCode point at index 0: " +
            String.format("U+%04X", text.codePointAt(0)));
        System.out.println("Code point at index 7: " +
            String.format("U+%04X", text.codePointAt(7)));

        // char[] vs code points
        char[] chars = text.toCharArray();
        System.out.println("\nchar array length: " + chars.length);

        // Using Character.charCount
        System.out.println("\nCharacter.charCount for each code point:");
        for (int i = 0; i < chars.length; ) {
            int cp = Character.codePointAt(chars, i);
            int count = Character.charCount(cp);
            System.out.println("  U+" + String.format("%04X", cp) +
                " uses " + count + " char(s)");
            i += count;
        }

        System.out.println();
    }

    private static void supplementaryCharacters() {
        System.out.println("--- 3. Supplementary Characters ---");

        // Supplementary characters (outside Basic Multilingual Plane)
        // These require surrogate pairs in UTF-16

        // Mathematical Double-Struck letters
        String supplementary = "𝕳𝖊𝖑𝖑𝖔";

        System.out.println("Supplementary string: " + supplementary);
        System.out.println("Length (char count): " + supplementary.length());

        // Each supplementary character uses 2 chars (surrogate pair)
        int codePoints = supplementary.codePointCount(0, supplementary.length());
        System.out.println("Code points: " + codePoints);

        // Check if characters are supplementary
        System.out.println("\nCharacter analysis:");
        for (int i = 0; i < supplementary.length(); i++) {
            char ch = supplementary.charAt(i);
            System.out.println("  char[" + i + "] = " +
                String.format("U+%04X", (int) ch) +
                " (isSurrogate: " + Character.isSurrogate(ch) + ")");
        }

        // Using codePointAt for supplementary characters
        System.out.println("\nCode points:");
        for (int i = 0; i < supplementary.length(); ) {
            int cp = supplementary.codePointAt(i);
            System.out.println("  U+" + String.format("%04X", cp));
            i += Character.charCount(cp);
        }

        // Creating supplementary characters
        int codePoint = 0x1D574; // Mathematical Double-Struck A
        String created = new String(Character.toChars(codePoint));
        System.out.println("\nCreated from code point: " + created);

        System.out.println();
    }

    private static void unicodeEscapeSequences() {
        System.out.println("--- 4. Unicode Escape Sequences ---");

        // Unicode escape in string literal
        String unicode1 = "Greek letter: \\u03B1";
        System.out.println("Unicode escape: " + unicode1);

        // Multiple escapes
        String unicode2 = "\\u0041\\u0042\\u0043";
        System.out.println("Multiple escapes: " + unicode2);

        // Unicode in char
        char alpha = '\\u03B1';
        System.out.println("Unicode char: " + alpha);

        // Unicode escapes are processed at compile time
        String compileTime = "\\u0048\\u0065\\u006C\\u006C\\u006F";
        System.out.println("Compile-time processing: " + compileTime);

        // Code point from escape
        int cp = 0x03B1;
        String fromCodePoint = new String(Character.toChars(cp));
        System.out.println("From code point: " + fromCodePoint);

        System.out.println();
    }

    private static void unicodeStringOperations() {
        System.out.println("--- 5. Unicode String Operations ---");

        String text = "Hello, 世界!";

        // Length vs code point count
        System.out.println("String: " + text);
        System.out.println("Length (char): " + text.length());
        System.out.println("Code points: " +
            text.codePointCount(0, text.length()));

        // Substring vs code point operations
        System.out.println("\nSubstring operations:");
        System.out.println("substring(0, 5): " + text.substring(0, 5));
        System.out.println("substring(7, 9): " + text.substring(7, 9));

        // Using offsetByCodePoints
        int offset = text.offsetByCodePoints(0, 5);
        System.out.println("\nOffset by 5 code points: " + offset);

        // Comparison
        String str1 = "cafe";
        String str2 = "caf\u00E9"; // é with combining accent
        System.out.println("\nComparison:");
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        System.out.println("equals: " + str1.equals(str2));
        System.out.println("equalsIgnoreCase: " +
            str1.equalsIgnoreCase(str2));

        // Normalize (important for Unicode comparison)
        String normalized1 = java.text.Normalizer.normalize(
            str1, java.text.Normalizer.Form.NFC);
        String normalized2 = java.text.Normalizer.normalize(
            str2, java.text.Normalizer.Form.NFC);
        System.out.println("Normalized equals: " + normalized1.equals(normalized2));

        System.out.println();
    }
}
