package academy.javaengineering.collections.iteration.memory;

import java.util.*;

public class IterationMemory {

    public static void main(String[] args) {
        System.out.println("=== Iteration Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Iterator vs for loop
        System.out.println("--- Iterator vs Indexed Loop ---");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) list.add(i);

        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) it.next();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Iterator traversal: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        for (int i = 0; i < list.size(); i++) list.get(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Indexed loop: " + (after - before) + " bytes");

        // 2. Spliterator characteristics
        System.out.println("\n--- Spliterator Memory ---");
        System.out.println("Spliterator: lightweight, no copy");
        System.out.println("Supports parallel processing");
        System.out.println("Characteristics: ORDERED, SIZED, IMMUTABLE");

        // 3. Stream pipeline memory
        System.out.println("\n--- Stream Pipeline Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        list.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2)
            .limit(1000)
            .collect(java.util.stream.Collectors.toList());
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Stream pipeline: " + (after - before) + " bytes");
        System.out.println("Lazy evaluation reduces intermediate objects");
    }
}
