package academy.javaengineering.generics.memory;

import java.util.*;

public class WildcardsMemory {

    public static void main(String[] args) {
        System.out.println("=== Wildcards Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Wildcard vs Type Parameter
        System.out.println("--- Wildcard vs Type Parameter ---");
        System.out.println("Wildcard: no new class created");
        System.out.println("Type parameter: may create bridge methods");
        System.out.println("Wildcard: compile-time only check");

        // 2. List<?> vs List<Object>
        System.out.println("\n--- List<?> vs List<Object> ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        List<?> wildcardList = new ArrayList<Integer>();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("List<?>: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Object> objectList = new ArrayList<Integer>();
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("List<Object>: " + (after - before) + " bytes");
        System.out.println("Same memory - both store Object references");

        // 3. PECS Memory Impact
        System.out.println("\n--- PECS Memory ---");
        System.out.println("? extends: no write capability");
        System.out.println("? super: type-safe write");
        System.out.println("No runtime overhead for wildcards");

        // 4. Wildcard in Collections API
        System.out.println("\n--- Collections API ---");
        System.out.println("Collections.sort(List<T>, Comparator<? super T>)");
        System.out.println("Arrays.asList(T... a) returns List<T>");
        System.out.println("Uses wildcards for flexibility");
    }
}
