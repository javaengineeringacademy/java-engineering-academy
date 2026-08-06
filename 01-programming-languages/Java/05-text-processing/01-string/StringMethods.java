package strings;

import java.util.Arrays;
import java.util.Locale;

/**
 * StringMethods - Demonstrates all common String methods
 *
 * Covers:
 * - Character access and search
 * - Extraction and transformation
 * - Trimming and padding
 * - Splitting and joining
 * - Regular expressions
 */
public class StringMethods {

    public static void main(String[] args) {
        System.out.println("=== Character Access ===");
        characterAccess();

        System.out.println("\n=== Search Methods ===");
        searchMethods();

        System.out.println("\n=== Extraction Methods ===");
        extractionMethods();

        System.out.println("\n=== Transformation Methods ===");
        transformationMethods();

        System.out.println("\n=== Trim and Pad ===");
        trimAndPad();

        System.out.println("\n=== Split and Join ===");
        splitAndJoin();
    }

    static void characterAccess() {
        String str = "Hello, World!";

        // charAt - get character at index
        System.out.println("charAt(0): " + str.charAt(0));

        // length - number of characters
        System.out.println("length(): " + str.length());

        // isEmpty - check if empty
        System.out.println("isEmpty(): " + str.isEmpty());

        // isBlank - check if blank (Java 11+)
        System.out.println("isBlank(): " + str.isBlank());

        // codePointAt - Unicode code point
        System.out.println("codePointAt(0): " + str.codePointAt(0));
    }

    static void searchMethods() {
        String str = "Java Programming Language";

        // indexOf - first occurrence
        System.out.println("indexOf('a'): " + str.indexOf('a'));
        System.out.println("indexOf('a', 2): " + str.indexOf('a', 2));

        // lastIndexOf - last occurrence
        System.out.println("lastIndexOf('a'): " + str.lastIndexOf('a'));

        // contains - substring check
        System.out.println("contains('Program'): " + str.contains("Program"));

        // startsWith and endsWith
        System.out.println("startsWith('Java'): " + str.startsWith("Java"));
        System.out.println("endsWith('age'): " + str.endsWith("age"));

        // matches - regex matching
        System.out.println("matches('[A-Z].*'): " + str.matches("[A-Z].*"));
    }

    static void extractionMethods() {
        String str = "Hello, World!";

        // substring - extract portion
        System.out.println("substring(7): " + str.substring(7));
        System.out.println("substring(0, 5): " + str.substring(0, 5));

        // toCharArray - convert to char array
        char[] chars = str.toCharArray();
        System.out.println("toCharArray(): " + Arrays.toString(chars));

        // getBytes - convert to byte array
        byte[] bytes = str.getBytes();
        System.out.println("getBytes().length: " + bytes.length);

        // format - formatted string
        String formatted = String.format("Name: %s, Age: %d", "John", 30);
        System.out.println("format(): " + formatted);
    }

    static void transformationMethods() {
        String str = "  Hello, World!  ";

        // toUpperCase and toLowerCase
        System.out.println("toUpperCase(): " + str.toUpperCase());
        System.out.println("toLowerCase(): " + str.toLowerCase());

        // toUpperCase with Locale
        System.out.println("toUpperCase(TURKISH): " + "istanbul".toUpperCase(Locale.TURKISH));

        // replace - replace characters
        System.out.println("replace('l', 'L'): " + str.replace('l', 'L'));

        // replaceAll - regex replacement
        System.out.println("replaceAll('[aeiou]', '*'): " +
            "Hello World".replaceAll("[aeiou]", "*"));

        // replaceFirst - replace first occurrence
        System.out.println("replaceFirst('l', 'L'): " + "Hello".replaceFirst("l", "L"));

        // concat - append string
        System.out.println("concat('!'): " + "Hello".concat("!"));

        // valueOf - convert to String
        System.out.println("valueOf(123): " + String.valueOf(123));
        System.out.println("valueOf(true): " + String.valueOf(true));
        System.out.println("valueOf(3.14): " + String.valueOf(3.14));
    }

    static void trimAndPad() {
        String padded = "  Hello, World!  ";

        // trim - remove leading/trailing whitespace
        System.out.println("trim(): '" + padded.trim() + "'");

        // strip - Java 11+, Unicode-aware
        System.out.println("strip(): '" + padded.strip() + "'");

        // stripLeading and stripTrailing
        System.out.println("stripLeading(): '" + padded.stripLeading() + "'");
        System.out.println("stripTrailing(): '" + padded.stripTrailing() + "'");

        // repeat - Java 11+
        System.out.println("repeat(3): " + "Ha".repeat(3));

        // indent - Java 12+
        System.out.println("indent(1): '" + "Hello".indent(1) + "'");

        // transform - Java 12+
        String transformed = "hello".transform(s -> s.toUpperCase() + "!");
        System.out.println("transform(): " + transformed);
    }

    static void splitAndJoin() {
        String csv = "apple,banana,cherry";

        // split - divide string by delimiter
        String[] fruits = csv.split(",");
        System.out.println("split(','):" );
        for (String fruit : fruits) {
            System.out.println("  - " + fruit);
        }

        // split with limit
        String limited = "a,b,c,d";
        String[] firstTwo = limited.split(",", 2);
        System.out.println("split(',', 2): " + Arrays.toString(firstTwo));

        // join - combine strings with delimiter
        String joined = String.join(" | ", fruits);
        System.out.println("join(' | '): " + joined);

        // StringJoiner - more control
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String fruit : fruits) {
            joiner.add(fruit);
        }
        System.out.println("StringJoiner: " + joiner.toString());

        // chars - Java 8+ stream of characters
        System.out.print("chars() stream: ");
        "Hello".chars()
            .mapToObj(c -> (char) c)
            .forEach(c -> System.out.print(c + " "));
        System.out.println();
    }
}