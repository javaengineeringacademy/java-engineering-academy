package academy.javaengineering.oop.internals;

public class MethodOverloadingInternals {

    static class Calculator {
        int add(int a, int b) {
            return a + b;
        }

        double add(double a, double b) {
            return a + b;
        }

        int add(int a, int b, int c) {
            return a + b + c;
        }

        String add(String a, String b) {
            return a + b;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Method Overloading Internals ===\n");

        Calculator calc = new Calculator();

        // 1. Same Name, Different Parameters
        System.out.println("--- Same Name, Different Params ---");
        System.out.println("add(2, 3) = " + calc.add(2, 3));
        System.out.println("add(2.5, 3.5) = " + calc.add(2.5, 3.5));
        System.out.println("add(1, 2, 3) = " + calc.add(1, 2, 3));
        System.out.println("add(\"A\", \"B\") = " + calc.add("A", "B"));

        // 2. Compile-Time Resolution
        System.out.println("\n--- Compile-Time Resolution ---");
        System.out.println("Compiler matches method signature");
        System.out.println("Resolves at compile time");
        System.out.println("No runtime overhead");

        // 3. Overloading Rules
        System.out.println("\n--- Overloading Rules ---");
        System.out.println("1. Different parameter types");
        System.out.println("2. Different number of parameters");
        System.out.println("3. Different order of parameters");
        System.out.println("Cannot overload by return type only");
    }
}
