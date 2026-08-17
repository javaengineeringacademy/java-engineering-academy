package academy.javaengineering.fundamentals.strings;

/**
 * Demonstrates string memory usage patterns.
 */
public class StringsMemory {

    public static void main(String[] args) {
        System.out.println("=== Strings Memory Demo ===\n");

        // 1. String pool memory
        System.out.println("--- String Pool Memory ---");
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        System.out.println("Pool entry 'Hello': shared by s1 and s2");
        System.out.println("s1 == s2: " + (s1 == s2) + " (same reference)");
        System.out.println("s1 == s3: " + (s1 == s3) + " (different objects)");

        // 2. String concatenation memory
        System.out.println("\n--- String Concatenation Memory ---");
        String concat1 = "Hello" + " " + "World";
        String concat2 = "Hello" + " " + System.currentTimeMillis();
        System.out.println("Compile-time constant: single pool entry");
        System.out.println("Dynamic: StringBuilder created on heap");

        // 3. StringBuilder memory
        System.out.println("\n--- StringBuilder Memory ---");
        StringBuilder sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity());
        for (int i = 0; i < 100; i++) {
            sb.append("a");
        }
        System.out.println("After 100 appends: capacity=" + sb.capacity());

        // 4. String immutability
        System.out.println("\n--- String Immutability Memory ---");
        String original = "Hello";
        String modified = original.toUpperCase();
        System.out.println("Original unchanged: " + original);
        System.out.println("New object created: " + modified);
        System.out.println("Different references: " + (original != modified));

        System.out.println("\n=== Memory Demo Complete ===");
    }
}
