package academy.javaengineering.text.internals;

public class UnicodeInternals {

    public static void main(String[] args) {
        System.out.println("=== Unicode Internals ===\n");

        // 1. Unicode Basics
        System.out.println("--- Unicode Basics ---");
        char a = 'A';
        System.out.println("'A' code point: " + (int) a);
        System.out.println("'A' hex: " + Integer.toHexString(a));

        // 2. Unicode Escapes
        System.out.println("\n--- Unicode Escapes ---");
        char unicode = '\u0041'; // 'A'
        System.out.println("\\u0041: " + unicode);
        char emoji = '\uD83D\uDE00'; // Grinning face
        System.out.println("Emoji: " + emoji);

        // 3. Code Points
        System.out.println("\n--- Code Points ---");
        String text = "Hello";
        System.out.println("Code points: " + text.codePointCount(0, text.length()));
        for (int i = 0; i < text.length(); i++) {
            System.out.println("charAt(" + i + "): " + text.charAt(i));
        }
    }
}
