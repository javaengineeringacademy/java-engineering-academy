package academy.javaengineering.generics.memory;

import java.util.*;

public class RawTypesMemory {

    public static void main(String[] args) {
        System.out.println("=== Raw Types Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Raw vs Generic Memory
        System.out.println("--- Raw vs Generic ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        @SuppressWarnings("rawtypes")
        List raw = new ArrayList();
        for (int i = 0; i < 10000; i++) raw.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Raw List 10K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> typed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) typed.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Typed List 10K: " + (after - before) + " bytes");
        System.out.println("IDENTICAL - erasure makes them equal");

        // 2. Autoboxing Cost
        System.out.println("\n--- Autoboxing Cost ---");
        System.out.println("Raw List: stores Object references");
        System.out.println("Typed List<Integer>: stores Integer objects");
        System.out.println("Same memory - both store Objects");

        // 3. Type Safety Cost
        System.out.println("\n--- Type Safety ---");
        System.out.println("Raw: no casts needed (already Object)");
        System.out.println("Typed: compiler inserts casts");
        System.out.println("Cost: ~1-2 cycles per cast");

        // 4. Legacy Integration
        System.out.println("\n--- Legacy ---");
        System.out.println("Raw types: 0 runtime cost");
        System.out.println("Trade-off: lose type safety");
        System.out.println("Recommendation: migrate to generics");
    }
}
