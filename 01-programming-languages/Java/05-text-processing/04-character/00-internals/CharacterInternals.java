package academy.javaengineering.text.internals;

public class CharacterInternals {

    public static void main(String[] args) {
        System.out.println("=== Character Internals ===\n");

        // 1. Character Class
        System.out.println("--- Character Class ---");
        char c = 'A';
        System.out.println("isLetter('A'): " + Character.isLetter(c));
        System.out.println("isDigit('5'): " + Character.isDigit('5'));
        System.out.println("toUpperCase('a'): " + Character.toUpperCase('a'));
        System.out.println("toLowerCase('A'): " + Character.toLowerCase('A'));

        // 2. Unicode
        System.out.println("\n--- Unicode ---");
        char unicode = '\u0041'; // 'A'
        System.out.println("Unicode \\u0041: " + unicode);
        System.out.println("Code point: " + (int) unicode);

        // 3. Character Methods
        System.out.println("\n--- Character Methods ---");
        System.out.println("isWhitespace(' '): " + Character.isWhitespace(' '));
        System.out.println("isAlphabetic('A'): " + Character.isAlphabetic('A'));
        System.out.println("getNumericValue('9'): " + Character.getNumericValue('9'));
    }
}
