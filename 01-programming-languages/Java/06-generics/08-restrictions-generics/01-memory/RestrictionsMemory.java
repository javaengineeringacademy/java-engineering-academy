package academy.javaengineering.generics.memory;

import java.util.*;

public class RestrictionsMemory {

    public static void main(String[] args) {
        System.out.println("=== Restrictions Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Wrapper Cost
        System.out.println("--- Wrapper Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<Integer> boxed = new ArrayList<>();
        for (int i = 0; i < 10000; i++) boxed.add(Integer.valueOf(i));
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Boxed 10K: " + (after - before) + " bytes");
        System.out.println("Each Integer: ~16 bytes vs int: 4 bytes");

        // 2. Static Restriction Memory
        System.out.println("\n--- Static Restriction ---");
        System.out.println("No static T: prevents class-level type info");
        System.out.println("Instance T: stored in object header");
        System.out.println("Cost: 8 bytes reference per instance");

        // 3. Array Restriction
        System.out.println("\n--- Array Restriction ---");
        System.out.println("Generic arrays would need runtime type info");
        System.out.println("Would break type erasure");
        System.out.println("Alternative: List<List<String>>");

        // 4. Heap Pollution Prevention
        System.out.println("\n--- Heap Pollution ---");
        System.out.println("Varargs can cause heap pollution");
        System.out.println("@SafeVarargs disables warning");
        System.out.println("Cost: creates defensive copy");
    }
}
