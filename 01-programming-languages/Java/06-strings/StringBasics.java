package strings;

/**
 * StringBasics - Demonstrates String creation, immutability, and comparison
 *
 * Key concepts:
 * - String creation: literal vs new
 * - Immutability: Strings cannot be changed after creation
 * - String pool: JVM optimization for String literals
 * - Comparison: == vs equals() vs compareTo()
 */
public class StringBasics {

    public static void main(String[] args) {
        System.out.println("=== String Creation ===");
        stringCreation();

        System.out.println("\n=== String Immutability ===");
        stringImmutability();

        System.out.println("\n=== String Comparison ===");
        stringComparison();

        System.out.println("\n=== String Pool ===");
        stringPool();
    }

    static void stringCreation() {
        // Method 1: String literal (stored in String pool)
        String literal = "Hello World";

        // Method 2: Using new keyword (stored in heap)
        String newString = new String("Hello World");

        // Method 3: From char array
        char[] chars = {'J', 'a', 'v', 'a'};
        String fromChars = new String(chars);

        // Method 4: From byte array
        byte[] bytes = {72, 101, 108, 108, 111};
        String fromBytes = new String(bytes);

        // Method 5: String concatenation
        String concatenated = "Hello" + " " + "World";

        System.out.println("Literal: " + literal);
        System.out.println("New String: " + newString);
        System.out.println("From chars: " + fromChars);
        System.out.println("From bytes: " + fromBytes);
        System.out.println("Concatenated: " + concatenated);
    }

    static void stringImmutability() {
        String original = "Hello";
        String modified = original.concat(" World");

        // original is unchanged
        System.out.println("Original after concat: " + original);
        System.out.println("Modified: " + modified);

        // String methods return new Strings
        String upper = original.toUpperCase();
        String replaced = original.replace('H', 'J');

        System.out.println("Upper case (new string): " + upper);
        System.out.println("Replaced (new string): " + replaced);
        System.out.println("Original still: " + original);

        // Using StringBuilder for mutability
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World");
        sb.replace(0, 5, "Hi");
        System.out.println("StringBuilder result: " + sb.toString());
    }

    static void stringComparison() {
        String str1 = "Hello";
        String str2 = "Hello";
        String str3 = new String("Hello");

        // == compares references (memory address)
        System.out.println("str1 == str2: " + (str1 == str2));  // true (same pool reference)
        System.out.println("str1 == str3: " + (str1 == str3));  // false (different objects)

        // equals() compares content
        System.out.println("str1.equals(str3): " + str1.equals(str3));  // true

        // equalsIgnoreCase() for case-insensitive comparison
        System.out.println("equalsIgnoreCase: " + str1.equalsIgnoreCase("hello"));

        // compareTo() for lexicographic ordering
        System.out.println("compareTo (equal): " + str1.compareTo(str3));  // 0
        System.out.println("compareTo (less): " + "Apple".compareTo("Banana"));  // negative
        System.out.println("compareTo (greater): " + "Banana".compareTo("Apple"));  // positive

        // startsWith and endsWith
        System.out.println("startsWith 'He': " + str1.startsWith("He"));
        System.out.println("endsWith 'llo': " + str1.endsWith("llo"));

        // contains
        System.out.println("contains 'ell': " + str1.contains("ell"));
    }

    static void stringPool() {
        // Strings created with literals are interned in the pool
        String pool1 = "Java";
        String pool2 = "Java";

        // Both point to the same object in the pool
        System.out.println("Pool strings same reference: " + (pool1 == pool2));

        // intern() explicitly adds to the pool
        String heap = new String("Python");
        String interned = heap.intern();

        String pool3 = "Python";
        System.out.println("intern() == pool: " + (interned == pool3));

        // Memory efficiency demonstration
        System.out.println("Pool string hashCode: " + pool1.hashCode());
        System.out.println("Same hashCode: " + pool1.hashCode() == pool2.hashCode());
    }
}