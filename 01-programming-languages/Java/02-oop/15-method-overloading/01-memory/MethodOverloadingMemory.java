package academy.javaengineering.oop.memory;

public class MethodOverloadingMemory {

    static class Calculator {
        int add(int a, int b) { return a + b; }
        double add(double a, double b) { return a + b; }
    }

    public static void main(String[] args) {
        System.out.println("=== Method Overloading Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Compile-Time Binding
        System.out.println("--- Compile-Time Binding ---");
        System.out.println("Method resolved at compile time");
        System.out.println("No virtual method table lookup");
        System.out.println("Direct method call");

        // 2. Memory Overhead
        System.out.println("\n--- Memory Overhead ---");
        System.out.println("Overloaded methods: no extra per-object cost");
        System.out.println("Method table: stores all overloads");
        System.out.println("Cost: ~16 bytes per method in class");

        // 3. Autoboxing Cost
        System.out.println("\n--- Autoboxing Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Calculator calc = new Calculator();
        calc.add(1, 2); // int version
        calc.add(1.0, 2.0); // double version
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Overhead: " + (after - before) + " bytes");
        System.out.println("Minimal - compile-time resolved");
    }
}
