package academy.javaengineering.generics.memory;

import java.util.*;

public class TypeInferenceMemory {

    public static void main(String[] args) {
        System.out.println("=== Type Inference Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. var vs Explicit Type
        System.out.println("--- var vs Explicit ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        var inferred = new ArrayList<Integer>();
        for (int i = 0; i < 10000; i++) inferred.add(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("var inference: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> explicit = new ArrayList<>();
        for (int i = 0; i < 10000; i++) explicit.add(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Explicit type: " + (after - before) + " bytes");
        System.out.println("Same - compile-time only difference");

        // 2. Diamond Operator Cost
        System.out.println("\n--- Diamond Cost ---");
        System.out.println("No runtime cost");
        System.out.println("Compiler resolves types");
        System.out.println("Bytecode identical to explicit");

        // 3. Inference Cache
        System.out.println("\n--- Inference Cache ---");
        System.out.println("Compiler caches type resolutions");
        System.out.println("Reduces compilation time");
        System.out.println("No runtime impact");
    }
}
