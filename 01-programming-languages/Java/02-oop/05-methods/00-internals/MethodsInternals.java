package academy.javaengineering.oop.internals;

public class MethodsInternals {

    static class Calculator {
        // Method with return value
        int add(int a, int b) {
            return a + b;
        }

        // Method without return value
        void printSum(int a, int b) {
            System.out.println("Sum: " + (a + b));
        }

        // Method with varargs
        int sum(int... numbers) {
            int total = 0;
            for (int n : numbers) total += n;
            return total;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Methods Internals ===\n");

        Calculator calc = new Calculator();

        // 1. Method Signature
        System.out.println("--- Method Signature ---");
        System.out.println("Return type + name + parameter types");
        System.out.println("add(int, int) vs add(double, double)");
        System.out.println("Overloading: same name, different params");

        // 2. Method Parameters
        System.out.println("\n--- Method Parameters ---");
        System.out.println("Pass by value (primitives)");
        System.out.println("Pass by reference value (objects)");
        System.out.println("Cannot change original primitive");
        System.out.println("Can change object state");

        // 3. Return Types
        System.out.println("\n--- Return Types ---");
        System.out.println("Primitive: value copied");
        System.out.println("Object: reference copied");
        System.out.println("void: no return");

        // 4. Static Methods
        System.out.println("\n--- Static Methods ---");
        System.out.println("Belong to class, not instance");
        System.out.println("Called via ClassName.method()");
        System.out.println("Cannot access instance variables");
    }
}
