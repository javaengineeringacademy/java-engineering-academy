package academy.javaengineering.oop.memory;

public class IntroductionMemory {

    public static void main(String[] args) {
        System.out.println("=== OOP Introduction Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Memory Layout
        System.out.println("--- Object Memory Layout ---");
        System.out.println("Object header: 12 bytes (mark + klass)");
        System.out.println("Reference: 8 bytes (compressed oops)");
        System.out.println("Fields: 4 bytes each (int), 8 bytes (reference)");

        // 2. Class vs Object Memory
        System.out.println("\n--- Class vs Object ---");
        System.out.println("Class: stored in Metaspace (one per class)");
        System.out.println("Object: stored in heap (one per instance)");
        System.out.println("Multiple objects can share same class");

        // 3. String Memory
        System.out.println("\n--- String Memory ---");
        System.out.println("String object: 40 bytes base + char[] content");
        System.out.println("String pool: shared strings in heap");
        System.out.println("String literal: reference to pool");

        // 4. Array Memory
        System.out.println("\n--- Array Memory ---");
        System.out.println("Array object: 16 bytes header + elements");
        System.out.println("int[]: 16 + 4 * length bytes");
        System.out.println("Object[]: 16 + 8 * length bytes");
    }
}
