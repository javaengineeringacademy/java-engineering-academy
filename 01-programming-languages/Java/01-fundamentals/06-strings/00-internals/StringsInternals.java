package academy.javaengineering.fundamentals.strings;

/**
 * Demonstrates string internals in Java.
 */
public class StringsInternals {

    public static void main(String[] args) {
        System.out.println("=== Strings Internals Demo ===\n");

        // 1. String pool behavior
        System.out.println("--- String Pool Internals ---");
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        String s4 = s3.intern();

        System.out.println("s1 == s2 (pool reuse): " + (s1 == s2));
        System.out.println("s1 == s3 (heap vs pool): " + (s1 == s3));
        System.out.println("s1 == s4 (interned): " + (s1 == s4));

        // 2. String concatenation
        System.out.println("\n--- String Concatenation ---");
        String concat1 = "Hello" + " " + "World";
        String concat2 = "Hello" + " " + System.currentTimeMillis();
        System.out.println("Compile-time: " + concat1 + " (single pool entry)");
        System.out.println("Dynamic: " + concat2 + " (StringBuilder created)");

        // 3. String immutability
        System.out.println("\n--- String Immutability ---");
        String original = "Hello";
        String upper = original.toUpperCase();
        String replaced = original.replace('l', 'L');
        System.out.println("Original: " + original);
        System.out.println("After toUpperCase: " + upper);
        System.out.println("After replace: " + replaced);

        // 4. String comparison
        System.out.println("\n--- String Comparison ---");
        String a = "Hello";
        String b = "Hello";
        String c = new String("Hello");
        System.out.println("a == b: " + (a == b));
        System.out.println("a == c: " + (a == c));
        System.out.println("a.equals(c): " + a.equals(c));

        // 5. String hash code
        System.out.println("\n--- String Hash Code ---");
        String str = "Hello";
        System.out.println("Hash of 'Hello': " + str.hashCode());
        System.out.println("Hash of 'World': " + "World".hashCode());

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
