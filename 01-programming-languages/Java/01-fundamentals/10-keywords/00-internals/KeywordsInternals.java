package academy.javaengineering.fundamentals.keywords;

/**
 * Demonstrates keyword internals in Java.
 */
public class KeywordsInternals {

    public static void main(String[] args) {
        System.out.println("=== Keywords Internals Demo ===\n");

        // 1. Data type keywords
        System.out.println("--- Data Type Keywords ---");
        byte b = 127;
        short s = 32767;
        int i = 2147483647;
        long l = 9223372036854775807L;
        float f = 3.14f;
        double d = 3.141592653589793;
        boolean bl = true;
        char c = 'A';
        System.out.println("All 8 primitive types declared");

        // 2. Modifier keywords
        System.out.println("\n--- Modifier Keywords ---");
        System.out.println("public: Accessible everywhere");
        System.out.println("private: Accessible only in this class");
        System.out.println("protected: Accessible in subclasses + same package");
        System.out.println("static: Belongs to class, not instance");
        System.out.println("final: Cannot be changed after initialization");

        // 3. Control flow keywords
        System.out.println("\n--- Control Flow Keywords ---");
        int x = 10;
        if (x > 5) {
            System.out.println("if: Conditional branch");
        } else {
            System.out.println("else: Alternative branch");
        }

        for (int j = 0; j < 3; j++) {
            if (j == 1) continue; // Skip 1
            System.out.println("for: Iteration " + j);
        }

        // 4. Class-related keywords
        System.out.println("\n--- Class Keywords ---");
        System.out.println("class: Defines a class");
        System.out.println("interface: Defines a contract");
        System.out.println("enum: Defines enumerated type");
        System.out.println("extends: Inherits from parent");
        System.out.println("implements: Implements interface");

        // 5. Exception keywords
        System.out.println("\n--- Exception Keywords ---");
        try {
            System.out.println("try: Begin exception handling");
            throw new RuntimeException("throw: Explicit exception");
        } catch (RuntimeException e) {
            System.out.println("catch: Handle exception");
        } finally {
            System.out.println("finally: Always executes");
        }

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
