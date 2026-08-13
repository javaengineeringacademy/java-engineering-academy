package academy.javaengineering.generics.memory;

import java.util.*;

public class GenericTypesMemory {

    public static void main(String[] args) {
        System.out.println("=== Generic Types Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Wrapper vs Primitive in Generics
        System.out.println("--- Wrapper Overhead ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<Integer> boxed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) boxed.add(Integer.valueOf(i));
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Boxed Integer list: " + (after - before) + " bytes");
        System.out.println("Each Integer: ~16 bytes vs int: 4 bytes");

        // 2. Type Parameter Object Overhead
        System.out.println("\n--- Type Parameter Overhead ---");
        System.out.println("T becomes Object at runtime");
        System.out.println("Object header: 12 bytes (mark + klass)");
        System.out.println("Reference: 8 bytes (compressed oops)");

        // 3. Generic vs Raw Type Memory
        System.out.println("\n--- Generic vs Raw Type ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        @SuppressWarnings("rawtypes")
        List raw = new ArrayList();
        for (int i = 0; i < 10000; i++) raw.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Raw type: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> typed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) typed.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Typed: " + (after - before) + " bytes");
        System.out.println("Same memory - erasure makes them identical");

        // 4. Box<T> Memory Layout
        System.out.println("\n--- Box<T> Memory ---");
        System.out.println("Box object: 16 bytes base");
        System.out.println("T value field: 8 bytes (reference)");
        System.out.println("Total per Box: ~24 bytes + referenced object");
    }
}
