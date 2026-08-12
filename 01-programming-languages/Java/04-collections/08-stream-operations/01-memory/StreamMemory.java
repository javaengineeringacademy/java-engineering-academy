package academy.javaengineering.collections.stream.memory;

import java.util.*;
import java.util.stream.*;

public class StreamMemory {

    public static void main(String[] args) {
        System.out.println("=== Stream Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Sequential vs Parallel stream
        System.out.println("--- Sequential vs Parallel ---");
        List<Integer> list = IntStream.range(1, 1000000).boxed().collect(Collectors.toList());

        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        long seqCount = list.stream().filter(n -> n % 2 == 0).count();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Sequential: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        long parCount = list.parallelStream().filter(n -> n % 2 == 0).count();
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Parallel: " + (after - before) + " bytes");
        System.out.println("Parallel has overhead for small datasets");

        // 2. Stream pipeline memory
        System.out.println("\n--- Pipeline Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> result = list.stream()
            .filter(n -> n > 500000)
            .map(n -> n * 2)
            .limit(1000)
            .collect(Collectors.toList());
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Pipeline result: " + (after - before) + " bytes");

        // 3. Collectors.toList() vs toSet()
        System.out.println("\n--- Collector Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        List<Integer> listResult = list.stream().limit(10000).collect(Collectors.toList());
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("toList(): " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Set<Integer> setResult = list.stream().limit(10000).collect(Collectors.toSet());
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("toSet(): " + (after - before) + " bytes");

        // 4. Lazy evaluation benefit
        System.out.println("\n--- Lazy Evaluation ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        list.stream()
            .filter(n -> {
                System.out.print(".");
                return n % 2 == 0;
            })
            .limit(5)
            .collect(Collectors.toList());
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("\nLazy filter: " + (after - before) + " bytes");
        System.out.println("Only 5 elements processed, not all");
    }
}
