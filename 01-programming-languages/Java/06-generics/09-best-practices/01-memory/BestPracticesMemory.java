package academy.javaengineering.generics.memory;

import java.util.*;

public class BestPracticesMemory {

    public static void main(String[] args) {
        System.out.println("=== Best Practices Memory ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Generic vs Raw Performance
        System.out.println("--- Generic vs Raw ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        @SuppressWarnings("rawtypes")
        List raw = new ArrayList();
        for (int i = 0; i < 10000; i++) raw.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Raw list: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> typed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) typed.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Typed list: " + (after - before) + " bytes");
        System.out.println("Same memory - erasure makes them equal");

        // 2. Wildcard vs Type Parameter
        System.out.println("\n--- Wildcard Cost ---");
        System.out.println("Wildcard: no runtime cost");
        System.out.println("Type parameter: may create bridge methods");
        System.out.println("Use wildcards when possible");

        // 3. Unbounded vs Bounded
        System.out.println("\n--- Unbounded vs Bounded ---");
        System.out.println("Unbounded <?>: Object cast");
        System.out.println("Bounded <? extends T>: T cast");
        System.out.println("Bounded: more efficient operations");
    }
}
