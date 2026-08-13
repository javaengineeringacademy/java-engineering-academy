package academy.javaengineering.oop.memory;

import java.util.function.*;

public class FunctionalInterfacesMemory {

    @FunctionalInterface
    interface Calculator { int calculate(int a, int b); }

    public static void main(String[] args) {
        System.out.println("=== Functional Interfaces Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Lambda Memory
        System.out.println("--- Lambda Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Calculator add = (a, b) -> a + b;
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Lambda: " + (after - before) + " bytes");
        System.out.println("Shared class via invokedynamic");

        // 2. Lambda vs Anonymous
        System.out.println("\n--- Lambda vs Anonymous ---");
        Calculator anon = new Calculator() {
            @Override
            public int calculate(int a, int b) { return a + b; }
        };
        System.out.println("Lambda: ~24 bytes");
        System.out.println("Anonymous: ~48 bytes");
        System.out.println("Lambda: 50% less memory");

        // 3. Method Reference
        System.out.println("\n--- Method Reference ---");
        Function<String, Integer> func = String::length;
        System.out.println("Method ref: shared class");
        System.out.println("No extra memory per call");
    }
}
