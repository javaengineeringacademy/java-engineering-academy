package academy.javaengineering.text.internals;

public class StringInternals {

    public static void main(String[] args) {
        System.out.println("=== String Internals ===\n");

        // 1. String Immutability
        System.out.println("--- String Immutability ---");
        String s1 = "Hello";
        String s2 = s1.concat(" World");
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("String objects cannot be modified");

        // 2. String Pool
        System.out.println("\n--- String Pool ---");
        String s3 = "Hello";
        String s4 = "Hello";
        String s5 = new String("Hello");
        System.out.println("s3 == s4: " + (s3 == s4));
        System.out.println("s3 == s5: " + (s3 == s5));
        System.out.println("Pool reuses identical strings");

        // 3. String Methods
        System.out.println("\n--- String Methods ---");
        String text = "Hello, World!";
        System.out.println("length(): " + text.length());
        System.out.println("charAt(0): " + text.charAt(0));
        System.out.println("substring(0, 5): " + text.substring(0, 5));
        System.out.println("toUpperCase(): " + text.toUpperCase());
        System.out.println("contains(\"World\"): " + text.contains("World"));
    }
}
