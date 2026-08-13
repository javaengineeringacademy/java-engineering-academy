package academy.javaengineering.generics.memory;

import java.lang.reflect.*;
import java.util.function.*;

public class ErasureMethodsMemory {

    static <T> T identity(T value) {
        return value;
    }

    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void main(String[] args) {
        System.out.println("=== Erasure Methods Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Method Erasure
        System.out.println("--- Method Erasure ---");
        Method[] methods = ErasureMethodsMemory.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName() + ":");
            System.out.println("  Return: " + m.getReturnType().getSimpleName());
            System.out.println("  Params: " + java.util.Arrays.toString(m.getParameterTypes()));
        }

        // 2. Bridge Method Cost
        System.out.println("\n--- Bridge Method Cost ---");
        System.out.println("Synthetic methods created by compiler");
        System.out.println("Cost: ~16 bytes per method");
        System.out.println("Impact: class file size increase");

        // 3. Function Interface
        System.out.println("\n--- Function Interface ---");
        Function<String, Integer> func = String::length;
        System.out.println("Lambda: ~24 bytes");
        System.out.println("Method reference: shared via invokedynamic");

        // 4. Cast Insertion
        System.out.println("\n--- Cast Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) list.add("item" + i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Cast overhead: ~1-2 cycles per access");
        System.out.println("Total: " + (after - before) + " bytes");
    }
}
