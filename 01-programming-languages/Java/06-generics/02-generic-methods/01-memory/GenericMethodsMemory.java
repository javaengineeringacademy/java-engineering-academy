package academy.javaengineering.generics.memory;

import java.util.*;

public class GenericMethodsMemory {

    public static void main(String[] args) {
        System.out.println("=== Generic Methods Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Method-Level vs Class-Level Type Safety
        System.out.println("--- Type Parameter Scope ---");
        System.out.println("Method-level: T created per call");
        System.out.println("Class-level: T shared across instance");
        System.out.println("Method-level: stack allocated type info");

        // 2. Autoboxing in Generic Methods
        System.out.println("\n--- Autoboxing Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(autoBox(i));
        }
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Autoboxing 10K: " + (after - before) + " bytes");

        // 3. Function Interface Memory
        System.out.println("\n--- Function<T,R> Memory ---");
        java.util.function.Function<String, Integer> func = String::length;
        System.out.println("Lambda: ~24 bytes per instance");
        System.out.println("Method ref: shares instance via invokedynamic");

        // 4. Type Witness Memory
        System.out.println("\n--- Type Witness ---");
        System.out.println("<String>method(args) - explicit type witness");
        System.out.println("No runtime cost - compile-time only");
    }

    private static Integer autoBox(int value) {
        return Integer.valueOf(value);
    }
}
