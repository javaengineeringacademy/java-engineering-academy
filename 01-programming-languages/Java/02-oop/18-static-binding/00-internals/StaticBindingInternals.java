package academy.javaengineering.oop.internals;

public class StaticBindingInternals {

    static class MathUtils {
        static int add(int a, int b) {
            return a + b;
        }

        static double add(double a, double b) {
            return a + b;
        }

        static int square(int x) {
            return x * x;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Static Binding Internals ===\n");

        // 1. Compile-Time Resolution
        System.out.println("--- Compile-Time Resolution ---");
        System.out.println("Method resolved at compile time");
        System.out.println("No runtime lookup needed");
        System.out.println("Faster than dynamic binding");

        // 2. Static Methods
        System.out.println("\n--- Static Methods ---");
        System.out.println("Called via class name");
        System.out.println("Cannot be overridden");
        System.out.println("Can be hidden in subclass");

        // 3. private, final, static Methods
        System.out.println("\n--- Static Binding Methods ---");
        System.out.println("private: not visible to subclass");
        System.out.println("final: cannot be overridden");
        System.out.println("static: belongs to class");
        System.out.println("All use static binding");
    }
}
