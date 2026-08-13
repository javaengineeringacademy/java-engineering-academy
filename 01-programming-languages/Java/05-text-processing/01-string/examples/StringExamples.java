package academy.javaengineering.text.examples;

/**
 * String Examples - Practical demonstrations of String usage.
 * 
 * WHY STRING IS IMMUTABLE:
 * - Security: String parameters can't be modified (URL, file paths, class loading)
 * - Thread Safety: Immutable objects are inherently thread-safe
 * - Hashing: String's hash code can be cached (performance optimization)
 * - String Pool: Only possible with immutable strings
 * 
 * TRADE-OFFS:
 * - Pros: Safe, thread-safe, cacheable
 * - Cons: Memory overhead for concatenation (use StringBuilder)
 */
public class StringExamples {

    public static void main(String[] args) {
        System.out.println("=== String Examples ===\n");

        // Example 1: String Immutability
        example1_Immutability();

        // Example 2: String Pool
        example2_StringPool();

        // Example 3: String Comparison
        example3_Comparison();

        // Example 4: String Methods
        example4_Methods();
    }

    /**
     * WHY: String immutability is a design decision for security and performance.
     * 
     * INTERNAL: String objects are stored in the string pool and cannot be modified.
     * When you "modify" a string, a new object is created.
     */
    private static void example1_Immutability() {
        System.out.println("--- Example 1: String Immutability ---");

        String s1 = "Hello";
        String s2 = s1.concat(" World");

        System.out.println("s1: " + s1);        // Hello (unchanged)
        System.out.println("s2: " + s2);        // Hello World (new object)
        System.out.println("s1 == s2: " + (s1 == s2));  // false
    }

    /**
     * WHY: String pool saves memory by reusing identical strings.
     * 
     * INTERNAL: JVM maintains a pool of string literals. When a literal is created,
     * JVM checks if it exists in the pool. If yes, returns reference to existing string.
     */
    private static void example2_StringPool() {
        System.out.println("\n--- Example 2: String Pool ---");

        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        String s4 = s3.intern();

        System.out.println("s1 == s2: " + (s1 == s2));  // true (same pool object)
        System.out.println("s1 == s3: " + (s1 == s3));  // false (different objects)
        System.out.println("s1 == s4: " + (s1 == s4));  // true (interned to pool)
    }

    /**
     * WHY: equals() compares content, == compares references.
     * 
     * ENGINEERING DECISION: Always use equals() for content comparison.
     * Use == only when checking if two references point to the same object.
     */
    private static void example3_Comparison() {
        System.out.println("\n--- Example 3: String Comparison ---");

        String s1 = "Hello";
        String s2 = new String("Hello");
        String s3 = "hello";

        System.out.println("s1.equals(s2): " + s1.equals(s2));      // true
        System.out.println("s1 == s2: " + (s1 == s2));               // false
        System.out.println("s1.equalsIgnoreCase(s3): " + s1.equalsIgnoreCase(s3));  // true
        System.out.println("s1.compareTo(s2): " + s1.compareTo(s2)); // 0
    }

    /**
     * WHY: String methods are designed for common text operations.
     * 
     * PERFORMANCE TIP: For repeated modifications, use StringBuilder.
     */
    private static void example4_Methods() {
        System.out.println("\n--- Example 4: String Methods ---");

        String s = "  Hello, World!  ";

        System.out.println("Original: '" + s + "'");
        System.out.println("trim(): '" + s.trim() + "'");
        System.out.println("toUpperCase(): " + s.trim().toUpperCase());
        System.out.println("toLowerCase(): " + s.trim().toLowerCase());
        System.out.println("length(): " + s.trim().length());
        System.out.println("charAt(0): " + s.trim().charAt(0));
        System.out.println("substring(7): " + s.trim().substring(7));
        System.out.println("contains(\"World\"): " + s.trim().contains("World"));
        System.out.println("replace(\"World\", \"Java\"): " + s.trim().replace("World", "Java"));
    }
}
