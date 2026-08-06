/**
 * CharacterDemo.java
 *
 * Demonstrates Character class methods and character operations.
 */
public class CharacterDemo {

    public static void main(String[] args) {
        System.out.println("=== Character Class Demo ===\n");

        // 1. Character creation
        characterCreation();

        // 2. Type checking methods
        typeChecking();

        // 3. Case conversion
        caseConversion();

        // 4. Unicode support
        unicodeSupport();

        // 5. char vs Character
        charVsCharacter();
    }

    private static void characterCreation() {
        System.out.println("--- 1. Character Creation ---");

        // Using constructor (deprecated in newer Java versions)
        Character ch1 = new Character('A');
        System.out.println("Character object: " + ch1);

        // Using valueOf (preferred)
        Character ch2 = Character.valueOf('A');
        System.out.println("Character.valueOf: " + ch2);

        // Auto-boxing
        Character ch3 = 'B';
        System.out.println("Auto-boxing: " + ch3);

        // Unboxing
        char ch4 = ch3;
        System.out.println("Unboxing: " + ch4);

        System.out.println();
    }

    private static void typeChecking() {
        System.out.println("--- 2. Type Checking Methods ---");

        char[] testChars = {'A', 'z', '0', '9', ' ', '@', '\n'};

        for (char ch : testChars) {
            System.out.println("Character '" + ch + "':");
            System.out.println("  isDigit: " + Character.isDigit(ch));
            System.out.println("  isLetter: " + Character.isLetter(ch));
            System.out.println("  isLetterOrDigit: " + Character.isLetterOrDigit(ch));
            System.out.println("  isUpperCase: " + Character.isUpperCase(ch));
            System.out.println("  isLowerCase: " + Character.isLowerCase(ch));
            System.out.println("  isWhitespace: " + Character.isWhitespace(ch));
            System.out.println("  isAlphabetic: " + Character.isAlphabetic(ch));
            System.out.println();
        }

        // Testing with strings
        String test = "Hello123";
        System.out.println("Analyzing string: \"" + test + "\"");

        int letters = 0, digits = 0, others = 0;
        for (char ch : test.toCharArray()) {
            if (Character.isLetter(ch)) {
                letters++;
            } else if (Character.isDigit(ch)) {
                digits++;
            } else {
                others++;
            }
        }

        System.out.println("Letters: " + letters);
        System.out.println("Digits: " + digits);
        System.out.println("Others: " + others);

        System.out.println();
    }

    private static void caseConversion() {
        System.out.println("--- 3. Case Conversion ---");

        char lower = 'a';
        char upper = Character.toUpperCase(lower);
        System.out.println("toUpperCase('" + lower + "'): " + upper);

        char upper2 = 'Z';
        char lower2 = Character.toLowerCase(upper2);
        System.out.println("toLowerCase('" + upper2 + "'): " + lower2);

        // Title case (Java 11+)
        char title = Character.toTitleCase('i');
        System.out.println("toTitleCase('i'): " + title);

        // Unicode case conversion
        String text = "hello world";
        System.out.println("toUpperCase(String): " + text.toUpperCase());
        System.out.println("toLowerCase(String): " + text.toUpperCase().toLowerCase());

        System.out.println();
    }

    private static void unicodeSupport() {
        System.out.println("--- 4. Unicode Support ---");

        // Regular characters
        char regular = 'A';
        System.out.println("Regular character:");
        System.out.println("  Unicode: " + (int) regular);
        System.out.println("  Unicode escape: \\u" +
            String.format("%04X", (int) regular));
        System.out.println("  charCount: " + Character.charCount((int) regular));

        // Supplementary characters (outside BMP)
        String supplementary = "𝕳"; // Mathematical Double-Struck H
        int codePoint = supplementary.codePointAt(0);
        System.out.println("\nSupplementary character: " + supplementary);
        System.out.println("  Code point: " + codePoint);
        System.out.println("  Unicode escape: \\U+" +
            String.format("%08X", codePoint));
        System.out.println("  charCount: " + Character.charCount(codePoint));

        // Character.isBmpCodePoint
        System.out.println("\nBMP code point 'A': " +
            Character.isBmpCodePoint('A'));
        System.out.println("BMP code point " + codePoint + ": " +
            Character.isBmpCodePoint(codePoint));

        // Character.isSupplementaryCodePoint
        System.out.println("Supplementary code point 'A': " +
            Character.isSupplementaryCodePoint('A'));
        System.out.println("Supplementary code point " + codePoint + ": " +
            Character.isSupplementaryCodePoint(codePoint));

        System.out.println();
    }

    private static void charVsCharacter() {
        System.out.println("--- 5. char vs Character ---");

        // char is primitive
        char primitive = 'A';
        System.out.println("char (primitive): " + primitive);
        System.out.println("  Size: 16 bits");
        System.out.println("  Default: '\\u0000'");

        // Character is wrapper class
        Character wrapper = Character.valueOf('A');
        System.out.println("\nCharacter (wrapper): " + wrapper);
        System.out.println("  Size: 16 bits (object overhead more)");
        System.out.println("  Default: null");

        // Auto-boxing
        Character autoBoxed = primitive; // auto-boxing
        char unboxed = wrapper;          // auto-unboxing
        System.out.println("\nAuto-boxing: " + primitive + " -> " + autoBoxed);
        System.out.println("Auto-unboxing: " + wrapper + " -> " + unboxed);

        // Comparison
        char c1 = 'A';
        char c2 = 'A';
        Character c3 = 'A';
        Character c4 = 'A';

        System.out.println("\nPrimitive comparison (==): " + (c1 == c2));
        System.out.println("Wrapper comparison (==): " + (c3 == c4));
        System.out.println("Wrapper equals(): " + c3.equals(c4));
        System.out.println("Wrapper compare(): " + Character.compare(c3, c4));

        // Character cache (for values -128 to 127)
        Character cached1 = 127;
        Character cached2 = 127;
        Character notCached1 = 128;
        Character notCached2 = 128;

        System.out.println("\nCached value (127) == : " + (cached1 == cached2));
        System.out.println("Non-cached value (128) == : " + (notCached1 == notCached2));
        System.out.println("Non-cached value (128) equals(): " +
            notCached1.equals(notCached2));

        System.out.println();
    }
}
